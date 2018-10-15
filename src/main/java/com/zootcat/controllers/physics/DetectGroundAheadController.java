package com.zootcat.controllers.physics;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.zootcat.controllers.logic.DirectionController;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.events.ZootEvents;
import com.zootcat.math.ZootBoundingBoxFactory;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;

public class DetectGroundAheadController extends DetectGroundController
{
	private BoundingBox boundingBox;
	private ZootDirection direction = ZootDirection.None;
	
	@Override
	public void init(ZootActor actor)
	{
		super.init(actor);
		boundingBox = new BoundingBox();
	}
	
	@Override
	public void onAdd(ZootActor actor)
	{
		super.onAdd(actor);
		setSensorPositionToFaceActorDirection(actor);
	}
	
	@Override
	public void preUpdate(float delta, ZootActor actor)
	{
		super.preUpdate(delta, actor);
		setSensorPositionToFaceActorDirection(actor);
	}
	
	@Override
	public void postUpdate(float delta, ZootActor actor)
	{
		if(!isOnGround())
		{
			ZootEvents.fireAndFree(actor, ZootEventType.NoGroundAhead);
		}	
	}

	private void setSensorPositionToFaceActorDirection(ZootActor actor)
	{
		ZootDirection oldDirection = direction;		
		actor.controllerAction(DirectionController.class, ctrl -> direction = ctrl.getDirection());
		
		if(direction != oldDirection)
		{
			float actorHalfWidth = actor.getWidth() * 0.5f;
			
			ZootBoundingBoxFactory.createAtRef(super.getSensor(), boundingBox);			
			float sensorHalfWidth = boundingBox.getWidth() * 0.5f;
			
			float newX = actorHalfWidth * direction.getHorizontalValue() + sensorHalfWidth * direction.getHorizontalValue();
			setSensorPosition(newX, super.getSensorPosition().y);			
		}		
	}
}
