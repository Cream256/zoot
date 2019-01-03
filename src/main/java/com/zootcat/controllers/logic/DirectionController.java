package com.zootcat.controllers.logic;

import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.scene.ZootDirection;

public class DirectionController extends ControllerAdapter
{
	@CtrlParam private ZootDirection direction = ZootDirection.Right;
		
	public ZootDirection getDirection()
	{
		return direction;
	}
	
	public void setDirection(ZootDirection direction)
	{
		this.direction = direction;		
	}
	
	@Override
	public boolean isSingleton()
	{
		return true;
	}
}
