package com.zootcat.actions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.zootcat.camera.ZootCamera;

public class ZootZoomCameraActionTest
{
	private ZootCamera camera;
	private ZootZoomCameraAction zoomAction;
		
	@Before
	public void setup()
	{
		camera = mock(ZootCamera.class);
		when(camera.getZoom()).thenReturn(1.0f);
		
		zoomAction = new ZootZoomCameraAction();		
	}
	
	@Test
	public void shouldHaveProperDefaultValues()
	{
		assertNull(zoomAction.getCamera());
		assertEquals(1.0f, zoomAction.getDesiredZoom(), 0.0f);
		assertEquals(1.0f, zoomAction.getDuration(), 0.0f);
		assertEquals(0.0f, zoomAction.getTimePassed(), 0.0f);		
	}
	
	@Test
	public void shouldSetCamera()
	{
		zoomAction.setCamera(camera);
		assertEquals(camera, zoomAction.getCamera());
	}
	
	@Test
	public void shouldSetDesiredZoom()
	{
		zoomAction.setDesiredZoom(5.0f);
		assertEquals(5.0f, zoomAction.getDesiredZoom(), 0.0f);
	}
	
	@Test
	public void shouldSetStartingZoom()
	{
		zoomAction.setStartingZoom(2.5f);
		assertEquals(2.5f, zoomAction.getStartingZoom(), 0.0f);
	}
	
	@Test
	public void shouldReturnTimePassed()
	{
		//given
		zoomAction.setCamera(camera);
		
		//when
		zoomAction.act(0.0f);
		
		//then
		assertEquals(0.0f, zoomAction.getTimePassed(), 0.0f);
		
		//when
		zoomAction.act(0.5f);
		
		//then
		assertEquals(0.5f, zoomAction.getTimePassed(), 0.0f);
		
		//when
		zoomAction.act(0.5f);
		
		//then
		assertEquals(1.0f, zoomAction.getTimePassed(), 0.0f);		
	}
	
	@Test
	public void shouldResetTimePassedAfterRestart()
	{
		//given
		zoomAction.setCamera(camera);
		zoomAction.act(1.0f);
		
		//when
		zoomAction.restart();
		
		//then
		assertEquals(0.0f, zoomAction.getTimePassed(), 0.0f);		
	}
	
	@Test
	public void shouldZoomCamera()
	{
		//given
		final float desiredZoom = 2.0f;
		final float duration = 10.0f;		
		
		zoomAction.setCamera(camera);
		zoomAction.setDesiredZoom(desiredZoom);
		zoomAction.setStartingZoom(1.0f);
		zoomAction.setDuration(duration);
		
		//when
		zoomAction.act(0.0f);
		
		//then
		verify(camera).setZoom(1.0f);
		
		//when
		zoomAction.act(5.0f);
		
		//then
		verify(camera).setZoom(1.5f);
		
		//when
		zoomAction.act(5.0f);
		
		//then
		verify(camera).setZoom(2.0f);		
	}	
	
	@Test
	public void shouldEndAfterDurationHasPassed()
	{
		//given
		zoomAction.setCamera(camera);
		zoomAction.setDuration(3.0f);
		
		//then
		assertFalse(zoomAction.act(1.0f));
		assertFalse(zoomAction.act(1.0f));
		assertTrue(zoomAction.act(1.0f));
	}
}
