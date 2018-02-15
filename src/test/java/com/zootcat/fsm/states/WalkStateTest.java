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
	public void getIdTest()
	{
		assertEquals(WalkState.ID, walkState.getId());
	}
	
	@Test
	public void onEnterShouldSetWalkAnimationTest()
	{
		walkState.onEnter(actor, createEvent(ZootEventType.WalkRight));
		verify(animatedSpriteCtrlMock).setAnimation(walkState.getName());
	}
	
	@Test
	public void onEnterShouldSetActorDirectionTest()
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
	public void onUpdateTest()
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
	public void handleStopEventTest()
	{
		assertTrue(walkState.handle(createEvent(ZootEventType.Stop)));
		assertEquals(IdleState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleJumpEventTest()
	{
		assertTrue(walkState.handle(createEvent(ZootEventType.Jump)));
		assertEquals(JumpState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleFallEventTest()
	{
		assertTrue(walkState.handle(createEvent(ZootEventType.Fall)));
		assertEquals(FallState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleInAirEventTest()
	{
		assertTrue(walkState.handle(createEvent(ZootEventType.InAir)));
		assertEquals(FallState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleAttackEventTest()
	{
		assertTrue(walkState.handle(createEvent(ZootEventType.Attack)));
		assertEquals(AttackState.ID, actor.getStateMachine().getCurrentState().getId());
	}	
	
	@Test
	public void handleHurtEventTest()
	{
		assertTrue(walkState.handle(createEvent(ZootEventType.Hurt)));
		assertEquals(HurtState.ID, actor.getStateMachine().getCurrentState().getId());
	}
}
