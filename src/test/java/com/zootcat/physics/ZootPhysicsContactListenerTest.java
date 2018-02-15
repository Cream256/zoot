package com.zootcat.physics;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.zootcat.physics.ZootCollisionEvent.Type;
import com.zootcat.scene.ZootActor;

public class ZootPhysicsContactListenerTest 
{
	@Mock private Body bodyA;
	@Mock private Body bodyB;
	@Mock private Fixture fixtureA;
	@Mock private Fixture fixtureB;
	@Mock private ZootActor actorA;
	@Mock private ZootActor actorB;
	@Mock private Contact contact;
	@Mock private Manifold oldManifold;
	@Mock private ContactImpulse contactImpulse;
	@Captor private ArgumentCaptor<ZootCollisionEvent> eventCaptor;
	private ZootPhysicsContactListener listener;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);	
		when(contact.getFixtureA()).thenReturn(fixtureA);
		when(contact.getFixtureB()).thenReturn(fixtureB);
		when(fixtureA.getBody()).thenReturn(bodyA);
		when(fixtureB.getBody()).thenReturn(bodyB);		
		when(bodyA.getUserData()).thenReturn(actorA);
		when(bodyB.getUserData()).thenReturn(actorB);
		listener = new ZootPhysicsContactListener();
	}
	
	@Test
	public void beginContactTest()
	{
		//when
		listener.beginContact(contact);		
				
		//then
		verify(actorA, times(1)).fire(anyObject());
		verify(actorA).fire(eventCaptor.capture());
		
		ZootCollisionEvent eventA = eventCaptor.getValue();
		assertEquals(actorA, eventA.getActorA());
		assertEquals(actorB, eventA.getActorB());
		assertEquals(contact, eventA.getContact());
		assertEquals(Type.BeginContact, eventA.getType());
		assertNull(eventA.getManifold());
		assertNull(eventA.getContactImpulse());
				
		verify(actorB, times(1)).fire(anyObject());
		verify(actorB).fire(eventCaptor.capture());
		
		ZootCollisionEvent eventB = eventCaptor.getValue();
		assertEquals(actorA, eventB.getActorA());
		assertEquals(actorB, eventB.getActorB());
		assertEquals(contact, eventB.getContact());
		assertEquals(Type.BeginContact, eventB.getType());
		assertNull(eventB.getManifold());
		assertNull(eventB.getContactImpulse());
	}
	
	@Test
	public void endContactTest()
	{
		//when
		listener.endContact(contact);		
				
		//then
		verify(actorA, times(1)).fire(anyObject());
		verify(actorA).fire(eventCaptor.capture());
		
		ZootCollisionEvent eventA = eventCaptor.getValue();
		assertEquals(actorA, eventA.getActorA());
		assertEquals(actorB, eventA.getActorB());
		assertEquals(contact, eventA.getContact());
		assertEquals(Type.EndContact, eventA.getType());
		assertNull(eventA.getManifold());
		assertNull(eventA.getContactImpulse());
				
		verify(actorB, times(1)).fire(anyObject());
		verify(actorB).fire(eventCaptor.capture());
		
		ZootCollisionEvent eventB = eventCaptor.getValue();
		assertEquals(actorA, eventB.getActorA());
		assertEquals(actorB, eventB.getActorB());
		assertEquals(contact, eventB.getContact());
		assertEquals(Type.EndContact, eventB.getType());
		assertNull(eventB.getManifold());
		assertNull(eventB.getContactImpulse());
	}
	
	@Test
	public void preSolveTest()
	{
		//when
		listener.preSolve(contact, oldManifold);		
				
		//then
		verify(actorA, times(1)).fire(anyObject());
		verify(actorA).fire(eventCaptor.capture());
		
		ZootCollisionEvent eventA = eventCaptor.getValue();
		assertEquals(actorA, eventA.getActorA());
		assertEquals(actorB, eventA.getActorB());
		assertEquals(contact, eventA.getContact());
		assertEquals(Type.PreSolve, eventA.getType());
		assertEquals(oldManifold, eventA.getManifold());
		assertNull(eventA.getContactImpulse());
				
		verify(actorB, times(1)).fire(anyObject());
		verify(actorB).fire(eventCaptor.capture());
		
		ZootCollisionEvent eventB = eventCaptor.getValue();
		assertEquals(actorA, eventB.getActorA());
		assertEquals(actorB, eventB.getActorB());
		assertEquals(contact, eventB.getContact());
		assertEquals(Type.PreSolve, eventB.getType());
		assertEquals(oldManifold, eventB.getManifold());
		assertNull(eventB.getContactImpulse());
	}
	
	@Test
	public void postSolveTest()
	{
		//when
		listener.postSolve(contact, contactImpulse);		
				
		//then
		verify(actorA, times(1)).fire(anyObject());
		verify(actorA).fire(eventCaptor.capture());
		
		ZootCollisionEvent eventA = eventCaptor.getValue();
		assertEquals(actorA, eventA.getActorA());
		assertEquals(actorB, eventA.getActorB());
		assertEquals(contact, eventA.getContact());
		assertEquals(Type.PostSolve, eventA.getType());
		assertNull(eventA.getManifold());
		assertEquals(contactImpulse, eventA.getContactImpulse());
				
		verify(actorB, times(1)).fire(anyObject());
		verify(actorB).fire(eventCaptor.capture());
		
		ZootCollisionEvent eventB = eventCaptor.getValue();
		assertEquals(actorA, eventB.getActorA());
		assertEquals(actorB, eventB.getActorB());
		assertEquals(contact, eventB.getContact());
		assertEquals(Type.PostSolve, eventB.getType());
		assertNull(eventB.getManifold());
		assertEquals(contactImpulse, eventB.getContactImpulse());
	}
	
}
