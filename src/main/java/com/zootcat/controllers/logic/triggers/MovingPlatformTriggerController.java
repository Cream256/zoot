package com.zootcat.controllers.logic.triggers;

import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.controllers.logic.MovingPlatformController;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;

public class MovingPlatformTriggerController extends TriggerEventListener
{
	@CtrlParam(required = true) private String platformActorName;
	@CtrlParam(global = true) private ZootScene scene;
		
	@Override
	public void triggerOn(ZootActor switchActor)
	{
		setMovingPlatformsMoving(true);
	}

	@Override
	public void triggerOff(ZootActor switchActor)
	{
		setMovingPlatformsMoving(false);		
	}
	
	protected void setMovingPlatformsMoving(boolean moving)
	{
		scene.getActors(actor -> actor.getName().equalsIgnoreCase(platformActorName))
	 		 .forEach(actor -> actor.controllersAction(MovingPlatformController.class, ctrl ->
	 		 {
	 			 ctrl.setMoving(moving);
	 		 }));
	}
}