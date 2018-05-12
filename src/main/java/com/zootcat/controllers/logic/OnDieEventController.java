package com.zootcat.controllers.logic;

import java.util.Arrays;

import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.scene.ZootActor;

public abstract class OnDieEventController extends OnZootEventController
{
	public OnDieEventController()
	{
		super(Arrays.asList(ZootEventType.Dead), true);
	}
	
	public boolean onZootEvent(ZootActor actor, ZootEvent event)
	{
		return onDie(actor, event);
	}

	protected abstract boolean onDie(ZootActor actor, ZootEvent event);	
}