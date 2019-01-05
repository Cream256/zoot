package com.zootcat.controllers;

public class ControllerClassEqualityComparator
{
	private ControllerClassEqualityComparator()
	{
		//use static method for comparsion
	}
	
	public static boolean isControllerOfClass(Controller ctrl, Class<? extends Controller> clazz)
	{
		return ctrl.getClass() == clazz;
	}	
}