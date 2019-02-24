package com.zootcat.camera;

import java.util.HashMap;
import java.util.Map;

public class ZootCameraRegistry
{
	public static final String DEFAULT_CAMERA_NAME = "default";
	
	private Map<String, ZootCamera> register = new HashMap<String, ZootCamera>();
	
	public ZootCamera getCamera(String name)
	{
		return register.getOrDefault(name, null);
	}
	
	public void registerCamera(String name, ZootCamera camera)
	{
		register.put(name, camera);
	}
	
	public void deregisterCamera(String name)
	{
		register.remove(name);
	}
	
	public int getRegisteredCount()
	{
		return register.size();
	}
}
