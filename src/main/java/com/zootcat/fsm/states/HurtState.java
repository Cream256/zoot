package com.zootcat.fsm.states;

import com.zootcat.controllers.logic.LifeController;
import com.zootcat.events.ZootEvent;
import com.zootcat.events.ZootEventType;
import com.zootcat.scene.ZootActor;

public class HurtState extends AnimationBasedState
{
	public static final int ID = HurtState.class.hashCode();
			
	public HurtState()
	{
		super("Hurt");
	}
	
	@Override
	public void onEnter(ZootActor actor, ZootEvent event)
	{		
		super.onEnter(actor, event);
		
		int damage = event.getUserObject(Integer.class, 0);
		actor.controllerAction(LifeController.class, c -> c.addToValue(damage));
	}
	
	@Override
	public boolean handle(ZootEvent event)
	{	
		if(event.getType() == ZootEventType.Dead)
		{
			changeState(event.getTargetZootActor(), DeadState.ID);
		}
		return true;
	}
	
	@Override
	public int getId()
	{
		return ID;
	}
}
