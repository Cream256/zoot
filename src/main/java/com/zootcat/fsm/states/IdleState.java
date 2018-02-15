package com.zootcat.fsm.states;

import com.zootcat.controllers.logic.DirectionController;
import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.events.ZootEvent;
import com.zootcat.events.ZootEventType;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;

public class IdleState extends BasicState
{	
	public static final int ID = IdleState.class.hashCode();
	
	private ZootDirection actorDirection;
	
	public IdleState()
	{
		super("Idle");
	}
	
	@Override
	public void onEnter(ZootActor actor, ZootEvent event)
	{
		setAnimationBasedOnStateName(actor);
		actor.controllerAction(PhysicsBodyController.class, (ctrl) -> ctrl.setVelocity(0.0f, 0.0f, true, false));
		
		actorDirection = ZootDirection.None;
		actor.controllerAction(DirectionController.class, c -> actorDirection = c.getDirection());
	}
	
	@Override
	public boolean handle(ZootEvent event)
	{		
		if(ZootStateUtils.isMoveEvent(event))
		{
			ZootDirection eventDirection = ZootStateUtils.getDirectionFromEvent(event);
			boolean turn = eventDirection != actorDirection && actorDirection != ZootDirection.None;			
			int nextStateId = turn ? TurnState.ID : (ZootStateUtils.isRunEvent(event) ? RunState.ID : WalkState.ID);
			changeState(event, nextStateId);
		}
		else if(event.getType() == ZootEventType.Jump)
		{		
			changeState(event, JumpState.ID);
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
		else if(event.getType() == ZootEventType.Down)
		{
			changeState(event, DownState.ID);
		}
		else if(event.getType() == ZootEventType.Dead)
		{
			changeState(event, DeadState.ID);
		}
		
		return true;
	}
	
	@Override
	public int getId()
	{
		return ID;
	}
}
