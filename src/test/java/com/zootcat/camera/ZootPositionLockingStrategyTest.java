package com.zootcat.camera;

import static org.mockito.Matchers.anyFloat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.zootcat.scene.ZootActor;

public class ZootPositionLockingStrategyTest
{
	@Test
	public void scrollCameraTest()
	{
		//given
		ZootActor actor = mock(ZootActor.class);
		ZootCamera camera = mock(ZootCamera.class);				
		ZootPositionLockingStrategy strategy = new ZootPositionLockingStrategy();
		
		//when
		when(camera.getTarget()).thenReturn(actor);
		when(actor.getX()).thenReturn(0.0f);
		when(actor.getY()).thenReturn(0.0f);		
		strategy.scrollCamera(camera, 1.0f);
		
		//then
		verify(camera, times(1)).setPosition(0.0f, 0.0f);
		
		//when
		when(actor.getX()).thenReturn(10.0f);
		when(actor.getY()).thenReturn(20.0f);
		strategy.scrollCamera(camera, 1.0f);
		
		//then
		verify(camera, times(1)).setPosition(10.0f, 20.0f);
	}
	
	@Test
	public void scrollCameraOnNullTargetShouldQuitEarlyTest()
	{
		//given
		ZootCamera camera = mock(ZootCamera.class);				
		ZootPositionLockingStrategy strategy = new ZootPositionLockingStrategy();
		
		//when
		when(camera.getTarget()).thenReturn(null);
		strategy.scrollCamera(camera, 1.0f);
		
		//then
		verify(camera, times(0)).setPosition(anyFloat(), anyFloat());
	}
}
