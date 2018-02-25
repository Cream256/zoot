package com.zootcat.fsm.states;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.zootcat.gfx.ZootAnimation;
import com.zootcat.testing.ZootStateTestCase;

public class ClimbStateTest extends ZootStateTestCase
{
	private ClimbState climbState;
	@Mock private ZootAnimation animationMock;
	
	@Before
	public void setup()
	{
		super.setup();
		climbState = new ClimbState();		
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
	public void shouldSetAnimationWhenEnteringState()
	{
		when(animatedSpriteCtrlMock.getAnimation(climbState.getName())).thenReturn(animationMock);
		
		climbState.onEnter(actor, null);
		verify(animatedSpriteCtrlMock).setAnimation(climbState.getName());
	}
		
}
