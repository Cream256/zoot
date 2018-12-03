package com.zootcat.controllers.physics;

import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.Array;
import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.physics.ZootBodyShape;
import com.zootcat.physics.ZootShapeFactory;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;
import com.zootcat.utils.BitMaskConverter;

/**
 * Fixture controller - adds new fixture to box2d body.
 * @ctrlParam density - box2d fixture density
 * @ctrlParam friction - box2d fixture friction
 * @ctrlParam restitution - box2d fixture restitution
 * @ctrlParam offsetX - box2d fixture x offset from the center of body
 * @ctrlParam offsetY - box2d fixture y offset from the center of body
 * @ctrlParam width - box2d fixture width, if not set actor width will be used
 * @ctrlParam height - box2d fixture height, if not set actor height will be used
 * @ctrlParam sensor - if true, box2d fixture will be sensor 
 * @ctrlParam shape - box2d fixture shape (box, circle, polygon, slope left, slope right)
 * @ctrlParam category - box2d filter category (e.g. "myCategory") 
 * @ctrlParam mask - box2d filter mask (e.g. "myCategory | otherCategory")
 * @author Cream
 *
 */
public class FixtureController extends ControllerAdapter
{
	@CtrlParam protected float density = 1.0f;
	@CtrlParam protected float friction = 0.2f;
	@CtrlParam protected float restitution = 0.0f;
	@CtrlParam protected float offsetX = 0.0f;
	@CtrlParam protected float offsetY = 0.0f;
	@CtrlParam protected float width = 0.0f;
	@CtrlParam protected float height = 0.0f;
	@CtrlParam protected boolean sensor = false;
	@CtrlParam protected ZootBodyShape shape = ZootBodyShape.BOX;
	@CtrlParam protected String category = "";
	@CtrlParam protected String mask = "";
	@CtrlParam(global = true) protected ZootScene scene;
	
	private Array<FixtureDef> fixtureDefs;
	private Array<Fixture> fixtures;
	
	@Override
	public void init(ZootActor actor)
	{
		fixtureDefs = createFixtureDefs(actor);
	}
	
	protected Array<FixtureDef> createFixtureDefs(ZootActor actor) 
	{		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = density;
		fixtureDef.friction = friction;
		fixtureDef.restitution = restitution;
		fixtureDef.isSensor = sensor;
		fixtureDef.shape = createShape(actor, shape);
		setupFilter(fixtureDef.filter);		
		
		Array<FixtureDef> fixtureDefs = new Array<FixtureDef>(1);
		fixtureDefs.add(fixtureDef);
		return fixtureDefs;
	}
	
	protected void setupFilter(Filter filter)
	{
		if(category != null && !category.isEmpty())
		{
			filter.categoryBits = BitMaskConverter.Instance.fromString(category);	
		}
		if(mask != null && !mask.isEmpty())
		{
			filter.maskBits = BitMaskConverter.Instance.fromString(mask);
		}
	}
		
	protected Shape createShape(ZootActor actor, ZootBodyShape shape)
	{		
		switch(shape)
		{
		case BOX:
			return ZootShapeFactory.createBox(
					getFixtureWidth(actor), 
					getFixtureHeight(actor), 
					offsetX * scene.getUnitScale(), 
					offsetY * scene.getUnitScale());
			
		case CIRCLE:
			return ZootShapeFactory.createCircle(getFixtureWidth(actor));
			
		case SLOPE_LEFT:
		case SLOPE_RIGHT:
			return ZootShapeFactory.createSlope(getFixtureWidth(actor), getFixtureHeight(actor), shape == ZootBodyShape.SLOPE_LEFT);
			
		case POLYGON:
			PolygonMapObject polygonObj = (PolygonMapObject) scene.getMap().getObjectById(actor.getId());
			return ZootShapeFactory.createPolygon(polygonObj.getPolygon(), actor.getX(), actor.getY(), scene.getUnitScale());
			
		default:
			throw new RuntimeZootException("Unknown fixture shape type for for actor: " + actor);
		}
	}
	
	protected float getFixtureWidth(ZootActor actor)
	{
		return width == 0.0f ? actor.getWidth() : width * scene.getUnitScale();
	}
	
	protected float getFixtureHeight(ZootActor actor)
	{
		return height == 0.0f ? actor.getHeight() : height * scene.getUnitScale();
	}
	
	@Override
	public void onAdd(ZootActor actor)
	{
		fixtures = new Array<Fixture>(fixtureDefs.size);
		fixtureDefs.forEach(def -> 
		{
			Fixture newFixture = actor.getController(PhysicsBodyController.class).addFixture(def, actor);
			fixtures.add(newFixture);
		});
	}
	
	public ImmutableArray<Fixture> getFixtures()
	{
		return new ImmutableArray<Fixture>(fixtures);
	}
	
	@Override
	public void onRemove(ZootActor actor)
	{
		fixtures.forEach(fixture -> actor.getController(PhysicsBodyController.class).removeFixture(fixture));
		fixtures.clear();
		fixtures = null;
	}
}
