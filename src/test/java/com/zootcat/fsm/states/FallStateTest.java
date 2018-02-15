package com.zootcat.fsm.states;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.zootcat.events.ZootEventType;
import com.zootcat.scene.ZootDirection;
import com.zootcat.testing.ZootStateTestCase;

public class FallStateTest extends ZootStateTestCase
{
	private FallState fallState;
	
	@Before
	public void setup()
	{
		super.setup();
		fallState = new FallState();
	}
	
	@Test
	public void getIdTest()
	{
		assertEquals(FallState.ID, fallState.getId());
	}
	
	@Test
	public void onEnterShouldSetFallAnimationTest()
	{
		fallState.onEnter(actor, null);
		verify(animatedSpriteCtrlMock).setAnimation(fallState.getName());
	}
	
	@Test
	public void handleGroundEventTest()
	{
		assertTrue(fallState.handle(createEvent(ZootEventType.Ground)));
		assertEquals(IdleState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleHurtEventTest()
	{
		assertTrue(fallState.handle(createEvent(ZootEventType.Hurt)));
		assertEquals(HurtState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleMoveEventTest()
	{		
		int currentStateId = actor.getStateMachine().getCurrentState().getId();
		
		assertTrue(fallState.handle(createEvent(ZootEventType.WalkRight)));
		assertEquals("State should not change", currentStateId, actor.getStateMachine().getCurrentState().getId());
		verify(moveableCtrlMock, times(1)).walk(ZootDirection.Right);
		
		assertTrue(fallState.handle(createEvent(ZootEventType.WalkLeft)));
		assertEquals("State should not change", currentStateId, actor.getStateMachine().getCurrentState().getId());
		verify(moveableCtrlMock, times(1)).walk(ZootDirection.Left);		
		
		assertTrue(fallState.handle(createEvent(ZootEventType.RunRight)));
		assertEquals("State should not change", currentStateId, actor.getStateMachine().getCurrentState().getId());
		verify(moveableCtrlMock, times(2)).walk(ZootDirection.Right);
		
		assertTrue(fallState.handle(createEvent(ZootEventType.RunLeft)));
		assertEquals("State should not change", currentStateId, actor.getStateMachine().getCurrentState().getId());
		verify(moveableCtrlMock, times(2)).walk(ZootDirection.Left);
	}
}
