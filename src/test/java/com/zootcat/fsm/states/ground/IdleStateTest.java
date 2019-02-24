package com.zootcat.fsm.states.ground;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.states.DeadState;
import com.zootcat.fsm.states.HurtState;
import com.zootcat.fsm.states.StunState;
import com.zootcat.testing.ZootStateTestCase;
import com.zootcat.utils.ZootDirection;

public class IdleStateTest extends ZootStateTestCase
{
	private IdleState idleState;	
		
	@Before
	public void setup()
	{		
		super.setup();
		idleState = new IdleState();
	}
	
	@Test
	public void shouldReturnValidId()
	{
		assertEquals(IdleState.ID, idleState.getId());
	}
	
	@Test
	public void shouldSetIdleAnimationOnEnter()
	{
		idleState.onEnter(actor, null);
		verify(animatedSpriteCtrlMock).setAnimation(idleState.getName());
	}
	
	@Test
	public void shouldShouldZeroHoritontalVelocityActorOnEnter()
	{
		idleState.onEnter(actor, null);
		verify(walkableCtrlMock).stop();
	}
	
	@Test
	public void shouldHandleRunEvent()
	{
		//when
		when(directionCtrlMock.getDirection()).thenReturn(ZootDirection.Right);
		when(walkableCtrlMock.canRun()).thenReturn(true);
		idleState.onEnter(actor, null);
		
		//then
		assertTrue(idleState.handle(createEvent(ZootEventType.RunRight)));
		assertEquals(RunState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void shouldHandleRunEventWhenActorCantRun()
	{
		//when
		when(directionCtrlMock.getDirection()).thenReturn(ZootDirection.Right);
		when(walkableCtrlMock.canRun()).thenReturn(false);
		idleState.onEnter(actor, null);
		
		//then
		assertTrue(idleState.handle(createEvent(ZootEventType.RunRight)));
		assertEquals(WalkState.ID, actor.getStateMachine().getCurrentState().getId());
	}
		
	@Test
	public void shouldHandleWalkEventAtTheSameDirection()
	{
		//when
		when(directionCtrlMock.getDirection()).thenReturn(ZootDirection.Right);		
		idleState.onEnter(actor, null);
		
		//then
		assertTrue(idleState.handle(createEvent(ZootEventType.WalkRight)));
		assertEquals(WalkState.ID, actor.getStateMachine().getCurrentState().getId());
		
		//when
		when(directionCtrlMock.getDirection()).thenReturn(ZootDirection.Left);
		idleState.onEnter(actor, null);
		
		//then
		assertTrue(idleState.handle(createEvent(ZootEventType.WalkLeft)));
		assertEquals(WalkState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void shouldHandleWalkEventAtDifferentDirection()
	{
		//when
		when(directionCtrlMock.getDirection()).thenReturn(ZootDirection.Left);		
		idleState.onEnter(actor, null);
		
		//then
		assertTrue(idleState.handle(createEvent(ZootEventType.WalkRight)));
		assertEquals(TurnState.ID, actor.getStateMachine().getCurrentState().getId());
		
		//when
		when(directionCtrlMock.getDirection()).thenReturn(ZootDirection.Right);
		idleState.onEnter(actor, null);
		
		//then
		assertTrue(idleState.handle(createEvent(ZootEventType.WalkLeft)));
		assertEquals(TurnState.ID, actor.getStateMachine().getCurrentState().getId());		
	}
		
	@Test
	public void shouldHandleJumpUpEvent()
	{
		assertTrue(idleState.handle(createEvent(ZootEventType.JumpUp)));
		assertEquals(JumpState.ID, actor.getStateMachine().getCurrentState().getId());
	}	
	
	@Test
	public void shouldHandleJumpUpEventWhenActorCantJump()
	{
		when(walkableCtrlMock.canJump()).thenReturn(false);
		assertTrue(idleState.handle(createEvent(ZootEventType.JumpUp)));
		assertEquals(IdleState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void shouldHandleJumpForwardEvent()
	{
		assertTrue(idleState.handle(createEvent(ZootEventType.JumpForward)));
		assertEquals(JumpForwardState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void shouldHandleJumpForwardEventWhenActorCantJump()
	{
		when(walkableCtrlMock.canJump()).thenReturn(false);
		assertTrue(idleState.handle(createEvent(ZootEventType.JumpForward)));
		assertEquals(IdleState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void shouldHandleFallEvent()
	{
		assertTrue(idleState.handle(createEvent(ZootEventType.Fall)));
		assertEquals(FallState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void shouldHandleInAirEvent()
	{
		assertTrue(idleState.handle(createEvent(ZootEventType.InAir)));
		assertEquals(FallState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void shouldHandleAttackEvent()
	{
		assertTrue(idleState.handle(createEvent(ZootEventType.Attack)));
		assertEquals(AttackState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void shouldHandleHurtEvent()
	{
		assertTrue(idleState.handle(createEvent(ZootEventType.Hurt)));
		assertEquals(HurtState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void shouldHandleHurtEventForImmortalActor()
	{
		when(lifeCtrlMock.isFrozen()).thenReturn(true);
		
		assertTrue(idleState.handle(createEvent(ZootEventType.Hurt)));
		assertEquals(IdleState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void shouldHandleDownEvent()
	{
		assertTrue(idleState.handle(createEvent(ZootEventType.Down)));
		assertEquals(DownState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void shouldHandleDeadEvent()
	{
		assertTrue(idleState.handle(createEvent(ZootEventType.Dead)));
		assertEquals(DeadState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void shouldHandleStunEvent()
	{
		assertTrue(idleState.handle(createEvent(ZootEventType.Stun)));
		assertEquals(StunState.ID, actor.getStateMachine().getCurrentState().getId());
	}
}
