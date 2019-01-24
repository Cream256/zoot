package com.zootcat.controllers.logic;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.controllers.physics.OnCollideController;
import com.zootcat.controllers.physics.OnCollideWithSensorController;
import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.events.ZootEvents;
import com.zootcat.scene.ZootActor;

/**
 * HurtOnCollide controller - a sensor is created for the actor. When other
 * actor collide with it, Hurt {@link ZootEvent} is sent to lower the health
 * of an collided actor.
 * 
 * It always collides per actor. It never collides with sensors.
 * 
 * @ctrlParam damage - amout of damage dealt to the collided actor, default 1
 * 
 * @author Cream
 * @see OnCollideController
 */
public class HurtOnCollideSensorController extends OnCollideWithSensorController
{
	@CtrlParam protected int damage = 1;
	@CtrlParam protected boolean hurtOwner = false;
	@CtrlParam protected boolean useAttackerDamage = false;
					
	@Override
	public final void onEnterCollision(Fixture fixture)
	{
		if(canHurt(fixture))
		{
			ZootActor actorToHurt = hurtOwner ? getControllerActor() : (ZootActor) fixture.getUserData();
			ZootActor attacker = hurtOwner ? (ZootActor) fixture.getUserData() : getControllerActor();
			hurt(actorToHurt, attacker);
		}
	}
	
	public boolean canHurt(Fixture otherFixture)
	{
		return true;
	}
	
	public void hurt(ZootActor actorToHurt, ZootActor attacker)
	{				
		ZootEvents.fireAndFree(actorToHurt, ZootEventType.Hurt, useAttackerDamage ? calculateAttackerDamage(attacker) : damage);
	}
		
	private int calculateAttackerDamage(ZootActor attacker)
	{
		return attacker.getSingleController(DamageController.class).getValue();
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
	
	public void setUseAttackerDamage(boolean value)
	{
		useAttackerDamage = value;
	}
	
	public boolean useAttackerDamage()
	{
		return useAttackerDamage;
	}
}