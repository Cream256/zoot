package com.zootcat.controllers.logic.triggers;

import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.controllers.logic.MovingPlatformController;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;

//TODO add tests
public class MovingPlatformTriggerController extends TriggerEventListener
{
	@CtrlParam(debug = true, required = true) private String platformActorName;
	@CtrlParam(global = true) private ZootScene scene;
		
	@Override
	public void triggerOn(ZootActor switchActor)
	{
		setMovingPlatformsEnabled(true);
	}

	@Override
	public void triggerOff(ZootActor switchActor)
	{
		setMovingPlatformsEnabled(false);		
	}
	
	protected void setMovingPlatformsEnabled(boolean enabled)
	{
		scene.getActors(actor -> actor.getName().equalsIgnoreCase(platformActorName))
		 	 .forEach(actor -> actor.controllerAction(MovingPlatformController.class, ctrl ->
		 	 {
		 		 ctrl.setEnabled(enabled);
		 	 }));
	}
}
