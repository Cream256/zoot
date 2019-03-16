package com.zootcat.camera;

import static org.mockito.Matchers.anyFloat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.zootcat.scene.ZootActor;

public class ZootPositionLockingScrollingStrategyTest
{
	@Test
	public void shouldSetCameraPositionAtTheCenterOfTheActor()
	{
		//given
		ZootActor actor = mock(ZootActor.class);
		when(actor.getX()).thenReturn(0.0f);
		when(actor.getY()).thenReturn(0.0f);
		when(actor.getWidth()).thenReturn(100.0f);
		when(actor.getHeight()).thenReturn(200.0f);
				
		ZootCamera camera = mock(ZootCamera.class);
		when(camera.getTarget()).thenReturn(actor);
		
		//when
		ZootPositionLockingScrollingStrategy strategy = new ZootPositionLockingScrollingStrategy();
		strategy.scrollCamera(camera, 1.0f);
		
		//then
		verify(camera).setPosition(50.0f, 100.0f);
		
		//when
		when(actor.getX()).thenReturn(10.0f);
		when(actor.getY()).thenReturn(20.0f);
		strategy.scrollCamera(camera, 1.0f);
		
		//then
		verify(camera).setPosition(60.0f, 120.0f);
	}
	
	@Test
	public void shouldNotScrollCameraIfTargetIsNotSet()
	{
		//given
		ZootCamera camera = mock(ZootCamera.class);				
		ZootPositionLockingScrollingStrategy strategy = new ZootPositionLockingScrollingStrategy();
		
		//when
		when(camera.getTarget()).thenReturn(null);
		strategy.scrollCamera(camera, 1.0f);
		
		//then
		verify(camera, never()).setPosition(anyFloat(), anyFloat());
	}
	
	@Test
	public void shouldNotInteractWithCameraOnReset()
	{
		//given
		ZootCamera camera = mock(ZootCamera.class);				
		ZootPositionLockingScrollingStrategy strategy = new ZootPositionLockingScrollingStrategy();
		
		//when
		strategy.reset();
		
		//then
		verifyZeroInteractions(camera);
	}
}
