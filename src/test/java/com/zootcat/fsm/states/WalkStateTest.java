package com.zootcat.fsm.states;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.scene.ZootDirection;
import com.zootcat.testing.ZootStateTestCase;

public class WalkStateTest extends ZootStateTestCase
{
	private WalkState walkState;	
	
	@Before
	public void setup()
	{
		super.setup();
		walkState = new WalkState();
	}
	
	@Test
	public void getId()
	{
		assertEquals(WalkState.ID, walkState.getId());
	}
	
	@Test
	public void onEnterShouldSetWalkAnimation()
	{
		walkState.onEnter(actor, createEvent(ZootEventType.WalkRight));
		verify(animatedSpriteCtrlMock).setAnimation(walkState.getName());
	}
	
	@Test
	public void onEnterShouldSetActorDirection()
	{
		walkState.onEnter(actor, createEvent(ZootEventType.WalkRight));		
		verify(directionCtrlMock).setDirection(ZootDirection.Right);
				
		walkState.onEnter(actor, createEvent(ZootEventType.WalkLeft));		
		verify(directionCtrlMock).setDirection(ZootDirection.Right);
		
		walkState.onEnter(actor, createEvent(ZootEventType.RunRight));		
		verify(directionCtrlMock, times(2)).setDirection(ZootDirection.Right);
		
		walkState.onEnter(actor, createEvent(ZootEventType.RunLeft));		
		verify(directionCtrlMock, times(2)).setDirection(ZootDirection.Left);
	}
	
	@Test
	public void onUpdate()
	{
		walkState.onEnter(actor, createEvent(ZootEventType.WalkRight));		
		walkState.onUpdate(actor, 1.0f);		
		verify(moveableCtrlMock, times(1)).walk(ZootDirection.Right);
		
		walkState.onUpdate(actor, 1.0f);
		verify(moveableCtrlMock, times(2)).walk(ZootDirection.Right);
		
		walkState.onEnter(actor, createEvent(ZootEventType.WalkLeft));
		walkState.onUpdate(actor, 1.0f);
		verify(moveableCtrlMock, times(1)).walk(ZootDirection.Left);
		
		walkState.onUpdate(actor, 1.0f);
		verify(moveableCtrlMock, times(2)).walk(ZootDirection.Left);
	}
	
	@Test
	public void handleStopEvent()
	{
		assertTrue(walkState.handle(createEvent(ZootEventType.Stop)));
		assertEquals(IdleState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleJumpUpEvent()
	{
		assertTrue(walkState.handle(createEvent(ZootEventType.JumpUp)));
		assertEquals(JumpState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleJumpUpEventWhenActorCantJump()
	{
		when(moveableCtrlMock.canJump()).thenReturn(false);
		assertTrue(walkState.handle(createEvent(ZootEventType.JumpUp)));
		assertEquals(IdleState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleJumpForwardEvent()
	{
		assertTrue(walkState.handle(createEvent(ZootEventType.JumpForward)));
		assertEquals(JumpForwardState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleJumpForwardEventWhenActorCantJump()
	{
		when(moveableCtrlMock.canJump()).thenReturn(false);
		assertTrue(walkState.handle(createEvent(ZootEventType.JumpForward)));
		assertEquals(IdleState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleFallEvent()
	{
		assertTrue(walkState.handle(createEvent(ZootEventType.Fall)));
		assertEquals(FallState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleInAirEvent()
	{
		assertTrue(walkState.handle(createEvent(ZootEventType.InAir)));
		assertEquals(FallState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleAttackEvent()
	{
		assertTrue(walkState.handle(createEvent(ZootEventType.Attack)));
		assertEquals(AttackState.ID, actor.getStateMachine().getCurrentState().getId());
	}	
	
	@Test
	public void handleHurtEvent()
	{
		assertTrue(walkState.handle(createEvent(ZootEventType.Hurt)));
		assertEquals(HurtState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleRunEvent()
	{
		walkState.onEnter(actor, createEvent(ZootEventType.WalkRight));
		assertTrue(walkState.handle(createEvent(ZootEventType.RunRight)));
		assertEquals(RunState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleRunEventWhenActorCantRun()
	{
		when(moveableCtrlMock.canRun()).thenReturn(false);
		walkState.onEnter(actor, createEvent(ZootEventType.WalkRight));
		assertTrue(walkState.handle(createEvent(ZootEventType.RunRight)));
		assertEquals(IdleState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleRunEventInDifferentDirection()
	{
		walkState.onEnter(actor, createEvent(ZootEventType.WalkRight));
		assertTrue(walkState.handle(createEvent(ZootEventType.RunLeft)));
		assertEquals(TurnState.ID, actor.getStateMachine().getCurrentState().getId());		
	}
	
	@Test
	public void handleRunEventInDifferentDirectionWhenActorCantRun()
	{
		when(moveableCtrlMock.canRun()).thenReturn(false);
		walkState.onEnter(actor, createEvent(ZootEventType.WalkRight));
		assertTrue(walkState.handle(createEvent(ZootEventType.RunLeft)));
		assertEquals(TurnState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleWalkEventInDifferentDirection()
	{
		walkState.onEnter(actor, createEvent(ZootEventType.WalkRight));
		assertTrue(walkState.handle(createEvent(ZootEventType.WalkLeft)));
		assertEquals(TurnState.ID, actor.getStateMachine().getCurrentState().getId());
	}
}
