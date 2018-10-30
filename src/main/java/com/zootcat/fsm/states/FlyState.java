package com.zootcat.fsm.states;

import com.zootcat.controllers.logic.DirectionController;
import com.zootcat.controllers.physics.FlyableController;
import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;

public class FlyState extends BasicState
{
	public static final int ID = FlyState.class.hashCode();
	
	protected ZootDirection moveDirection = ZootDirection.None;
	
	public FlyState()
	{
		super("Fly");
	}
	
	@Override
	public void onEnter(ZootActor actor, ZootEvent event)
	{
		setAnimationBasedOnStateName(actor);
		moveDirection = ZootStateUtils.getDirectionFromEvent(event);
		actor.controllerAction(DirectionController.class, (ctrl) -> ctrl.setDirection(moveDirection));
	}
	
	@Override
	public void onUpdate(ZootActor actor, float delta)
	{
		actor.controllerAction(FlyableController.class, (ctrl) -> ctrl.fly(moveDirection));		
	}
	
	@Override
	public int getId()
	{
		return ID;
	}
	
	@Override
	public boolean handle(ZootEvent event)
	{
		if(event.getType() == ZootEventType.Stop)
		{
			changeState(event, IdleState.ID);
		}
		else if(ZootStateUtils.isFlyEvent(event))
		{
			ZootDirection eventDirection = ZootStateUtils.getDirectionFromEvent(event);
			if(eventDirection != moveDirection)
			{				
				changeState(event, TurnState.ID);
				moveDirection = eventDirection;
			}
		}		
		else if(event.getType() == ZootEventType.Attack)
		{
			changeState(event, AttackState.ID);
		}
		else if(event.getType() == ZootEventType.Hurt)
		{
			changeState(event, HurtState.ID);
		}		
		else if(event.getType() == ZootEventType.Stun)
		{
			changeState(event, StunState.ID);
		}
		
		return true;
	}
}
