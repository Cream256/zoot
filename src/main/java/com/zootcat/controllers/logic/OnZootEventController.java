package com.zootcat.controllers.logic;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.zootcat.events.ZootEvent;
import com.zootcat.events.ZootEventType;
import com.zootcat.scene.ZootActor;

public class OnZootEventController extends ZootEventListenerController
{
	private boolean done = false;
	private boolean singleExecution = false;
	private Set<ZootEventType> allowedEventTypes = new HashSet<ZootEventType>();
	
	public OnZootEventController()
	{
		this(Arrays.asList(ZootEventType.values()), false);
	}
	
	public OnZootEventController(boolean singleExecution)
	{
		this(Arrays.asList(ZootEventType.values()), singleExecution);
	}
	
	public OnZootEventController(Collection<ZootEventType> types, boolean singleExecution)
	{		
		allowedEventTypes.addAll(types);
		this.singleExecution = singleExecution;
	}
			
	public boolean isSingleExecution()
	{
		return singleExecution;
	}
	
	public void setSingleExecution(boolean value)
	{
		singleExecution = value;
	}
	
	public boolean isDone()
	{
		return done;
	}
		
	public Set<ZootEventType> getEventTypes()
	{
		return allowedEventTypes;
	}
	
	@Override
	public boolean handleZootEvent(ZootEvent event)
	{	
		if(done || !allowedEventTypes.contains(event.getType()))
		{
			return false;
		}
		
		done = singleExecution;
		return onZootEvent(event.getTargetZootActor(), event);
	}
	
	public boolean onZootEvent(ZootActor actor, ZootEvent event)
	{
		return true;
	}
}