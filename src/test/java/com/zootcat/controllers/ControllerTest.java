package com.zootcat.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.zootcat.controllers.factory.mocks.Mock1Controller;
import com.zootcat.controllers.factory.mocks.Mock2Controller;
import com.zootcat.controllers.factory.mocks.MockBaseController;
import com.zootcat.controllers.factory.mocks.MockDerivedController;

public class ControllerTest
{
	@Test
	public void shouldReturnTrueForEqualityBetweenTheSameControllers()
	{
		Controller ctrl1 = new Mock1Controller();
		Controller ctrl2 = new Mock1Controller();
		
		assertTrue(Controller.areEqual(ctrl1, ctrl2));
		assertTrue(Controller.areEqual(ctrl2, ctrl1));		
	}
	
	@Test
	public void shouldReturnFalseForEqualityBetweenDifferentControllers()
	{
		Controller ctrl1 = new Mock1Controller();
		Controller ctrl2 = new Mock2Controller();
		
		assertFalse(Controller.areEqual(ctrl1, ctrl2));
		assertFalse(Controller.areEqual(ctrl2, ctrl1));				
	}
	
	@Test
	public void shouldReturnFalseForEqualityBetweenBaseAndDerivedTypes()
	{
		Controller ctrl1 = new MockDerivedController();
		Controller ctrl2 = new MockBaseController();
		
		assertFalse(Controller.areEqual(ctrl1, ctrl2));
		assertFalse(Controller.areEqual(ctrl2, ctrl1));			
	}
	
	@Test
	public void shouldReturnTrueForEqualityBetweenTheSameControllerTypes()
	{
		assertTrue(Controller.areEqual(Mock1Controller.class, Mock1Controller.class));
	}
	
	@Test
	public void shouldReturnFalseForEqualityBetweenTheDifferentControllerTypes()
	{
		assertFalse(Controller.areEqual(Mock1Controller.class, Mock2Controller.class));
	}
	
	@Test
	public void shouldReturnCorrectControllerIdForController()
	{
		Controller ctrl1 = new Mock1Controller();
		assertEquals(ctrl1.hashCode(), Controller.getControllerId(ctrl1));
	}
	
	@Test
	public void shouldReturnCorrectControllerIdForControllerClass()
	{
		assertEquals(Mock1Controller.class.hashCode(), Controller.getControllerId(Mock1Controller.class));
	}
}
