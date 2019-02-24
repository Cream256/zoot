package com.zootcat.camera;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ZootCameraRegistryTest
{
	@Mock private ZootCamera camera;
	private ZootCameraRegistry register;
		
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		register = new ZootCameraRegistry();
	}
	
	@Test
	public void shouldConstructWithEmptyRegister()
	{
		assertEquals(0, register.getRegisteredCount());
	}
	
	@Test
	public void shouldRegisterCamera()
	{
		//given
		final String cameraName = "default";
		
		//when
		register.registerCamera(cameraName, camera);
		
		//then
		assertEquals(1, register.getRegisteredCount());
		assertEquals(camera, register.getCamera(cameraName));
	}
	
	@Test
	public void shouldDeregisterCamera()
	{
		//given
		final String cameraName = "default";
		
		//when
		register.registerCamera(cameraName, camera);
		register.deregisterCamera(cameraName);
		
		//then
		assertEquals(0, register.getRegisteredCount());
		assertNull(register.getCamera(cameraName));
	}
	
	@Test
	public void shouldReturnNullForUnregisteredCamera()
	{		
		//when
		register.registerCamera("default", camera);
		
		//then
		assertEquals(1, register.getRegisteredCount());
		assertNull(register.getCamera("nameOfNotRegisteredCamera"));		
	}	
}
