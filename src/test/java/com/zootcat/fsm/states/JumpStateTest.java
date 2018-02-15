package com.zootcat.fsm.states;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.zootcat.events.ZootEventType;
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
	public void getIdTest()
	{
		assertEquals(JumpState.ID, jumpState.getId());
	}
	
	@Test
	public void onEnterShouldSetFallAnimationTest()
	{
		jumpState.onEnter(actor, null);
		verify(animatedSpriteCtrlMock).setAnimation(jumpState.getName());
	}
	
	@Test
	public void onEnterShouldPerformJumpTest()
	{
		jumpState.onEnter(actor, null);
		verify(moveableCtrlMock).jump();
	}
	
	@Test
	public void handleFallEventTest()
	{
		assertTrue(jumpState.handle(createEvent(ZootEventType.Fall)));
		assertEquals(FallState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleGroundEventTest()
	{
		assertTrue(jumpState.handle(createEvent(ZootEventType.Ground)));
		assertEquals(IdleState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleHurtEventTest()
	{
		assertTrue(jumpState.handle(createEvent(ZootEventType.Hurt)));
		assertEquals(HurtState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleMoveEventTest()
	{		
		int currentStateId = actor.getStateMachine().getCurrentState().getId();
		
		assertTrue(jumpState.handle(createEvent(ZootEventType.WalkRight)));
		assertEquals("State should not change", currentStateId, actor.getStateMachine().getCurrentState().getId());
		verify(moveableCtrlMock, times(1)).walk(ZootDirection.Right);
		
		assertTrue(jumpState.handle(createEvent(ZootEventType.WalkLeft)));
		assertEquals("State should not change", currentStateId, actor.getStateMachine().getCurrentState().getId());
		verify(moveableCtrlMock, times(1)).walk(ZootDirection.Left);		
		
		assertTrue(jumpState.handle(createEvent(ZootEventType.RunRight)));
		assertEquals("State should not change", currentStateId, actor.getStateMachine().getCurrentState().getId());
		verify(moveableCtrlMock, times(2)).walk(ZootDirection.Right);
		
		assertTrue(jumpState.handle(createEvent(ZootEventType.RunLeft)));
		assertEquals("State should not change", currentStateId, actor.getStateMachine().getCurrentState().getId());
		verify(moveableCtrlMock, times(2)).walk(ZootDirection.Left);
	}
}
