package com.zootcat.physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.zootcat.physics.ZootCollisionEvent.Type;
import com.zootcat.scene.ZootActor;

public class ZootPhysicsContactListener implements ContactListener 
{
	private final ZootCollisionEvent event = new ZootCollisionEvent();
	
	@Override
	public void beginContact(Contact contact) 
	{
		ZootActor actorA = (ZootActor) contact.getFixtureA().getBody().getUserData();
		ZootActor actorB = (ZootActor) contact.getFixtureB().getBody().getUserData();			
		setupEvent(actorA, actorB, Type.BeginContact, contact, null, null);		
		fireEventOnActors(actorA, actorB);
	}

	@Override
	public void endContact(Contact contact) 
	{
		ZootActor actorA = (ZootActor) contact.getFixtureA().getBody().getUserData();
		ZootActor actorB = (ZootActor) contact.getFixtureB().getBody().getUserData();		
	
		setupEvent(actorA, actorB, Type.EndContact, contact, null, null);		
		fireEventOnActors(actorA, actorB);
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) 
	{
		ZootActor actorA = (ZootActor) contact.getFixtureA().getBody().getUserData();
		ZootActor actorB = (ZootActor) contact.getFixtureB().getBody().getUserData();	
	
		setupEvent(actorA, actorB, Type.PreSolve, contact, oldManifold, null);		
		fireEventOnActors(actorA, actorB);
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) 
	{
		ZootActor actorA = (ZootActor) contact.getFixtureA().getBody().getUserData();
		ZootActor actorB = (ZootActor) contact.getFixtureB().getBody().getUserData();
		setupEvent(actorA, actorB, Type.PostSolve, contact, null, impulse);		
		fireEventOnActors(actorA, actorB);
	}
	
	private void setupEvent(ZootActor actorA, ZootActor actorB, Type type, Contact contact, Manifold manifold, ContactImpulse impulse) 
	{
		event.reset();
		event.setActorA(actorA);
		event.setActorB(actorB);
		event.setType(type);
		event.setContact(contact);
		event.setManifold(manifold);
		event.setContactImpulse(impulse);
	}
	
	private void fireEventOnActors(ZootActor actorA, ZootActor actorB)
	{
		actorA.fire(event);
		actorB.fire(event);
	}
}
