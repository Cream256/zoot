package com.zootcat.controllers.logic;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.zootcat.controllers.physics.OnCollideWithSensorController;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.events.ZootEvents;
import com.zootcat.scene.ZootActor;

public abstract class CollectOnCollideSensorController extends OnCollideWithSensorController
{
	private boolean collected = false;
	
	@Override
	public void onEnterCollision(Fixture fixture)
	{
		if(!collected && onCollect(getControllerActor(), (ZootActor)fixture.getUserData()))
		{
			removeCollectible(getControllerActor());
		}
	}
	
	@Override
	public SensorCollisionResult onCollision(Fixture fixture)
	{
		if(!collected && onCollect(getControllerActor(), (ZootActor)fixture.getUserData()))
		{
			removeCollectible(getControllerActor());
			return SensorCollisionResult.StopProcessing;
		}		
		return collected ? SensorCollisionResult.StopProcessing : SensorCollisionResult.ProcessNext;
	}
	
	private void removeCollectible(ZootActor collectible)
	{
		ZootEvents.fireAndFree(collectible, ZootEventType.Dead);
		collectible.addAction(Actions.removeActor());
		collected = true;
	}
	
	/**
	 * Method that is called when collision occurs and the collectible is trying to
	 * be collected by the collector.
	 * @param collectible - collectible actor (like life, mana, any other bonus)
	 * @param collector - collector actor (like player, npc)
	 * @return true if collection was successful, false otherwise
	 */
	public abstract boolean onCollect(ZootActor collectible, ZootActor collector);	
}
