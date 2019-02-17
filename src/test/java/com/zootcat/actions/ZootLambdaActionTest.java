package com.zootcat.actions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.function.Function;

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
	
	@Test
	public void shouldReturnLambda()
	{
		//given	
		Function<Float, Boolean> lambda = (delta) -> true;

		//when
		ZootLambdaAction action = new ZootLambdaAction(lambda);	

		//then
		assertEquals(lambda, action.getLambda());	
		assertTrue(lambda.apply(0.0f));
	}
	
	@Test
	public void shouldSetLambda()
	{
		//given
		Function<Float, Boolean> lambda = (delta) -> true;
		ZootLambdaAction action = new ZootLambdaAction();
		
		//when
		action.setLambda(lambda);
		
		//then
		assertEquals(lambda, action.getLambda());
	}
}
