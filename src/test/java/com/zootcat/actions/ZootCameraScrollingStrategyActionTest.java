package com.zootcat.actions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.zootcat.camera.ZootCamera;
import com.zootcat.camera.ZootCameraScrollingStrategy;
import com.zootcat.scene.ZootActor;

public class ZootCameraScrollingStrategyActionTest
{
	@Mock private ZootCamera camera;
	@Mock private ZootCameraScrollingStrategy strategy;
	private ZootCameraScrollingStrategyAction action;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		action = new ZootCameraScrollingStrategyAction();
	}
	
	@Test
	public void shouldSetCamera()
	{
		action.setCamera(camera);
		assertEquals(camera, action.getCamera());		
	}
	
	@Test
	public void shouldSetStrategy()
	{
		action.setStrategy(strategy);
		assertEquals(strategy, action.getStrategy());
	}
	
	@Test
	public void shouldSetCameraScrollingStrategy()
	{
		//given
		action.setCamera(camera);
		action.setStrategy(strategy);
		
		//when
		assertTrue(action.act(0.0f));
		
		//then
		verify(camera).setScrollingStrategy(strategy);		
	}
	
	@Test
	public void shouldProperlyResetAction()
	{
		//given
		action.setCamera(camera);
		action.setStrategy(strategy);
		action.setTarget(mock(ZootActor.class));
		action.setActor(mock(ZootActor.class));
		
		//when
		action.reset();
		
		//then
		assertNull(action.getCamera());
		assertNull(action.getStrategy());
		assertNull(action.getTarget());
		assertNull(action.getActor());
	}
}
