package com.zootcat.camera;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Test;

public class ZootNullScrollingStrategyTest
{
	@Test
	public void shouldNotInteractWithCameraOnScroll()
	{
		//given
		ZootCamera camera = mock(ZootCamera.class);
		ZootNullScrollingStrategy strategy = ZootNullScrollingStrategy.Instance;
		
		//when
		strategy.scrollCamera(camera, 1.0f);
		
		//then
		verifyZeroInteractions(camera);
	}
	
	@Test
	public void shouldNotInteractWithCameraOnReset()
	{
		//given
		ZootCamera camera = mock(ZootCamera.class);
		ZootNullScrollingStrategy strategy = ZootNullScrollingStrategy.Instance;
		
		//when
		strategy.reset();
		
		//then
		verifyZeroInteractions(camera);		
	}
}
