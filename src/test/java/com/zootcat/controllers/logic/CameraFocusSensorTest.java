package com.zootcat.controllers.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.zootcat.camera.ZootCamera;
import com.zootcat.camera.ZootCameraScrollingStrategy;
import com.zootcat.camera.ZootScrollToScrollingStrategy;
import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;

public class CameraFocusSensorTest
{
	private final static String FOCUSED_ACTOR_NAME = "CameraAttractor";
	
	@Mock private ZootScene scene;
	@Mock private ZootActor actor;
	@Mock private ZootCamera camera;
	@Captor private ArgumentCaptor<ZootCameraScrollingStrategy> scrollingStrategyCaptor;
	@Mock private ZootCameraScrollingStrategy previousScrollingStrategy;
	private CameraFocusSensor cameraFocusSensor;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		when(scene.getUnitScale()).thenReturn(1.0f);
		when(scene.getCamera()).thenReturn(camera);
		when(scene.getFirstActor(any())).thenReturn(actor);
		when(camera.getScrollingStrategy()).thenReturn(previousScrollingStrategy);
		
		cameraFocusSensor = new CameraFocusSensor();
		ControllerAnnotations.setControllerParameter(cameraFocusSensor, "focusedActorName", FOCUSED_ACTOR_NAME);
		ControllerAnnotations.setControllerParameter(cameraFocusSensor, "scene", scene);
	}
	
	@Test(expected = RuntimeZootException.class)
	public void shouldThrowIfFocusActorWasNotFound()
	{
		//given
		when(scene.getFirstActor(any())).thenReturn(null);
		
		//when
		cameraFocusSensor.onEnterCollision(mock(Fixture.class));
		
		//then throw
	}
	
	@Test
	public void shouldFocusOnActorWhenCollisionBegins()
	{
		//when
		cameraFocusSensor.onEnterCollision(mock(Fixture.class));
		
		//then
		verify(camera).setScrollingStrategy(scrollingStrategyCaptor.capture());
		
		ZootCameraScrollingStrategy newScrollingStrategy = scrollingStrategyCaptor.getValue();
		assertNotNull(newScrollingStrategy);
		assertEquals(ZootScrollToScrollingStrategy.class, newScrollingStrategy.getClass());
	}	
	
	@Test
	public void shouldFocusOnlyOncePerMultiplyCollisions()
	{
		//when
		cameraFocusSensor.onEnterCollision(mock(Fixture.class));
		cameraFocusSensor.onEnterCollision(mock(Fixture.class));
		cameraFocusSensor.onEnterCollision(mock(Fixture.class));
		
		//then
		verify(camera, times(1)).setScrollingStrategy(scrollingStrategyCaptor.capture());
	}
	
	@Test
	public void shouldLoseFocusWhenAllCollisionsStop()
	{		
		//when
		cameraFocusSensor.onEnterCollision(mock(Fixture.class));
		cameraFocusSensor.onEnterCollision(mock(Fixture.class));
		cameraFocusSensor.onEnterCollision(mock(Fixture.class));
		cameraFocusSensor.onLeaveCollision(mock(Fixture.class));
		cameraFocusSensor.onLeaveCollision(mock(Fixture.class));
		cameraFocusSensor.onLeaveCollision(mock(Fixture.class));
		
		//then
		verify(camera, times(1)).setScrollingStrategy(previousScrollingStrategy);		
	}
}
