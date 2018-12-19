package com.zootcat.controllers.recognizer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.zootcat.controllers.Controller;
import com.zootcat.controllers.factory.mocks.Mock1Controller;
import com.zootcat.controllers.factory.mocks.Mock2Controller;
import com.zootcat.controllers.factory.mocks.MockBaseController;
import com.zootcat.controllers.factory.mocks.MockDerivedController;

public class DefaultControllerRecognizerTest
{
	private DefaultControllerRecognizer defaultRecognizer;
	
	@Before
	public void setup()
	{
		defaultRecognizer = DefaultControllerRecognizer.Instance;
	}
	
	@Test
	public void shouldReturnTrueForEqualityBetweenTheSameControllers()
	{
		Controller ctrl1 = new Mock1Controller();
		Controller ctrl2 = new Mock1Controller();
		
		assertTrue(defaultRecognizer.areEqual(ctrl1, ctrl2));
		assertTrue(defaultRecognizer.areEqual(ctrl2, ctrl1));		
	}
	
	@Test
	public void shouldReturnFalseForEqualityBetweenDifferentControllers()
	{
		Controller ctrl1 = new Mock1Controller();
		Controller ctrl2 = new Mock2Controller();
		
		assertFalse(defaultRecognizer.areEqual(ctrl1, ctrl2));
		assertFalse(defaultRecognizer.areEqual(ctrl2, ctrl1));				
	}
	
	@Test
	public void shouldReturnFalseForEqualityBetweenBaseAndDerivedTypes()
	{
		Controller ctrl1 = new MockDerivedController();
		Controller ctrl2 = new MockBaseController();
		
		assertFalse(defaultRecognizer.areEqual(ctrl1, ctrl2));
		assertFalse(defaultRecognizer.areEqual(ctrl2, ctrl1));			
	}
	
	@Test
	public void shouldReturnTrueForEqualityBetweenTheSameControllerTypes()
	{
		assertTrue(defaultRecognizer.areEqual(Mock1Controller.class, Mock1Controller.class));
	}
	
	@Test
	public void shouldReturnFalseForEqualityBetweenTheDifferentControllerTypes()
	{
		assertFalse(defaultRecognizer.areEqual(Mock1Controller.class, Mock2Controller.class));
	}	
}
