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
import com.zootcat.fsm.states.flying.FlyState;
import com.zootcat.fsm.states.ground.AttackState;
import com.zootcat.fsm.states.ground.DownState;
import com.zootcat.fsm.states.ground.FallState;
import com.zootcat.fsm.states.ground.IdleState;
import com.zootcat.fsm.states.ground.JumpForwardState;
import com.zootcat.fsm.states.ground.JumpState;
import com.zootcat.fsm.states.ground.RunState;
import com.zootcat.fsm.states.ground.StunState;
import com.zootcat.fsm.states.ground.TurnState;
import com.zootcat.fsm.states.ground.WalkState;
import com.zootcat.scene.ZootDirection;
import com.zootcat.testing.ZootStateTestCase;

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
	public void getId()
	{
		assertEquals(IdleState.ID, idleState.getId());
	}
	
	@Test
	public void onEnterShouldSetIdleAnimation()
	{
		idleState.onEnter(actor, null);
		verify(animatedSpriteCtrlMock).setAnimation(idleState.getName());
	}
	
	@Test
	public void onEnterShouldZeroHoritontalVelocityActor()
	{
		idleState.onEnter(actor, null);
		verify(walkableCtrlMock).stop();
	}
	
	@Test
	public void handleRunEvent()
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
	public void handleRunEventWhenActorCantRun()
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
	public void handleFlyEvent()
	{
		//when
		when(directionCtrlMock.getDirection()).thenReturn(ZootDirection.Right);
		idleState.onEnter(actor, null);
		
		//then
		assertTrue(idleState.handle(createEvent(ZootEventType.FlyRight)));
		assertEquals(FlyState.ID, actor.getStateMachine().getCurrentState().getId());		
	}
	
	@Test
	public void handleWalkEventAtTheSameDirection()
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
	public void handleWalkEventAtDifferentDirection()
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
	public void handleJumpUpEvent()
	{
		assertTrue(idleState.handle(createEvent(ZootEventType.JumpUp)));
		assertEquals(JumpState.ID, actor.getStateMachine().getCurrentState().getId());
	}	
	
	@Test
	public void handleJumpUpEventWhenActorCantJump()
	{
		when(walkableCtrlMock.canJump()).thenReturn(false);
		assertTrue(idleState.handle(createEvent(ZootEventType.JumpUp)));
		assertEquals(IdleState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleJumpForwardEvent()
	{
		assertTrue(idleState.handle(createEvent(ZootEventType.JumpForward)));
		assertEquals(JumpForwardState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleJumpForwardEventWhenActorCantJump()
	{
		when(walkableCtrlMock.canJump()).thenReturn(false);
		assertTrue(idleState.handle(createEvent(ZootEventType.JumpForward)));
		assertEquals(IdleState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleFallEvent()
	{
		assertTrue(idleState.handle(createEvent(ZootEventType.Fall)));
		assertEquals(FallState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleInAirEvent()
	{
		assertTrue(idleState.handle(createEvent(ZootEventType.InAir)));
		assertEquals(FallState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleAttackEvent()
	{
		assertTrue(idleState.handle(createEvent(ZootEventType.Attack)));
		assertEquals(AttackState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleHurtEvent()
	{
		assertTrue(idleState.handle(createEvent(ZootEventType.Hurt)));
		assertEquals(HurtState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleDownEvent()
	{
		assertTrue(idleState.handle(createEvent(ZootEventType.Down)));
		assertEquals(DownState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleDeadEvent()
	{
		assertTrue(idleState.handle(createEvent(ZootEventType.Dead)));
		assertEquals(DeadState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleStunEvent()
	{
		assertTrue(idleState.handle(createEvent(ZootEventType.Stun)));
		assertEquals(StunState.ID, actor.getStateMachine().getCurrentState().getId());
	}
}
