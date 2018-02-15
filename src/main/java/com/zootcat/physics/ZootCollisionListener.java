package com.zootcat.physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.scene.ZootActor;

public abstract class ZootCollisionListener implements EventListener 
{
	@Override
	public boolean handle (Event e) 
	{
		if (!(e instanceof ZootCollisionEvent)) return false;
		ZootCollisionEvent event = (ZootCollisionEvent)e;

		switch(event.getType())
		{
		case BeginContact:
			beginContact(event.getActorA(), event.getActorB(), event.getContact());
			return true;
		
		case EndContact:
			endContact(event.getActorA(), event.getActorB(), event.getContact());
			return true;
			
		case PreSolve:
			preSolve(event.getActorA(), event.getActorB(), event.getContact(), event.getManifold());
			return true;
		
		case PostSolve:
			postSolve(event.getActorA(), event.getActorB(), event.getContactImpulse());
			return true;
			
		default:
			throw new RuntimeZootException("Unsupported collision event type: " + event.getType());
		}
	}

	public abstract void beginContact(ZootActor actorA, ZootActor actorB, Contact contact); 	
	
	public abstract void endContact(ZootActor actorA, ZootActor actorB, Contact contact);	
	
	public abstract void preSolve(ZootActor actorA, ZootActor actorB, Contact contact, Manifold manifold);
	
	public abstract void postSolve(ZootActor actorA, ZootActor actorB, ContactImpulse contactImpulse);
}
