package com.zootcat.camera;

import com.zootcat.scene.ZootActor;

public class ZootPositionLockingStrategy implements ZootCameraScrollingStrategy
{
	@Override
	public void scrollCamera(ZootCamera camera, float delta)
	{
		ZootActor target = camera.getTarget();
		if(target == null)
		{
			return;
		}
		
		camera.setPosition(target.getX(), target.getY());
	}
}
