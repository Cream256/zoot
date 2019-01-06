package com.zootcat.controllers.logic;

import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.controllers.ControllerPriority;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;

public class SizeController extends ControllerAdapter
{
	@CtrlParam(required = true) private float width;
	@CtrlParam(required = true) private float height;
	@CtrlParam(global = true) private ZootScene scene;
	
	@Override
	public boolean isSingleton()
	{
		return true;
	}
	
	@Override
	public void init(ZootActor actor) 
	{
		actor.setSize(width * scene.getUnitScale(), height * scene.getUnitScale());
	}
	
	@Override
	public ControllerPriority getPriority() 
	{ 
		return ControllerPriority.Critical; 
	}
}