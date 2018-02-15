package com.zootcat.controllers.logic;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class IgnorePlatformsControllerTest
{
	@Test
	public void constructorTest()
	{
		IgnorePlatformsController ctrl = new IgnorePlatformsController();
		assertTrue(ctrl.isActive());
	}
	
	@Test
	public void setActiveTest()
	{
		IgnorePlatformsController ctrl = new IgnorePlatformsController();
		
		ctrl.setActive(false);
		assertFalse(ctrl.isActive());
		
		ctrl.setActive(true);
		assertTrue(ctrl.isActive());
	}
}
