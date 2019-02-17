package com.zootcat.actions;

import java.util.function.Function;

public class ZootLambdaAction extends ZootAction
{
	private Function<Float, Boolean> lambda;
	
	public ZootLambdaAction()
	{
		this.lambda = null;
	}
	
	public ZootLambdaAction(Function<Float, Boolean> lambda)
	{
		this.lambda = lambda;
	}
	
	@Override
	public boolean act(float delta)
	{
		return lambda.apply(delta);
	}
	
	public Function<Float, Boolean> getLambda()
	{
		return lambda;
	}
	
	public void setLambda(Function<Float, Boolean> lambda)
	{
		this.lambda = lambda;
	}
}
