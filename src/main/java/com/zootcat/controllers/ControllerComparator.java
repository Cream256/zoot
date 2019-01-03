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
		ControllerPriority p1 = ctrl1.getPriority() != null ? ctrl1.getPriority() : ControllerPriority.Normal;
		ControllerPriority p2 = ctrl2.getPriority() != null ? ctrl2.getPriority() : ControllerPriority.Normal;
		return p2.getValue() - p1.getValue();
	}
}