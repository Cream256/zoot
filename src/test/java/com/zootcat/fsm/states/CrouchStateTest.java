package com.zootcat.fsm.states;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.zootcat.controllers.physics.PhysicsBodyScale;
import com.zootcat.events.ZootEventType;
import com.zootcat.scene.ZootDirection;
import com.zootcat.testing.ZootStateTestCase;

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
	public void getIdTest()
	{
		assertEquals(CrouchState.ID, crouchState.getId());
	}
	
	@Test
	public void onEnterShouldSetCrouchAnimationTest()
	{
		crouchState.onEnter(actor, createEvent(ZootEventType.WalkRight));
		verify(animatedSpriteCtrlMock).setAnimation(crouchState.getName());
	}
	
	@Test
	public void onEnterShouldShrinkActorTest()
	{		
		PhysicsBodyScale bodyScale = new PhysicsBodyScale(0.75f, 2.50f, 1.25f, false);
		crouchState.setBodyScaling(bodyScale);
		crouchState.onEnter(actor, createEvent(ZootEventType.Down));
		
		verify(physicsBodyCtrlMock, times(1)).scale(bodyScale);
	}
	
	@Test
	public void onEnterShouldNotShrinkActorIfScaleIsNotProvidedTest()
	{		
		crouchState.setBodyScaling(null);
		crouchState.onEnter(actor, createEvent(ZootEventType.Down));
		
		verify(physicsBodyCtrlMock, times(0)).scale(anyObject());
	}
	
	@Test
	public void onLeaveShouldGrowActorTest()
	{
		PhysicsBodyScale bodyScale = new PhysicsBodyScale(0.75f, 2.50f, 1.25f, false);
		crouchState.setBodyScaling(bodyScale);
		crouchState.onLeave(actor, createEvent(ZootEventType.Up));
		
		verify(physicsBodyCtrlMock, times(1)).scale(bodyScale.invert());
	}
	
	@Test
	public void onLeaveShouldNotGrowActorIfScaleIsNotProvidedTest()
	{		
		crouchState.setBodyScaling(null);
		crouchState.onLeave(actor, createEvent(ZootEventType.Up));
		
		verify(physicsBodyCtrlMock, times(0)).scale(anyObject());
	}
	
	@Test
	public void onEnterShouldSetActorDirectionTest()
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
	public void onUpdateTest()
	{
		crouchState.onEnter(actor, createEvent(ZootEventType.WalkRight));		
		crouchState.onUpdate(actor, 1.0f);		
		verify(moveableCtrlMock, times(1)).walk(ZootDirection.Right);
		
		crouchState.onUpdate(actor, 1.0f);
		verify(moveableCtrlMock, times(2)).walk(ZootDirection.Right);
		
		crouchState.onEnter(actor, createEvent(ZootEventType.WalkLeft));
		crouchState.onUpdate(actor, 1.0f);
		verify(moveableCtrlMock, times(1)).walk(ZootDirection.Left);
		
		crouchState.onUpdate(actor, 1.0f);
		verify(moveableCtrlMock, times(2)).walk(ZootDirection.Left);
	}
	
	@Test
	public void handleStopEventTest()
	{
		assertTrue(crouchState.handle(createEvent(ZootEventType.Stop)));
		assertEquals(DownState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleFallEventTest()
	{
		assertTrue(crouchState.handle(createEvent(ZootEventType.Fall)));
		assertEquals(FallState.ID, actor.getStateMachine().getCurrentState().getId());
	}
		
	@Test
	public void handleHurtEventTest()
	{
		assertTrue(crouchState.handle(createEvent(ZootEventType.Hurt)));
		assertEquals(HurtState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleUpEventTest()
	{
		assertTrue(crouchState.handle(createEvent(ZootEventType.Up)));
		assertEquals(IdleState.ID, actor.getStateMachine().getCurrentState().getId());
	}
}
