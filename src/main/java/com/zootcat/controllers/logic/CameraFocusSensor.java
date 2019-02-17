package com.zootcat.controllers.logic;

import java.util.function.Function;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.zootcat.actions.ZootZoomCameraAction;
import com.zootcat.camera.ZootCamera;
import com.zootcat.camera.ZootCameraScrollingStrategy;
import com.zootcat.camera.ZootScrollToScrollingStrategy;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.controllers.physics.OnCollideWithSensorController;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.scene.ZootActor;

public class CameraFocusSensor extends OnCollideWithSensorController
{
	private static final float DURATION = 1.0f;
	
	@CtrlParam(required = true) private String focusedActorName;
	@CtrlParam private float zoom = 1.0f;
	
	private int acceptedCollisions = 0;
	private boolean focused = false;
	private ZootCameraScrollingStrategy previousScrollingStrategy = null;	
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
		ZootCamera camera = scene.getCamera();		
		
		camera.setScrollingStrategy(previousScrollingStrategy);
				
		previousScrollingStrategy = null;
		
		zoomingOnFocus = false;
		zoomingOnDefocus = true;
		focused = false;
	}
	
	private void focus()
	{		
		//fetch actor to focus on
		ZootActor actorToFocus = scene.getFirstActor(act -> act.getName().equalsIgnoreCase(focusedActorName));
		if(actorToFocus == null) throw new RuntimeZootException("Actor to focus not found: " + focusedActorName);
				
		//set the scrolling strategy
		ZootCamera camera = scene.getCamera();
		previousScrollingStrategy = camera.getScrollingStrategy();		
		camera.setScrollingStrategy(new ZootScrollToScrollingStrategy(actorToFocus, DURATION));
				
		//prepare zoom actions
		zoomOnFocus.setCamera(camera);
		zoomOnFocus.setDesiredZoom(zoom);		
		zoomOnFocus.setStartingZoom(camera.getZoom());
		zoomOnFocus.setDuration(DURATION);
		zoomOnFocus.restart();
		
		zoomOnDefocus.setCamera(camera);
		zoomOnDefocus.setStartingZoom(zoom);
		zoomOnDefocus.setDesiredZoom(camera.getZoom());
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
}
