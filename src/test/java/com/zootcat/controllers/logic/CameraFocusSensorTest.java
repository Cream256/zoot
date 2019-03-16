package com.zootcat.controllers.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.zootcat.camera.ZootCamera;
import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;

public class CameraFocusSensorTest
{	
	private static final String FOCUSED_ACTOR_NAME = "CameraAttractor";
	private static final float SCENE_WIDTH = 128.0f;
	private static final float SCENE_HEIGHT = 256.0f;
	
	@Mock private ZootScene scene;
	@Mock private ZootActor ctrlActor;
	@Mock private ZootActor focusedActor;	
	@Mock private ZootCamera defaultCamera;
	private CameraFocusSensor cameraFocusSensor;
	
	@BeforeClass
	public static void initialize()
	{
		Box2D.init();
	}
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		when(scene.getWidth()).thenReturn(SCENE_WIDTH);
		when(scene.getHeight()).thenReturn(SCENE_HEIGHT);
		when(scene.getUnitScale()).thenReturn(1.0f);
		when(scene.getActiveCamera()).thenReturn(defaultCamera);
		when(scene.getFirstActor(any())).thenReturn(focusedActor);
		when(defaultCamera.getPosition()).thenReturn(new Vector3());
		when(ctrlActor.getSingleController(PhysicsBodyController.class)).thenReturn(mock(PhysicsBodyController.class));
		
		cameraFocusSensor = new CameraFocusSensor();
		ControllerAnnotations.setControllerParameter(cameraFocusSensor, "scene", scene);
		ControllerAnnotations.setControllerParameter(cameraFocusSensor, "focusedActorName", FOCUSED_ACTOR_NAME);
		CameraFocusSensor.clearCameraStack();
	}
	
	@Test
	public void shouldNotBeFocusedByDefault()
	{
		assertFalse(cameraFocusSensor.isFocused());
	}
	
	@Test
	public void shouldAcceptAnyFixtureByDefault()
	{
		//when
		cameraFocusSensor.onCollision(mock(Fixture.class));
		cameraFocusSensor.onCollision(mock(Fixture.class));
		cameraFocusSensor.onCollision(mock(Fixture.class));
		
		//then
		assertEquals(3, cameraFocusSensor.getAcceptedCollisionCount());
	}
	
	@Test
	public void shouldSetAcceptFixtureFunction()
	{
		//when
		cameraFocusSensor.setFixtureAcceptFunc(fix -> false);		
		cameraFocusSensor.onCollision(mock(Fixture.class));
		cameraFocusSensor.onCollision(mock(Fixture.class));
		cameraFocusSensor.onCollision(mock(Fixture.class));
		
		//then
		assertEquals(0, cameraFocusSensor.getAcceptedCollisionCount());
	}
	
	@Test
	public void shouldCreateValidFocusCamera()
	{		
		//when
		cameraFocusSensor.onAdd(ctrlActor);
		
		//then
		ZootCamera focusCamera = cameraFocusSensor.getFocusCamera();
		assertEquals(SCENE_WIDTH, focusCamera.getWorldWidth(), 0.0f);
		assertEquals(SCENE_HEIGHT, focusCamera.getWorldHeight(), 0.0f);
	}
	
	@Test
	public void shouldFocusAndDefocus()
	{
		//when
		cameraFocusSensor.onAdd(ctrlActor);
		cameraFocusSensor.preUpdate(0.0f, ctrlActor);
		cameraFocusSensor.onCollision(mock(Fixture.class));
		cameraFocusSensor.postUpdate(0.0f, ctrlActor);
		
		//then
		assertTrue(cameraFocusSensor.isFocused());
		
		//when
		cameraFocusSensor.preUpdate(0.0f, ctrlActor);
		cameraFocusSensor.postUpdate(0.0f, ctrlActor);
		
		//then
		assertFalse(cameraFocusSensor.isFocused());
	}
	
	@Test
	public void shouldSetValidCameraWhenFocusing()
	{
		//when
		cameraFocusSensor.onAdd(ctrlActor);
		cameraFocusSensor.preUpdate(0.0f, ctrlActor);
		cameraFocusSensor.onCollision(mock(Fixture.class));
		cameraFocusSensor.postUpdate(0.0f, ctrlActor);
		
		//then
		ZootCamera focusCamera = cameraFocusSensor.getFocusCamera();		
		verify(scene).setActiveCamera(focusCamera);
	}
	
	@Test
	public void shouldProperlyRevertToPreviousCameras()
	{
		//given
		CameraFocusSensor cameraFocusSensor2 = new CameraFocusSensor();
		ControllerAnnotations.setControllerParameter(cameraFocusSensor2, "scene", scene);
		ControllerAnnotations.setControllerParameter(cameraFocusSensor2, "focusedActorName", FOCUSED_ACTOR_NAME);
		
		CameraFocusSensor cameraFocusSensor3 = new CameraFocusSensor();
		ControllerAnnotations.setControllerParameter(cameraFocusSensor3, "scene", scene);
		ControllerAnnotations.setControllerParameter(cameraFocusSensor3, "focusedActorName", FOCUSED_ACTOR_NAME);
		
		//when
		cameraFocusSensor.onAdd(ctrlActor);
		cameraFocusSensor2.onAdd(ctrlActor);
		cameraFocusSensor3.onAdd(ctrlActor);
		
		cameraFocusSensor.preUpdate(0.0f, ctrlActor);
		cameraFocusSensor.onCollision(mock(Fixture.class));
		cameraFocusSensor.postUpdate(0.0f, ctrlActor);
				
		cameraFocusSensor2.preUpdate(0.0f, ctrlActor);
		cameraFocusSensor2.onCollision(mock(Fixture.class));
		cameraFocusSensor2.postUpdate(0.0f, ctrlActor);
		
		cameraFocusSensor3.preUpdate(0.0f, ctrlActor);
		cameraFocusSensor3.onCollision(mock(Fixture.class));
		cameraFocusSensor3.postUpdate(0.0f, ctrlActor);
		
		//then
		InOrder inOrder = inOrder(scene);		
		inOrder.verify(scene).setActiveCamera(cameraFocusSensor.getFocusCamera());
		inOrder.verify(scene).setActiveCamera(cameraFocusSensor2.getFocusCamera());
		inOrder.verify(scene).setActiveCamera(cameraFocusSensor3.getFocusCamera());
		
		//when
		cameraFocusSensor3.preUpdate(0.0f, ctrlActor);
		cameraFocusSensor3.postUpdate(0.0f, ctrlActor);
		
		cameraFocusSensor2.preUpdate(0.0f, ctrlActor);
		cameraFocusSensor2.postUpdate(0.0f, ctrlActor);
		
		cameraFocusSensor.preUpdate(0.0f, ctrlActor);
		cameraFocusSensor.postUpdate(0.0f, ctrlActor);
		
		//then
		inOrder.verify(scene).setActiveCamera(cameraFocusSensor2.getFocusCamera());
		inOrder.verify(scene).setActiveCamera(cameraFocusSensor.getFocusCamera());
	}
	
	@Test(expected = RuntimeZootException.class)
	public void shouldThrowIfFocusActorWasNotFound()
	{
		//given
		when(scene.getFirstActor(any())).thenReturn(null);
		
		//when
		cameraFocusSensor.onAdd(ctrlActor);
		cameraFocusSensor.preUpdate(1.0f, mock(ZootActor.class));
		cameraFocusSensor.onCollision(mock(Fixture.class));
		cameraFocusSensor.postUpdate(1.0f, mock(ZootActor.class));
		
		//then throw
	}
	
	@Test
	public void shouldProperlySetupFocusCameraWhenFocusing()
	{
		//given
		when(defaultCamera.getPosition()).thenReturn(new Vector3(1.0f, 2.0f, 0.0f));
		when(defaultCamera.isEdgeSnapping()).thenReturn(true);
		when(defaultCamera.getTarget()).thenReturn(focusedActor);
		when(defaultCamera.getViewportWidth()).thenReturn(100.0f);
		when(defaultCamera.getViewportHeight()).thenReturn(200.0f);
		when(defaultCamera.getZoom()).thenReturn(1.28f);
		
		//when
		cameraFocusSensor.onAdd(ctrlActor);
		cameraFocusSensor.preUpdate(1.0f, mock(ZootActor.class));
		cameraFocusSensor.onCollision(mock(Fixture.class));
		cameraFocusSensor.postUpdate(1.0f, mock(ZootActor.class));
		
		//then
		ZootCamera focusCamera = cameraFocusSensor.getFocusCamera();
		assertEquals(defaultCamera.getTarget(), focusCamera.getTarget());
		assertEquals(defaultCamera.getViewportWidth(), focusCamera.getViewportWidth(), 0.0f);
		assertEquals(defaultCamera.getViewportHeight(), focusCamera.getViewportHeight(), 0.0f);
		assertEquals(defaultCamera.getZoom(), focusCamera.getZoom(), 0.0f);		
		assertEquals(defaultCamera.isEdgeSnapping(), focusCamera.isEdgeSnapping());
		assertEquals(defaultCamera.getPosition(), focusCamera.getPosition());
	}
	
	@Test
	public void shouldProperlySetupPreviousCameraWhenDefocusing()
	{
		//when
		cameraFocusSensor.onAdd(ctrlActor);
		cameraFocusSensor.preUpdate(1.0f, mock(ZootActor.class));
		cameraFocusSensor.onCollision(mock(Fixture.class));
		cameraFocusSensor.postUpdate(1.0f, mock(ZootActor.class));
		
		cameraFocusSensor.getFocusCamera().setPosition(100.0f, 200.0f);
		cameraFocusSensor.preUpdate(1.0f, mock(ZootActor.class));
		cameraFocusSensor.postUpdate(1.0f, mock(ZootActor.class));
		
		//then
		verify(defaultCamera).setPosition(100.0f, 200.0f);
	}
	
	@Test
	public void shouldFocusOnlyOnce()
	{
		//when
		cameraFocusSensor.onAdd(ctrlActor);
		cameraFocusSensor.preUpdate(1.0f, mock(ZootActor.class));
		cameraFocusSensor.onCollision(mock(Fixture.class));
		cameraFocusSensor.postUpdate(1.0f, mock(ZootActor.class));
		
		cameraFocusSensor.preUpdate(1.0f, mock(ZootActor.class));
		cameraFocusSensor.onCollision(mock(Fixture.class));
		cameraFocusSensor.postUpdate(1.0f, mock(ZootActor.class));
		
		//then
		verify(scene, times(1)).setActiveCamera(cameraFocusSensor.getFocusCamera());				
	}
	
	@Test
	public void shouldDefocusOnlyOnce()
	{
		//when
		cameraFocusSensor.onAdd(ctrlActor);
		cameraFocusSensor.preUpdate(1.0f, mock(ZootActor.class));
		cameraFocusSensor.onCollision(mock(Fixture.class));
		cameraFocusSensor.postUpdate(1.0f, mock(ZootActor.class));
		
		cameraFocusSensor.preUpdate(1.0f, mock(ZootActor.class));
		cameraFocusSensor.postUpdate(1.0f, mock(ZootActor.class));
		
		cameraFocusSensor.preUpdate(1.0f, mock(ZootActor.class));
		cameraFocusSensor.postUpdate(1.0f, mock(ZootActor.class));
		
		//then
		verify(scene, times(1)).setActiveCamera(defaultCamera);
	}
}

/*	//TODO remove dead code
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
*/