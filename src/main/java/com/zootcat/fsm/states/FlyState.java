package com.zootcat.fsm.states;

public class FlyState extends WalkState
{
	public FlyState()
	{
		super("Fly");
	}
	
	@Override
	public int getId()
	{
		//FlyState should mimic WalkState
		return WalkState.ID;
	}
}
