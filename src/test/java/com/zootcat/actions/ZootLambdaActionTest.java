package com.zootcat.actions;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ZootLambdaActionTest
{
	private boolean executed = false;
	
	@Test
	public void shouldExecuteLambda()
	{
		//given						
		ZootLambdaAction action = new ZootLambdaAction((delta) -> 
		{
			executed = true;
			return true;
		});
		
		//then
		assertTrue(action.act(1.0f));
		assertTrue(executed);		
	}	
}
