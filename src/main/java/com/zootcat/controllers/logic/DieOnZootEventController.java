package com.zootcat.controllers.logic;

import java.util.List;

import com.zootcat.actions.ZootActions;
import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.fsm.events.ZootEventTypeEnum;
import com.zootcat.scene.ZootActor;

public class DieOnZootEventController extends OnZootEventController
{
	public DieOnZootEventController(List<ZootEventTypeEnum> zootEvents, boolean singleExecution)
	{
		super(zootEvents, singleExecution);
	}
	
	public boolean onZootEvent(ZootActor actor, ZootEvent event)
	{
		actor.addAction(ZootActions.killActor(actor));
		return true;
	}
}