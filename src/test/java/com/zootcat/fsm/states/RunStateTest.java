package com.zootcat.fsm.states;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.zootcat.events.ZootEventType;
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
	public void getIdTest()
	{
		assertEquals(RunState.ID, runState.getId());
	}
	
	@Test
	public void onEnterShouldSetWalkAnimationTest()
	{
		runState.onEnter(actor, createEvent(ZootEventType.WalkRight));
		verify(animatedSpriteCtrlMock).setAnimation(runState.getName());
	}
	
	@Test
	public void onEnterShouldSetActorDirectionTest()
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
	public void onUpdateTest()
	{
		runState.onEnter(actor, createEvent(ZootEventType.WalkRight));		
		runState.onUpdate(actor, 1.0f);		
		verify(moveableCtrlMock, times(1)).run(ZootDirection.Right);
		
		runState.onUpdate(actor, 1.0f);
		verify(moveableCtrlMock, times(2)).run(ZootDirection.Right);
		
		runState.onEnter(actor, createEvent(ZootEventType.WalkLeft));
		runState.onUpdate(actor, 1.0f);
		verify(moveableCtrlMock, times(1)).run(ZootDirection.Left);
		
		runState.onUpdate(actor, 1.0f);
		verify(moveableCtrlMock, times(2)).run(ZootDirection.Left);
	}
	
	@Test
	public void handleStopEventTest()
	{
		assertTrue(runState.handle(createEvent(ZootEventType.Stop)));
		assertEquals(IdleState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleJumpEventTest()
	{
		assertTrue(runState.handle(createEvent(ZootEventType.Jump)));
		assertEquals(JumpState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleFallEventTest()
	{
		assertTrue(runState.handle(createEvent(ZootEventType.Fall)));
		assertEquals(FallState.ID, actor.getStateMachine().getCurrentState().getId());
	}	
	
	@Test
	public void handleHurtEventTest()
	{
		assertTrue(runState.handle(createEvent(ZootEventType.Hurt)));
		assertEquals(HurtState.ID, actor.getStateMachine().getCurrentState().getId());
	}
}
