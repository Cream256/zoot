package com.zootcat.controllers.physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.scene.ZootActor;

public class KnockbackOnTouchController extends OnCollideController
{
	@CtrlParam(debug = true) private float knockbackX = 1.0f;
	@CtrlParam(debug = true) private float knockbackY = 1.0f;
		
	@Override
	public void onEnter(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		ZootActor actorToKnockback = getOtherActor(actorA, actorB);
		actorToKnockback.controllerAction(PhysicsBodyController.class, ctrl -> ctrl.applyImpulse(knockbackX, knockbackY));		
	}

	@Override
	public void onLeave(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		//noop
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
}
