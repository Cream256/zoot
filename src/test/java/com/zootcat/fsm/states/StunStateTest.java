package com.zootcat.fsm.states;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.states.DeadState;
import com.zootcat.fsm.states.HurtState;
import com.zootcat.fsm.states.StunState;
import com.zootcat.gfx.ZootAnimation;
import com.zootcat.testing.ZootStateTestCase;

public class StunStateTest extends ZootStateTestCase
{
	private StunState stunState;
	
	@Before
	public void setup()
	{
		super.setup();
		stunState = new StunState();
	}
	
	@Test
	public void shouldReturnValidName()
	{
		assertEquals("Stun", stunState.getName());
	}
	
	@Test
	public void shouldReturnValidId()
	{
		assertEquals(StunState.ID, stunState.getId());
	}
	
	@Test
	public void onEnterShouldSetStunAnimation()
	{
		when(animatedSpriteCtrlMock.getAnimation("Stun")).thenReturn(mock(ZootAnimation.class));
		
		stunState.onEnter(actor, createEvent(ZootEventType.Stun));
		verify(animatedSpriteCtrlMock).setAnimation(stunState.getName());
	}
	
	@Test
	public void onEnterShouldStopWalkingActor()
	{
		stunState.onEnter(actor, createEvent(ZootEventType.Stun));		
		verify(walkableCtrlMock).stop();
	}
	
	@Test
	public void onEnterShouldStopFlyingActor()
	{
		stunState.onEnter(actor, createEvent(ZootEventType.Stun));		
		verify(flyableCtrlMock).stop();
	}
		
	@Test
	public void handleHurtEvent()
	{
		assertTrue(stunState.handle(createEvent(ZootEventType.Hurt)));
		assertEquals(HurtState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleDeadEvent()
	{
		assertTrue(stunState.handle(createEvent(ZootEventType.Dead)));
		assertEquals(DeadState.ID, actor.getStateMachine().getCurrentState().getId());
	}
}
