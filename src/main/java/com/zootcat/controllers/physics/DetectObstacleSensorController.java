package com.zootcat.controllers.physics;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.zootcat.controllers.logic.DirectionController;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.events.ZootEvents;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;

public class DetectObstacleSensorController extends OnCollideWithSensorController
{
	private ZootDirection direction = ZootDirection.None;
		
	@Override
	public void onAdd(ZootActor actor)
	{
		super.onAdd(actor);
		updateSensor(actor);
	}
		
	@Override
	public void postUpdate(float delta, ZootActor actor)
	{
		updateSensor(actor);	
	}
	
	@Override
	public SensorCollisionResult onCollision(Fixture fixture)
	{
		ZootEvents.fireAndFree(getControllerActor(), ZootEventType.Obstacle);		
		return SensorCollisionResult.StopProcessing;
	}
	
	protected void updateSensor(ZootActor actor)
	{
		ZootDirection oldDirection = direction;		
		actor.controllersAction(DirectionController.class, ctrl -> direction = ctrl.getDirection());
		
		if(oldDirection != direction)
		{
			float newX = actor.getWidth() * 0.5f * direction.getHorizontalValue();
			super.setSensorPosition(newX, 0.0f);
		}		
	}
}
