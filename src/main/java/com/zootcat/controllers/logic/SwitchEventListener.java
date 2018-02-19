package com.zootcat.controllers.logic;

import com.zootcat.events.ZootEvent;
import com.zootcat.scene.ZootActor;

//TODO add test
//TODO add doc
public abstract class SwitchEventListener extends ZootEventListenerController
{
	@Override
	public boolean handleZootEvent(ZootEvent event)
	{
		switch(event.getType())
		{
		case SwitchOn:
			turnOn(event.getUserObject(ZootActor.class));
			return true;
			
		case SwitchOff:
			turnOff(event.getUserObject(ZootActor.class));
			return true;
		
		default:
			return false;		
		}
	}

	public abstract void turnOn(ZootActor switchActor);
	public abstract void turnOff(ZootActor switchActor);
}