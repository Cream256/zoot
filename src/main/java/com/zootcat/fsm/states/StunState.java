package com.zootcat.fsm.states;

import com.zootcat.controllers.physics.FlyableController;
import com.zootcat.controllers.physics.WalkableController;
import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.scene.ZootActor;

/*
 * Stun state - the actor is stunned. Stun duration is as long as 
 * stun animation duration.
 * 
 * @author Cream
 */
public class StunState extends AnimationBasedState
{
	public static final int ID = StunState.class.hashCode();	
		
	public StunState()
	{
		super("Stun");
	}
		
	@Override
	public void onEnter(ZootActor actor, ZootEvent event)
	{	
		super.onEnter(actor, event);
		actor.controllersAction(WalkableController.class, ctrl -> ctrl.stop());
		actor.controllersAction(FlyableController.class, ctrl -> ctrl.stop());		
	}
	
	@Override
	public boolean handle(ZootEvent event)
	{		
		if(event.getType() == ZootEventType.Hurt && ZootStateUtils.canHurtActor(event))
		{
			changeState(event, HurtState.ID);
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
