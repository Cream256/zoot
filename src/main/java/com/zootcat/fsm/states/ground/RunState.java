package com.zootcat.fsm.states.ground;

import com.zootcat.controllers.physics.WalkableController;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;

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
		actor.controllersAction(WalkableController.class, (mvCtrl) -> 
		{
			if(mvCtrl.canRun())
			{			
				mvCtrl.run(moveDirection);
			} 
			else 
			{				
				changeState(moveDirection == ZootDirection.Right ? ZootEventType.WalkRight : ZootEventType.WalkLeft, actor, WalkState.ID);
			}
		});		
	}
		
	@Override
	public int getId()
	{
		return ID;
	}
}
