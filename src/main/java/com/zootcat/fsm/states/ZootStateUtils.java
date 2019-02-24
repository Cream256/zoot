package com.zootcat.fsm.states;

import java.util.Arrays;
import java.util.List;

import com.zootcat.controllers.logic.LifeController;
import com.zootcat.controllers.physics.WalkableController;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.events.ZootEventTypeEnum;
import com.zootcat.scene.ZootActor;
import com.zootcat.utils.ZootDirection;

public class ZootStateUtils
{
	private static final List<ZootEventType> MOVE_EVENTS = Arrays.asList(ZootEventType.WalkRight, 
			ZootEventType.WalkLeft, ZootEventType.RunLeft, ZootEventType.RunRight);

	private static final List<ZootEventType> RUN_EVENTS = Arrays.asList(ZootEventType.RunRight, ZootEventType.RunLeft);
	private static final List<ZootEventType> WALK_EVENTS = Arrays.asList(ZootEventType.WalkRight, ZootEventType.WalkLeft);	
	private static final List<ZootEventType> JUMP_EVENTS = Arrays.asList(ZootEventType.JumpUp, ZootEventType.JumpForward);
	private static final List<ZootEventType> FLY_EVENTS = Arrays.asList(ZootEventType.FlyLeft, ZootEventType.FlyRight);
	
	public static boolean isMoveEvent(ZootEvent event)
	{
		return MOVE_EVENTS.contains(event.getType());
	}
		
	public static boolean isWalkEvent(ZootEvent event)
	{
		return WALK_EVENTS.contains(event.getType());
	}
	
	public static boolean isJumpEvent(ZootEvent event)
	{
		return JUMP_EVENTS.contains(event.getType());
	}
	
	public static boolean isRunEvent(ZootEvent event)
	{
		return RUN_EVENTS.contains(event.getType());
	}
	
	public static boolean isFlyEvent(ZootEvent event)
	{
		return FLY_EVENTS.contains(event.getType());
	}
	
	public static boolean canActorRun(ZootEvent event)
	{
		WalkableController moveCtrl = getWalkableController(event.getTargetZootActor());
		return moveCtrl != null ? moveCtrl.canRun() : true;
	}
	
	public static boolean canActorJump(ZootEvent event)
	{
		WalkableController moveCtrl = getWalkableController(event.getTargetZootActor());
		return moveCtrl != null ? moveCtrl.canJump() : true;
	}
	
	public static boolean canHurtActor(ZootEvent event)
	{
		return canHurtActor(event.getTargetZootActor());
	}
	
	public static boolean canHurtActor(ZootActor actor)
	{
		LifeController lifeCtrl = actor.tryGetSingleController(LifeController.class);
		return lifeCtrl != null ? !lifeCtrl.isFrozen() : true;
	}
	
	private static WalkableController getWalkableController(ZootActor actor)
	{
		try
		{
			return actor != null ? actor.getSingleController(WalkableController.class) : null;	
		}
		catch(RuntimeZootException e)
		{
			return null;
		}		
	}
	
	public static ZootDirection getDirectionFromEvent(ZootEvent event)
	{
		ZootEventTypeEnum eventType = event.getType();
		if(eventType == ZootEventType.RunRight || eventType == ZootEventType.WalkRight || eventType == ZootEventType.FlyRight)
		{
			return ZootDirection.Right; 
		}
		
		if(eventType == ZootEventType.RunLeft || eventType == ZootEventType.WalkLeft || eventType == ZootEventType.FlyLeft)
		{
			return ZootDirection.Left;
		}

		return ZootDirection.None;
	}
}
