package com.zootcat.fsm.states;

import java.util.Arrays;
import java.util.List;

import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.events.ZootEventTypeEnum;
import com.zootcat.scene.ZootDirection;

public class ZootStateUtils
{
	private static final List<ZootEventType> MOVE_EVENTS = Arrays.asList(ZootEventType.WalkRight, 
			ZootEventType.WalkLeft, ZootEventType.RunLeft, ZootEventType.RunRight);

	private static final List<ZootEventType> RUN_EVENTS = Arrays.asList(ZootEventType.RunRight, ZootEventType.RunLeft);
	
	private static final List<ZootEventType> JUMP_EVENTS = Arrays.asList(ZootEventType.JumpUp, ZootEventType.JumpForward);
	
	public static boolean isMoveEvent(ZootEvent event)
	{
		return MOVE_EVENTS.contains(event.getType());
	}
	
	public static boolean isJumpEvent(ZootEvent event)
	{
		return JUMP_EVENTS.contains(event.getType());
	}
	
	public static boolean isRunEvent(ZootEvent event)
	{
		return RUN_EVENTS.contains(event.getType());
	}
	
	public static ZootDirection getDirectionFromEvent(ZootEvent event)
	{
		ZootEventTypeEnum eventType = event.getType();
		if(eventType == ZootEventType.RunRight || eventType == ZootEventType.WalkRight)
		{
			return ZootDirection.Right; 
		}
		
		if(eventType == ZootEventType.RunLeft || eventType == ZootEventType.WalkLeft)
		{
			return ZootDirection.Left;
		}

		return ZootDirection.None;
	}
}
