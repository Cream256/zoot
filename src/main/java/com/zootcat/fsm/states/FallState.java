package com.zootcat.fsm.states;

import com.zootcat.controllers.logic.DirectionController;
import com.zootcat.controllers.physics.MoveableController;
import com.zootcat.events.ZootEvent;
import com.zootcat.events.ZootEventType;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;

public class FallState extends BasicState
{
	public static final int ID = FallState.class.hashCode();
	
	public FallState()
	{
		super("Fall");
	}

	@Override
	public void onEnter(ZootActor actor, ZootEvent event)
	{
		setAnimationBasedOnStateName(actor);
	}
	
	@Override
	public boolean handle(ZootEvent event)
	{
		if(event.getType() == ZootEventType.Ground)
		{
			changeState(event, IdleState.ID);
		}
		else if(ZootStateUtils.isMoveEvent(event))
		{
			ZootDirection dir = ZootStateUtils.getDirectionFromEvent(event);
			event.getTargetZootActor().controllerAction(MoveableController.class, (ctrl) -> ctrl.walk(dir));
			event.getTargetZootActor().controllerAction(DirectionController.class, (ctrl) -> ctrl.setDirection(dir));
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
