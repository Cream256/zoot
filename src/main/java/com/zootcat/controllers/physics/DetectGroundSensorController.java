package com.zootcat.controllers.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.zootcat.controllers.factory.CtrlDebug;
import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.events.ZootEvents;
import com.zootcat.scene.ZootActor;

/**
 * DetectGround controller - Creates a feet sensor, that is detecting if
 * the actor is in contact with the ground. When he is, the controller 
 * emits Ground {@link ZootEvent}.
 * 
 * @author Cream
 *
 */
public class DetectGroundSensorController extends OnCollideWithSensorController	 
{		
	@CtrlDebug private boolean isOnGround = false;
			
	@Override
	public void onAdd(ZootActor actor)
	{
		setSensorPosition(actor);
		super.setCollideWithSensors(true);
		super.onAdd(actor);
	}
	
	private void setSensorPosition(ZootActor actor)
	{
		Vector2 sensorPosition = new Vector2(0.0f, -actor.getHeight() / super.getScene().getUnitScale() * 0.5f);
		super.sensorX = sensorPosition.x;
		super.sensorY = sensorPosition.y;						
	}
	
	@Override
	public void preUpdate(float delta, ZootActor actor)
	{
		isOnGround = false;
	}

	@Override
	public void postUpdate(float delta, ZootActor actor)
	{
		if(isOnGround) 
		{
			ZootEvents.fireAndFree(actor, ZootEventType.Ground);
		}
	}
	
	@Override
	public SensorCollisionResult onCollision(Fixture fixture)
	{
		if(fixture.isSensor())
		{
			return SensorCollisionResult.ProcessNext;
		}
		
		isOnGround = true;
		return SensorCollisionResult.StopProcessing;
	}

	public boolean isOnGround()
	{
		return isOnGround;
	}
}
