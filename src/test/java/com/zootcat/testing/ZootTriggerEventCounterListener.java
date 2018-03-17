package com.zootcat.testing;

import com.zootcat.controllers.logic.triggers.TriggerEventListener;
import com.zootcat.scene.ZootActor;

public class ZootTriggerEventCounterListener extends TriggerEventListener
{
	public int onCount;
	public int offCount;
	
	@Override
	public void triggerOn(ZootActor switchActor)
	{
		++onCount;
	}

	@Override
	public void triggerOff(ZootActor switchActor)
	{
		++offCount;
	}; 
}
