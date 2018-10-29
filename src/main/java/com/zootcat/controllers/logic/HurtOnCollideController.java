package com.zootcat.controllers.logic;

import com.badlogic.gdx.physics.box2d.Contact;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.controllers.physics.OnCollideController;
import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.events.ZootEvents;
import com.zootcat.scene.ZootActor;

/**
 * HurtOnCollide controller - sends Hurt {@link ZootEvent} to lower the health
 * of an actor. Also applies knockback, if set.
 * 
 * @ctrlParam damage - amout of damage dealt to the collided actor, default 1
 * 
 * @author Cream
 * @see OnCollideController
 */
public class HurtOnCollideController extends OnCollideController
{
	@CtrlParam(debug = true) private int damage = 1;
	@CtrlParam(debug = true) private boolean hurtOwner = false;
	@CtrlParam(debug = true) private float knockbackX = 0.0f;
	@CtrlParam(debug = true) private float knockbackY = 0.0f;
	
	@Override
	public void onEnter(ZootActor actorA, ZootActor actorB, Contact contact)
	{		
		ZootActor actorToHurt = hurtOwner ? getControllerActor() : getOtherActor(actorA, actorB);
		hurt(actorToHurt);
		applyKnockback(actorToHurt);
	}

	@Override
	public void onLeave(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		//noop
	}
	
	public void hurt(ZootActor actorToHurt)
	{		
		ZootEvents.fireAndFree(actorToHurt, ZootEventType.Hurt, damage);
	}
	
	public void setDamage(int value)
	{
		damage = value;
	}
	
	public void applyKnockback(ZootActor actorToHurt)
	{
		actorToHurt.controllerAction(PhysicsBodyController.class, ctrl -> ctrl.applyImpulse(knockbackX, knockbackY));
	}
	
	public int getDamage()
	{
		return damage;
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
	
	public void setHurtOwner(boolean value)
	{
		hurtOwner = value;
	}
	
	public boolean getHurtOwner()
	{
		return hurtOwner;
	}
}