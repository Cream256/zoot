package com.zootcat.controllers.physics;

import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;

public class FlyableController extends ControllerAdapter
{
	@CtrlParam(debug = true) private float flyVel = 1.0f;
	
	private PhysicsBodyController physicsCtrl;
	
	@Override
	public void onAdd(ZootActor actor)
	{
		physicsCtrl = actor.getController(PhysicsBodyController.class);
	}
	
	@Override
	public void onRemove(ZootActor actor)
	{
		physicsCtrl = null;
	}
		
	public void fly(ZootDirection direction)
	{
		physicsCtrl.setVelocity(flyVel * direction.getHorizontalValue(), 0.0f, true, false);
	}
	
	public void stop()
	{
		physicsCtrl.setVelocity(0.0f, 0.0f, true, false);
	}
}
