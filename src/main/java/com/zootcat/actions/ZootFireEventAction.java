package com.zootcat.actions;

import com.zootcat.fsm.events.ZootEvent;

public class ZootFireEventAction extends ZootAction
{
	private ZootEvent event;
		
	@Override
	public boolean act(float delta)
	{
		getTargetZootActor().fire(event);
		return true;
	}
	
	public void setEvent(ZootEvent event)
	{
		this.event = event;
	}
	
	public ZootEvent getEvent()
	{
		return event;
	}

}
