package com.zootcat.fsm.states;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.zootcat.events.ZootEventType;
import com.zootcat.gfx.ZootAnimation;
import com.zootcat.scene.ZootDirection;
import com.zootcat.testing.ZootStateTestCase;

public class AttackStateTest extends ZootStateTestCase
{
	private AttackState attackState;
	@Mock private ZootAnimation attackAnimationMock;
	
	@Before
	public void setup()
	{
		super.setup();
		attackState = new AttackState();
	}
	
	@Test
	public void getIdTest()
	{
		assertEquals(AttackState.ID, attackState.getId());
	}
	
	@Test
	public void onEnterShouldNotChangeAnimationIfTurnAnimationIsNotPresentTest()
	{
		when(animatedSpriteCtrlMock.getAnimation(attackState.getName())).thenReturn(null);
		attackState.onEnter(actor, null);
		
		verify(animatedSpriteCtrlMock, times(0)).setAnimation(attackState.getName());
	}
	
	@Test
	public void onEnterShouldSetTurnAnimationIfPresentTest()
	{
		when(animatedSpriteCtrlMock.getAnimation(attackState.getName())).thenReturn(attackAnimationMock);
		attackState.onEnter(actor, null);
		
		verify(animatedSpriteCtrlMock).setAnimation(attackState.getName());
	}
	
	@Test
	public void onEnterShouldStopActorIfAnimationIsPresentTest()
	{
		reset(physicsBodyCtrlMock);
		when(animatedSpriteCtrlMock.getAnimation(attackState.getName())).thenReturn(attackAnimationMock);
		attackState.onEnter(actor, null);
		
		verify(physicsBodyCtrlMock, times(1)).setVelocity(0.0f, 0.0f, true, false);		
	}

	@Test
	public void onEnterShouldNotStopActorIfAnimationIsNotPresentTest()
	{
		reset(physicsBodyCtrlMock);
		when(animatedSpriteCtrlMock.getAnimation(attackState.getName())).thenReturn(null);
		attackState.onEnter(actor, null);
		
		verify(physicsBodyCtrlMock, times(0)).setVelocity(0.0f, 0.0f, true, false);		
	}
	
	@Test
	public void onUpdateShouldChangeToIdleStateWhenThereIsNoAnimationTest()
	{
		//given
		when(directionCtrlMock.getDirection()).thenReturn(ZootDirection.Right);
		actor.getStateMachine().init(attackState);
		attackState.onEnter(actor, null);		
		assertEquals(AttackState.ID, actor.getStateMachine().getCurrentState().getId());
		
		//when
		attackState.onUpdate(actor, 1.0f);
		
		//then
		assertEquals(IdleState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void onUpdateShouldChangeToIdleStateWhenAnimationHasFinishedTest()
	{
		//given
		when(animatedSpriteCtrlMock.getAnimation(attackState.getName())).thenReturn(attackAnimationMock);
		when(directionCtrlMock.getDirection()).thenReturn(ZootDirection.Left);		
		actor.getStateMachine().init(attackState);
		attackState.onEnter(actor, null);		
		assertEquals(AttackState.ID, actor.getStateMachine().getCurrentState().getId());
		
		//when
		when(attackAnimationMock.isFinished()).thenReturn(false);
		attackState.onUpdate(actor, 1.0f);
		
		//then
		assertEquals(AttackState.ID, actor.getStateMachine().getCurrentState().getId());
		
		//when
		when(attackAnimationMock.isFinished()).thenReturn(true);
		attackState.onUpdate(actor, 1.0f);		
		
		//then
		assertEquals(IdleState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleHurtEventTest()
	{
		assertTrue(attackState.handle(createEvent(ZootEventType.Hurt)));
		assertEquals(HurtState.ID, actor.getStateMachine().getCurrentState().getId());
	}
}
