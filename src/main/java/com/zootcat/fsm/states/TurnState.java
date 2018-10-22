package com.zootcat.fsm.states;

import com.zootcat.controllers.logic.DirectionController;
import com.zootcat.controllers.physics.FlyableController;
import com.zootcat.controllers.physics.WalkableController;
import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.fsm.events.ZootEventType;
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
		
		direction = ZootDirection.None;
		actor.controllerAction(DirectionController.class, ctrl -> direction = ctrl.getDirection());
		actor.controllerAction(WalkableController.class, ctrl -> ctrl.stop());
		actor.controllerAction(FlyableController.class, ctrl -> ctrl.stop());
	}
	
	@Override
	public void onLeave(ZootActor actor, ZootEvent event)
	{
		actor.controllerAction(DirectionController.class, ctrl -> ctrl.setDirection(direction.invert()));
	}
		
	@Override
	public boolean handle(ZootEvent event)
	{
		if(event.getType() == ZootEventType.JumpUp && ZootStateUtils.canActorJump(event))
		{		
			changeState(event, JumpState.ID);
		}
		else if(event.getType() == ZootEventType.JumpForward && ZootStateUtils.canActorJump(event))
		{
			changeState(event, JumpForwardState.ID);
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
