package com.zootcat.controllers.physics;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.zootcat.controllers.factory.CtrlDebug;
import com.zootcat.controllers.logic.DirectionController;
import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.events.ZootEvents;
import com.zootcat.math.ZootBoundingBoxFactory;
import com.zootcat.scene.ZootActor;
import com.zootcat.utils.ZootDirection;

/**
 * DetectGround controller - Creates a sensor, that is detecting if
 * the actor has ground in front of him. If not, controller emits NoGroundAhead {@link ZootEvent}.
 * 
 * @author Cream
 */
public class DetectGroundAheadSensorController extends OnCollideWithSensorController
{
	public static final float SENSOR_HEIGHT_PERCENT = 0.2f;
	
	@CtrlDebug private boolean isGroundAhead = false;
	@CtrlDebug private ZootDirection direction = ZootDirection.None;
	private BoundingBox boundingBox = new BoundingBox();
	
	@Override
	public void onAdd(ZootActor actor)
	{
		super.setCollideWithSensors(true);
		super.onAdd(actor);
		setSensorPositionToFaceActorDirection(actor);
	}
		
	@Override
	public void preUpdate(float delta, ZootActor actor)
	{
		isGroundAhead = false;
		setSensorPositionToFaceActorDirection(actor);
	}

	@Override
	public void postUpdate(float delta, ZootActor actor)
	{
		if(!isGroundAhead) 
		{
			ZootEvents.fireAndFree(actor, ZootEventType.NoGroundAhead);
		}
	}
	
	@Override
	public SensorCollisionResult onCollision(Fixture fixture)
	{
		if(fixture.isSensor())
		{
			return SensorCollisionResult.ProcessNext;
		}
		
		isGroundAhead = true;
		return SensorCollisionResult.StopProcessing;
	}

	public boolean isGroundAhead()
	{
		return isGroundAhead;
	}	
	
	private void setSensorPositionToFaceActorDirection(ZootActor actor)
	{
		ZootDirection oldDirection = direction;		
		actor.controllersAction(DirectionController.class, ctrl -> direction = ctrl.getDirection());
		
		if(direction != oldDirection)
		{
			float actorHalfWidth = actor.getWidth() * 0.5f;
			float actorHalfHeight = actor.getHeight() * 0.5f;
			
			ZootBoundingBoxFactory.createAtRef(super.getSensor(), boundingBox);			
			float sensorHalfWidth = boundingBox.getWidth() * 0.5f * super.getScene().getUnitScale();
			
			float newX = actorHalfWidth * direction.getHorizontalValue() + sensorHalfWidth * direction.getHorizontalValue();
			float newY = -actorHalfHeight;
			setSensorPosition(newX, newY);			
		}		
	}
}
