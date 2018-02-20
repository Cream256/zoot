package com.zootcat.controllers.physics;

import com.zootcat.controllers.Controller;
import com.zootcat.physics.ZootCollisionListener;
import com.zootcat.scene.ZootActor;

public abstract class PhysicsCollisionController extends ZootCollisionListener implements Controller 
{
	@Override
	public void init(ZootActor actor)
	{
		//noop
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
}
