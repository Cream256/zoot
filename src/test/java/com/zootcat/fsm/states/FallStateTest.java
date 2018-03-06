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
	private static final float JUMP_TIMEOUT = 1.0f;
	
	private FallState fallState;
	
	@Before
	public void setup()
	{
		super.setup();
		fallState = new FallState();
	}
	
	@Test
	public void shouldReturnId()
	{
		assertEquals(FallState.ID, fallState.getId());
	}
	
	@Test
	public void onEnterShouldSetFallAnimation()
	{
		fallState.onEnter(actor, null);
		verify(animatedSpriteCtrlMock).setAnimation(fallState.getName());
	}
	
	@Test
	public void handleGroundEvent()
	{
		assertTrue(fallState.handle(createEvent(ZootEventType.Ground)));
		assertEquals(IdleState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleHurtEvent()
	{
		assertTrue(fallState.handle(createEvent(ZootEventType.Hurt)));
		assertEquals(HurtState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleMoveEvent()
	{		
		int currentStateId = actor.getStateMachine().getCurrentState().getId();
		
		assertTrue(fallState.handle(createEvent(ZootEventType.WalkRight)));
		assertEquals("State should not change", currentStateId, actor.getStateMachine().getCurrentState().getId());
		verify(moveableCtrlMock, times(1)).moveInAir(ZootDirection.Right);
		
		assertTrue(fallState.handle(createEvent(ZootEventType.WalkLeft)));
		assertEquals("State should not change", currentStateId, actor.getStateMachine().getCurrentState().getId());
		verify(moveableCtrlMock, times(1)).moveInAir(ZootDirection.Left);		
		
		assertTrue(fallState.handle(createEvent(ZootEventType.RunRight)));
		assertEquals("State should not change", currentStateId, actor.getStateMachine().getCurrentState().getId());
		verify(moveableCtrlMock, times(2)).moveInAir(ZootDirection.Right);
		
		assertTrue(fallState.handle(createEvent(ZootEventType.RunLeft)));
		assertEquals("State should not change", currentStateId, actor.getStateMachine().getCurrentState().getId());
		verify(moveableCtrlMock, times(2)).moveInAir(ZootDirection.Left);
	}
	
	@Test
	public void handleClimbEvent()
	{
		assertTrue(fallState.handle(createEvent(ZootEventType.Grab)));
		assertEquals(ClimbState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleJumpUpEventOnDefaultJumpTimeoutShouldNotChangeState()
	{		
		actor.getStateMachine().changeState(fallState, null);
		assertTrue(fallState.handle(createEvent(ZootEventType.JumpUp)));
		assertEquals(FallState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleJumpForwardEventOnDefaultJumpTimeoutShouldNotChangeState()
	{	
		actor.getStateMachine().changeState(fallState, null);
		assertTrue(fallState.handle(createEvent(ZootEventType.JumpForward)));
		assertEquals(FallState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleJumpUpEventOnCustomJumpTimeoutShouldAllowForDelayedJump()
	{
		//when
		actor.getStateMachine().changeState(fallState, null);
		fallState.setAllowedJumpDelay(JUMP_TIMEOUT);
		fallState.onEnter(actor, null);
		
		//then
		assertTrue(fallState.handle(createEvent(ZootEventType.JumpUp)));
		assertEquals(JumpState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleJumpForwardEventOnCustomJumpTimeoutShouldAllowForDelayedJump()
	{
		//when
		actor.getStateMachine().changeState(fallState, null);
		fallState.setAllowedJumpDelay(JUMP_TIMEOUT);
		fallState.onEnter(actor, null);
		
		//then
		assertTrue(fallState.handle(createEvent(ZootEventType.JumpForward)));
		assertEquals(JumpForwardState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleJumpUpEventOnCustomJumpTimeoutAfterItExpiredShouldNotAllowForDelayedJump()
	{
		//when
		actor.getStateMachine().changeState(fallState, null);
		fallState.setAllowedJumpDelay(JUMP_TIMEOUT);
		fallState.onEnter(actor, null);
		fallState.onUpdate(actor, JUMP_TIMEOUT);
		
		//then
		assertTrue(fallState.handle(createEvent(ZootEventType.JumpUp)));
		assertEquals(FallState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleJumpForwardEventOnCustomJumpTimeoutAfterItExpiredShouldNotAllowForDelayedJump()
	{
		//when
		actor.getStateMachine().changeState(fallState, null);
		fallState.setAllowedJumpDelay(JUMP_TIMEOUT);
		fallState.onEnter(actor, null);
		fallState.onUpdate(actor, JUMP_TIMEOUT);
		
		//then
		assertTrue(fallState.handle(createEvent(ZootEventType.JumpForward)));
		assertEquals(FallState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleJumpForwardEventOnCustomJumpTimeoutWhenEnteringFromJumpStateShouldNotAllowForDelayedJump()
	{
		//when
		actor.getStateMachine().changeState(new JumpState(), null);
		actor.getStateMachine().changeState(fallState, null);
		fallState.setAllowedJumpDelay(JUMP_TIMEOUT);
		fallState.onEnter(actor, null);
		
		//then
		assertTrue(fallState.handle(createEvent(ZootEventType.JumpForward)));
		assertEquals(FallState.ID, actor.getStateMachine().getCurrentState().getId());		
	}
	
	@Test
	public void handleJumpUpEventOnCustomJumpTimeoutWhenEnteringFromJumpStateShouldNotAllowForDelayedJump()
	{
		//when
		actor.getStateMachine().changeState(new JumpState(), null);
		actor.getStateMachine().changeState(fallState, null);
		fallState.setAllowedJumpDelay(JUMP_TIMEOUT);
		fallState.onEnter(actor, null);
		
		//then
		assertTrue(fallState.handle(createEvent(ZootEventType.JumpUp)));
		assertEquals(FallState.ID, actor.getStateMachine().getCurrentState().getId());		
	}
}
