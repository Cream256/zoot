package com.zootcat.fsm.states;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.fsm.states.ground.IdleState;
import com.zootcat.gfx.ZootAnimation;
import com.zootcat.testing.ZootStateTestCase;
import com.zootcat.utils.ZootDirection;

public class DeadStateTest extends ZootStateTestCase
{
	private DeadState deadState;
	@Mock private ZootAnimation animationMock;
	
	@Before
	public void setup()
	{
		super.setup();
		deadState = new DeadState();
	}
	
	@Test
	public void getIdTest()
	{
		assertEquals(DeadState.ID, deadState.getId());
	}
	
	@Test
	public void onEnterShouldNotChangeAnimationIfAnimationIsNotPresentTest()
	{
		when(animatedSpriteCtrlMock.getAnimation(deadState.getName())).thenReturn(null);
		deadState.onEnter(actor, null);
		
		verify(animatedSpriteCtrlMock, times(0)).setAnimation(deadState.getName());
	}
	
	@Test
	public void onEnterShouldSetAnimationIfIsPresentTest()
	{
		when(animatedSpriteCtrlMock.getAnimation(deadState.getName())).thenReturn(animationMock);
		deadState.onEnter(actor, null);
		
		verify(animatedSpriteCtrlMock).setAnimation(deadState.getName());
	}
		
	@Test
	public void onUpdateShouldNotChangeToIdleStateWhenThereIsNoAnimationTest()
	{
		//given
		when(directionCtrlMock.getDirection()).thenReturn(ZootDirection.Right);
		actor.getStateMachine().init(deadState);
		deadState.onEnter(actor, null);		
		assertEquals(DeadState.ID, actor.getStateMachine().getCurrentState().getId());
		
		//when
		deadState.onUpdate(actor, 1.0f);
		
		//then
		assertEquals(DeadState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void onUpdateShouldNotChangeToIdleStateWhenAnimationHasFinishedTest()
	{
		//given
		when(animatedSpriteCtrlMock.getAnimation(deadState.getName())).thenReturn(animationMock);
		when(directionCtrlMock.getDirection()).thenReturn(ZootDirection.Left);		
		actor.getStateMachine().init(deadState);
		deadState.onEnter(actor, null);		
		assertEquals(DeadState.ID, actor.getStateMachine().getCurrentState().getId());
		
		//when
		when(animationMock.isFinished()).thenReturn(false);
		deadState.onUpdate(actor, 1.0f);
		
		//then
		assertEquals(DeadState.ID, actor.getStateMachine().getCurrentState().getId());
		
		//when
		when(animationMock.isFinished()).thenReturn(true);
		deadState.onUpdate(actor, 1.0f);		
		
		//then
		assertEquals(DeadState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void onUpdateShouldChangeToIdleStateWhenPlayerBecomesAliveTest()
	{
		//given
		actor.getStateMachine().init(deadState);
		deadState.onEnter(actor, null);
		assertEquals(DeadState.ID, actor.getStateMachine().getCurrentState().getId());
		
		//when
		when(lifeCtrlMock.isAlive()).thenReturn(false);
		deadState.onUpdate(actor, 1.0f);
		
		//then
		assertEquals(DeadState.ID, actor.getStateMachine().getCurrentState().getId());
		
		//when
		when(lifeCtrlMock.isAlive()).thenReturn(true);
		deadState.onUpdate(actor, 1.0f);
		
		//then
		assertEquals(IdleState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleTest()
	{
		ZootEvent eventMock = mock(ZootEvent.class);		
		assertTrue(deadState.handle(eventMock));
		verifyNoMoreInteractions(eventMock);
	}
}
