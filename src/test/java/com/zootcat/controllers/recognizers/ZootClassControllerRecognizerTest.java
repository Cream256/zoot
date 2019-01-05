package com.zootcat.controllers.recognizers;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.controllers.logic.HurtOnCollideController;
import com.zootcat.controllers.logic.HurtOnJumpController;
import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.controllers.recognizers.ZootClassControllerRecognizer;

public class ZootClassControllerRecognizerTest
{
	@Test
	public void shouldReturnTrueWhenComparingTheSameClass()
	{
		ZootClassControllerRecognizer.Instance.isControllerExact(new ControllerAdapter(), ControllerAdapter.class);		
	}
	
	@Test
	public void shouldReturnFalseWhenComparingDifferentClass()
	{
		ZootClassControllerRecognizer.Instance.isControllerExact(new ControllerAdapter(), PhysicsBodyController.class);
	}
	
	@Test
	public void shouldReturnFalseWhenComparingToBaseClass()
	{
		ZootClassControllerRecognizer.Instance.isControllerExact(new HurtOnJumpController(), HurtOnCollideController.class);
	}
	
	@Test
	public void shouldReturnFalseWhenComparingToDerivedClass()
	{
		ZootClassControllerRecognizer.Instance.isControllerExact(new HurtOnCollideController(), HurtOnJumpController.class);
	}
	
	@Test
	public void shouldGetNotNullInstance()
	{
		assertNotNull(ZootClassControllerRecognizer.Instance);
	}
}
