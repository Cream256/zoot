package com.zootcat.fsm.states;

import com.zootcat.controllers.logic.ClimbController;
import com.zootcat.events.ZootEvent;
import com.zootcat.scene.ZootActor;

//TODO add test
public class ClimbState extends AnimationBasedState
{
	public static final int ID = ClimbState.class.hashCode();
		
	public ClimbState()
	{
		super("Climb");
	}
	
	@Override
	public void onEnter(ZootActor actor, ZootEvent event)
	{		
		super.onEnter(actor, event);
		
		actor.controllerAction(ClimbController.class, ctrl -> ctrl.grab());
	}
		
	@Override
	public boolean handle(ZootEvent event)
	{
		ZootActor actor = event.getTargetZootActor();
		switch(event.getType())
		{
		case Hurt:
			changeState(event, HurtState.ID);
			return true;
			
		case Up:
			actor.controllerAction(ClimbController.class, ctrl -> 
			{
				if(ctrl.climb()) changeState(event, IdleState.ID);
			});
			return true;
		
		case Down:	//TODO add test			
			actor.controllerAction(ClimbController.class, ctrl -> ctrl.letGo());			
			changeState(event, FallState.ID);
			return true;
		
		default:
			return true;		
		}
	}
	
	@Override
	public void onUpdate(ZootActor actor, float delta)
	{
		//noop
	}
	
	@Override
	public int getId()
	{
		return ID;
	}	
}
