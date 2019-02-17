package com.zootcat.controllers.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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
	private static final String FOCUSED_ACTOR_NAME = "CameraAttractor";
	
	@Mock private ZootScene scene;
	@Mock private ZootActor actor;
	@Mock private ZootCamera camera;
	@Captor private ArgumentCaptor<ZootCameraScrollingStrategy> scrollingStrategyCaptor;
	@Mock private ZootCameraScrollingStrategy previousScrollingStrategy;
	private CameraFocusSensor cameraFocusSensor;
	private boolean acceptFixtureResult;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		when(scene.getUnitScale()).thenReturn(1.0f);
		when(scene.getCamera()).thenReturn(camera);
		when(scene.getFirstActor(any())).thenReturn(actor);
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
	
	@Test
	public void shouldFocusOnActor()
	{
		//when
		cameraFocusSensor.preUpdate(1.0f, mock(ZootActor.class));
		cameraFocusSensor.onCollision(mock(Fixture.class));
		cameraFocusSensor.postUpdate(1.0f, mock(ZootActor.class));
		
		//then
		assertTrue(cameraFocusSensor.isFocused());
	}
	
	@Test
	public void shouldZoomOnFocus()
	{
		//given
		final float desiredZoomOnFocus = 2.0f;
		ControllerAnnotations.setControllerParameter(cameraFocusSensor, "zoom", desiredZoomOnFocus);
		
		//when
		cameraFocusSensor.preUpdate(0.5f, mock(ZootActor.class));
		cameraFocusSensor.onCollision(mock(Fixture.class));
		cameraFocusSensor.postUpdate(0.5f, mock(ZootActor.class));
		
		//then
		verify(camera).setZoom(desiredZoomOnFocus / 2.0f);
		
		//when
		cameraFocusSensor.preUpdate(0.5f, mock(ZootActor.class));
		cameraFocusSensor.onCollision(mock(Fixture.class));
		cameraFocusSensor.postUpdate(0.5f, mock(ZootActor.class));
		
		//then
		verify(camera).setZoom(desiredZoomOnFocus);
	}
	
	@Test
	public void shouldZoomOnDefocus()
	{
		//given
		final float desiredZoomOnFocus = 2.0f;
		ControllerAnnotations.setControllerParameter(cameraFocusSensor, "zoom", desiredZoomOnFocus);
		
		//when
		cameraFocusSensor.preUpdate(1.0f, mock(ZootActor.class));
		cameraFocusSensor.onCollision(mock(Fixture.class));
		cameraFocusSensor.postUpdate(1.0f, mock(ZootActor.class));
		
		//then
		verify(camera).setZoom(desiredZoomOnFocus);
		
		//when
		cameraFocusSensor.preUpdate(0.5f, mock(ZootActor.class));
		cameraFocusSensor.postUpdate(0.5f, mock(ZootActor.class));
		
		//then
		verify(camera).setZoom(1.0f);
		
		//when
		cameraFocusSensor.preUpdate(0.5f, mock(ZootActor.class));
		cameraFocusSensor.postUpdate(0.5f, mock(ZootActor.class));
		
		//then
		verify(camera).setZoom(0.0f);
	}
	
	@Test
	public void shouldSetNewScrollingStrategyWhenFocusing()
	{
		//when
		cameraFocusSensor.preUpdate(1.0f, mock(ZootActor.class));
		cameraFocusSensor.onCollision(mock(Fixture.class));
		cameraFocusSensor.postUpdate(1.0f, mock(ZootActor.class));
		
		//then
		verify(camera).setScrollingStrategy(scrollingStrategyCaptor.capture());
		
		ZootCameraScrollingStrategy newScrollingStrategy = scrollingStrategyCaptor.getValue();
		assertNotNull(newScrollingStrategy);
		assertEquals(ZootScrollToScrollingStrategy.class, newScrollingStrategy.getClass());
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
	public void shouldDefocusWhenFixtureIsNotAcceptedAnymore()
	{
		//given
		cameraFocusSensor.preUpdate(1.0f, mock(ZootActor.class));
		cameraFocusSensor.onCollision(mock(Fixture.class));
		cameraFocusSensor.postUpdate(1.0f, mock(ZootActor.class));
		
		//when
		acceptFixtureResult = false;
		cameraFocusSensor.preUpdate(1.0f, mock(ZootActor.class));
		cameraFocusSensor.onCollision(mock(Fixture.class));
		cameraFocusSensor.postUpdate(1.0f, mock(ZootActor.class));
		
		//then
		assertFalse(cameraFocusSensor.isFocused());
	}
}
