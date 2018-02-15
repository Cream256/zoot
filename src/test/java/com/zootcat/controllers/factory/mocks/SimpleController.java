package com.zootcat.controllers.factory.mocks;

import com.zootcat.controllers.ControllerAdapter;

public class SimpleController extends ControllerAdapter
{
	//simple mock that can be found by ControllerFactory
	
	private int value = 0;
	
	public void set(int value)
	{
		this.value = value;
	}
	
	public int get()
	{
		return value;
	}
}
