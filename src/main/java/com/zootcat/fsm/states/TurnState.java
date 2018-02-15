package com.zootcat.fsm.states;

import com.zootcat.controllers.logic.DirectionController;
import com.zootcat.events.ZootEvent;
import com.zootcat.events.ZootEventType;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;

public class TurnState extends AnimationBasedState
{
	public static final int ID = TurnState.class.hashCode();
	
	private ZootDirection direction;
	
	public TurnState()
	{
		super("Turn");
	}
	
	@Override
	public void onEnter(ZootActor actor, ZootEvent event)
	{		
		super.onEnter(actor, event);
		
		//get actor direction
		direction = ZootDirection.None;
		actor.controllerAction(DirectionController.class, ctrl -> direction = ctrl.getDirection());
	}
	
	@Override
	public void onLeave(ZootActor actor, ZootEvent event)
	{
		actor.controllerAction(DirectionController.class, ctrl -> ctrl.setDirection(direction.invert()));
	}
		
	@Override
	public boolean handle(ZootEvent event)
	{
		if(event.getType() == ZootEventType.Jump)
		{
			changeState(event.getTargetZootActor(), JumpState.ID);
		}	
		else if(event.getType() == ZootEventType.Hurt)
		{
			changeState(event, HurtState.ID);
		}
		
		return true;
	}
	
	@Override
	public int getId()
	{
		return ID;
	}
}
