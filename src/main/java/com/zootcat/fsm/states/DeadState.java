package com.zootcat.fsm.states;

import com.zootcat.controllers.logic.LifeController;
import com.zootcat.events.ZootEvent;
import com.zootcat.scene.ZootActor;

public class DeadState extends AnimationBasedState
{
	public static final int ID = DeadState.class.hashCode();
			
	public DeadState()
	{
		super("Dead");
	}
	
	@Override
	public void onUpdate(ZootActor actor, float delta)
	{
		boolean alive = actor.controllerCondition(LifeController.class, c -> c.isAlive());
		if(alive)
		{
			changeState(actor, IdleState.ID);
		}
	}
	
	@Override
	public boolean handle(ZootEvent event)
	{	
		return true;
	}
	
	@Override
	public int getId()
	{
		return ID;
	}
}
