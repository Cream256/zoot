package com.zootcat.fsm.states;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.badlogic.gdx.math.Vector2;
import com.zootcat.controllers.logic.ClimbController;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;
import com.zootcat.testing.ZootStateTestCase;

public class JumpStateTest extends ZootStateTestCase
{
	private JumpState jumpState;
	@Mock private ClimbController climbCtrlMock;
	
	@Before
	public void setup()
	{
		super.setup();
		actor.addController(climbCtrlMock);
		jumpState = new JumpState();
	}
	
	@Test
	public void shouldReturnId()
	{
		assertEquals(JumpState.ID, jumpState.getId());
	}
	
	@Test
	public void shouldSetJumpingAnimationOnEnteringState()
	{
		jumpState.onEnter(actor, createEvent(ZootEventType.JumpUp));
		verify(animatedSpriteCtrlMock).setAnimation(jumpState.getName());
	}
		
	@Test
	public void shouldPerformJumpUpOnEnteringState()
	{
		jumpState.onEnter(actor, createEvent(ZootEventType.JumpUp));
		verify(walkableCtrlMock).jumpUp();
	}
	
	@Test
	public void shouldSetClimbControllerSensorPositionOnEnteringState()
	{
		jumpState.onEnter(actor, createEvent(ZootEventType.JumpUp));
		verify(climbCtrlMock).setSensorPosition(ZootDirection.Up);
	}
	
	@Test
	public void shouldNotInteractWithActorOnUpdate()
	{
		ZootActor actor = mock(ZootActor.class);				
		jumpState.onUpdate(actor, 1.0f);
		verifyZeroInteractions(actor);
	}
	
	@Test
	public void handleFallEvent()
	{
		assertTrue(jumpState.handle(createEvent(ZootEventType.Fall)));
		assertEquals(FallState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleGroundEventWhenActorIsFallingDown()
	{		
		actor.getStateMachine().changeState(new JumpState(), createEvent(ZootEventType.JumpUp));
		when(physicsBodyCtrlMock.getVelocity()).thenReturn(new Vector2(0.0f, -1.0f));		
		
		assertTrue(jumpState.handle(createEvent(ZootEventType.Ground)));
		assertEquals(IdleState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleGroundEventWhenActorIsJumping()
	{
		actor.getStateMachine().changeState(new JumpState(), createEvent(ZootEventType.JumpUp));
		when(physicsBodyCtrlMock.getVelocity()).thenReturn(new Vector2(0.0f, 1.0f));
		
		assertTrue(jumpState.handle(createEvent(ZootEventType.Ground)));
		assertEquals(JumpState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleHurtEvent()
	{
		assertTrue(jumpState.handle(createEvent(ZootEventType.Hurt)));
		assertEquals(HurtState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleGrabEvent()
	{
		assertTrue(jumpState.handle(createEvent(ZootEventType.Grab)));
		assertEquals(ClimbState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleGrabSideEvent()
	{
		assertTrue(jumpState.handle(createEvent(ZootEventType.GrabSide)));
		assertEquals(ClimbState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleMoveEvent()
	{		
		int currentStateId = actor.getStateMachine().getCurrentState().getId();
		
		assertTrue(jumpState.handle(createEvent(ZootEventType.WalkRight)));
		assertEquals("State should not change", currentStateId, actor.getStateMachine().getCurrentState().getId());
		verify(walkableCtrlMock).moveInAir(ZootDirection.Right);
		
		assertTrue(jumpState.handle(createEvent(ZootEventType.WalkLeft)));
		assertEquals("State should not change", currentStateId, actor.getStateMachine().getCurrentState().getId());
		verify(walkableCtrlMock).moveInAir(ZootDirection.Left);		
		
		assertTrue(jumpState.handle(createEvent(ZootEventType.RunRight)));
		assertEquals("State should not change", currentStateId, actor.getStateMachine().getCurrentState().getId());
		verify(walkableCtrlMock, times(2)).moveInAir(ZootDirection.Right);
		
		assertTrue(jumpState.handle(createEvent(ZootEventType.RunLeft)));
		assertEquals("State should not change", currentStateId, actor.getStateMachine().getCurrentState().getId());
		verify(walkableCtrlMock, times(2)).moveInAir(ZootDirection.Left);
	}
	
	@Test
	public void shouldSetDirectionOnMoveEvent()
	{
		jumpState.handle(createEvent(ZootEventType.WalkRight));
		jumpState.handle(createEvent(ZootEventType.RunRight));
		verify(directionCtrlMock, times(2)).setDirection(ZootDirection.Right);
		
		jumpState.handle(createEvent(ZootEventType.WalkLeft));
		jumpState.handle(createEvent(ZootEventType.RunLeft));
		verify(directionCtrlMock, times(2)).setDirection(ZootDirection.Left);
	}
	
	@Test
	public void shouldSetClimbControllerSensorPositionOnMoveEvent()
	{
		jumpState.handle(createEvent(ZootEventType.WalkRight));
		jumpState.handle(createEvent(ZootEventType.RunRight));
		verify(climbCtrlMock, times(2)).setSensorPosition(ZootDirection.Right);
		
		jumpState.handle(createEvent(ZootEventType.WalkLeft));
		jumpState.handle(createEvent(ZootEventType.RunLeft));
		verify(climbCtrlMock, times(2)).setSensorPosition(ZootDirection.Left);
	}
	
	@Test
	public void shouldReturnProperName()
	{
		assertEquals(JumpState.NAME, jumpState.getName());
	}
}
