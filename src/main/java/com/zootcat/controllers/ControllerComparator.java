package com.zootcat.controllers;

import java.util.Comparator;

public class ControllerComparator implements Comparator<Controller>
{
	public static final ControllerComparator Instance = new ControllerComparator();
	
	private ControllerComparator()
	{
		//use instance
	}
	
	@Override
	public int compare(Controller ctrl1, Controller ctrl2)
	{
		return ctrl2.getPriority().getValue() - ctrl1.getPriority().getValue();
	}
}