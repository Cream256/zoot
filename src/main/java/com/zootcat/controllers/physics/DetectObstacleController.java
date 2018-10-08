package com.zootcat.controllers.physics;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.zootcat.controllers.logic.DirectionController;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.events.ZootEvents;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;

public class DetectObstacleController extends OnCollideWithSensorController
{
	private ZootDirection direction = ZootDirection.Right;
	
	@Override
	public void onAdd(ZootActor actor)
	{
		super.onAdd(actor);
		updateSensor(actor);
	}
	
	@Override
	public void onUpdate(float delta, ZootActor actor)
	{
		super.onUpdate(delta, actor);
		updateSensor(actor);		
	}
	
	@Override
	protected SensorCollisionResult onCollideWithSensor(Fixture fixture)
	{
		ZootEvents.fireAndFree(getControllerActor(), ZootEventType.Obstacle);		
		return SensorCollisionResult.StopProcessing;
	}
	
	protected void updateSensor(ZootActor actor)
	{
		actor.controllerAction(DirectionController.class, ctrl -> direction = ctrl.getDirection());
		
		float halfWidth = actor.getWidth() * 0.5f;
		float x = halfWidth * direction.getHorizontalValue();		
		super.setSensorPosition(x, 0.0f);		
	}
}
