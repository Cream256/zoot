package com.zootcat.fsm.states;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.scene.ZootDirection;
import com.zootcat.testing.ZootStateTestCase;

public class JumpForwardStateTest extends ZootStateTestCase
{	
	private JumpForwardState jumpForwardState;
	
	@Before
	public void setup()
	{
		super.setup();
		jumpForwardState = new JumpForwardState();
	}
	
	@Test
	public void shouldReturnId()
	{
		assertEquals(JumpForwardState.ID, jumpForwardState.getId());
	}
	
	@Test
	public void shouldSetJumpingAnimationOnEnteringState()
	{
		jumpForwardState.onEnter(actor, createEvent(ZootEventType.JumpForward));
		verify(animatedSpriteCtrlMock).setAnimation(jumpForwardState.getName());
	}
			
	@Test
	public void shouldPerformJumpForwardOnEnteringState()
	{
		when(directionCtrlMock.getDirection()).thenReturn(ZootDirection.Right);
		jumpForwardState.onEnter(actor, createEvent(ZootEventType.JumpForward));
		verify(walkableCtrlMock).jumpForward(ZootDirection.Right, false);
	}
	
	@Test
	public void handleFallEvent()
	{
		assertTrue(jumpForwardState.handle(createEvent(ZootEventType.Fall)));
		assertEquals(FallForwardState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleAnyOtherEventShouldWorkLikeInBaseClass()
	{
		assertTrue(jumpForwardState.handle(createEvent(ZootEventType.Hurt)));
		assertEquals(HurtState.ID, actor.getStateMachine().getCurrentState().getId());	
	}
	
	@Test
	public void shouldReturnProperName()
	{
		assertEquals(JumpForwardState.NAME, jumpForwardState.getName());
	}
}
