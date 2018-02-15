package com.zootcat.fsm.states;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.zootcat.events.ZootEventType;
import com.zootcat.gfx.ZootAnimation;
import com.zootcat.scene.ZootDirection;
import com.zootcat.testing.ZootStateTestCase;

public class TurnStateTest extends ZootStateTestCase
{
	private TurnState turnState;
	@Mock private ZootAnimation turnAnimationMock;
	
	@Before
	public void setup()
	{
		super.setup();
		turnState = new TurnState();
	}
	
	@Test
	public void getIdTest()
	{
		assertEquals(TurnState.ID, turnState.getId());
	}
	
	@Test
	public void onEnterShouldNotChangeAnimationIfTurnAnimationIsNotPresentTest()
	{
		when(animatedSpriteCtrlMock.getAnimation(turnState.getName())).thenReturn(null);
		turnState.onEnter(actor, null);
		
		verify(animatedSpriteCtrlMock, times(0)).setAnimation(turnState.getName());
	}
	
	@Test
	public void onEnterShouldSetTurnAnimationIfPresentTest()
	{
		when(animatedSpriteCtrlMock.getAnimation(turnState.getName())).thenReturn(turnAnimationMock);
		turnState.onEnter(actor, null);
		
		verify(animatedSpriteCtrlMock).setAnimation(turnState.getName());
	}
	
	@Test
	public void onUpdateShouldChangeToIdleStateWhenThereIsNoAnimationTest()
	{
		//given
		when(directionCtrlMock.getDirection()).thenReturn(ZootDirection.Right);
		actor.getStateMachine().init(turnState);
		turnState.onEnter(actor, null);		
		assertEquals(TurnState.ID, actor.getStateMachine().getCurrentState().getId());
		
		//when
		turnState.onUpdate(actor, 1.0f);
		
		//then
		assertEquals(IdleState.ID, actor.getStateMachine().getCurrentState().getId());
		verify(directionCtrlMock).setDirection(ZootDirection.Left);
	}
	
	@Test
	public void onUpdateShouldChangeToIdleStateWhenAnimationHasFinishedTest()
	{
		//given
		when(animatedSpriteCtrlMock.getAnimation(turnState.getName())).thenReturn(turnAnimationMock);
		when(directionCtrlMock.getDirection()).thenReturn(ZootDirection.Left);		
		actor.getStateMachine().init(turnState);
		turnState.onEnter(actor, null);		
		assertEquals(TurnState.ID, actor.getStateMachine().getCurrentState().getId());
		
		//when
		when(turnAnimationMock.isFinished()).thenReturn(false);
		turnState.onUpdate(actor, 1.0f);
		
		//then
		assertEquals(TurnState.ID, actor.getStateMachine().getCurrentState().getId());
		
		//when
		when(turnAnimationMock.isFinished()).thenReturn(true);
		turnState.onUpdate(actor, 1.0f);		
		
		//then
		assertEquals(IdleState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void onLeaveShouldChangeDirectionTest()
	{
		//given right
		when(directionCtrlMock.getDirection()).thenReturn(ZootDirection.Right);		
		turnState.onEnter(actor, null);				
		
		//when
		turnState.onLeave(actor, null);

		//then
		verify(directionCtrlMock, times(1)).setDirection(ZootDirection.Left);
		
		//given left
		when(directionCtrlMock.getDirection()).thenReturn(ZootDirection.Left);		
		turnState.onEnter(actor, null);
		
		//when
		turnState.onLeave(actor, null);
		
		//then
		verify(directionCtrlMock, times(1)).setDirection(ZootDirection.Right);
	}
	
	@Test
	public void handleJumpEventTest()
	{
		assertTrue(turnState.handle(createEvent(ZootEventType.Jump)));
		assertEquals(JumpState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleHurtEventTest()
	{
		assertTrue(turnState.handle(createEvent(ZootEventType.Hurt)));
		assertEquals(HurtState.ID, actor.getStateMachine().getCurrentState().getId());
	}
}
