package com.zootcat.controllers.physics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PhysicsBodyScaleTest
{
	@Test
	public void defaultCtorTest()
	{
		PhysicsBodyScale scale = new PhysicsBodyScale();
		assertEquals(1.0f, scale.scaleX, 0.0f);
		assertEquals(1.0f, scale.scaleY, 0.0f);
		assertEquals(1.0f, scale.radiusScale, 0.0f);
		assertTrue(scale.scaleSensors);
	}
	
	@Test
	public void ctorTest()
	{
		PhysicsBodyScale scale = new PhysicsBodyScale(1.25f, 0.5f, 0.75f, false);
		assertEquals(1.25f, scale.scaleX, 0.0f);
		assertEquals(0.5f, scale.scaleY, 0.0f);
		assertEquals(0.75f, scale.radiusScale, 0.0f);
		assertFalse(scale.scaleSensors);		
	}
	
	@Test
	public void invertTest()
	{
		PhysicsBodyScale scale = new PhysicsBodyScale(0.5f, 0.25f, 1.00f, true);
		PhysicsBodyScale inverted = scale.invert();
		
		assertEquals(2.0f, inverted.scaleX, 0.0f);
		assertEquals(4.0f, inverted.scaleY, 0.0f);
		assertEquals(1.0f, inverted.radiusScale, 0.0f);
		assertEquals(true, inverted.scaleSensors);		
		assertFalse(scale.equals(inverted));
		assertFalse(inverted.equals(scale));
	}
	
	@Test
	public void equalsTest()
	{
		PhysicsBodyScale scale1 = new PhysicsBodyScale(1.25f, 0.5f, 0.75f, false);
		PhysicsBodyScale scale2 = new PhysicsBodyScale(1.25f, 0.5f, 0.75f, false);
		PhysicsBodyScale scale3 = new PhysicsBodyScale(0.00f, 0.5f, 0.75f, false);
		PhysicsBodyScale scale4 = new PhysicsBodyScale(1.25f, 0.0f, 0.75f, false);
		PhysicsBodyScale scale5 = new PhysicsBodyScale(1.25f, 0.5f, 0.00f, false);
		PhysicsBodyScale scale6 = new PhysicsBodyScale(1.25f, 0.5f, 0.75f, true);
		
		assertTrue(scale1.equals(scale1));
		assertTrue(scale1.equals(scale2));
		assertTrue(scale2.equals(scale2));
		assertTrue(scale2.equals(scale1));
		assertTrue(scale3.equals(scale3));
		
		assertFalse(scale1.equals(scale3));
		assertFalse(scale1.equals(scale4));
		assertFalse(scale1.equals(scale5));
		assertFalse(scale1.equals(scale6));
		assertFalse(scale2.equals(scale3));
		assertFalse(scale3.equals("string"));
		assertFalse(scale3.equals(128));
	}
	
	@Test
	public void hashCodeTest()
	{
		PhysicsBodyScale scale1 = new PhysicsBodyScale(1.25f, 0.5f, 0.75f, false);
		PhysicsBodyScale scale2 = new PhysicsBodyScale(1.25f, 0.5f, 0.75f, false);
		PhysicsBodyScale scale3 = new PhysicsBodyScale(0.00f, 0.5f, 0.75f, false);
		PhysicsBodyScale scale4 = new PhysicsBodyScale(1.25f, 0.0f, 0.75f, false);
		PhysicsBodyScale scale5 = new PhysicsBodyScale(1.25f, 0.5f, 0.00f, false);
		PhysicsBodyScale scale6 = new PhysicsBodyScale(1.25f, 0.5f, 0.75f, true);
		
		assertTrue(scale1.hashCode() == scale1.hashCode());
		assertTrue(scale1.hashCode() == scale2.hashCode());
		assertFalse(scale1.hashCode() == scale3.hashCode());
		assertFalse(scale1.hashCode() == scale4.hashCode());
		assertFalse(scale1.hashCode() == scale5.hashCode());
		assertFalse(scale1.hashCode() == scale6.hashCode());
	}
	
}
