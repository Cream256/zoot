package com.zootcat.fsm.states;

import com.zootcat.fsm.ZootState;
import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.scene.ZootActor;

public class ZootStateWrapper implements ZootState
{
	private ZootState wrappedState;
	
	public ZootStateWrapper(ZootState wrappedState)
	{
		this.wrappedState = wrappedState;
	}
	
	public ZootState getWrappedState()
	{
		return wrappedState;
	}
	
	@Override
	public int getId()
	{
		return wrappedState.getId();
	}

	@Override
	public String getName()
	{
		return wrappedState.getName();
	}

	@Override
	public void onEnter(ZootActor actor, ZootEvent event)
	{
		wrappedState.onEnter(actor, event);
	}

	@Override
	public void onLeave(ZootActor actor, ZootEvent event)
	{
		wrappedState.onLeave(actor, event);
	}

	@Override
	public void onUpdate(ZootActor actor, float delta)
	{
		wrappedState.onUpdate(actor, delta);
	}

	@Override
	public boolean handle(ZootEvent event)
	{
		return wrappedState.handle(event);
	}
	
	@Override
	public String toString()
	{
		return "(Wrapped) " + wrappedState.toString();
	}
}
