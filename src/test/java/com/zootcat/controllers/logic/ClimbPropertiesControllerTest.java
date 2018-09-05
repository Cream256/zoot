package com.zootcat.controllers.logic;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ClimbPropertiesControllerTest
{
	@Test
	public void shouldReturnDefaultValueForCanGrab()
	{
		ClimbPropertiesController ctrl = new ClimbPropertiesController();
		assertTrue(ctrl.canGrab());
	}
	
	@Test
	public void shouldReturnCanGrab()
	{
		ClimbPropertiesController ctrl = new ClimbPropertiesController();
		
		ctrl.setCanGrab(false);
		assertFalse(ctrl.canGrab());
		
		ctrl.setCanGrab(true);
		assertTrue(ctrl.canGrab());		
	}	
}
