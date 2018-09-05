package com.zootcat.controllers.logic;

import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.controllers.factory.CtrlParam;

public class ClimbPropertiesController extends ControllerAdapter
{
	@CtrlParam private boolean canGrab = true;
	
	public boolean canGrab()
	{
		return canGrab;
	}	
	
	public void setCanGrab(boolean value)
	{
		canGrab = value;
	}
}
