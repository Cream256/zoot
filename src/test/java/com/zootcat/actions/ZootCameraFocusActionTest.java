package com.zootcat.actions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.zootcat.camera.ZootCamera;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.scene.ZootActor;

public class ZootCameraFocusActionTest
{
	@Mock private ZootCamera camera;
	@Mock private ZootActor targetActor;	
	private ZootCameraFocusAction action;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		action = new ZootCameraFocusAction();
	}
	
	@Test
	public void shouldReturnNullCamera()
	{
		assertNull(action.getCamera());
	}
	
	@Test(expected = RuntimeZootException.class)
	public void shouldThrowIfNoCameraWasSet()
	{
		action.setCamera(null);
		action.act(1.0f);
	}
	
	@Test
	public void shouldReturnTrueOneAct()
	{
		action.setCamera(camera);
		assertTrue(action.act(1.0f));
	}
	
	@Test
	public void shouldSetCamera()
	{
		action.setCamera(camera);
		assertEquals(camera, action.getCamera());
	}
	
	@Test
	public void shouldSetCameraFocusOnActor()
	{
		action.setTarget(targetActor);
		action.setCamera(camera);
		action.act(1.0f);
		
		verify(camera).setTarget(targetActor);
	}
}
