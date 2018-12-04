package com.zootcat.controllers.logic;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.zootcat.actions.ZootActions;
import com.zootcat.controllers.physics.OnCollideWithSensorController;
import com.zootcat.scene.ZootActor;

//TODO remake tests
public class DieOnCollideController extends OnCollideWithSensorController
{	
	@Override
	protected SensorCollisionResult onCollideWithSensor(Fixture fixture)
	{
		die();
		return SensorCollisionResult.StopProcessing;
	}
	
	protected void die()
	{
		getControllerActor().addAction(ZootActions.killActor(getControllerActor()));
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
}