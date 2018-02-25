package com.zootcat.fsm.states;

public class ClimbState extends AnimationBasedState
{
	public static final int ID = ClimbState.class.hashCode();
	
	public ClimbState()
	{
		super("Climb");
	}
	
	@Override
	public int getId()
	{
		return ID;
	}	
}
