package com.zootcat.fsm.states.ground;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.states.HurtState;
import com.zootcat.fsm.states.StunState;
import com.zootcat.fsm.states.ground.FallState;
import com.zootcat.fsm.states.ground.JumpForwardState;
import com.zootcat.fsm.states.ground.JumpState;
import com.zootcat.fsm.states.ground.RunState;
import com.zootcat.scene.ZootDirection;
import com.zootcat.testing.ZootStateTestCase;

public class RunStateTest extends ZootStateTestCase
{
	private RunState runState;	
	
	@Before
	public void setup()
	{
		super.setup();
		runState = new RunState();
	}
	
	@Test
	public void getId()
	{
		assertEquals(RunState.ID, runState.getId());
	}
	
	@Test
	public void onEnterShouldSetWalkAnimation()
	{
		runState.onEnter(actor, createEvent(ZootEventType.WalkRight));
		verify(animatedSpriteCtrlMock).setAnimation(runState.getName());
	}
	
	@Test
	public void onEnterShouldSetActorDirection()
	{
		runState.onEnter(actor, createEvent(ZootEventType.WalkRight));		
		verify(directionCtrlMock).setDirection(ZootDirection.Right);
				
		runState.onEnter(actor, createEvent(ZootEventType.WalkLeft));		
		verify(directionCtrlMock).setDirection(ZootDirection.Right);
		
		runState.onEnter(actor, createEvent(ZootEventType.RunRight));		
		verify(directionCtrlMock, times(2)).setDirection(ZootDirection.Right);
		
		runState.onEnter(actor, createEvent(ZootEventType.RunLeft));		
		verify(directionCtrlMock, times(2)).setDirection(ZootDirection.Left);
	}
	
	@Test
	public void updateShouldMoveActor()
	{
		runState.onEnter(actor, createEvent(ZootEventType.WalkRight));		
		runState.onUpdate(actor, 1.0f);		
		verify(walkableCtrlMock, times(1)).run(ZootDirection.Right);
		
		runState.onUpdate(actor, 1.0f);
		verify(walkableCtrlMock, times(2)).run(ZootDirection.Right);
		
		runState.onEnter(actor, createEvent(ZootEventType.WalkLeft));
		runState.onUpdate(actor, 1.0f);
		verify(walkableCtrlMock, times(1)).run(ZootDirection.Left);
		
		runState.onUpdate(actor, 1.0f);
		verify(walkableCtrlMock, times(2)).run(ZootDirection.Left);
	}
	
	@Test
	public void updateShouldChangeStateToWalkingRightWhenActorCanNoLongerRun()
	{
		runState.onEnter(actor, createEvent(ZootEventType.RunRight));
		runState.onUpdate(actor, 1.0f);
		verify(walkableCtrlMock).run(ZootDirection.Right);
		
		when(walkableCtrlMock.canRun()).thenReturn(false);
		runState.onUpdate(actor, 1.0f);
		verify(walkableCtrlMock).run(ZootDirection.Right);
	}
	
	@Test
	public void updateShouldChangeStateToWalkingLeftWhenActorCanNoLongerRun()
	{
		runState.onEnter(actor, createEvent(ZootEventType.RunLeft));
		runState.onUpdate(actor, 1.0f);
		verify(walkableCtrlMock, times(1)).run(ZootDirection.Left);
		
		when(walkableCtrlMock.canRun()).thenReturn(false);
		runState.onUpdate(actor, 1.0f);
		verify(walkableCtrlMock, times(1)).run(ZootDirection.Left);
	}
	
	@Test
	public void handleStopEvent()
	{
		assertTrue(runState.handle(createEvent(ZootEventType.Stop)));
		assertEquals(IdleState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleJumpUpEvent()
	{
		assertTrue(runState.handle(createEvent(ZootEventType.JumpUp)));
		assertEquals(JumpState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleJumpForwardEvent()
	{
		assertTrue(runState.handle(createEvent(ZootEventType.JumpForward)));
		assertEquals(JumpForwardState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleFallEvent()
	{
		assertTrue(runState.handle(createEvent(ZootEventType.Fall)));
		assertEquals(FallState.ID, actor.getStateMachine().getCurrentState().getId());
	}	
	
	@Test
	public void handleHurtEvent()
	{
		assertTrue(runState.handle(createEvent(ZootEventType.Hurt)));
		assertEquals(HurtState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleStunEvent()
	{
		assertTrue(runState.handle(createEvent(ZootEventType.Stun)));
		assertEquals(StunState.ID, actor.getStateMachine().getCurrentState().getId());
	}
}
