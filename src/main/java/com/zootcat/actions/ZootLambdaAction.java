package com.zootcat.actions;

import java.util.function.Function;

public class ZootLambdaAction extends ZootAction
{
	private Function<Float, Boolean> lambda;
	
	public ZootLambdaAction(Function<Float, Boolean> lambda)
	{
		this.lambda = lambda;
	}
	
	@Override
	public boolean act(float delta)
	{
		return lambda.apply(delta);
	}
}
