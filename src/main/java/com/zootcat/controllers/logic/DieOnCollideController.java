package com.zootcat.controllers.logic;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.zootcat.actions.ZootActions;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.controllers.physics.OnCollideWithSensorController;
import com.zootcat.scene.ZootActor;

/**
 * DieOnCollide controller - kills actor after collision.
 * @ctrlParam delay - delay after the actor will be killed, default 0.0
 * @author Cream
 *
 */
public class DieOnCollideController extends OnCollideWithSensorController
{	
	@CtrlParam private float delay = 0.0f;
	
	private boolean done = false;
	
	@Override
	protected SensorCollisionResult onCollideWithSensor(Fixture fixture)
	{
		if(!done)
		{
			ZootActor ctrlActor = getControllerActor();		
			ctrlActor.addAction(Actions.delay(delay, ZootActions.killActor(ctrlActor)));
			done = true;
		}
		return SensorCollisionResult.StopProcessing;
	}
}