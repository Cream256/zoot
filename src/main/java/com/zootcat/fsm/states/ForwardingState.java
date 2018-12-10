package com.zootcat.fsm.states;

import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.scene.ZootActor;

public class ForwardingState extends BasicState
{
	private int forwardedStateId = 0;
	private int originalStateId = 0;
	private ZootEvent entryEvent = null;
	
	public ForwardingState(int originalStateId, int forwardedStateId)
	{
		super("Forwarding");
		this.originalStateId = originalStateId;
		this.forwardedStateId = forwardedStateId;
		this.entryEvent = new ZootEvent();
	}
	
	@Override
	public void onEnter(ZootActor actor, ZootEvent event)
	{		
		entryEvent.reset();
		entryEvent.setTarget(actor);
		
		if(event != null)			
		{
			entryEvent.setType(event.getType());		
			entryEvent.setUserObject(event.getUserObject(Object.class));
		}
		
		changeState(entryEvent, forwardedStateId);		
	}
		
	@Override
	public int getId()
	{
		return originalStateId;
	}
	
	public int getForwardedStateId()
	{
		return forwardedStateId;
	}
}
