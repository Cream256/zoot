package com.zootcat.controllers.logic;

import java.util.Stack;
import java.util.function.Function;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.zootcat.camera.ZootCamera;
import com.zootcat.camera.ZootNullScrollingStrategy;
import com.zootcat.camera.ZootScrollToScrollingStrategy;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.controllers.physics.OnCollideWithSensorController;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.scene.ZootActor;

public class CameraFocusSensor extends OnCollideWithSensorController
{
	private static final Stack<ZootCamera> cameraStack = new Stack<ZootCamera>();
	
	@CtrlParam private float zoom = 1.0f;
	@CtrlParam private float focusDuration = 1.0f;
	@CtrlParam(required = true) private String focusedActorName;
		
	private boolean isFocused = false;
	private int acceptedCollisions = 0;	
	private ZootCamera focusCamera = null;	
	private Function<Fixture, Boolean> acceptFixture = fix -> true;
	
	@Override
	public void onAdd(ZootActor actor)
	{
		super.onAdd(actor);		
		focusCamera = new ZootCamera(scene.getWidth() / scene.getUnitScale(), scene.getHeight() / scene.getUnitScale());
	}
	
	@Override
	public SensorCollisionResult onCollision(Fixture fixture)
	{
		acceptedCollisions += acceptFixture.apply(fixture) ? 1 : 0;
		return SensorCollisionResult.ProcessNext;
	}
	
	@Override
	public void preUpdate(float delta, ZootActor actor)
	{
		acceptedCollisions = 0;
	}
	
	@Override
	public void postUpdate(float delta, ZootActor actor)
	{
		if(acceptedCollisions > 0 && !isFocused)
		{
			focus();
		}
		else if(acceptedCollisions == 0 && isFocused)
		{
			defocus();
		}		
	}
	
	private void focus()
	{
		if(cameraStack.isEmpty()) cameraStack.push(scene.getActiveCamera());
		
		prepareFocusCamera(cameraStack.peek());
		scene.setActiveCamera(focusCamera);
		isFocused = true;
		
		cameraStack.push(focusCamera);
	}
	
	private void prepareFocusCamera(ZootCamera previousCamera)
	{
		//fetch actor to focus on
		ZootActor actorToFocus = scene.getFirstActor(act -> act.getName().equalsIgnoreCase(focusedActorName));
		if(actorToFocus == null) throw new RuntimeZootException("Actor to focus not found: " + focusedActorName);
		
		//prepare camera
		focusCamera.setScene(scene);
		if(focusCamera.getScrollingStrategy() == ZootNullScrollingStrategy.Instance)
		{
			focusCamera.setScrollingStrategy(new ZootScrollToScrollingStrategy(actorToFocus, focusDuration));	
		}
		focusCamera.getScrollingStrategy().reset();
		focusCamera.setTarget(previousCamera.getTarget());
		focusCamera.setEdgeSnapping(previousCamera.isEdgeSnapping());
		focusCamera.setPosition(previousCamera.getPosition().x, previousCamera.getPosition().y);
		focusCamera.setViewportSize(previousCamera.getViewportWidth(), previousCamera.getViewportHeight());
		focusCamera.setZoom(previousCamera.getZoom());
	}
	
	private void defocus()
	{		
		cameraStack.pop();	
		
		ZootCamera previousCamera = cameraStack.peek();
		previousCamera.setPosition(focusCamera.getPosition().x, focusCamera.getPosition().y);
		
		scene.setActiveCamera(previousCamera);
		isFocused = false;
	}
	
	public ZootCamera getFocusCamera()
	{
		return focusCamera;
	}
	
	public boolean isFocused()
	{
		return isFocused;
	}
	
	public void setFixtureAcceptFunc(Function<Fixture, Boolean> acceptFixtureFunc)
	{
		acceptFixture = acceptFixtureFunc;
	}
	
	public int getAcceptedCollisionCount()
	{
		return acceptedCollisions;
	}

	public static void clearCameraStack()
	{
		cameraStack.clear();
	}
}

/*
public class CameraFocusSensor extends OnCollideWithSensorController
{	
	private static final float DURATION = 1.0f;
	private static List<ZootCamera> CAMERA_QUEUE = new ArrayList<ZootCamera>();
	
	@CtrlParam(required = true) private String focusedActorName;
	@CtrlParam private float zoom = 1.0f;
	
	private ZootCamera focusCamera;
	private int acceptedCollisions = 0;
	private boolean focused = false;	
	private Function<Fixture, Boolean> acceptFixture = fix -> true;
	
	private boolean zoomingOnFocus = false;
	private boolean zoomingOnDefocus = false;	
	private ZootZoomCameraAction zoomOnFocus = new ZootZoomCameraAction();
	private ZootZoomCameraAction zoomOnDefocus = new ZootZoomCameraAction();
		
	public void setFixtureAcceptFunc(Function<Fixture, Boolean> acceptFixtureFunc)
	{
		acceptFixture = acceptFixtureFunc;
	}
	
	@Override
	public void onAdd(ZootActor actor)
	{
		super.onAdd(actor);
		
		if(CAMERA_QUEUE.isEmpty())
		{
			queueCamera(scene.getActiveCamera());
		}
	}
	
	@Override
	public void preUpdate(float delta, ZootActor actor)
	{
		acceptedCollisions = 0;
	}
	
	@Override
	public void postUpdate(float delta, ZootActor actor)
	{
		if(acceptedCollisions == 0 && focused)
		{
			defocus();
		}		
		else if(acceptedCollisions > 0 && !focused)
		{
			focus();
		}
				
		if(zoomingOnFocus)
		{
			zoomingOnFocus = !zoomOnFocus.act(delta);
		}		
		else if(zoomingOnDefocus)
		{
			zoomingOnDefocus = !zoomOnDefocus.act(delta);
		}
	}
	
	private void defocus()
	{
		ZootCamera defaultCamera = scene.getCameraRegistry().getCamera(ZootCameraRegistry.DEFAULT_CAMERA_NAME);
		defaultCamera.setPosition(focusCamera.getPosition().x, focusCamera.getPosition().y);
		scene.setActiveCamera(defaultCamera);
		
		zoomingOnFocus = false;
		zoomingOnDefocus = true;
		focused = false;
	}
	
	private void focus()
	{		
		//fetch actor to focus on
		ZootActor actorToFocus = scene.getFirstActor(act -> act.getName().equalsIgnoreCase(focusedActorName));
		if(actorToFocus == null) throw new RuntimeZootException("Actor to focus not found: " + focusedActorName);
		
		//create focus camera
		if(focusCamera == null)
		{
			focusCamera = new ZootCamera(scene.getWidth() / scene.getUnitScale(), scene.getHeight() / scene.getUnitScale());
			focusCamera.setScene(scene);
			focusCamera.setScrollingStrategy(new ZootScrollToScrollingStrategy(actorToFocus, DURATION));
		}	
		
		ZootCamera defaultCamera = scene.getCameraRegistry().getCamera(ZootCameraRegistry.DEFAULT_CAMERA_NAME);
		focusCamera.getScrollingStrategy().reset();
		focusCamera.setTarget(defaultCamera.getTarget());
		focusCamera.setEdgeSnapping(defaultCamera.isEdgeSnapping());
		focusCamera.setPosition(defaultCamera.getPosition().x, defaultCamera.getPosition().y);
		focusCamera.setViewportSize(defaultCamera.getViewportWidth(), defaultCamera.getViewportHeight());
		focusCamera.setZoom(defaultCamera.getZoom());
		focusCamera.setPosition(defaultCamera.getPosition().x, defaultCamera.getPosition().y);
		
		//assign camera
		scene.setActiveCamera(focusCamera);
		queueCamera(focusCamera);
						
		//prepare zoom actions
		zoomOnFocus.setCamera(focusCamera);
		zoomOnFocus.setDesiredZoom(zoom);		
		zoomOnFocus.setStartingZoom(focusCamera.getZoom());
		zoomOnFocus.setDuration(DURATION);
		zoomOnFocus.restart();
		
		zoomOnDefocus.setCamera(defaultCamera);
		zoomOnDefocus.setStartingZoom(zoom);
		zoomOnDefocus.setDesiredZoom(defaultCamera.getZoom());
		zoomOnDefocus.setDuration(DURATION);
		zoomOnDefocus.restart();
				
		//set flags
		zoomingOnFocus = true;
		zoomingOnDefocus = false;		
		focused = true;
	}
	
	@Override
	public SensorCollisionResult onCollision(Fixture fixture)
	{
		acceptedCollisions += acceptFixture.apply(fixture) ? 1 : 0;
		return SensorCollisionResult.ProcessNext;
	}
			
	public boolean isFocused()
	{
		return focused;
	}
	
	private static void queueCamera(ZootCamera camera)
	{
		CAMERA_QUEUE.add(camera);
	}
	
	private static ZootCamera popCamera()
	{
		ZootCamera camera = CAMERA_QUEUE.remove(CAMERA_QUEUE.size() - 1);
		return camera;
	}
	
	private static ZootCamera peekCamera()
	{
		return CAMERA_QUEUE.get(CAMERA_QUEUE.size() - 1);
	}
}
*/