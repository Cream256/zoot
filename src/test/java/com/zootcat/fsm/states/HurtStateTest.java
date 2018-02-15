package com.zootcat.fsm.states;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.zootcat.events.ZootEvent;
import com.zootcat.events.ZootEventType;
import com.zootcat.gfx.ZootAnimation;
import com.zootcat.testing.ZootStateTestCase;

public class HurtStateTest extends ZootStateTestCase
{
	private HurtState hurtState;
	@Mock private ZootAnimation attackAnimationMock;
	
	@Before
	public void setup()
	{
		super.setup();
		hurtState = new HurtState();
	}
	
	@Test
	public void getIdTest()
	{
		assertEquals(HurtState.ID, hurtState.getId());
	}
	
	@Test
	public void onEnterShouldNotChangeAnimationIfTurnAnimationIsNotPresentTest()
	{
		when(animatedSpriteCtrlMock.getAnimation(hurtState.getName())).thenReturn(null);
		hurtState.onEnter(actor, createEvent(ZootEventType.Hurt));
		
		verify(animatedSpriteCtrlMock, times(0)).setAnimation(hurtState.getName());
	}
	
	@Test
	public void onEnterShouldSetTurnAnimationIfPresentTest()
	{
		when(animatedSpriteCtrlMock.getAnimation(hurtState.getName())).thenReturn(attackAnimationMock);
		hurtState.onEnter(actor, createEvent(ZootEventType.Hurt));
		
		verify(animatedSpriteCtrlMock).setAnimation(hurtState.getName());
	}
	
	@Test
	public void onEnterShouldReduceLifeTest()
	{
		//given
		final int damage = -2;
		ZootEvent event = createEvent(ZootEventType.Hurt, damage);
		
		//when
		hurtState.onEnter(actor, event);
		
		//then
		verify(lifeCtrlMock).addToValue(damage);
	}
		
	@Test
	public void onUpdateShouldChangeToIdleStateWhenThereIsNoAnimationTest()
	{
		//given
		actor.getStateMachine().init(hurtState);
		hurtState.onEnter(actor, createEvent(ZootEventType.Hurt));		
		assertEquals(HurtState.ID, actor.getStateMachine().getCurrentState().getId());
		
		//when
		hurtState.onUpdate(actor, 1.0f);
		
		//then
		assertEquals(IdleState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void onUpdateShouldChangeToIdleStateWhenAnimationHasFinishedTest()
	{
		//given
		when(animatedSpriteCtrlMock.getAnimation(hurtState.getName())).thenReturn(attackAnimationMock);		
		actor.getStateMachine().init(hurtState);
		hurtState.onEnter(actor, createEvent(ZootEventType.Hurt));		
		assertEquals(HurtState.ID, actor.getStateMachine().getCurrentState().getId());
		
		//when
		when(attackAnimationMock.isFinished()).thenReturn(false);
		hurtState.onUpdate(actor, 1.0f);
		
		//then
		assertEquals(HurtState.ID, actor.getStateMachine().getCurrentState().getId());
		
		//when
		when(attackAnimationMock.isFinished()).thenReturn(true);
		hurtState.onUpdate(actor, 1.0f);		
		
		//then
		assertEquals(IdleState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleDeadEventTest()
	{
		assertTrue(hurtState.handle(createEvent(ZootEventType.Dead)));
		assertEquals(DeadState.ID, actor.getStateMachine().getCurrentState().getId());
	}
}
