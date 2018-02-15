package com.zootcat.fsm.states;

import com.zootcat.events.ZootEvent;
import com.zootcat.fsm.ZootState;
import com.zootcat.scene.ZootActor;

public class NullState implements ZootState
{
	public static final NullState INSTANCE = new NullState();
	
	private NullState()
	{
		//use instance
	}
	
	@Override
	public void onEnter(ZootActor actor, ZootEvent event)
	{
		//noop	
	}

	@Override
	public void onLeave(ZootActor actor, ZootEvent event)
	{
		//noop
	}

	@Override
	public void onUpdate(ZootActor actor, float delta)
	{
		//noop
	}

	@Override
	public boolean handle(ZootEvent event)
	{
		return false;
	}

	@Override
	public int getId()
	{
		return 0;
	}

	@Override
	public String getName()
	{
		return null;
	}
}
