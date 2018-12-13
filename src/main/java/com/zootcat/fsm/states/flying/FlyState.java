package com.zootcat.fsm.states.flying;

import com.zootcat.controllers.logic.DirectionController;
import com.zootcat.controllers.physics.FlyableController;
import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.states.BasicState;
import com.zootcat.fsm.states.HurtState;
import com.zootcat.fsm.states.ZootStateUtils;
import com.zootcat.fsm.states.ground.AttackState;
import com.zootcat.fsm.states.ground.StunState;
import com.zootcat.fsm.states.ground.TurnState;
import com.zootcat.fsm.states.ground.WalkState;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;

public class FlyState extends BasicState
{
	public static final int ID = WalkState.ID;
	
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
	public boolean handle(ZootEvent event)
	{
		if(event.getType() == ZootEventType.Stop)
		{
			changeState(event, FlyIdleState.ID);
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
	
	@Override
	public int getId()
	{
		return ID;
	}
}
