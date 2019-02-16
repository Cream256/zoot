package com.zootcat.controllers.logic;

import java.util.function.Function;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.zootcat.camera.ZootCameraScrollingStrategy;
import com.zootcat.camera.ZootScrollToScrollingStrategy;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.controllers.physics.OnCollideWithSensorController;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.scene.ZootActor;

public class CameraFocusSensor extends OnCollideWithSensorController
{
	@CtrlParam(required = true) private String focusedActorName;
	
	private int acceptedCollisions = 0;
	private boolean focused = false;
	private ZootCameraScrollingStrategy previousScrollingStrategy = null;
	private Function<Fixture, Boolean> acceptFixture;		
	
	public CameraFocusSensor()
	{
		this(fix -> true);
	}
	
	public CameraFocusSensor(Function<Fixture, Boolean> acceptFixtureFunc)
	{
		this.acceptFixture = acceptFixtureFunc;
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
	}
	
	private void defocus()
	{
		scene.getCamera().setScrollingStrategy(previousScrollingStrategy);
		previousScrollingStrategy = null;
		focused = false;
	}
	
	private void focus()
	{
		focused = true;
		previousScrollingStrategy = scene.getCamera().getScrollingStrategy();
		
		ZootActor actorToFocus = scene.getFirstActor(act -> act.getName().equalsIgnoreCase(focusedActorName));
		if(actorToFocus == null) throw new RuntimeZootException("Actor to focus not found: " + focusedActorName);
		
		scene.getCamera().setScrollingStrategy(new ZootScrollToScrollingStrategy(actorToFocus, 1.0f));
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
