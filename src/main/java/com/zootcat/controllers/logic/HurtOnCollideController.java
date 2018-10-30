package com.zootcat.controllers.logic;

import com.badlogic.gdx.physics.box2d.Contact;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.controllers.physics.OnCollideController;
import com.zootcat.fsm.events.ZootEvent;
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
	
	@Override
	public void onEnter(ZootActor actorA, ZootActor actorB, Contact contact)
	{		
		ZootActor actorToHurt = hurtOwner ? getControllerActor() : getOtherActor(actorA, actorB);
		hurt(actorToHurt);
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
		
	public int getDamage()
	{
		return damage;
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