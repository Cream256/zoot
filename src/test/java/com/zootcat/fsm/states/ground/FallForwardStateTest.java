package com.zootcat.fsm.states.ground;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.zootcat.fsm.states.ground.FallForwardState;
import com.zootcat.testing.ZootStateTestCase;

public class FallForwardStateTest extends ZootStateTestCase
{
	private FallForwardState fallForwardState;
	
	@Before
	public void setup()
	{
		super.setup();
		fallForwardState = new FallForwardState();
	}
	
	@Test
	public void shouldReturnId()
	{		
		assertEquals(FallForwardState.ID, fallForwardState.getId());
	}
}
