package com.zootcat.controllers.logic;

import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.scene.ZootActor;

public class OnEnterStateController extends ControllerAdapter
{
	@CtrlParam(required = true) private String stateName;
	
	private boolean enteredThisUpdate = false;
		
	@Override
	public void onUpdate(float delta, ZootActor actor)
	{
		boolean properState = actor.getStateMachine().getCurrentState().getName().equalsIgnoreCase(stateName);        
        if(properState)
        {
            if(!enteredThisUpdate)
            {
            	onEnterState(actor);
                enteredThisUpdate = true;
            }
        }
        else
        {
            enteredThisUpdate = false;
        }
	}

	public void onEnterState(ZootActor actor)
	{
		//noop, to be overriden
	}
}

