package com.zootcat.controllers.recognizer;

import com.zootcat.controllers.Controller;

public class MockControllerRecognizer implements ControllerRecognizer
{
	public static final MockControllerRecognizer Instance = new MockControllerRecognizer();
	private static final String MockitoMark = "$$EnhancerByMockitoWithCGLIB$$";
	
	@Override
	public boolean areEqual(Controller ctrl1, Controller ctrl2)
	{
		return areEqual(ctrl1.getClass(), ctrl2.getClass());		
	}
	
	@Override
	public boolean areEqual(Class<? extends Controller> ctrlClass1, Class<? extends Controller> ctrlClass2)
	{
		String name1 = sanitizeName(ctrlClass1.getName());
		String name2 = sanitizeName(ctrlClass2.getName());		
		return name1.equalsIgnoreCase(name2);
	}
	
	private String sanitizeName(String className)
	{
		int index = className.indexOf(MockitoMark);
		if(index == -1)
		{
			return className;
		}
		return className.substring(0, index);		
	}	
}
