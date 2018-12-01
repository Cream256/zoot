package com.zootcat.controllers.physics;

import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.physics.ZootBodyShape;
import com.zootcat.physics.ZootShapeFactory;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;

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
	@CtrlParam(global = true) protected ZootScene scene;
	
	private FixtureDef fixtureDef;
	private Fixture fixture;
	
	@Override
	public void init(ZootActor actor)
	{
		fixtureDef = createFixtureDef(actor);
	}
	
	protected FixtureDef createFixtureDef(ZootActor actor) 
	{		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = density;
		fixtureDef.friction = friction;
		fixtureDef.restitution = restitution;
		fixtureDef.isSensor = sensor;
		fixtureDef.shape = createShape(actor, shape);
		return fixtureDef;
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
		fixture = actor.getController(PhysicsBodyController.class).addFixture(fixtureDef, actor);		
	}
	
	@Override
	public void onRemove(ZootActor actor)
	{
		actor.getController(PhysicsBodyController.class).removeFixture(fixture);
		fixture = null;
	}
}
