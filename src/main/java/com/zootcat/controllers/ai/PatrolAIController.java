package com.zootcat.controllers.ai;

import com.badlogic.gdx.math.Vector2;
import com.zootcat.controllers.factory.CtrlDebug;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.controllers.logic.ZootEventListenerController;
import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.events.ZootEventTypeEnum;
import com.zootcat.fsm.events.ZootEvents;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;
import com.zootcat.scene.ZootScene;

public class PatrolAIController extends ZootEventListenerController
{	
	public static final float TURN_COOLDOWN = 0.5f;
	
	@CtrlParam private int distance = 0;
	@CtrlParam private String startDirection = "Left";
	@CtrlParam private boolean isFlying = false;
	@CtrlParam(global = true) private ZootScene scene;
	@CtrlDebug private float turnCooldown = 0.0f;
	
	private Vector2 start;
	private ZootDirection direction = ZootDirection.Left;
	private PhysicsBodyController physicsCtrl;
			
	@Override
	public void onAdd(ZootActor actor) 
	{
		super.onAdd(actor);
		physicsCtrl = actor.getController(PhysicsBodyController.class);
		start = physicsCtrl.getCenterPositionRef().cpy();
		direction = ZootDirection.fromString(startDirection);
	}

	@Override
	public void onRemove(ZootActor actor)
	{
		super.onRemove(actor);
		start = null;
		scene = null;
		physicsCtrl = null;		
	}
	
	public ZootDirection getCurrentDirection()
	{
		return direction;
	}

	@Override
	public void onUpdate(float delta, ZootActor actor)
	{		
		float currentX = physicsCtrl.getCenterPositionRef().x;
		float realDist = distance * scene.getUnitScale();
		if(direction == ZootDirection.Left)
		{
			if(currentX < start.x - realDist) direction = ZootDirection.Right;
		}
		else
		{
			if(currentX > start.x + realDist) direction = ZootDirection.Left;			
		}		
		ZootEvents.fireAndFree(actor, getEvent(direction));		
		turnCooldown = Math.max(0.0f, turnCooldown - delta);
	}
	
	private ZootEventTypeEnum getEvent(ZootDirection direction)
	{
		switch(direction)
		{
		case Right:
			return isFlying ? ZootEventType.FlyRight : ZootEventType.WalkRight;
			
		default:
			return isFlying ? ZootEventType.FlyLeft : ZootEventType.WalkLeft;
		}
	}

	@Override
	public boolean handleZootEvent(ZootEvent event)
	{
		boolean shouldTurn = event.getType() == ZootEventType.Obstacle || event.getType() == ZootEventType.NoGroundAhead;
		if(shouldTurn && turnCooldown == 0.0f)
		{
			turnCooldown = TURN_COOLDOWN;
			direction = direction.invert();
			return true;
		}
		
		return false;
	}	
}
