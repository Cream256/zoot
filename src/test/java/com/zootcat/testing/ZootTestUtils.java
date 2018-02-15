package com.zootcat.testing;

import static org.junit.Assert.fail;

import java.lang.reflect.Field;

import com.zootcat.controllers.Controller;

public class ZootTestUtils
{	
	public static String getResourcePath(String path, Object testCase)
	{		
		try
		{
			return testCase.getClass().getClassLoader().getResource(path).getPath();	
		}
		catch(Exception e)
		{
			throw new RuntimeException("Unable to get resource: " + e.getMessage(), e);
		}
	}
	
	public static void setCtrlParam(Controller ctrl, Field field, Object value)
	{
		try
		{
			field.setAccessible(true);
			field.set(ctrl, value);
		}
		catch (IllegalArgumentException | IllegalAccessException e)
		{
			fail("Unable to set controller parameter: " + e.getMessage());
		}
	}
}
