package com.zootcat.controllers;

import static org.junit.Assert.*;

import org.junit.Test;

import com.zootcat.controllers.factory.mocks.Mock1Controller;
import com.zootcat.controllers.factory.mocks.Mock2Controller;

public class ControllerTypeTest
{
	@Test
	public void shouldReturnValidId()
	{
		ControllerType type1 = ControllerType.forClass(Mock1Controller.class);
		ControllerType type2 = ControllerType.forClass(Mock2Controller.class);
				
		assertNotNull(type1);
		assertTrue(type1.getId() > 0);
		
		assertNotNull(type1);
		assertTrue(type2.getId() > type1.getId());
	}
	
	@Test
	public void shouldReturnValidHashCode()
	{
		ControllerType type1 = ControllerType.forClass(Mock1Controller.class);
		ControllerType type2 = ControllerType.forClass(Mock2Controller.class);
		
		assertEquals(type1.getId(), type1.hashCode());
		assertEquals(type2.getId(), type2.hashCode());		
	}
	
	@Test
	public void shouldReturnEqualsForSameControllerType() 
	{
		ControllerType type1 = ControllerType.forClass(Mock1Controller.class);
		ControllerType type2 = ControllerType.forClass(Mock1Controller.class);

		assertEquals(true, type1.equals(type2));
		assertEquals(true, type2.equals(type1));
		assertEquals(type1.getId(), type2.getId());
	}

	@Test
	public void shouldReturnNotEqualsForDifferentControllerType() 
	{
		ControllerType type1 = ControllerType.forClass(Mock1Controller.class);
		ControllerType type2 = ControllerType.forClass(Mock2Controller.class);

		assertEquals(false, type1.equals(type2));
		assertEquals(false, type2.equals(type1));
		assertFalse(type1.getId() == type2.getId());
	}
	
	@Test
	public void shouldReturnFalseForNullEquality()
	{
		ControllerType type1 = ControllerType.forClass(Mock1Controller.class);
		assertFalse(type1.equals(null));
	}
	
	@Test
	public void shouldReturnFalseForDifferentClassEquality()
	{
		ControllerType type1 = ControllerType.forClass(Mock1Controller.class);
		assertFalse(type1.equals("string"));
	}
}
