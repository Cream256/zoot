package com.zootcat.controllers.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DamageControllerTest
{
	@Test
	public void shouldCreateValidInstanceTest()
	{
		DamageController ctrl = new DamageController();
		assertEquals(0, ctrl.getValue());
	}	
	
	@Test
	public void shouldBeSingleton()
	{
		assertTrue(new DamageController().isSingleton());
	}	
}
