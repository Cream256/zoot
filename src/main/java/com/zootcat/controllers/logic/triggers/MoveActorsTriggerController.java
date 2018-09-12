package com.zootcat.controllers.logic.triggers;

import com.zootcat.actions.ZootActions;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;

public class MoveActorsTriggerController extends TriggerEventListener
{
	@CtrlParam(debug = true, required = true) private String actorName;
	@CtrlParam(debug = true, required = true) private float mx;
	@CtrlParam(debug = true, required = true) private float my;
	@CtrlParam(debug = true) private boolean canRevert = false;
	@CtrlParam(global = true) private ZootScene scene;
	
	private boolean moved = false;
	
	@Override
	public void triggerOn(ZootActor switchActor)
	{		
		if(!moved)
		{
			move(false);
		}
	}

	@Override
	public void triggerOff(ZootActor switchActor)
	{
		if(moved && canRevert)
		{
			move(true);
		}
	}
	
	protected void move(boolean revertToPreviousLocation)
	{
		moved = !revertToPreviousLocation;
		float distX = mx * scene.getUnitScale() * (revertToPreviousLocation ? -1 : 1);
		float distY = my * scene.getUnitScale() * (revertToPreviousLocation ? -1 : 1);
		
		scene.getActors(act -> act.getName().equalsIgnoreCase(actorName)).forEach(act -> 
		{
			act.addAction(ZootActions.moveActor(act, distX, distY));
		});
	}
}
