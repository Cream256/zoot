package com.zootcat.fsm.states;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.zootcat.fsm.ZootState;
import com.zootcat.fsm.ZootStateMachine;
import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.events.ZootEvents;
import com.zootcat.scene.ZootActor;

public class ForwardingStateTest
{
	private static final int ORIGINAL_STATE_ID = 128;
	private static final int FORWARDED_STATE_ID = 256;
	
	@Mock private ZootActor actor;
	@Mock private ZootStateMachine stateMachine;
	@Mock private ZootState expectedState;
	@Mock private ZootState initialState;
	private ForwardingState forwardingState;
		
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		when(actor.getStateMachine()).thenReturn(stateMachine);
		
		forwardingState = new ForwardingState(ORIGINAL_STATE_ID, FORWARDED_STATE_ID);
	}
	
	@Test
	public void shouldReturnOriginalStateIdAsId()
	{
		assertEquals(ORIGINAL_STATE_ID, forwardingState.getId());
	}
	
	@Test
	public void shouldReturnForwardingStateId()
	{
		assertEquals(FORWARDED_STATE_ID, forwardingState.getForwardedStateId());
	}
	
	@Test
	public void shouldChangeToForwardedStateOnUpdate()
	{
		//given
		ArgumentCaptor<ZootEvent> captor = ArgumentCaptor.forClass(ZootEvent.class);
		
		String expectedUserObject = "text";
		ZootEvent expectedEvent = ZootEvents.get(ZootEventType.InitEvent);
		expectedEvent.setUserObject(expectedUserObject);
		
		//when
		when(stateMachine.getStateById(ORIGINAL_STATE_ID)).thenReturn(initialState);
		when(stateMachine.getStateById(FORWARDED_STATE_ID)).thenReturn(expectedState);		
		forwardingState.onEnter(actor, expectedEvent);		
		
		//then
		verify(stateMachine).changeState(eq(expectedState), captor.capture());
		assertNotNull(captor.getValue());
		assertEquals(ZootEventType.InitEvent, captor.getValue().getType());
		assertEquals(expectedUserObject, captor.getValue().getUserObject(Object.class));
		assertEquals(actor, captor.getValue().getTargetZootActor());
	}
	
	@Test
	public void shouldSendNoneEventWhenChangingToForwardedState()
	{
		//given
		ArgumentCaptor<ZootEvent> captor = ArgumentCaptor.forClass(ZootEvent.class);
		
		//when
		when(stateMachine.getStateById(ORIGINAL_STATE_ID)).thenReturn(initialState);
		when(stateMachine.getStateById(FORWARDED_STATE_ID)).thenReturn(expectedState);		
		forwardingState.onEnter(actor, null);		
		
		//then
		verify(stateMachine).changeState(eq(expectedState), captor.capture());
		assertNotNull(captor.getValue());
		assertEquals(ZootEventType.None, captor.getValue().getType());
	}
	
	@Test
	public void shouldReturnValidStateName()
	{
		assertEquals("Forwarding", forwardingState.getName());
	}
}
