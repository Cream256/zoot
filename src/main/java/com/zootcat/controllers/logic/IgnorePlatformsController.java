package com.zootcat.controllers.logic;

import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.controllers.factory.CtrlParam;

public class IgnorePlatformsController extends ControllerAdapter
{
	@CtrlParam private boolean active = true;
	
	public boolean isActive()
	{
		return active;
	}
	
	public void setActive(boolean value)
	{
		active = value;
	}
	
	@Override
	public boolean isSingleton()
	{
		return true;
	}
}
