package com.zootcat.actions;

import com.zootcat.camera.ZootCamera;
import com.zootcat.utils.ZootUtils;

public class ZootZoomCameraAction extends ZootAction
{	
	private float desiredZoom = 1.0f;	
	private float startingZoom = 1.0f;
	private float duration = 1.0f;
	private float timePassed = 0.0f;
	private ZootCamera camera = null;
	
	public void setCamera(ZootCamera camera)
	{
		this.camera = camera;
	}
	
	public ZootCamera getCamera()
	{
		return camera;
	}
		
	public void setDesiredZoom(float zoom)
	{
		this.desiredZoom = zoom;
	}
	
	public float getDesiredZoom()
	{
		return desiredZoom;
	}
	
	public void setDuration(float duration)
	{
		this.duration = duration;
	}
	
	public float getDuration()
	{
		return duration;
	}
	
	public float getTimePassed()
	{
		return timePassed;
	}
	
	public void setStartingZoom(float startingZoom)
	{
		this.startingZoom = startingZoom;
	}
	
	public float getStartingZoom()
	{
		return startingZoom;
	}
	
	@Override
	public boolean act(float delta)
	{
		timePassed += delta;
		
		float currentZoom = ZootUtils.lerp(timePassed / duration, startingZoom, desiredZoom);
		camera.setZoom(currentZoom);
		
		return timePassed >= duration;
	}
	
	@Override
	public void restart()
	{
		timePassed = 0.0f;
	}
}
