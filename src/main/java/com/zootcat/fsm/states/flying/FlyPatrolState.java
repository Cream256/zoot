package com.zootcat.fsm.states.flying;

import com.zootcat.controllers.physics.FlyableController;
import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.fsm.states.PatrolState;
import com.zootcat.scene.ZootActor;

public class FlyPatrolState extends PatrolState
{
	public static final int ID = FlyState.ID;
	
	@Override
	public void onEnter(ZootActor actor, ZootEvent event)
	{		
		super.onEnter(actor, event);
		setActorAnimation(actor, "Fly");
	}
	
	@Override
	protected void move(ZootActor actor, float delta)
	{
		actor.controllersAction(FlyableController.class, flyCtrl -> flyCtrl.fly(moveDirection));
	}
	
	@Override
	public int getId()
	{
		return ID;
	}
}
