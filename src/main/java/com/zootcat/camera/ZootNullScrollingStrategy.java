package com.zootcat.camera;

public class ZootNullScrollingStrategy implements ZootCameraScrollingStrategy
{
	public static final ZootNullScrollingStrategy Instance = new ZootNullScrollingStrategy();
	
	private ZootNullScrollingStrategy()
	{
		//use instance
	}
	
	@Override
	public void scrollCamera(ZootCamera camera, float delta)
	{
		//noop
	}
}