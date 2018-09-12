package com.zootcat.actions;

import com.zootcat.controllers.input.InputProcessorController;

public class ZootEnableInputProcessorControllerAction extends ZootAction
{
	private boolean controllerEnabled = true;
	
	@Override
	public boolean act(float delta)
	{
		getTargetZootActor().controllerAction(InputProcessorController.class, ctrl -> ctrl.setEnabled(controllerEnabled));
		return true;
	}
	
	public void setControllerEnabled(boolean value)
	{
		controllerEnabled = value;
	}

	public boolean getControllerEnabled()
	{
		return controllerEnabled;
	}
}
