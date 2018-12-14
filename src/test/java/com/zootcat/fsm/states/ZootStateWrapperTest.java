package com.zootcat.fsm.states;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.zootcat.fsm.ZootState;
import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.scene.ZootActor;

public class ZootStateWrapperTest
{
	private ZootState wrappedState;
	private ZootStateWrapper stateWrapper;
	
	@Before
	public void setup()
	{
		wrappedState = mock(ZootState.class);
		stateWrapper = new ZootStateWrapper(wrappedState);
	}
	
	@Test
	public void shouldReturnWrappedState()
	{
		assertEquals(wrappedState, stateWrapper.getWrappedState());
	}
	
	@Test
	public void shouldReturnWrappedId()
	{
		when(wrappedState.getId()).thenReturn(128);
		assertEquals(128, stateWrapper.getId());		
	}
	
	@Test
	public void shouldReturnWrappedToString()
	{
		when(wrappedState.toString()).thenReturn("Run");
		assertEquals("(Wrapped) Run", stateWrapper.toString());
	}
	
	@Test
	public void shouldReturnWrappedName()
	{
		when(wrappedState.getName()).thenReturn("Name");
		assertEquals("Name", stateWrapper.getName());
	}
	
	@Test
	public void shouldWrapOnEnter()
	{
		ZootActor actor = mock(ZootActor.class);
		ZootEvent event = mock(ZootEvent.class);		
		stateWrapper.onEnter(actor, event);
		
		verify(wrappedState).onEnter(actor, event);
	}
	
	@Test
	public void shouldWrapOnLeave()
	{
		ZootActor actor = mock(ZootActor.class);
		ZootEvent event = mock(ZootEvent.class);		
		stateWrapper.onLeave(actor, event);
		
		verify(wrappedState).onLeave(actor, event);
	}
	
	@Test
	public void shouldWrapOnUpdate()
	{
		ZootActor actor = mock(ZootActor.class);		
		stateWrapper.onUpdate(actor, 1.23f);
		
		verify(wrappedState).onUpdate(actor, 1.23f);
	}
	
	@Test
	public void shouldWrapHandle()
	{
		ZootEvent event = mock(ZootEvent.class);		
		when(wrappedState.handle(event)).thenReturn(true);
		
		assertTrue(stateWrapper.handle(event));		
		verify(wrappedState).handle(event);
	}
}
