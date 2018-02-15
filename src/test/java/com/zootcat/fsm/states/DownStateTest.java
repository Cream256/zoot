package com.zootcat.fsm.states;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.zootcat.controllers.physics.PhysicsBodyScale;
import com.zootcat.events.ZootEventType;
import com.zootcat.scene.ZootDirection;
import com.zootcat.testing.ZootStateTestCase;

public class DownStateTest extends ZootStateTestCase
{
	private DownState downState;	
		
	@Before
	public void setup()
	{		
		super.setup();
		downState = new DownState();
	}
	
	@Test
	public void getIdTest()
	{
		assertEquals(DownState.ID, downState.getId());
	}
	
	@Test
	public void onEnterShouldSetDownAnimationTest()
	{
		reset(animatedSpriteCtrlMock);
		downState.onEnter(actor, createEvent(ZootEventType.Down));
		verify(animatedSpriteCtrlMock, times(1)).setAnimation(downState.getName());
	}
	
	@Test
	public void onEnterShouldZeroHoritontalVelocityActorTest()
	{
		reset(physicsBodyCtrlMock);
		downState.onEnter(actor, createEvent(ZootEventType.Down));
		verify(physicsBodyCtrlMock, times(1)).setVelocity(0.0f, 0.0f, true, false);
	}
	
	@Test
	public void onEnterShouldShrinkActorTest()
	{		
		PhysicsBodyScale bodyScale = new PhysicsBodyScale(0.75f, 2.50f, 1.25f, false);
		downState.setBodyScaling(bodyScale);
		downState.onEnter(actor, createEvent(ZootEventType.Down));
		
		verify(physicsBodyCtrlMock, times(1)).scale(bodyScale);
	}
	
	@Test
	public void onEnterShouldNotShrinkActorIfNoScaleIsProvidedTest()
	{
		downState.setBodyScaling(null);
		downState.onEnter(actor, createEvent(ZootEventType.Down));		
		
		verify(physicsBodyCtrlMock, times(0)).scale(anyObject());
	}

	@Test
	public void onLeaveShouldGrowActorTest()
	{
		PhysicsBodyScale bodyScale = new PhysicsBodyScale(0.75f, 2.50f, 1.25f, false);
		downState.setBodyScaling(bodyScale);
		downState.onLeave(actor, createEvent(ZootEventType.Up));
		
		verify(physicsBodyCtrlMock, times(1)).scale(bodyScale.invert());
	}
	
	@Test
	public void onLeaveShouldNotGrowActorIfNoScaleIsProvidedTest()
	{
		downState.onEnter(actor, createEvent(ZootEventType.Down));
		downState.onLeave(actor, createEvent(ZootEventType.Up));
		
		verify(physicsBodyCtrlMock, times(0)).scale(anyObject());		
	}
	
	@Test
	public void handleWalkEventAtTheSameDirectionTest()
	{
		//when
		when(directionCtrlMock.getDirection()).thenReturn(ZootDirection.Right);		
		downState.onEnter(actor, createEvent(ZootEventType.Down));
		
		//then
		assertTrue(downState.handle(createEvent(ZootEventType.WalkRight)));
		assertEquals(CrouchState.ID, actor.getStateMachine().getCurrentState().getId());
		
		//when
		when(directionCtrlMock.getDirection()).thenReturn(ZootDirection.Left);
		downState.onEnter(actor, createEvent(ZootEventType.Down));
		
		//then
		assertTrue(downState.handle(createEvent(ZootEventType.WalkLeft)));
		assertEquals(CrouchState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleWalkEventAtDifferentDirectionTest()
	{
		//when
		when(directionCtrlMock.getDirection()).thenReturn(ZootDirection.Left);		
		downState.onEnter(actor, createEvent(ZootEventType.Down));
		
		//then
		assertTrue(downState.handle(createEvent(ZootEventType.WalkRight)));
		assertEquals(CrouchState.ID, actor.getStateMachine().getCurrentState().getId());
		
		//when
		when(directionCtrlMock.getDirection()).thenReturn(ZootDirection.Right);
		downState.onEnter(actor, createEvent(ZootEventType.Down));
		
		//then
		assertTrue(downState.handle(createEvent(ZootEventType.WalkLeft)));
		assertEquals(CrouchState.ID, actor.getStateMachine().getCurrentState().getId());		
	}
			
	@Test
	public void handleFallEventTest()
	{
		assertTrue(downState.handle(createEvent(ZootEventType.Fall)));
		assertEquals(FallState.ID, actor.getStateMachine().getCurrentState().getId());
	}
		
	@Test
	public void handleHurtEventTest()
	{
		assertTrue(downState.handle(createEvent(ZootEventType.Hurt)));
		assertEquals(HurtState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleUpEventTest()
	{
		assertTrue(downState.handle(createEvent(ZootEventType.Up)));
		assertEquals(IdleState.ID, actor.getStateMachine().getCurrentState().getId());		
	}
}
