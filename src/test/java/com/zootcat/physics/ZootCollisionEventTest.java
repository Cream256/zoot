package com.zootcat.physics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.zootcat.physics.ZootCollisionEvent;
import com.zootcat.physics.ZootCollisionEvent.Type;
import com.zootcat.scene.ZootActor;

public class ZootCollisionEventTest 
{
	@Mock private ZootActor actorA;
	@Mock private ZootActor actorB;
	@Mock private Contact contact;
	@Mock private Manifold manifold;
	@Mock private ContactImpulse contactImpulse;
		
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void setActorTest()
	{
		ZootCollisionEvent event = new ZootCollisionEvent();
		assertNull(event.getActorA());
		assertNull(event.getActorB());
		
		event.setActorA(actorA);
		event.setActorB(actorB);
		assertEquals(actorA, event.getActorA());
		assertEquals(actorB, event.getActorB());
	}
	
	@Test
	public void secondCtorTest()
	{		
		ZootCollisionEvent event = new ZootCollisionEvent(actorA, actorB, Type.BeginContact);
		assertEquals(actorA, event.getActorA());
		assertEquals(actorB, event.getActorB());
		assertEquals(Type.BeginContact, event.getType());
	}
	
	@Test
	public void getTypeTest()
	{
		ZootCollisionEvent beginContactEvent = new ZootCollisionEvent(actorA, actorB, Type.BeginContact);
		assertEquals(Type.BeginContact, beginContactEvent.getType());
		
		ZootCollisionEvent endContactEvent = new ZootCollisionEvent(actorA, actorB, Type.EndContact);
		assertEquals(Type.EndContact, endContactEvent.getType());
		
		ZootCollisionEvent preSolveEvent = new ZootCollisionEvent(actorA, actorB, Type.PreSolve);
		assertEquals(Type.PreSolve, preSolveEvent.getType());
		
		ZootCollisionEvent postSolveEvent = new ZootCollisionEvent(actorA, actorB, Type.PostSolve);
		assertEquals(Type.PostSolve, postSolveEvent.getType());
	}
	
	@Test
	public void setContactTest()
	{		
		ZootCollisionEvent event = new ZootCollisionEvent(actorA, actorB, Type.BeginContact);
		assertNull(event.getContact());
		
		event.setContact(contact);
		assertEquals(contact, event.getContact());
		
		event.setContact(null);
		assertNull(event.getContact());
	}
	
	@Test
	public void setManifoldTest()
	{		
		ZootCollisionEvent event = new ZootCollisionEvent(actorA, actorB, Type.BeginContact);
		assertNull(event.getManifold());
		
		event.setManifold(manifold);
		assertEquals(manifold, event.getManifold());
		
		event.setManifold(null);
		assertNull(event.getManifold());
	}
		
	@Test
	public void setContactImpulseTest()
	{		
		ZootCollisionEvent event = new ZootCollisionEvent(actorA, actorB, Type.BeginContact);
		assertNull(event.getContactImpulse());
		
		event.setContactImpulse(contactImpulse);
		assertEquals(contactImpulse, event.getContactImpulse());
		
		event.setContactImpulse(null);
		assertNull(event.getContactImpulse());
	}
	
	@Test
	public void setTypeTest()
	{
		ZootCollisionEvent event = new ZootCollisionEvent();
		assertEquals(Type.Unknown, event.getType());
		
		event.setType(Type.BeginContact);
		assertEquals(Type.BeginContact, event.getType());
		
		event.setType(Type.EndContact);
		assertEquals(Type.EndContact, event.getType());
		
		event.setType(Type.PostSolve);
		assertEquals(Type.PostSolve, event.getType());
		
		event.setType(Type.PreSolve);
		assertEquals(Type.PreSolve, event.getType());
	}
	
	@Test
	public void resetTest()
	{
		ZootCollisionEvent event = new ZootCollisionEvent(actorA, actorB, Type.PostSolve);
		event.setContact(contact);
		event.setManifold(manifold);
		event.setContactImpulse(contactImpulse);
		event.reset();
		
		assertNull(event.getActorA());
		assertNull(event.getActorB());
		assertNull(event.getContact());
		assertNull(event.getManifold());
		assertNull(event.getContactImpulse());
		assertEquals(Type.Unknown, event.getType());
	}

}
