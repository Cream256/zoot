package com.zootcat.controllers.logic;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.events.ZootEvent;
import com.zootcat.scene.ZootActor;

public abstract class ZootEventListenerController extends ControllerAdapter implements EventListener
{
	@Override
	public void onAdd(ZootActor actor) 
	{
		actor.addListener(this);
	}

	@Override
	public void onRemove(ZootActor actor) 
	{
		actor.removeListener(this);
	}
	
	@Override
	public boolean handle(Event event)
	{
		if(event == null || !ClassReflection.isInstance(ZootEvent.class, event))
		{
			return false;
		}
		
		return handleZootEvent((ZootEvent)event);
	}
	
	public abstract boolean handleZootEvent(ZootEvent event);
}
