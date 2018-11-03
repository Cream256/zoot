package com.zootcat.controllers.physics;

import com.badlogic.gdx.math.MathUtils;
import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.scene.ZootActor;

public class ApplySinImpulseController extends ControllerAdapter
{
	@CtrlParam private float impulseX = 1.0f;
	@CtrlParam private float impulseY = 1.0f;
	@CtrlParam private float mul = 1.0f;

	private float time = 0.0f;
		
	@Override
	public void onUpdate(float delta, ZootActor actor)
	{
		time += delta;	
		
		float sin = MathUtils.sin(time * mul);	
		float ix = impulseX * sin;
		float iy = impulseY * sin;
		
		actor.controllerAction(PhysicsBodyController.class, ctrl -> ctrl.applyImpulse(ix, iy));
	}
	
	public void setImpulseX(float value)
	{
		impulseX = value;
	}
	
	public void setImpulseY(float value)
	{
		impulseY = value;
	}
	
	public void setMul(float value)
	{
		mul = value;
	}
}
