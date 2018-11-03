package com.zootcat.controllers.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.scene.ZootActor;

/**
 * KnockbackOnTouch controller - sets knockback force when collision happens
 * between two fixtures. It is best to set collidePerActor = true and collideWithSensors = false
 * for collision between player and enemy characters.
 * 
 * @ctrlParam knockbackX - knockback force in X axis
 * @ctrlParam knockbackY - knockback force in Y axis
 * @ctrlParam varyHorizontal - true if knockbackX signum should be calculated based on collision position
 * 
 * @author Cream
 * @see OnCollideController
 */
public class KnockbackOnTouchController extends OnCollideController
{
	@CtrlParam private float knockbackX = 1.0f;
	@CtrlParam private float knockbackY = 1.0f;
	@CtrlParam private boolean varyHorizontal = false;
		
	@Override
	public void onEnter(ZootActor actorA, ZootActor actorB, Contact contact)
	{				
		ZootActor actorToKnockback = getOtherActor(actorA, actorB);		
		float kx = varyHorizontal ? calculateHorizontalKnockback(actorA, actorB) : knockbackX;		
		actorToKnockback.controllerAction(PhysicsBodyController.class, ctrl -> ctrl.setVelocity(kx, knockbackY, kx != 0.0f, knockbackY != 0.0f));		
	}

	private float calculateHorizontalKnockback(ZootActor actorA, ZootActor actorB)
	{
		ZootActor ctrlActor = getControllerActor();
		ZootActor otherActor = getOtherActor(actorA, actorB);
		
		Vector2 ctrlActorPos = ctrlActor.getController(PhysicsBodyController.class).getCenterPositionRef();
		Vector2 otherActorPos = otherActor.getController(PhysicsBodyController.class).getCenterPositionRef();
		
		return ctrlActorPos.x <= otherActorPos.x ? knockbackX : -knockbackX;
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
	
	public void setVaryHorizontal(boolean value)
	{
		varyHorizontal = value;
	}
}
