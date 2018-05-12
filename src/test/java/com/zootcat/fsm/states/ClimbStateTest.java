package com.zootcat.fsm.states;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.zootcat.controllers.logic.ClimbController;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.events.ZootEvents;
import com.zootcat.gfx.ZootAnimation;
import com.zootcat.testing.ZootStateTestCase;

public class ClimbStateTest extends ZootStateTestCase
{
	private ClimbState climbState;
	@Mock private ZootAnimation animationMock;
	@Mock private ClimbController climbCtrlMock;
	
	@Before
	public void setup()
	{
		super.setup();
		climbState = new ClimbState();
		actor.addController(climbCtrlMock);
	}
	
	@Test
	public void shouldReturnId()
	{
		assertEquals(ClimbState.ID, climbState.getId());
	}
	
	@Test
	public void shouldReturnProperName()
	{
		assertEquals("Climb", climbState.getName());
	}
	
	@Test
	public void shouldSetClimbingAnimationWhenEnteringState()
	{
		when(animatedSpriteCtrlMock.getAnimation(climbState.getName())).thenReturn(animationMock);
		
		climbState.onEnter(actor, ZootEvents.get(ZootEventType.Grab));
		verify(animatedSpriteCtrlMock).setAnimation(ClimbState.CLIMB_ANIMATION);
	}
	
	@Test
	public void shouldSetClimbingSideAnimationWhenEnteringState()
	{
		when(animatedSpriteCtrlMock.getAnimation(climbState.getName())).thenReturn(animationMock);
		
		climbState.onEnter(actor, ZootEvents.get(ZootEventType.GrabSide));
		verify(animatedSpriteCtrlMock).setAnimation(ClimbState.CLIMB_SIDE_ANIMATION);
	}
	
	@Test
	public void shouldGrabOnEnteringState()
	{
		climbState.onEnter(actor, ZootEvents.get(ZootEventType.Grab));
		verify(climbCtrlMock).grab();		
	}
	
	@Test
	public void handleHurtEvent()
	{
		assertTrue(climbState.handle(createEvent(ZootEventType.Hurt)));
		assertEquals(HurtState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleUpEventShouldClimb()
	{
		when(climbCtrlMock.climb()).thenReturn(true);
		assertTrue(climbState.handle(createEvent(ZootEventType.Up)));
		assertEquals(IdleState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleUpEventShouldNotClimb()
	{
		actor.getStateMachine().changeState(climbState, createEvent(ZootEventType.Grab));		
		when(climbCtrlMock.climb()).thenReturn(false);
		
		assertTrue(climbState.handle(createEvent(ZootEventType.Up)));
		assertEquals(ClimbState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void handleDownEvent()
	{
		assertTrue(climbState.handle(createEvent(ZootEventType.Down)));
		assertEquals(FallState.ID, actor.getStateMachine().getCurrentState().getId());
		verify(climbCtrlMock).letGo();
	}
		
	@Test
	public void shouldReturnTrueOnOtherEventsAndNotChangeState()
	{
		actor.getStateMachine().changeState(climbState, createEvent(ZootEventType.Grab));		
		assertTrue(climbState.handle(createEvent(ZootEventType.Update)));
		assertEquals(ClimbState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
}
