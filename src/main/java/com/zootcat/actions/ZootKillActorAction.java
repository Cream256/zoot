package com.zootcat.actions;

import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction;
import com.zootcat.events.ZootEventType;
import com.zootcat.events.ZootEvents;

public class ZootKillActorAction extends ZootAction
{
	private boolean done = false;
	
	@Override
	public boolean act(float delta)
	{
		if(!done)
		{
			ZootEvents.fireAndFree(getTargetZootActor(), ZootEventType.Dead);				
			getTargetZootActor().addAction(new RemoveActorAction());	
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
