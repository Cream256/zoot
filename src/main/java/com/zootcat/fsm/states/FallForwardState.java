package com.zootcat.fsm.states;

import com.zootcat.events.ZootEvent;
import com.zootcat.scene.ZootActor;

public class FallForwardState extends FallState
{
	public static final int ID = FallForwardState.class.hashCode();
	
	public FallForwardState()
	{
		super("FallForward");
	}
	
	@Override
	public void onEnter(ZootActor actor, ZootEvent event)
	{
		setAnimationBasedOnStateName(actor);
	}
	
	@Override
	public int getId()
	{
		return ID;
	}
}
