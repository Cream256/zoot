package com.zootcat.fsm.states.ground;

import com.zootcat.controllers.logic.DirectionController;
import com.zootcat.controllers.physics.WalkableController;
import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.states.BasicState;
import com.zootcat.fsm.states.HurtState;
import com.zootcat.fsm.states.StunState;
import com.zootcat.fsm.states.ZootStateUtils;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;

public class WalkState extends BasicState
{	
	public static final int ID = WalkState.class.hashCode();
	
	protected ZootDirection moveDirection = ZootDirection.None;
		
	public WalkState()
	{
		super("Walk");
	}
	
	protected WalkState(String name)
	{
		super(name);
	}

	@Override
	public void onEnter(ZootActor actor, ZootEvent event)
	{
		setAnimationBasedOnStateName(actor);
		moveDirection = ZootStateUtils.getDirectionFromEvent(event);
		actor.controllersAction(DirectionController.class, (ctrl) -> ctrl.setDirection(moveDirection));
	}
	
	@Override
	public void onUpdate(ZootActor actor, float delta)
	{
		actor.controllersAction(WalkableController.class, (mvCtrl) -> mvCtrl.walk(moveDirection));		
	}
	
	@Override
	public boolean handle(ZootEvent event)
	{
		if(event.getType() == ZootEventType.Stop)
		{
			changeState(event, IdleState.ID);
		}
		else if(ZootStateUtils.isMoveEvent(event))
		{
			ZootDirection eventDirection = ZootStateUtils.getDirectionFromEvent(event);
			if(eventDirection != moveDirection)
			{				
				changeState(event, TurnState.ID);
				moveDirection = eventDirection;
			}
			else if(ZootStateUtils.isRunEvent(event) && ZootStateUtils.canActorRun(event))
			{
				changeState(event, RunState.ID);
			}
		}		
		else if(event.getType() == ZootEventType.JumpUp && ZootStateUtils.canActorJump(event))
		{		
			changeState(event, JumpState.ID);
		}
		else if(event.getType() == ZootEventType.JumpForward && ZootStateUtils.canActorJump(event))
		{
			changeState(event, JumpForwardState.ID);
		}	
		else if(event.getType() == ZootEventType.Fall || event.getType() == ZootEventType.InAir)
		{
			changeState(event, FallState.ID);
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
