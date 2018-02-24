package com.zootcat.controllers.physics;

import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.controllers.factory.CtrlDebug;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;
import com.zootcat.utils.ZootUtils;

public class MoveableController extends ControllerAdapter
{
	@CtrlParam(debug = true) private float walkForce = 1.0f;
	@CtrlParam(debug = true) private float runForce = 2.0f;
	@CtrlParam(debug = true) private float jumpUpForce = 1.0f;
	@CtrlParam(debug = true) private float jumpForwardForce = 1.0f;
	@CtrlParam(debug = true) private int jumpTimeout = 0;
	@CtrlDebug private int timeout = 0;
	
	private PhysicsBodyController physicsCtrl;
	private DetectGroundController groundCtrl;
	
	@Override
	public void onAdd(ZootActor actor)
	{
		timeout = 0;
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
	
	public void jumpUp()
	{
		if(!canJump()) return;
		
		physicsCtrl.setVelocity(0.0f, jumpUpForce, false, true);
		setJumpTimeout();
	}
	
	public void jumpForward(ZootDirection direction)
	{
		if(!canJump()) return;
		
		physicsCtrl.setVelocity(jumpForwardForce * direction.getHorizontalValue(), jumpUpForce, true, true);
		setJumpTimeout();
	}
	
	public void walk(ZootDirection direction)
	{		
		physicsCtrl.setVelocity(walkForce * direction.getHorizontalValue(), 0.0f, true, false);
	}

	public void run(ZootDirection direction)
	{
		physicsCtrl.setVelocity(runForce * direction.getHorizontalValue(), 0.0f, true, false);
	}
	
	protected boolean canJump()
	{
		return timeout == 0 && groundCtrl.isOnGround();
	}
	
	protected void setJumpTimeout()
	{
		timeout = jumpTimeout;
	}
}
