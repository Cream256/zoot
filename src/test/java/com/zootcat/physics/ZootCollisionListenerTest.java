package com.zootcat.physics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.events.ZootEvents;
import com.zootcat.physics.ZootCollisionEvent.Type;
import com.zootcat.scene.ZootActor;

public class ZootCollisionListenerTest
{
	private ZootCollisionListener listener;	
	private int postSolveCount;
	private int preSolveCount;
	private int endContactCount;
	private int beginContactCount;
	
	@Mock private ZootActor actorA;
	@Mock private ZootActor actorB;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		beginContactCount = 0;
		endContactCount = 0;
		preSolveCount = 0;
		postSolveCount = 0;
				
		listener = new ZootCollisionListener(){
			@Override
			public void beginContact(ZootActor actorA, ZootActor actorB, Contact contact)
			{
				++beginContactCount;
			}

			@Override
			public void endContact(ZootActor actorA, ZootActor actorB, Contact contact)
			{
				++endContactCount;
			}

			@Override
			public void preSolve(ZootActor actorA, ZootActor actorB, Contact contact, Manifold manifold)
			{
				++preSolveCount;
			}

			@Override
			public void postSolve(ZootActor actorA, ZootActor actorB, ContactImpulse contactImpulse)
			{
				++postSolveCount;
			}};
	}
	
	@Test
	public void shouldReturnFalseForNonZootCollisionEvent()
	{
		Arrays.stream(ZootEventType.values())
			  .forEach(type -> assertFalse(listener.handle(ZootEvents.get(type))));
	}
	
	@Test
	public void shouldReturnTrueOnZootCollisionEvent()
	{
		assertTrue(listener.handle(new ZootCollisionEvent(actorA, actorB, Type.BeginContact)));
	}
	
	@Test(expected = RuntimeZootException.class)
	public void shouldThrowOnUnknownZootCollisionType()
	{
		ZootCollisionEvent event = new ZootCollisionEvent();
		event.setType(Type.Unknown);
		listener.handle(event);
	}
	
	@Test
	public void shouldInvokeBeginContact()
	{
		ZootCollisionEvent event = new ZootCollisionEvent(actorA, actorB, Type.BeginContact);
		assertTrue(listener.handle(event));
		assertEquals(1, beginContactCount);
	}
	
	@Test
	public void shouldInvokeEndContact()
	{
		ZootCollisionEvent event = new ZootCollisionEvent(actorA, actorB, Type.EndContact);
		assertTrue(listener.handle(event));
		assertEquals(1, endContactCount);
	}
	
	@Test
	public void shouldInvokePreSolve()
	{
		ZootCollisionEvent event = new ZootCollisionEvent(actorA, actorB, Type.PreSolve);
		assertTrue(listener.handle(event));
		assertEquals(1, preSolveCount);
	}
	
	@Test
	public void shouldPostPreSolve()
	{
		ZootCollisionEvent event = new ZootCollisionEvent(actorA, actorB, Type.PostSolve);
		assertTrue(listener.handle(event));
		assertEquals(1, postSolveCount);
	}
}
