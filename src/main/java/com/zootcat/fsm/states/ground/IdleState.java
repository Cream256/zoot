package com.zootcat.fsm.states.ground;

import com.zootcat.controllers.logic.DirectionController;
import com.zootcat.controllers.physics.WalkableController;
import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.states.BasicState;
import com.zootcat.fsm.states.DeadState;
import com.zootcat.fsm.states.HurtState;
import com.zootcat.fsm.states.StunState;
import com.zootcat.fsm.states.ZootStateUtils;
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
		actor.controllersAction(WalkableController.class, (ctrl) -> ctrl.stop());
		
		actorDirection = ZootDirection.None;
		actor.controllersAction(DirectionController.class, c -> actorDirection = c.getDirection());
	}
	
	@Override
	public boolean handle(ZootEvent event)
	{		
		if(ZootStateUtils.isMoveEvent(event))
		{
			ZootDirection eventDirection = ZootStateUtils.getDirectionFromEvent(event);			
			boolean turn = eventDirection != actorDirection && actorDirection != ZootDirection.None;
			boolean run = ZootStateUtils.isRunEvent(event) && ZootStateUtils.canActorRun(event);
			int nextStateId = turn ? TurnState.ID : (run ? RunState.ID : WalkState.ID);
			changeState(event, nextStateId);
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
		else if(event.getType() == ZootEventType.Hurt && ZootStateUtils.canHurtActor(event))
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
