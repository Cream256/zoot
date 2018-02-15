package com.zootcat.fsm.states;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Test;

import com.zootcat.events.ZootEvent;
import com.zootcat.scene.ZootActor;

public class BasicStateTest
{
	@Test
	public void hashCodeTest()
	{
		BasicState state1 = new BasicState("test");
		BasicState state2 = new BasicState("test");
		BasicState state3 = new BasicState("TEST");
		
		assertEquals(state1.hashCode(), state2.hashCode());
		assertFalse(state2.hashCode() == state3.hashCode());
	}
		
	@Test
	public void equalsTest()
	{
		BasicState state1 = new BasicState("test");
		BasicState state2 = new BasicState("test");
		BasicState state3 = new BasicState("TEST");
		
		assertTrue(state1.equals(state2));
		assertTrue(state1.equals(state1));
		assertTrue(state2.equals(state1));
		assertTrue(state2.equals(state2));
		assertFalse(state1.equals(state3));
		assertFalse(state2.equals(state3));
		assertFalse(state3.equals(state1));
		assertFalse(state3.equals(state2));
		
		assertFalse(state1.equals(1));
		assertFalse(state1.equals(null));
		assertFalse(state1.equals("this is a string"));		
	}
	
	@Test
	public void getIdTest()
	{
		BasicState state1 = new BasicState("test");
		BasicState state2 = new BasicState("test");
		BasicState state3 = new BasicState("TEST");
		
		assertEquals("test".hashCode(), state1.getId());
		assertEquals("test".hashCode(), state2.getId());
		assertEquals("TEST".hashCode(), state3.getId());
		assertEquals(state1.getId(), state2.getId());
		assertFalse(state2.getId() == state3.getId());
	}
	
	@Test
	public void getNameTest()
	{
		assertEquals("ABC", new BasicState("ABC").getName());
		assertEquals("test", new BasicState("test").getName());
		assertEquals("", new BasicState("").getName());
	}
	
	@Test
	public void toStringTest()
	{
		assertEquals("ABC", new BasicState("ABC").toString());
		assertEquals("test", new BasicState("test").toString());
		assertEquals("", new BasicState("").toString());		
	}
	
	@Test
	public void handleTest()
	{
		assertFalse("NamedState should always return false", new BasicState("").handle(new ZootEvent()));
	}
	
	@Test
	public void actionsShouldNotCauseAnySideEffectsTest()
	{
		//given
		BasicState state = new BasicState("test");
		ZootActor actor = mock(ZootActor.class);
		
		//when
		state.onEnter(actor, null);
		state.onUpdate(actor, 0.0f);
		state.onLeave(actor, null);
		
		//then
		verifyZeroInteractions(actor);		
	}
}
