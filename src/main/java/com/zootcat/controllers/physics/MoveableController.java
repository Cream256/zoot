package com.zootcat.controllers.physics;

import com.badlogic.gdx.math.MathUtils;
import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.controllers.factory.CtrlDebug;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;
import com.zootcat.utils.ZootUtils;

/**
 * Controller used to move physicall body around the scene.
 * 
 * @ctrlParam walkForce - horitontal force that will be set during walking
 * @ctrlParam runForce - horizontal force that will be set during running
 * @ctrlParam jumpUpForce - vertical force that will be set when jumping up
 * @ctrlParam jumpForwardForce - horizontal force that will be set when jumping forward
 * @ctrlParam inAirForce - horizontal force that will be used to move actor in air
 * @ctrlParam maxInAirForce - maximum allowed horizontal force when moving in air
 * @ctrlParam jumpTimeout - timeout in ms after which actor can try to jump again, default 100
 * @author Cream
 *
 */
public class MoveableController extends ControllerAdapter
{
	@CtrlParam(debug = true) private float walkForce = 1.0f;
	@CtrlParam(debug = true) private float runForce = 2.0f;
	@CtrlParam(debug = true) private float jumpUpForce = 1.0f;
	@CtrlParam(debug = true) private float jumpForwardForce = 1.0f;
	@CtrlParam(debug = true) private float inAirForce = 1.0f;
	@CtrlParam(debug = true) private float maxInAirForce = 1.0f;		
	@CtrlParam(debug = true) private int jumpTimeout = 100;
	@CtrlDebug private int timeout = 0;
	
	private float lastDelta;
	private PhysicsBodyController physicsCtrl;
	private DetectGroundController groundCtrl;
	
	@Override
	public void onAdd(ZootActor actor)
	{
		timeout = 0;
		lastDelta = 0.0f;
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
		lastDelta = delta;
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
	
	public void moveInAir(ZootDirection direction)
	{
		float vx = physicsCtrl.getVelocity().x + inAirForce * direction.getHorizontalValue() * lastDelta;		
		
		boolean moveToRight = direction == ZootDirection.Right;
		vx = MathUtils.clamp(vx, moveToRight ? 0.0f : -maxInAirForce, moveToRight ? maxInAirForce : 0.0f);	
	
		physicsCtrl.setVelocity(vx, 0.0f, true, false);
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
