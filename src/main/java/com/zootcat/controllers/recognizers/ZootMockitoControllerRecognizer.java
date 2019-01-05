package com.zootcat.controllers.recognizers;

import com.zootcat.controllers.Controller;

/**
 * Comparator used only in unit tests. Compares controller's using their class name. 
 * It allows for recognizing mockito Mock classes. 
 * @author Cream
 *
 */
public class ZootMockitoControllerRecognizer implements ZootControllerRecognizer
{
	public static final ZootMockitoControllerRecognizer Instance = new ZootMockitoControllerRecognizer();
	
	private ZootMockitoControllerRecognizer()
	{
		//use Instance
	}
	
	@Override
	public boolean isControllerExact(Controller ctrl, Class<? extends Controller> clazz)
	{
		String name1 = sanitizeName(ctrl.getClass().getName());
		String name2 = sanitizeName(clazz.getName());		
		return name1.equalsIgnoreCase(name2);
	}

	private String sanitizeName(String className)
	{
		int index = className.indexOf("$$EnhancerByMockitoWithCGLIB$$");
		if(index == -1)
		{
			return className;
		}
		return className.substring(0, index);		
	}
}
