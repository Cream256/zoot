package com.zootcat.controllers.factory.mocks;

import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.scene.ZootActor;

public class CountingController extends ControllerAdapter
{
	private int count = 0;
	
	@Override
	public void onAdd(ZootActor actor) 
	{
		count = actor.getControllers().size();
	}
	
	public int getControllersCountOnAdd()
	{
		return count;
	}
	
}
