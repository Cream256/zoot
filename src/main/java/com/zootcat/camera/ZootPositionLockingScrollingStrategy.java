package com.zootcat.camera;

import com.zootcat.scene.ZootActor;

public class ZootPositionLockingScrollingStrategy implements ZootCameraScrollingStrategy
{
	@Override
	public void scrollCamera(ZootCamera camera, float delta)
	{
		ZootActor target = camera.getTarget();
		if(target == null)
		{
			return;
		}

		float x = target.getX() + target.getWidth() * 0.5f; 
		float y = target.getY() + target.getHeight() * 0.5f;		
		camera.setPosition(x, y);
	}

	@Override
	public void reset()
	{
		//noop
	}
}
