package com.zootcat.controllers.logic;

import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction;
import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.events.ZootEvents;
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
