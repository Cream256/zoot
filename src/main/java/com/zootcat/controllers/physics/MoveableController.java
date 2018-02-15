package com.zootcat.controllers.physics;

import com.zootcat.controllers.Controller;
import com.zootcat.controllers.factory.CtrlDebug;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;
import com.zootcat.utils.ZootUtils;

public class MoveableController implements Controller
{
	@CtrlParam(debug = true) private float walkForce = 1.0f;
	@CtrlParam(debug = true) private float runForce = 2.0f;
	@CtrlParam(debug = true) private float jumpForce = 1.0f;
	@CtrlParam(debug = true) private int jumpTimeout = 0;
	@CtrlDebug private int timeout = 0;
	
	private PhysicsBodyController physicsCtrl;
	private DetectGroundController groundCtrl;
	
	@Override
	public void init(ZootActor actor)
	{
		//noop
	}

	@Override
	public void onAdd(ZootActor actor)
	{
		physicsCtrl = actor.getController(PhysicsBodyController.class);
		groundCtrl = actor.getController(DetectGroundController.class);
	}

	@Override
	public void onRemove(ZootActor actor)
	{
		physicsCtrl = null;
		groundCtrl = null;
	}

	@Override
	public void onUpdate(float delta, ZootActor actor)
	{
		timeout = Math.max(0, timeout - ZootUtils.trunc(delta * 1000));
	}
	
	public void jump()
	{
		if(timeout > 0) return;
		if(!groundCtrl.isOnGround()) return;
		
		physicsCtrl.setVelocity(0.0f, jumpForce, false, true);
		timeout = jumpTimeout;
	}
	
	public void walk(ZootDirection direction)
	{
		float vx = direction == ZootDirection.Right ? walkForce : -walkForce;		
		physicsCtrl.setVelocity(vx, 0.0f, true, false);
	}

	public void run(ZootDirection direction)
	{
		float vx = direction == ZootDirection.Right ? runForce : -runForce;
		physicsCtrl.setVelocity(vx, 0.0f, true, false);
	}
}
