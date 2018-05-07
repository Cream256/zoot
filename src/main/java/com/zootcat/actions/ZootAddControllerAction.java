package com.zootcat.actions;

import com.zootcat.controllers.Controller;

public class ZootAddControllerAction extends ZootAction
{
	private Controller ctrl;
	
	@Override
	public boolean act(float delta)
	{
		ctrl.init(getTargetZootActor());
		getTargetZootActor().addController(ctrl);		
		return true;
	}
	
	@Override
	public void restart()
	{
		ctrl = null;
	}

	public Controller getController()
	{
		return ctrl;
	}
	
	public void setController(Controller ctrl)
	{
		this.ctrl = ctrl;
	}
}
