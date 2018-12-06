package com.zootcat.controllers.logic;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.zootcat.actions.ZootActions;
import com.zootcat.controllers.physics.OnCollideWithSensorController;
import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.events.ZootEvents;

/**
 * Sends attack event on collision.
 * 
 * @author Cream
 *
 */
public class AttackOnCollideController extends OnCollideWithSensorController
{
	@Override
	protected SensorCollisionResult onCollideWithSensor(Fixture fixture)
	{
		ZootEvent attackEvent = ZootEvents.get(ZootEventType.Attack, fixture.getUserData());
		getControllerActor().addAction(ZootActions.fireEvent(getControllerActor(), attackEvent));
		return SensorCollisionResult.StopProcessing;
	}
}
