package com.zootcat.controllers.physics;

import com.zootcat.controllers.Controller;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.physics.ZootCollisionListener;
import com.zootcat.scene.ZootActor;

public abstract class PhysicsCollisionController extends ZootCollisionListener implements Controller 
{
	private ZootActor controllerActor;
	private boolean enabled = true;
	
	@Override
	public void init(ZootActor actor)	
	{
		controllerActor = actor;
	}

	@Override
	public void onAdd(ZootActor actor) 
	{
		actor.addListener(this);
	}

	@Override
	public void onRemove(ZootActor actor) 
	{
		actor.removeListener(this);
	}

	@Override
	public void onUpdate(float delta, ZootActor actor) 
	{
		//noop
	}
	
	@Override
	public boolean isEnabled()
	{
		return enabled;
	}
	
	@Override
	public void setEnabled(boolean value)
	{
		enabled = value;
	}
	
	public ZootActor getControllerActor()
	{
		if(controllerActor == null)
		{
			throw new RuntimeZootException("PhysicsCollisionController::init() was not called.");
		}
		return controllerActor;
	}
}
