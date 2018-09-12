package com.zootcat.actions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.scene.ZootActor;

public class ZootFireEventActionTest
{
	@Mock private ZootActor actor;
	private ZootFireEventAction action;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		action = new ZootFireEventAction();
	}
	
	@Test
	public void shouldSetEvent()
	{
		ZootEvent event = mock(ZootEvent.class);
		action.setEvent(event);
		assertEquals(event, action.getEvent());
	}
	
	@Test
	public void shouldSendEvent()
	{
		ZootEvent event = mock(ZootEvent.class);
		action.setEvent(event);
		action.setTarget(actor);
		action.act(1.0f);
		
		verify(actor).fire(event);
	}
	
	@Test
	public void shouldReturnTrue()
	{
		action.setEvent(mock(ZootEvent.class));
		action.setTarget(actor);
		assertTrue(action.act(1.0f));
	}
}
