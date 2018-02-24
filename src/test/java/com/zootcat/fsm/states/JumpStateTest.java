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

import com.badlogic.gdx.math.Vector2;
import com.zootcat.events.ZootEventType;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;
import com.zootcat.testing.ZootStateTestCase;

public class JumpStateTest extends ZootStateTestCase
{
	private JumpState jumpState;
	
	@Before
	public void setup()
	{
		super.setup();
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
		verify(moveableCtrlMock).jumpUp();
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
	public void handleMoveEvent()
	{		
		int currentStateId = actor.getStateMachine().getCurrentState().getId();
		
		assertTrue(jumpState.handle(createEvent(ZootEventType.WalkRight)));
		assertEquals("State should not change", currentStateId, actor.getStateMachine().getCurrentState().getId());
		verify(moveableCtrlMock).moveInAir(ZootDirection.Right);
		
		assertTrue(jumpState.handle(createEvent(ZootEventType.WalkLeft)));
		assertEquals("State should not change", currentStateId, actor.getStateMachine().getCurrentState().getId());
		verify(moveableCtrlMock).moveInAir(ZootDirection.Left);		
		
		assertTrue(jumpState.handle(createEvent(ZootEventType.RunRight)));
		assertEquals("State should not change", currentStateId, actor.getStateMachine().getCurrentState().getId());
		verify(moveableCtrlMock, times(2)).moveInAir(ZootDirection.Right);
		
		assertTrue(jumpState.handle(createEvent(ZootEventType.RunLeft)));
		assertEquals("State should not change", currentStateId, actor.getStateMachine().getCurrentState().getId());
		verify(moveableCtrlMock, times(2)).moveInAir(ZootDirection.Left);
	}
}
