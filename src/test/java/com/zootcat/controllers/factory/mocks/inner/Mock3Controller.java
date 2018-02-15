package com.zootcat.controllers.factory.mocks.inner;

import com.zootcat.controllers.Controller;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;

public class Mock3Controller implements Controller 
{	
	@CtrlParam public int param;
	@CtrlParam(global=true) public ZootScene scene;

	@Override
	public void onAdd(ZootActor actor) 
	{
		//noop
	}

	@Override
	public void onRemove(ZootActor actor) 
	{
		//noop	
	}

	@Override
	public void onUpdate(float delta, ZootActor actor) 
	{
		//noop
	}

	@Override
	public void init(ZootActor actor) 
	{
		//noop
	}
}
