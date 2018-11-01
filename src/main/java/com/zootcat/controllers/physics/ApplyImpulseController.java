package com.zootcat.controllers.physics;

import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.scene.ZootActor;

public class ApplyImpulseController extends ControllerAdapter 
{
	@CtrlParam private float impulseX = 0.0f;
	@CtrlParam private float impulseY = 0.0f;
		
	@Override
	public void onUpdate(float delta, ZootActor actor)
	{
		actor.controllerAction(PhysicsBodyController.class, ctrl -> ctrl.applyImpulse(impulseX * delta, impulseY * delta));
	}
	
	public float getImpulseX()
	{
		return impulseX;
	}
	
	public float getImpulseY()
	{
		return impulseY;
	}
	
	public void setImpulseX(float value)
	{
		impulseX = value;
	}
	
	public void setImpulseY(float value)
	{
		impulseY = value;
	}
}
