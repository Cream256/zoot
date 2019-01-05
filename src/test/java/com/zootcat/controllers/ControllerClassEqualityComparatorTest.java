package com.zootcat.controllers;

import org.junit.Test;

import com.zootcat.controllers.logic.HurtOnCollideController;
import com.zootcat.controllers.logic.HurtOnJumpController;
import com.zootcat.controllers.physics.PhysicsBodyController;

public class ControllerClassEqualityComparatorTest
{
	@Test
	public void shouldReturnTrueWhenComparingTheSameClass()
	{
		ControllerClassEqualityComparator.isControllerOfClass(new ControllerAdapter(), ControllerAdapter.class);		
	}
	
	@Test
	public void shouldReturnFalseWhenComparingDifferentClass()
	{
		ControllerClassEqualityComparator.isControllerOfClass(new ControllerAdapter(), PhysicsBodyController.class);
	}
	
	@Test
	public void shouldReturnFalseWhenComparingToBaseClass()
	{
		ControllerClassEqualityComparator.isControllerOfClass(new HurtOnJumpController(), HurtOnCollideController.class);
	}
	
	@Test
	public void shouldReturnFalseWhenComparingToDerivedClass()
	{
		ControllerClassEqualityComparator.isControllerOfClass(new HurtOnCollideController(), HurtOnJumpController.class);
	}
}
