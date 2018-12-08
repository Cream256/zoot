package com.zootcat.controllers.physics;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.zootcat.actions.ZootActions;
import com.zootcat.actions.ZootKnockbackAction;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.scene.ZootActor;

/**
 * KnockbackOnTouch controller - sets knockback force when collision happens
 * between two fixtures. It is best to set collidePerActor = true and collideWithSensors = false
 * for collision between player and enemy characters.
 * 
 * @ctrlParam knockbackX - knockback force in X axis, default 1.0f
 * @ctrlParam knockbackY - knockback force in Y axis, default 1.0f
 * @ctrlParam varyHorizontal - true if knockbackX signum should be calculated based on collision position, default false
 * 
 * @author Cream
 * @see OnCollideController
 */
public class KnockbackOnTouchController extends OnCollideWithSensorController
{
	@CtrlParam private float knockbackX = 1.0f;
	@CtrlParam private float knockbackY = 1.0f;
	@CtrlParam private boolean varyHorizontal = false;
			
	@Override
	public void onEnterCollision(Fixture otherFixture)
	{
		ZootActor target = (ZootActor) otherFixture.getUserData();
		ZootActor owner = getControllerActor();		
		
		ZootKnockbackAction knockback = ZootActions.knockback(knockbackX, knockbackY, varyHorizontal, target, owner);
		target.addAction(knockback);
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
