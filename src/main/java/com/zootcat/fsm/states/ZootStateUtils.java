package com.zootcat.fsm.states;

import com.zootcat.events.ZootEvent;
import com.zootcat.scene.ZootDirection;

public class ZootStateUtils
{
	public static boolean isMoveEvent(ZootEvent event)
	{
		switch(event.getType())
		{
		case WalkRight:
		case WalkLeft:
		case RunRight:
		case RunLeft:
			return true;
		
		default:
			return false;
		}
	}
	
	public static boolean isRunEvent(ZootEvent event)
	{
		switch(event.getType())
		{
		case RunRight:
		case RunLeft:
			return true;
		
		default:
			return false;
		}	
	}
	
	public static ZootDirection getDirectionFromEvent(ZootEvent event)
	{
		switch(event.getType())
		{
		case RunRight:
		case WalkRight:		
			return ZootDirection.Right;
			
		case RunLeft:
		case WalkLeft:
			return ZootDirection.Left;
				
		default:
			return ZootDirection.None;
		}
	}
}
