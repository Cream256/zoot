package com.zootcat.controllers.logic;

import java.util.List;

import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction;
import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.events.ZootEventTypeEnum;
import com.zootcat.fsm.events.ZootEvents;
import com.zootcat.scene.ZootActor;

public class DieOnZootEventController extends OnZootEventController
{
	public DieOnZootEventController(List<ZootEventTypeEnum> zootEvents, boolean singleExecution)
	{
		super(zootEvents, singleExecution);
	}
	
	public boolean onZootEvent(ZootActor actor, ZootEvent event)
	{
		ZootEvents.fireAndFree(actor, ZootEventType.Dead);
		actor.addAction(new RemoveActorAction());
		return true;
	}
}