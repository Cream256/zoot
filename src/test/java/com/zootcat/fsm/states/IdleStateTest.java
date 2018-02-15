package com.zootcat.fsm.states;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.zootcat.events.ZootEventType;
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
	public void getIdTest()
	{
		assertEquals(IdleState.ID, idleState.getId());
	}
	
	@Test
	public void onEnterShouldSetIdleAnimationTest()
	{
		//first invocation is done by DefaultStateMachineController
		verify(animatedSpriteCtrlMock, times(1)).setAnimation(idleState.getName());
		idleState.onEnter(actor, null);
		verify(animatedSpriteCtrlMock, times(2)).setAnimation(idleState.getName());
	}
	
	@Test
	public void onEnterShouldZeroHoritontalVelocityActorTest()
	{
		//first invocation is done by DefaultStateMachineController
		verify(animatedSpriteCtrlMock, times(1)).setAnimation(idleState.getName());
		idleState.onEnter(actor, null);
		verify(physicsBodyCtrlMock, times(2)).setVelocity(0.0f, 0.0f, true, false);
	}
	
	@Test
	public void handleWalkEventAtTheSameDirectionTest()
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
	public void handleWalkEventAtDifferentDirectionTest()
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
	public void handleJumpEventTest()
	{
		assertTrue(idleState.handle(createEvent(ZootEventType.Jump)));
		assertEquals(JumpState.ID, actor.getStateMachine().getCurrentState().getId());
	}	
	
	@Test
	public void handleFallEventTest()
	{
		assertTrue(idleState.handle(createEvent(ZootEventType.Fall)));
		assertEquals(FallState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleInAirEventTest()
	{
		assertTrue(idleState.handle(createEvent(ZootEventType.InAir)));
		assertEquals(FallState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleAttackEventTest()
	{
		assertTrue(idleState.handle(createEvent(ZootEventType.Attack)));
		assertEquals(AttackState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleHurtEventTest()
	{
		assertTrue(idleState.handle(createEvent(ZootEventType.Hurt)));
		assertEquals(HurtState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleDownEventTest()
	{
		assertTrue(idleState.handle(createEvent(ZootEventType.Down)));
		assertEquals(DownState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleDeadEventTest()
	{
		assertTrue(idleState.handle(createEvent(ZootEventType.Dead)));
		assertEquals(DeadState.ID, actor.getStateMachine().getCurrentState().getId());
	}
}
