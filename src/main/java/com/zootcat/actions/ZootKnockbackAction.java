package com.zootcat.actions;

import com.badlogic.gdx.math.Vector2;
import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.scene.ZootActor;

public class ZootKnockbackAction extends ZootAction
{
	private float knockbackX = 0.0f;
	private float knockbackY = 0.0f;
	private boolean varyHorizontal = false;
	
	@Override
	public boolean act(float delta)
	{		
		ZootActor actionActor = getActionZootActor();
		ZootActor knockbackActor = getTargetZootActor();
		
		float kx = varyHorizontal ? calculateHorizontalKnockback(actionActor, knockbackActor) : knockbackX;		
		knockbackActor.controllerAction(PhysicsBodyController.class, ctrl -> ctrl.setVelocity(kx, knockbackY, kx != 0.0f, knockbackY != 0.0f));
		
		return true;
	}
	
	private float calculateHorizontalKnockback(ZootActor actionActor, ZootActor knockbackActor)
	{		
		Vector2 ctrlActorPos = actionActor.getController(PhysicsBodyController.class).getCenterPositionRef();
		Vector2 otherActorPos = knockbackActor.getController(PhysicsBodyController.class).getCenterPositionRef();		
		return ctrlActorPos.x <= otherActorPos.x ? knockbackX : -knockbackX;
	}

	@Override
	public void reset()
	{
		knockbackX = 0.0f;
		knockbackY = 0.0f;
		varyHorizontal = false;
		super.reset();
	}
	
	public void setKnockback(float knockbackX, float knockbackY)
	{
		this.knockbackX = knockbackX;
		this.knockbackY = knockbackY;
	}
	
	public float getKnockbackX()
	{
		return knockbackX;
	}
	
	public float getKnockbackY()
	{
		return knockbackY;
	}
	
	public void setVaryHorizontal(boolean value)
	{
		varyHorizontal = value;
	}
	
	public boolean getVaryHorizontal()
	{
		return varyHorizontal;
	}
}
