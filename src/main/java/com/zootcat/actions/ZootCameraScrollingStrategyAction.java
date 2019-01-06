package com.zootcat.actions;

import com.zootcat.camera.ZootCamera;
import com.zootcat.camera.ZootCameraScrollingStrategy;

public class ZootCameraScrollingStrategyAction extends ZootAction
{
	private ZootCamera camera;
	private ZootCameraScrollingStrategy strategy;
	
	public void setCamera(ZootCamera camera)
	{
		this.camera = camera;
	}
	
	public ZootCamera getCamera()
	{
		return camera;
	}
	
	public void setStrategy(ZootCameraScrollingStrategy strategy)
	{
		this.strategy = strategy;
	}
	
	public ZootCameraScrollingStrategy getStrategy()
	{
		return strategy;
	}
	
	@Override
	public boolean act(float delta)
	{
		camera.setScrollingStrategy(strategy);
		return true;
	}
	
	@Override
	public void reset()
	{
		super.reset();
		camera = null;
		strategy = null;
	}
}
