package com.zootcat.actions;

import com.zootcat.camera.ZootCamera;
import com.zootcat.exceptions.RuntimeZootException;

public class ZootCameraFocusAction extends ZootAction
{
	private ZootCamera camera;	
	
	@Override
	public boolean act(float delta)
	{
		if(camera == null) throw new RuntimeZootException("No camera was set for action.");
		
		camera.setTarget(getTargetZootActor());
		return true;
	}
	
	public void setCamera(ZootCamera camera)
	{
		this.camera = camera;
	}
	
	public ZootCamera getCamera()
	{
		return camera;
	}
}
