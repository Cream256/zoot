package com.zootcat.physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.zootcat.scene.ZootActor;

public class ZootCollisionEvent extends Event 
{
	private Type type = Type.Unknown;
	private ZootActor actorA;
	private ZootActor actorB;
	private Contact contact;
	private Manifold manifold;
	private ContactImpulse contactImpulse;
		
	public ZootCollisionEvent()
	{
		//empty ctor for pooling
	}
	
	public ZootCollisionEvent(ZootActor actorA, ZootActor actorB, Type type)
	{
		this.actorA = actorA;
		this.actorB = actorB;
		this.type = type;
	}
	
	public void setActorA(ZootActor actorA)
	{
		this.actorA = actorA;
	}
	
	public ZootActor getActorA()
	{
		return actorA;
	}
	
	public void setActorB(ZootActor actorB)
	{
		this.actorB = actorB;
	}
	
	public ZootActor getActorB()
	{
		return actorB;
	}
	
	public void setType(Type type)
	{
		this.type = type;
	}
	
	public Type getType()
	{
		return type;
	}
	
	public void setContact(Contact contact)
	{
		this.contact = contact;
	}
	
	public Contact getContact()
	{
		return contact;
	}
	
	public Manifold getManifold()
	{
		return manifold;
	}
	
	public void setManifold(Manifold manifold)
	{
		this.manifold = manifold;
	}
	
	public ContactImpulse getContactImpulse()
	{
		return contactImpulse;
	}
	
	public void setContactImpulse(ContactImpulse contactImpulse)
	{
		this.contactImpulse = contactImpulse;
	}
	
	@Override
	public String toString () 
	{
		return type.toString();
	}
	
	@Override
	public void reset()
	{
		actorA = null;
		actorB = null;
		contact = null;
		manifold = null;
		contactImpulse = null;
		type = Type.Unknown;
		super.reset();
	}
	
	static public enum Type
	{
		Unknown, BeginContact, EndContact, PreSolve, PostSolve;
	}
	
}
