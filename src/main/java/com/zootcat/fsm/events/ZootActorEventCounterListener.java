package com.zootcat.fsm.events;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.utils.reflect.ClassReflection;

public class ZootActorEventCounterListener implements EventListener
{
	private int count = 0;
	private Event lastEvent = null;
	
	@Override
	public boolean handle(Event event)
	{
		++count;
		
		boolean zootEvent = ClassReflection.isInstance(ZootEvent.class, event);
		lastEvent = zootEvent ? new ZootEvent((ZootEvent)event) : event;
		
		return true;
	}
	
	public int getCount()
	{
		return count;
	}
	
	public Event getLastEvent()
	{
		return lastEvent;
	}
	
	public ZootEvent getLastZootEvent()
	{
		if(ClassReflection.isInstance(ZootEvent.class, lastEvent))
		{
			return (ZootEvent) lastEvent;
		}
		return null;
	}
}
