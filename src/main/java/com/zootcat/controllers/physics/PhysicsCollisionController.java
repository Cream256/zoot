package com.zootcat.controllers.physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.zootcat.controllers.Controller;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.physics.ZootCollisionListener;
import com.zootcat.scene.ZootActor;

public abstract class PhysicsCollisionController extends ZootCollisionListener implements Controller 
{
	private ZootActor controllerActor;
	private boolean enabled = true;
	
	@Override
	public void init(ZootActor actor)	
	{
		controllerActor = actor;
	}

	@Override
	public void onAdd(ZootActor actor) 
	{
		actor.addListener(this);
	}

	@Override
	public void onRemove(ZootActor actor) 
	{
		actor.removeListener(this);
	}

	@Override
	public void onUpdate(float delta, ZootActor actor) 
	{
		//noop
	}
	
	@Override
	public final void beginContact(ZootActor actorA, ZootActor actorB, Contact contact)
	{						
		if(!isEnabled()) return;		
		onBeginContact(actorA, actorB, contact);
	}
	
	public abstract void onBeginContact(ZootActor actorA, ZootActor actorB, Contact contact);

	@Override
	public final void endContact(ZootActor actorA, ZootActor actorB, Contact contact)
	{		
		if(!isEnabled()) return;
		onEndContact(actorA, actorB, contact);
	}
	
	public abstract void onEndContact(ZootActor actorA, ZootActor actorB, Contact contact);
		
	@Override
	public final void preSolve(ZootActor actorA, ZootActor actorB, Contact contact, Manifold manifold)
	{
		if(!isEnabled()) return;
		onPreSolve(actorA, actorB, contact, manifold);
	}
	
	public abstract void onPreSolve(ZootActor actorA, ZootActor actorB, Contact contact, Manifold manifold);
	
	@Override
	public final void postSolve(ZootActor actorA, ZootActor actorB, ContactImpulse contactImpulse)
	{
		if(!isEnabled()) return;
		onPostSolve(actorA, actorB, contactImpulse);
	}
	
	public abstract void onPostSolve(ZootActor actorA, ZootActor actorB, ContactImpulse contactImpulse);
	
	@Override
	public boolean isEnabled()
	{
		return enabled;
	}
	
	@Override
	public void setEnabled(boolean value)
	{
		enabled = value;
	}
	
	public ZootActor getOtherActor(ZootActor actorA, ZootActor actorB)
	{
		return actorA == getControllerActor() ? actorB : actorA;
	}
	
	public Fixture getOtherFixture(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		return (actorA == getControllerActor()) ? contact.getFixtureB() : contact.getFixtureA();
	}
	
	public Fixture getControllerActorFixture(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		return (actorA == getControllerActor()) ? contact.getFixtureA() : contact.getFixtureB();
	}
	
	public ZootActor getControllerActor()
	{
		if(controllerActor == null)
		{
			throw new RuntimeZootException("PhysicsCollisionController::init() was not called.");
		}
		return controllerActor;
	}
}
