package com.zootcat.events;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.zootcat.scene.ZootActor;

public class ZootEvent extends Event
{
	private Object userObject;
	private ZootEventType type;
		
	public ZootEvent()
	{
		this(ZootEventType.None);
	}
	
	public ZootEvent(ZootEventType type)
	{
		this.type = type;
		this.userObject = null;
	}
		
	public ZootEvent(ZootEvent event)
	{
		this.type = event.type;
		this.userObject = event.userObject;		
	}
		
	public ZootEventType getType()
	{
		return type;
	}
	
	public void setType(ZootEventType type)
	{
		this.type = type;
	}
	
	public ZootActor getTargetZootActor()
	{
		Actor target = getTarget();
		if(ClassReflection.isInstance(ZootActor.class, target))
		{
			return (ZootActor)target;
		}
		return null;
	}
	
	public void setUserObject(Object obj)
	{
		userObject = obj;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getUserObject(Class<T> clazz)
	{
		if(ClassReflection.isInstance(clazz, userObject))
		{		
			return (T)userObject;
		}
		return null;
	}
	
	public <T> T getUserObject(Class<T> clazz, T defaultValue)
	{
		T result = getUserObject(clazz);
		return result != null ? result : defaultValue;
	}
	
	@Override
	public void reset() 
	{
		super.reset();
		userObject = null;
		type = ZootEventType.None;		
	}
	
	@Override
	public String toString() 
	{
		return type.toString();
	}
}
