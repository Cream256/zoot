package com.zootcat.controllers.logic;

import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;

//TODO add tests
public class MovingPlatformSwitchController extends SwitchEventListener
{
	@CtrlParam(debug = true, required = true) private String platformActorName;
	@CtrlParam(global = true) private ZootScene scene;
		
	@Override
	public void turnOn(ZootActor switchActor)
	{
		setMovingPlatformsEnabled(true);
	}

	@Override
	public void turnOff(ZootActor switchActor)
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
