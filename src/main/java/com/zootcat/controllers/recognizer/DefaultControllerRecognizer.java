package com.zootcat.controllers.recognizer;

import com.zootcat.controllers.Controller;

public class DefaultControllerRecognizer implements ControllerRecognizer
{
	public static final DefaultControllerRecognizer Instance = new DefaultControllerRecognizer();
	
	private DefaultControllerRecognizer()
	{
		//private, use instance
	}
	
	@Override
	public boolean areEqual(Controller ctrl1, Controller ctrl2)
	{
		return ctrl1.getClass().hashCode() == ctrl2.getClass().hashCode();
	}

	@Override
	public boolean areEqual(Class<? extends Controller> ctrlClass1, Class<? extends Controller> ctrlClass2)
	{
		return ctrlClass1.hashCode() == ctrlClass2.hashCode();
	}
}
