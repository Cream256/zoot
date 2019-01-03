package com.zootcat.actions;

import com.badlogic.gdx.math.Vector2;
import com.zootcat.controllers.physics.PhysicsBodyController;

public class ZootMoveActorAction extends ZootAction
{
	private float mx = 0.0f;
	private float my = 0.0f;
	
	public void setMovementX(float value)
	{
		mx = value;
	}
	
	public void setMovementY(float value) 
	{
		my = value;
	}
	
	public float getMovementX()
	{
		return mx;
	}
	
	public float getMovementY()
	{
		return my;
	}
		
	@Override
	public boolean act(float delta)
	{
		getTargetZootActor().controllersAction(PhysicsBodyController.class, ctrl -> 
		{
			Vector2 pos = ctrl.getCenterPositionRef();
			ctrl.setPosition(pos.x + mx, pos.y + my);
		});		
		return true;
	}
}
