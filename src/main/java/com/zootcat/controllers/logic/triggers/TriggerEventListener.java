package com.zootcat.controllers.logic.triggers;

import com.zootcat.controllers.logic.ZootEventListenerController;
import com.zootcat.events.ZootEvent;
import com.zootcat.scene.ZootActor;

/**
 * Zoot Event listener controller that catches the TriggerOn/TriggerOff
 * {@link ZootEvent}'s. Override it's triggerOn/triggerOff methods to implement
 * behaviour when trigger changes state.
 * 
 * @author Cream
 * @see TriggerController
 */
public abstract class TriggerEventListener extends ZootEventListenerController
{
	@Override
	public boolean handleZootEvent(ZootEvent event)
	{
		switch(event.getType())
		{
		case TriggerOn:
			triggerOn(event.getUserObject(ZootActor.class));
			return true;
			
		case TriggerOff:
			triggerOff(event.getUserObject(ZootActor.class));
			return true;
		
		default:
			return false;		
		}
	}

	public abstract void triggerOn(ZootActor switchActor);
	
	public abstract void triggerOff(ZootActor switchActor);
}