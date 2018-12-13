package com.zootcat.fsm.states.flying;

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
import com.zootcat.fsm.states.ground.AttackState;
import com.zootcat.fsm.states.ground.IdleState;
import com.zootcat.fsm.states.ground.TurnState;
import com.zootcat.scene.ZootDirection;
import com.zootcat.testing.ZootStateTestCase;

public class FlyIdleStateTest extends ZootStateTestCase
{
	private FlyIdleState flyIdleState;	
		
	@Before
	public void setup()
	{		
		super.setup();
		flyIdleState = new FlyIdleState();
	}
	
	@Test
	public void shouldReturnFlyIdleStateId()
	{
		assertEquals(FlyIdleState.ID, flyIdleState.getId());
	}
	
	@Test 
	public void shouldReplaceIdleStateId()
	{
		assertEquals(IdleState.ID, flyIdleState.getId());
	}
	
	@Test
	public void shouldSetIdleAnimation()
	{
		flyIdleState.onEnter(actor, null);
		verify(animatedSpriteCtrlMock).setAnimation(flyIdleState.getName());
	}
	
	@Test
	public void shouldStopActor()
	{
		flyIdleState.onEnter(actor, null);
		verify(flyableCtrlMock).stop();
	}
			
	@Test
	public void shouldHandleFlyEvent()
	{
		//when
		when(directionCtrlMock.getDirection()).thenReturn(ZootDirection.Right);
		flyIdleState.onEnter(actor, null);
		
		//then
		assertTrue(flyIdleState.handle(createEvent(ZootEventType.FlyRight)));
		assertEquals(FlyState.ID, actor.getStateMachine().getCurrentState().getId());		
	}
	
	@Test
	public void shouldHandleFlyEventAtTheSameDirection()
	{
		//when
		when(directionCtrlMock.getDirection()).thenReturn(ZootDirection.Right);		
		flyIdleState.onEnter(actor, null);
		
		//then
		assertTrue(flyIdleState.handle(createEvent(ZootEventType.FlyRight)));
		assertEquals(FlyState.ID, actor.getStateMachine().getCurrentState().getId());
		
		//when
		when(directionCtrlMock.getDirection()).thenReturn(ZootDirection.Left);
		flyIdleState.onEnter(actor, null);
		
		//then
		assertTrue(flyIdleState.handle(createEvent(ZootEventType.FlyLeft)));
		assertEquals(FlyState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void shouldHandleFlyEventAtDifferentDirection()
	{
		//when
		when(directionCtrlMock.getDirection()).thenReturn(ZootDirection.Left);		
		flyIdleState.onEnter(actor, null);
		
		//then
		assertTrue(flyIdleState.handle(createEvent(ZootEventType.FlyRight)));
		assertEquals(TurnState.ID, actor.getStateMachine().getCurrentState().getId());
		
		//when
		when(directionCtrlMock.getDirection()).thenReturn(ZootDirection.Right);
		flyIdleState.onEnter(actor, null);
		
		//then
		assertTrue(flyIdleState.handle(createEvent(ZootEventType.FlyLeft)));
		assertEquals(TurnState.ID, actor.getStateMachine().getCurrentState().getId());		
	}
			
	@Test
	public void handleAttackEvent()
	{
		assertTrue(flyIdleState.handle(createEvent(ZootEventType.Attack)));
		assertEquals(AttackState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleHurtEvent()
	{
		assertTrue(flyIdleState.handle(createEvent(ZootEventType.Hurt)));
		assertEquals(HurtState.ID, actor.getStateMachine().getCurrentState().getId());
	}
		
	@Test
	public void handleDeadEvent()
	{
		assertTrue(flyIdleState.handle(createEvent(ZootEventType.Dead)));
		assertEquals(DeadState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleStunEvent()
	{
		assertTrue(flyIdleState.handle(createEvent(ZootEventType.Stun)));
		assertEquals(StunState.ID, actor.getStateMachine().getCurrentState().getId());
	}
}
