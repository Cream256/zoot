package com.zootcat.controllers.recognizers;

import com.zootcat.controllers.Controller;

/**
 * Default comparator. Compares controller's using their classes. 
 * Only exact class will be matched, base and dervied classes are not recognized. 
 * @author Cream
 *
 */
public class ZootClassControllerRecognizer implements ZootControllerRecognizer
{
	public static final ZootClassControllerRecognizer Instance = new ZootClassControllerRecognizer(); 
	
	private ZootClassControllerRecognizer()
	{
		//use instance
	}
		
	@Override
	public boolean isControllerExact(Controller ctrl, Class<? extends Controller> clazz)
	{
		return ctrl.getClass() == clazz;
	}	
}