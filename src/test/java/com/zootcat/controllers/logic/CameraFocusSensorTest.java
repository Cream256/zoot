package com.zootcat.controllers.logic;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.zootcat.camera.ZootCamera;
import com.zootcat.camera.ZootCameraRegistry;
import com.zootcat.camera.ZootCameraScrollingStrategy;
import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;

public class CameraFocusSensorTest
{
	private static final String FOCUSED_ACTOR_NAME = "CameraAttractor";
	
	@Mock private ZootScene scene;
	@Mock private ZootActor focusedActor;
	@Mock private ZootCamera camera;
	@Mock private ZootCameraRegistry cameraRegistry;
	@Mock private ZootCameraScrollingStrategy previousScrollingStrategy;
	@Captor private ArgumentCaptor<ZootCamera> zootCameraCaptor;	
	
	private CameraFocusSensor cameraFocusSensor;
	private boolean acceptFixtureResult;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		when(scene.getUnitScale()).thenReturn(1.0f);
		when(scene.getActiveCamera()).thenReturn(camera);
		when(scene.getFirstActor(any())).thenReturn(focusedActor);
		when(scene.getCameraRegistry()).thenReturn(cameraRegistry);
		when(camera.getScrollingStrategy()).thenReturn(previousScrollingStrategy);
		when(camera.getZoom()).thenReturn(0.0f);
		
		acceptFixtureResult = true;
		cameraFocusSensor = new CameraFocusSensor();
		cameraFocusSensor.setFixtureAcceptFunc(fix -> acceptFixtureResult);
		ControllerAnnotations.setControllerParameter(cameraFocusSensor, "focusedActorName", FOCUSED_ACTOR_NAME);
		ControllerAnnotations.setControllerParameter(cameraFocusSensor, "scene", scene);
	}
	
	@Test
	public void shouldNotBeFocusedByDefault()
	{
		assertFalse(cameraFocusSensor.isFocused());
	}
	
	@Test
	public void shouldSetFixtureAcceptFunction()
	{
		//given
		cameraFocusSensor.setFixtureAcceptFunc(fix -> false);
		
		//when
		cameraFocusSensor.preUpdate(1.0f, mock(ZootActor.class));
		cameraFocusSensor.onCollision(mock(Fixture.class));
		cameraFocusSensor.postUpdate(1.0f, mock(ZootActor.class));
		
		//then
		assertFalse(cameraFocusSensor.isFocused());		
	}
			
	@Test(expected = RuntimeZootException.class)
	public void shouldThrowIfFocusActorWasNotFound()
	{
		//given
		when(scene.getFirstActor(any())).thenReturn(null);
		
		//when
		cameraFocusSensor.preUpdate(1.0f, mock(ZootActor.class));
		cameraFocusSensor.onCollision(mock(Fixture.class));
		cameraFocusSensor.postUpdate(1.0f, mock(ZootActor.class));
		
		//then throw
	}
	
	@Test
	public void shouldNotFocusIfFixtureIsNotAccepted()
	{
		//given
		acceptFixtureResult = false;
		
		//when
		cameraFocusSensor.preUpdate(1.0f, mock(ZootActor.class));
		cameraFocusSensor.onCollision(mock(Fixture.class));
		cameraFocusSensor.postUpdate(1.0f, mock(ZootActor.class));
		
		//then
		assertFalse(cameraFocusSensor.isFocused());
		verify(camera, never()).setScrollingStrategy(any());
	}
	
	@Test
	public void shouldSetNewActiveCameraThatFocusesOnFocusActor()
	{
		//given
		ZootCamera defaultCamera = mock(ZootCamera.class);
		when(defaultCamera.getPosition()).thenReturn(new Vector3());
		
		acceptFixtureResult = true;
		when(cameraRegistry.getCamera(ZootCameraRegistry.DEFAULT_CAMERA_NAME)).thenReturn(defaultCamera);
		
		//when
		cameraFocusSensor.preUpdate(1.0f, mock(ZootActor.class));
		cameraFocusSensor.onCollision(mock(Fixture.class));
		cameraFocusSensor.postUpdate(1.0f, mock(ZootActor.class));
		
		//then
		verify(scene).setActiveCamera(zootCameraCaptor.capture());
		assertNotNull(zootCameraCaptor.getValue());		
	}
}
