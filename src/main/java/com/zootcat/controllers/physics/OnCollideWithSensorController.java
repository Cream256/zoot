package com.zootcat.controllers.physics;

import java.util.LinkedHashSet;
import java.util.Set;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.physics.ZootShapeFactory;
import com.zootcat.scene.ZootActor;

public abstract class OnCollideWithSensorController extends OnCollideController
{
	@CtrlParam(debug = true) private float sensorWidth = 1.0f;
	@CtrlParam(debug = true) private float sensorHeight = 1.0f;
	@CtrlParam(debug = true) private float sensorX = 0.0f;
	@CtrlParam(debug = true) private float sensorY = 0.0f;
	
	private Fixture sensor;
	private Set<Fixture> collisions = new LinkedHashSet<Fixture>();
	
	@Override
	public void onAdd(ZootActor actor)
	{
		super.onAdd(actor);
		
		//create sensor
		Shape sensorShape = createSensorShape(actor);
		FixtureDef sensorFixtureDef = createSensorFixtureDef(actor, sensorShape);		
		sensor = actor.getController(PhysicsBodyController.class).addFixture(sensorFixtureDef, actor);
				
		//cleanup
		sensorShape.dispose();
		collisions.clear();
	}
	
	@Override
	public void onEnter(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		if(collidedWithSensor(contact))
		{
			collisions.add(getOtherFixture(actorA, actorB, contact));
		}
	}

	@Override
	public void onLeave(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		collisions.remove(getOtherFixture(actorA, actorB, contact));
	}

	@Override
	public void onUpdate(float delta, ZootActor actor) 
	{
		for(Fixture fixture : collisions)
		{
			if(!onCollideWithSensor(fixture))
			{
				break;
			}
		}
	}
	
	public Fixture getSensor()
	{
		return sensor;
	}
	
	protected abstract boolean onCollideWithSensor(Fixture fixture);
	
	private boolean collidedWithSensor(Contact contact)
	{
		return contact.getFixtureA() == sensor || contact.getFixtureB() == sensor;
	}
	
	private FixtureDef createSensorFixtureDef(ZootActor actor, Shape climbSensorShape)
	{
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = climbSensorShape;
		fixtureDef.isSensor = true;
		fixtureDef.filter.categoryBits = getFilter().categoryBits;
		fixtureDef.filter.maskBits = getFilter().maskBits;
		fixtureDef.filter.groupIndex = getFilter().groupIndex;
		return fixtureDef;
	}

	private Shape createSensorShape(ZootActor actor)
	{
		return ZootShapeFactory.createBox(sensorWidth, sensorHeight, sensorX, sensorY);		
	}
}
