package com.zootcat.controllers.logic;

import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction;
import com.zootcat.events.ZootEvent;
import com.zootcat.events.ZootEventType;
import com.zootcat.events.ZootEvents;
import com.zootcat.scene.ZootActor;

public class DieOnHurtController extends ZootEventListenerController
{
	@Override
	public boolean handleZootEvent(ZootEvent event)
	{
		if(event.getType() == ZootEventType.Hurt)
		{
			die(event.getTargetZootActor());
			return true;
		}
		return false;
	}
	
	public void die(ZootActor actor)
	{
		ZootEvents.fireAndFree(actor, ZootEventType.Dead);				
		actor.addAction(new RemoveActorAction());
	}
}
