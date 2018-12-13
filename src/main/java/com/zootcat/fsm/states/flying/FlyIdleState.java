package com.zootcat.fsm.states.flying;

import com.zootcat.controllers.logic.DirectionController;
import com.zootcat.controllers.physics.FlyableController;
import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.states.BasicState;
import com.zootcat.fsm.states.DeadState;
import com.zootcat.fsm.states.HurtState;
import com.zootcat.fsm.states.ZootStateUtils;
import com.zootcat.fsm.states.ground.AttackState;
import com.zootcat.fsm.states.ground.IdleState;
import com.zootcat.fsm.states.ground.StunState;
import com.zootcat.fsm.states.ground.TurnState;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;

public class FlyIdleState extends BasicState
{	
	public static final int ID = IdleState.ID;
	
	private ZootDirection actorDirection;
	
	public FlyIdleState()
	{
		super("Idle");
	}
	
	@Override
	public void onEnter(ZootActor actor, ZootEvent event)
	{
		setAnimationBasedOnStateName(actor);
		actor.controllerAction(FlyableController.class, (ctrl) -> ctrl.stop());
		
		actorDirection = ZootDirection.None;
		actor.controllerAction(DirectionController.class, c -> actorDirection = c.getDirection());
	}
	
	@Override
	public boolean handle(ZootEvent event)
	{		
		if(ZootStateUtils.isFlyEvent(event))
		{
			ZootDirection eventDirection = ZootStateUtils.getDirectionFromEvent(event);			
			boolean turn = eventDirection != actorDirection && actorDirection != ZootDirection.None;
			changeState(event, turn ? TurnState.ID : FlyState.ID);
		}
		else if(event.getType() == ZootEventType.Attack)
		{
			changeState(event, AttackState.ID);
		}
		else if(event.getType() == ZootEventType.Hurt)
		{
			changeState(event, HurtState.ID);
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
