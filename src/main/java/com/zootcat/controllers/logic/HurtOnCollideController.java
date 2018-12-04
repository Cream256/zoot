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
 * @ctrlParam damage - amout of damage dealt to the collided actor, default 1
 * 
 * @author Cream
 * @see OnCollideController
 */
public class HurtOnCollideController extends OnCollideWithSensorController
{
	@CtrlParam private int damage = 1;
	@CtrlParam private boolean hurtOwner = false;
	
	@Override
	protected SensorCollisionResult onCollideWithSensor(Fixture fixture)
	{
		ZootActor actorToHurt = hurtOwner ? getControllerActor() : (ZootActor) fixture.getUserData();
		hurt(actorToHurt);
		return SensorCollisionResult.StopProcessing;
	}
	
	public void hurt(ZootActor actorToHurt)
	{		
		ZootEvents.fireAndFree(actorToHurt, ZootEventType.Hurt, damage);
	}
	
	@Override
	public void preUpdate(float delta, ZootActor actor)
	{
		//noop
	}

	@Override
	public void postUpdate(float delta, ZootActor actor)
	{
		//noop
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