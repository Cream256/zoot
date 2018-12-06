package com.zootcat.actions;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.events.ZootEvents;

public class ZootKillActorAction extends ZootAction
{
	private boolean done = false;
	
	@Override
	public boolean act(float delta)
	{
		if(!done)
		{
			ZootEvents.fireAndFree(getTargetZootActor(), ZootEventType.Dead);				
			getTargetZootActor().addAction(Actions.removeActor());	
			done = true;
		}
		
		return true;
	}
	
	@Override
	public void restart()
	{
		done = false;
	}
}
