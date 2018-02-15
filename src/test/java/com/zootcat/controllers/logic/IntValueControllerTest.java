package com.zootcat.controllers.logic;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.scene.ZootActor;

public class IntValueControllerTest
{
	private IntValueController ctrl;	
	
	@Before
	public void setup()
	{
		ctrl = new IntValueController();
	}
	
	@Test
	public void initShouldFixInvalidParametersTest()
	{
		//when
		ControllerAnnotations.setControllerParameter(ctrl, "minValue", 10);
		ControllerAnnotations.setControllerParameter(ctrl, "maxValue", -5);
		ControllerAnnotations.setControllerParameter(ctrl, "value", 500);
		ctrl.init(mock(ZootActor.class));
		
		//then
		assertEquals(10, ctrl.getMaxValue());
		assertEquals(-5, ctrl.getMinValue());
		assertEquals(10, ctrl.getValue());
	}
	
	@Test
	public void defaultValuesTest()
	{
		assertEquals(0, ctrl.getValue());
		assertEquals(0, ctrl.getMaxValue());
		assertEquals(0, ctrl.getMinValue());
	}
	
	@Test
	public void setMaxValueTest()
	{
		ctrl.setMaxValue(10);
		assertEquals(10, ctrl.getMaxValue());
		
		ctrl.setMaxValue(0);
		assertEquals(0, ctrl.getMaxValue());
		
		ctrl.setMinValue(-10);
		ctrl.setMaxValue(-20);
		assertEquals("Max value should be clamped to min value", ctrl.getMinValue(), ctrl.getMaxValue());
	}
	
	@Test
	public void setValueTest()
	{
		//given
		final int max = 10;
		final int min = 0;
		ctrl.setMinValue(min);
		ctrl.setMaxValue(max);
		
		//when
		ctrl.setValue(0);		
		assertEquals(0, ctrl.getValue());
		
		ctrl.setValue(5);		
		assertEquals(5, ctrl.getValue());
		
		ctrl.setValue(max);		
		assertEquals(max, ctrl.getValue());
		
		ctrl.setValue(max * 2);
		assertEquals("Should clamp to max value", max, ctrl.getValue());
		
		ctrl.setValue(max * -2);
		assertEquals("Should clamp to min value", min, ctrl.getValue());		
	}
	
	@Test
	public void setMinValueTest()
	{
		ctrl.setMinValue(0);
		assertEquals(0, ctrl.getMinValue());
		
		ctrl.setMinValue(-10);
		assertEquals(-10, ctrl.getMinValue());
		
		ctrl.setMaxValue(25);
		ctrl.setMinValue(26);
		assertEquals("Min value should be clamped to max value", 25, ctrl.getMinValue());
	}
	
	@Test
	public void addToValueTest()
	{
		//given
		final int min = -10;
		final int max = 10;
		ctrl.setMinValue(min);
		ctrl.setMaxValue(max);
		ctrl.setValue(0);
		
		//when
		ctrl.addToValue(0);
		assertEquals(0, ctrl.getValue());
		
		ctrl.addToValue(1);
		assertEquals(1, ctrl.getValue());
		
		ctrl.addToValue(-2);
		assertEquals(-1, ctrl.getValue());
		
		ctrl.addToValue(Integer.MAX_VALUE);
		assertEquals("Should clamp to max value", max, ctrl.getValue());
		
		ctrl.setValue(0);
		ctrl.addToValue(Integer.MIN_VALUE);
		assertEquals("Should clamp to min value", min, ctrl.getValue());
	}
	
	@Test
	public void addToMaxValueTest()
	{
		//given
		ctrl.setMinValue(0);
		ctrl.setMaxValue(10);
		
		//when
		ctrl.addToMaxValue(5);
		assertEquals(15, ctrl.getMaxValue());
		
		ctrl.addToMaxValue(-10);
		assertEquals(5, ctrl.getMaxValue());
		
		ctrl.addToMaxValue(Integer.MIN_VALUE);
		assertEquals("Should clamp to min value", 0, ctrl.getMaxValue());
	}
	
	@Test
	public void addToMinValueTest()
	{
		//given
		ctrl.setMaxValue(10);
		ctrl.setMinValue(-10);
		
		//when
		ctrl.addToMinValue(5);
		assertEquals(-5, ctrl.getMinValue());
		
		ctrl.addToMinValue(-10);
		assertEquals(-15, ctrl.getMinValue());
		
		ctrl.addToMinValue(Integer.MAX_VALUE);
		assertEquals("Should clamp to max value", 10, ctrl.getMinValue());		
	}
}
