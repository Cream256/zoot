package com.zootcat.controllers.logic;

import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.events.ZootEvents;
import com.zootcat.scene.ZootActor;

public class DieOnTimerController extends OnTimerController
{	
	@Override
	public void onTimer(float delta, ZootActor actor)
	{
		ZootEvents.fireAndFree(actor, ZootEventType.Dead);				
		actor.addAction(new RemoveActorAction());
	}
}
