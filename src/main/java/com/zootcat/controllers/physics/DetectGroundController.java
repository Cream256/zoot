package com.zootcat.controllers.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.zootcat.controllers.factory.CtrlDebug;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.events.ZootEvents;
import com.zootcat.scene.ZootActor;

/**
 * DetectGround controller - Creates a feet sensor, that is detecting if
 * the actor is in contact with the ground. When he is, the controller 
 * emits Ground {@link ZootEvent}.
 * 
 * @ctrlParam useActorSize - If set, actor width and 10% of actor height will be used
 * to set sensor size. Otherwise sensorWidth and sensorHeight will be used
 * 
 * @ctrlParam useActorFilter - If set, actor collision filter will be used. Otherwise
 * category and mask parameters must be supplied.
 * 
 * @author Cream
 *
 */
public class DetectGroundController extends OnCollideWithSensorController	 
{
	public static final float SENSOR_HEIGHT_PERCENT = 0.2f;
		
	@CtrlParam private boolean useActorSize = true;
	@CtrlParam private boolean useActorFilter = true;
	@CtrlDebug private boolean isOnGround = false;
			
	@Override
	public void onAdd(ZootActor actor)
	{
		setSensorSizeAndPosition(actor);
		super.setCollideWithSensors(true);
		super.onAdd(actor);
		setSensorFilter(actor);
	}
	
	private void setSensorSizeAndPosition(ZootActor actor)
	{
		Vector2 sensorPosition = new Vector2(0.0f, -actor.getHeight() / super.getScene().getUnitScale() * 0.5f);		
		super.sensorHeight = calculateSensorHeight(actor);
		super.sensorWidth = calculateSensorWidth(actor);
		super.sensorX = sensorPosition.x;
		super.sensorY = sensorPosition.y;						
	}
	
	private float calculateSensorWidth(ZootActor actor)
	{
		return useActorSize ? actor.getWidth() / super.getScene().getUnitScale() : sensorWidth;
	}
	
	private float calculateSensorHeight(ZootActor actor)
	{
		return useActorSize ? (actor.getHeight() / super.getScene().getUnitScale()) * SENSOR_HEIGHT_PERCENT : sensorHeight;		
	}

	@Override
	public void onUpdate(float delta, ZootActor actor)
	{		
		isOnGround = false;		
		super.onUpdate(delta, actor);
		
		if(isOnGround) 
		{
			ZootEvents.fireAndFree(actor, ZootEventType.Ground);
		}
	}
	
	@Override
	protected SensorCollisionResult onCollideWithSensor(Fixture fixture)
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
		
	
	private void setSensorFilter(ZootActor actor)
	{		
		if(!useActorFilter) return;
		
		CollisionFilterController filterCtrl = actor.tryGetController(CollisionFilterController.class);
		if(filterCtrl != null)
		{
			setFilter(filterCtrl.getCollisionFilter());			
		}
	}
}
