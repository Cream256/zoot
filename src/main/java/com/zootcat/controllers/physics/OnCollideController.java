package com.zootcat.controllers.physics;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.physics.ZootDefaultContactFilter;
import com.zootcat.scene.ZootActor;
import com.zootcat.utils.BitMaskConverter;

/**
 * OnCollide Controller - abstract class used to do some action when 
 * collision begins and ends. 
 * 
 * OnEnter and OnLeave will be executed per actor, so even if Box2D 
 * uses collision on fixture level, OnEnter/OnLeave will be executed 
 * only once even if actor has different fixtures assigned to single
 * box2d body.
 * 
 * Override:
 * onEnter - will be executed when collision begins
 * onLeave - will be executed when collision ends
 * 
 * @ctrlParam category - category name for collision detection,
 * if nothing is given a default value will be used
 * 
 * @ctrlParam mask - categories for which the collision will take place, separated with "|". 
 * If nothing is given, mask that collides with everything will be used.
 * 
 * @ctrlParam collideWithSensors - if sensors should count to collision, default true 
 * 
 * @author Cream
 */
public abstract class OnCollideController extends PhysicsCollisionController
{
	@CtrlParam(debug = true) private String category = null;
	@CtrlParam(debug = true) private String mask = null;
	@CtrlParam(debug = true) private boolean collideWithSensors = true;
	
	private Filter filter;
	private ZootActor controllerActor;
	private Set<ZootActor> collidingActors = new HashSet<ZootActor>();
		
	@Override
	public void init(ZootActor actor)
	{		
		controllerActor = actor;						
		collidingActors.clear();
		
		filter = new Filter();	
		filter.maskBits = BitMaskConverter.Instance.fromString(mask);
		if(category != null && !category.isEmpty())
		{
			filter.categoryBits = BitMaskConverter.Instance.fromString(category);
		}
	}
		
	@Override
	public void beginContact(ZootActor actorA, ZootActor actorB, Contact contact)
	{				
		ZootActor otherActor = getOtherActor(actorA, actorB);
		if(collides(actorA, actorB, contact) && !collidingActors.contains(otherActor))
		{
			collidingActors.add(otherActor);
			onEnter(actorA, actorB, contact);
		}
	}
	
	@Override
	public void endContact(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		ZootActor otherActor = getOtherActor(actorA, actorB);
		if(collides(actorA, actorB, contact) && collidingActors.contains(otherActor))
		{			
			onLeave(actorA, actorB, contact);
			collidingActors.remove(otherActor);
		}
	}
	
	@Override
	public void preSolve(ZootActor actorA, ZootActor actorB, Contact contact, Manifold manifold)
	{
		//noop
	}
	
	@Override
	public void postSolve(ZootActor actorA, ZootActor actorB, ContactImpulse contactImpulse)
	{
		//noop
	}
		
	public ZootActor getControllerActor()
	{
		return controllerActor;
	}
	
	public abstract void onEnter(ZootActor actorA, ZootActor actorB, Contact contact);
	
	public abstract void onLeave(ZootActor actorA, ZootActor actorB, Contact contact);
	
	protected ZootActor getOtherActor(ZootActor actorA, ZootActor actorB)
	{
		return actorA == controllerActor ? actorB : actorA;
	}
	
	protected Fixture getOtherFixture(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		return (actorA == controllerActor) ? contact.getFixtureB() : contact.getFixtureA();
	}
	
	protected Fixture getControllerActorFixture(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		return (actorA == controllerActor) ? contact.getFixtureA() : contact.getFixtureB();
	}
	
	private boolean collides(ZootActor actorA, ZootActor actorB, Contact contact)
	{				
		Fixture otherFixture = getOtherFixture(actorA, actorB, contact);
		
		boolean collisionDetected = ZootDefaultContactFilter.shouldCollide(filter, otherFixture.getFilterData());
		boolean sensorOk = collideWithSensors || !otherFixture.isSensor();
		return collisionDetected && sensorOk;
	}
}