package com.zootcat.actions;

import com.badlogic.gdx.math.Vector2;
import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.scene.ZootActor;

public class ZootKnockbackAction extends ZootAction
{
	private float knockbackX = 0.0f;
	private float knockbackY = 0.0f;
	private boolean varyHorizontal = false;
	private ZootActor attackActor = null;
	private ZootActor knockbackActor = null;
	
	@Override
	public boolean act(float delta)
	{				
		float kx = varyHorizontal ? calculateHorizontalKnockback(attackActor, knockbackActor) : knockbackX;		
		knockbackActor.controllersAction(PhysicsBodyController.class, ctrl -> ctrl.setVelocity(kx, knockbackY, kx != 0.0f, knockbackY != 0.0f));
		
		return true;
	}
	
	private float calculateHorizontalKnockback(ZootActor actionActor, ZootActor knockbackActor)
	{		
		PhysicsBodyController actionActorPhysBodyCtrl = actionActor.tryGetSingleController(PhysicsBodyController.class);
		PhysicsBodyController knockbackActorPhysBodyCtrl = knockbackActor.tryGetSingleController(PhysicsBodyController.class);
		if(actionActorPhysBodyCtrl == null || knockbackActorPhysBodyCtrl == null)
		{
			return 0.0f;
		}
				
		Vector2 ctrlActorPos = actionActorPhysBodyCtrl.getCenterPositionRef();
		Vector2 otherActorPos = knockbackActorPhysBodyCtrl.getCenterPositionRef();		
		return ctrlActorPos.x <= otherActorPos.x ? knockbackX : -knockbackX;
	}

	@Override
	public void reset()
	{
		knockbackX = 0.0f;
		knockbackY = 0.0f;
		varyHorizontal = false;
		knockbackActor = null;
		attackActor = null;
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
	
	public void setAttackActor(ZootActor actor)
	{
		attackActor = actor;
	}
	
	public ZootActor getAttackActor()
	{
		return attackActor;
	}
	
	public void setKnockbackActor(ZootActor actor)
	{
		knockbackActor = actor;
	}
	
	public ZootActor getKnockbackActor()
	{
		return knockbackActor;
	}
}
