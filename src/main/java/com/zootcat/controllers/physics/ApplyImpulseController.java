package com.zootcat.controllers.physics;

import com.zootcat.controllers.Controller;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.scene.ZootActor;

public class ApplyImpulseController implements Controller 
{
	@CtrlParam private float impulseX = 0.0f;
	@CtrlParam private float impulseY = 0.0f;
			
	@Override
	public void init(ZootActor actor)
	{
		//noop
	}

	@Override
	public void onAdd(ZootActor actor)
	{
		//noop
	}

	@Override
	public void onRemove(ZootActor actor)
	{
		//noop
	}

	@Override
	public void onUpdate(float delta, ZootActor actor)
	{
		actor.controllerAction(PhysicsBodyController.class, ctrl -> ctrl.applyImpulse(impulseX * delta, impulseY * delta));
	}
}
