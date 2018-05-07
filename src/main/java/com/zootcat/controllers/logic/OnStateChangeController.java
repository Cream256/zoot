package com.zootcat.controllers.logic;

import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.controllers.ControllerPriority;
import com.zootcat.fsm.ZootState;
import com.zootcat.scene.ZootActor;

public abstract class OnStateChangeController extends ControllerAdapter
{	
	private ZootState lastState = null;
		
	public void init(ZootActor actor)
	{
		lastState = actor.getStateMachine().getCurrentState();
	}
	
	@Override
	public void onUpdate(float delta, ZootActor actor)
	{
		ZootState currentState = actor.getStateMachine().getCurrentState();
		if(lastState != currentState)
        {                        
        	if(lastState != null) 
        	{ 
        		onLeaveState(actor, lastState); 
        	}			
			onEnterState(actor, actor.getStateMachine().getCurrentState());
			lastState = currentState;
        }
	}
	
	@Override
	public ControllerPriority getPriority() 
	{ 
		return ControllerPriority.Low; 
	}

	public abstract void onEnterState(ZootActor actor, ZootState state);
	
	public abstract void onLeaveState(ZootActor actor, ZootState state);
}

