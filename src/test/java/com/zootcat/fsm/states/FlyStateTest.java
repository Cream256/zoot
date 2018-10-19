package com.zootcat.fsm.states;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.scene.ZootDirection;
import com.zootcat.testing.ZootStateTestCase;

public class FlyStateTest extends ZootStateTestCase
{
	private FlyState flyState;
	
	@Before
	public void setup()
	{
		super.setup();
		flyState = new FlyState();
	}
	
	@Test
	public void shouldReturnValidName()
	{
		assertEquals("Fly", flyState.getName());
	}
	
	@Test
	public void shouldReturnValidId()
	{
		assertEquals(FlyState.ID, flyState.getId());
	}
	
	@Test
	public void onEnterShouldSetFlyAnimation()
	{
		flyState.onEnter(actor, createEvent(ZootEventType.FlyRight));
		verify(animatedSpriteCtrlMock).setAnimation(flyState.getName());
	}
	
	@Test
	public void onEnterShouldSetActorDirection()
	{
		flyState.onEnter(actor, createEvent(ZootEventType.FlyRight));		
		verify(directionCtrlMock).setDirection(ZootDirection.Right);
				
		flyState.onEnter(actor, createEvent(ZootEventType.FlyLeft));		
		verify(directionCtrlMock).setDirection(ZootDirection.Right);
	}
	
	@Test
	public void handleStopEvent()
	{
		assertTrue(flyState.handle(createEvent(ZootEventType.Stop)));
		assertEquals(IdleState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleAttackEvent()
	{
		assertTrue(flyState.handle(createEvent(ZootEventType.Attack)));
		assertEquals(AttackState.ID, actor.getStateMachine().getCurrentState().getId());
	}	
	
	@Test
	public void handleHurtEvent()
	{
		assertTrue(flyState.handle(createEvent(ZootEventType.Hurt)));
		assertEquals(HurtState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleFlyEventInDifferentDirection()
	{
		flyState.onEnter(actor, createEvent(ZootEventType.FlyRight));
		assertTrue(flyState.handle(createEvent(ZootEventType.FlyLeft)));
		assertEquals(TurnState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void onUpdate()
	{
		flyState.onEnter(actor, createEvent(ZootEventType.FlyRight));		
		flyState.onUpdate(actor, 1.0f);		
		verify(flyableCtrlMock, times(1)).fly(ZootDirection.Right);
		
		flyState.onUpdate(actor, 1.0f);
		verify(flyableCtrlMock, times(2)).fly(ZootDirection.Right);
		
		flyState.onEnter(actor, createEvent(ZootEventType.FlyLeft));
		flyState.onUpdate(actor, 1.0f);
		verify(flyableCtrlMock, times(1)).fly(ZootDirection.Left);
		
		flyState.onUpdate(actor, 1.0f);
		verify(flyableCtrlMock, times(2)).fly(ZootDirection.Left);
	}
}
