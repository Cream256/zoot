package com.zootcat.controllers.physics;

import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.controllers.factory.CtrlDebug;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;
import com.zootcat.utils.ZootUtils;

/**
 * Controller used to move physicall body around the scene by walking.<br/>
 * For flying movement use {@link FlyableController}.
 * 
 * @ctrlParam walkVel - horitontal velocity that will be set during walking
 * @ctrlParam runVel - horizontal velocity that will be set during running
 * @ctrlParam jumpUpVel - vertical velocity that will be set when jumping up
 * @ctrlParam jumpForwardVelX - horizontal velocity that will be set when jumping forward
 * @ctrlParam jumpForwardVelY - vertical velocity that will be set when jumping forward
 * @ctrlParam inAirVelX - horizontal velocity that will be used to move actor in air
 * @ctrlParam maxInAirVel - maximum allowed horizontal velocity when moving in air
 * @ctrlParam jumpTimeout - timeout in ms after which actor can try to jump again, default 100
 * @ctrlParam canJump - if the actor can jump, default true
 * @ctrlParam canRun - if the actor can run, default true
 * @author Cream
 *
 */
public class WalkableController extends ControllerAdapter
{
	@CtrlParam private float walkVel = 1.0f;
	@CtrlParam private float runVel = 2.0f;
	@CtrlParam private float jumpUpVel = 1.0f;
	@CtrlParam private float jumpForwardVelX = 1.0f;
	@CtrlParam private float jumpForwardVelY = 1.0f;	
	@CtrlParam private float inAirVelX = 1.0f;			
	@CtrlParam private int jumpTimeout = 100;
	@CtrlParam private boolean canJump = true;
	@CtrlParam private boolean canRun = true;
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
	
	public void setRunVelocity(float velocity)
	{
		runVel = velocity;
	}
	
	public float getRunVelocity()
	{
		return runVel;
	}
	
	public void setJumpTimeout(int timeout)
	{
		jumpTimeout = timeout;
	}
	
	public int getJumpTimeout()
	{
		return jumpTimeout;
	}
	
	public void setJumpUpVelocity(float velocity)
	{
		jumpUpVel = velocity;
	}
	
	public float getJumpUpVelocity()
	{
		return jumpUpVel;
	}
	
	public boolean canJump()
	{
		return canJump;
	}
	
	public void setCanJump(boolean value)
	{
		canJump = value;
	}
	
	public boolean canRun()
	{
		return canRun;
	}
	
	public void setCanRun(boolean value)
	{
		canRun = value;
	}
	
	public void jumpUp()
	{
		jumpUp(true);
	}
	
	public void jumpUp(boolean verifyJump)
	{
		if(verifyJump && !canMakeJump()) return;
		
		physicsCtrl.setVelocity(0.0f, jumpUpVel, false, true);
		setTimeout();
	}
	
	public void jumpForward(ZootDirection direction)
	{
		jumpForward(direction, true);
	}
	
	public void jumpForward(ZootDirection direction, boolean verifyJump)
	{
		if(verifyJump && !canMakeJump()) return;
		
		physicsCtrl.setVelocity(jumpForwardVelX * direction.getHorizontalValue(), jumpForwardVelY, true, true);
		setTimeout();
	}
	
	public void setForwardJumpVelocity(float velX, float velY)
	{
		this.jumpForwardVelX = velX;
		this.jumpForwardVelY = velY;
	}
	
	public void moveInAir(ZootDirection direction)
	{
		float currentVel = physicsCtrl.getVelocity().x;		 
		float newVel = Math.max(Math.abs(currentVel), inAirVelX);
		physicsCtrl.setVelocity(newVel * direction.getHorizontalValue(), 0.0f, true, false);
	}
	
	public void walk(ZootDirection direction)
	{		
		physicsCtrl.setVelocity(walkVel * direction.getHorizontalValue(), 0.0f, true, false);
	}

	public void run(ZootDirection direction)
	{
		if(canRun())
		{
			physicsCtrl.setVelocity(runVel * direction.getHorizontalValue(), 0.0f, true, false);	
		}
	}
	
	public void stop()
	{
		physicsCtrl.setVelocity(0.0f, 0.0f, true, false);
	}
	
	protected boolean canMakeJump()
	{
		return timeout == 0 && canJump() && groundCtrl.isOnGround();
	}
	
	protected void setTimeout()
	{
		timeout = jumpTimeout;
	}
}
