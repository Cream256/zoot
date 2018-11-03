package com.zootcat.controllers.physics;

import com.badlogic.gdx.math.MathUtils;
import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.scene.ZootActor;

public class ApplySinVelocityController extends ControllerAdapter
{
	@CtrlParam private float velocityX = 1.0f;
	@CtrlParam private float velocityY = 1.0f;
	@CtrlParam private boolean useX = true;
	@CtrlParam private boolean useY = true;
	@CtrlParam private float mul = 1.0f;

	private float time = 0.0f;
		
	@Override
	public void onUpdate(float delta, ZootActor actor)
	{
		time += delta;	
		
		float sin = MathUtils.sin(time * mul);	
		float vx = velocityX * sin;
		float vy = velocityY * sin;
		
		actor.controllerAction(PhysicsBodyController.class, ctrl -> ctrl.setVelocity(vx, vy, useX, useY));
	}
	
	public void setVelocityX(float value)
	{
		velocityX = value;
	}
	
	public void setVelocityY(float value)
	{
		velocityY = value;
	}
	
	public void setMul(float value)
	{
		mul = value;
	}
	
	public void setUseX(boolean value)
	{
		useX = value;
	}
	
	public void setUseY(boolean value)
	{
		useY = value;
	}
}
