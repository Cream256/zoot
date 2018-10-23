package com.zootcat.testing;

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
}
