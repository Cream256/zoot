package com.zootcat.controllers.logic;

import java.util.Arrays;

import com.zootcat.events.ZootEvent;
import com.zootcat.events.ZootEventType;
import com.zootcat.scene.ZootActor;

public class OnDieEventController extends OnZootEventController
{
	public OnDieEventController()
	{
		super(Arrays.asList(ZootEventType.Dead), true);
	}
	
	public boolean onZootEvent(ZootActor actor, ZootEvent event)
	{
		return true;
	}
	
}
