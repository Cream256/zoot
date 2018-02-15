package com.zootcat.controllers.logic;

import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;

public class SizeController extends ControllerAdapter
{
	@CtrlParam(debug = true, required = true) private float width;
	@CtrlParam(debug = true, required = true) private float height;
	@CtrlParam(global = true) private ZootScene scene;
	
	@Override
	public void init(ZootActor actor) 
	{
		actor.setSize(width * scene.getUnitScale(), height * scene.getUnitScale());
	}
}