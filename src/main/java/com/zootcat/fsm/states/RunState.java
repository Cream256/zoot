package com.zootcat.fsm.states;

import com.zootcat.controllers.physics.MoveableController;
import com.zootcat.scene.ZootActor;

public class RunState extends WalkState
{	
	public static final int ID = RunState.class.hashCode();
		
	public RunState()
	{
		super("Run");
	}
	
	@Override
	public void onUpdate(ZootActor actor, float delta)
	{
		actor.controllerAction(MoveableController.class, (mvCtrl) -> mvCtrl.run(moveDirection));		
	}
		
	@Override
	public int getId()
	{
		return ID;
	}
}
