package com.zootcat.controllers.physics;

import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.physics.ZootBodyShape;
import com.zootcat.physics.ZootFixtureDefBuilder;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;
import com.zootcat.utils.CollisionMask;

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
	
	protected Array<FixtureDef> fixtureDefs;
	protected Array<Fixture> fixtures = new Array<Fixture>(0);
	protected CollisionMask collisionMask = new CollisionMask();
	protected boolean disposeShapesAutomatically = true;
	
	@Override
	public void init(ZootActor actor)
	{
		fixtureDefs = createFixtureDefs(actor);
		collisionMask = new CollisionMask(mask);
	}
	
	protected Array<FixtureDef> createFixtureDefs(ZootActor actor) 
	{				
		FixtureDef fixtureDef = new ZootFixtureDefBuilder(scene)
				.setDensity(density)
				.setFriction(friction)
				.setRestitution(restitution)
				.setOffsetX(offsetX)
				.setOffsetY(offsetY)
				.setWidth(width)
				.setHeight(height)
				.setSensor(sensor)
				.setShape(shape)
				.setCategory(category)
				.setMask(mask)
				.build(actor);
			
		Array<FixtureDef> fixtureDefs = new Array<FixtureDef>(1);
		fixtureDefs.add(fixtureDef);
		return fixtureDefs;
	}
					
	@Override
	public void onAdd(ZootActor actor)
	{
		PhysicsBodyController physicsBodyCtrl = actor.getSingleController(PhysicsBodyController.class);		
		fixtures = new Array<Fixture>(fixtureDefs.size);
		fixtureDefs.forEach(def -> 
		{
			Fixture newFixture = physicsBodyCtrl.addFixture(def, actor);
			fixtures.add(newFixture);
			
			if(disposeShapesAutomatically)
			{
				def.shape.dispose();
				def.shape = null;
			}
		});
	}
		
	@Override
	public void onRemove(ZootActor actor)
	{
		fixtures.forEach(fixture -> actor.getSingleController(PhysicsBodyController.class).removeFixture(fixture));
		fixtures.clear();
		fixtures = null;
	}
	
	public ImmutableArray<Fixture> getFixtures()
	{
		return new ImmutableArray<Fixture>(fixtures);
	}
	
	public String getCollisionMask()
	{
		return collisionMask.toString();
	}

	public void addCollisionMaskValue(String value)
	{
		collisionMask.add(value);
		updateFixturesFilter(collisionMask.toBitMask());
	}	
	
	public void removeCollisionMaskValue(String value)
	{
		collisionMask.remove(value);
		updateFixturesFilter(collisionMask.toBitMask());		
	}
	
	private void updateFixturesFilter(short newMask)
	{
		fixtures.forEach(fixture -> 
		{
			Filter filter = fixture.getFilterData();
			filter.maskBits = newMask;		
			fixture.setFilterData(filter);	
		});
	}
			
	public String getCollisionCategory()
	{
		return category;
	}
	
	public void setDisposeShapesAutomatically(boolean dispose)
	{
		disposeShapesAutomatically = dispose;
	}
}
