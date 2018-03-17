package com.zootcat.controllers.logic.triggers;

import com.zootcat.actions.ZootActions;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;

public class KillActorsTriggerController extends TriggerEventListener
{
	@CtrlParam(debug = true, required = true) private String actorName;
	@CtrlParam(global = true) private ZootScene scene;
	
	@Override
	public void triggerOn(ZootActor switchActor)
	{
		scene.getActors(act -> act.getName().equalsIgnoreCase(actorName)).forEach(act -> 
		{
			act.addAction(ZootActions.killActorAction(act));	 
		});
	}

	@Override
	public void triggerOff(ZootActor switchActor)
	{
		//noop
	}
}
