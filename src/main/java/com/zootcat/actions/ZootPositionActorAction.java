package com.zootcat.actions;

import com.zootcat.controllers.physics.PhysicsBodyController;

public class ZootPositionActorAction extends ZootAction
{
	private float posX = 0.0f;
	private float posY = 0.0f;
	
	public void setPosition(float x, float y)
	{
		posX = x;
		posY = y;
	}
		
	public float getX()
	{
		return posX;
	}
	
	public float getY()
	{
		return posY;
	}
		
	@Override
	public boolean act(float delta)
	{
		getTargetZootActor().controllersAction(PhysicsBodyController.class, ctrl -> 
		{
			ctrl.setPosition(posX, posY);
		});		
		return true;
	}
}