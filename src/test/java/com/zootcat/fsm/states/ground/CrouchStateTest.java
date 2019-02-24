package com.zootcat.fsm.states.ground;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.zootcat.controllers.physics.PhysicsBodyScale;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.states.HurtState;
import com.zootcat.fsm.states.ground.CrouchState;
import com.zootcat.fsm.states.ground.DownState;
import com.zootcat.fsm.states.ground.FallState;
import com.zootcat.testing.ZootStateTestCase;
import com.zootcat.utils.ZootDirection;

public class CrouchStateTest extends ZootStateTestCase
{
	private CrouchState crouchState;	
	
	@Before
	public void setup()
	{
		super.setup();
		crouchState = new CrouchState();
	}
	
	@Test
	public void getId()
	{
		assertEquals(CrouchState.ID, crouchState.getId());
	}
	
	@Test
	public void onEnterShouldSetCrouchAnimation()
	{
		crouchState.onEnter(actor, createEvent(ZootEventType.WalkRight));
		verify(animatedSpriteCtrlMock).setAnimation(crouchState.getName());
	}
	
	@Test
	public void onEnterShouldShrinkActor()
	{		
		PhysicsBodyScale bodyScale = new PhysicsBodyScale(0.75f, 2.50f, 1.25f, false);
		crouchState.setBodyScaling(bodyScale);
		crouchState.onEnter(actor, createEvent(ZootEventType.Down));
		
		verify(physicsBodyCtrlMock, times(1)).scale(bodyScale);
	}
	
	@Test
	public void onEnterShouldNotShrinkActorIfScaleIsNotProvided()
	{		
		crouchState.setBodyScaling(null);
		crouchState.onEnter(actor, createEvent(ZootEventType.Down));
		
		verify(physicsBodyCtrlMock, times(0)).scale(anyObject());
	}
	
	@Test
	public void onLeaveShouldGrowActor()
	{
		PhysicsBodyScale bodyScale = new PhysicsBodyScale(0.75f, 2.50f, 1.25f, false);
		crouchState.setBodyScaling(bodyScale);
		crouchState.onLeave(actor, createEvent(ZootEventType.Up));
		
		verify(physicsBodyCtrlMock, times(1)).scale(bodyScale.invert());
	}
	
	@Test
	public void onLeaveShouldNotGrowActorIfScaleIsNotProvided()
	{		
		crouchState.setBodyScaling(null);
		crouchState.onLeave(actor, createEvent(ZootEventType.Up));
		
		verify(physicsBodyCtrlMock, times(0)).scale(anyObject());
	}
	
	@Test
	public void onEnterShouldSetActorDirection()
	{
		crouchState.onEnter(actor, createEvent(ZootEventType.WalkRight));		
		verify(directionCtrlMock).setDirection(ZootDirection.Right);
				
		crouchState.onEnter(actor, createEvent(ZootEventType.WalkLeft));		
		verify(directionCtrlMock).setDirection(ZootDirection.Right);
		
		crouchState.onEnter(actor, createEvent(ZootEventType.RunRight));		
		verify(directionCtrlMock, times(2)).setDirection(ZootDirection.Right);
		
		crouchState.onEnter(actor, createEvent(ZootEventType.RunLeft));		
		verify(directionCtrlMock, times(2)).setDirection(ZootDirection.Left);
	}
	
	@Test
	public void onUpdate()
	{
		crouchState.onEnter(actor, createEvent(ZootEventType.WalkRight));		
		crouchState.onUpdate(actor, 1.0f);		
		verify(walkableCtrlMock, times(1)).walk(ZootDirection.Right);
		
		crouchState.onUpdate(actor, 1.0f);
		verify(walkableCtrlMock, times(2)).walk(ZootDirection.Right);
		
		crouchState.onEnter(actor, createEvent(ZootEventType.WalkLeft));
		crouchState.onUpdate(actor, 1.0f);
		verify(walkableCtrlMock, times(1)).walk(ZootDirection.Left);
		
		crouchState.onUpdate(actor, 1.0f);
		verify(walkableCtrlMock, times(2)).walk(ZootDirection.Left);
	}
	
	@Test
	public void handleStopEvent()
	{
		assertTrue(crouchState.handle(createEvent(ZootEventType.Stop)));
		assertEquals(DownState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleFallEvent()
	{
		assertTrue(crouchState.handle(createEvent(ZootEventType.Fall)));
		assertEquals(FallState.ID, actor.getStateMachine().getCurrentState().getId());
	}
		
	@Test
	public void handleHurtEvent()
	{
		assertTrue(crouchState.handle(createEvent(ZootEventType.Hurt)));
		assertEquals(HurtState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void shouldHandleHurtEventForImmortalActor()
	{
		when(lifeCtrlMock.isFrozen()).thenReturn(true);
		
		assertTrue(crouchState.handle(createEvent(ZootEventType.Hurt)));
		assertEquals(IdleState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleUpEvent()
	{
		assertTrue(crouchState.handle(createEvent(ZootEventType.Up)));
		assertEquals(IdleState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void shouldSetBodyScaling()
	{		
		PhysicsBodyScale bodyScale = new PhysicsBodyScale(0.75f, 2.50f, 1.25f, false);
		crouchState.setBodyScaling(bodyScale);
		
		assertEquals(bodyScale, crouchState.getBodyScaling());
	}

}
