package com.zootcat.controllers.factory;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.Test;

import com.zootcat.controllers.factory.mocks.ControllerWithDebugFieldsMock;
import com.zootcat.controllers.factory.mocks.Mock2Controller;
import com.zootcat.exceptions.RuntimeZootException;

public class ControllerAnnotationsTest 
{	
	@Test
	public void shouldSetControllerParameterValue()
	{
		Mock2Controller ctrl = new Mock2Controller();
		ctrl.a = 0;
		
		ControllerAnnotations.setControllerParameter(ctrl, "a", 5);
		assertEquals(5, ctrl.a);
	}
	
	@Test(expected = RuntimeZootException.class)
	public void shouldThrowIfParameterWasNotFound()
	{
		ControllerAnnotations.setControllerParameter(new Mock2Controller(), "notExistingParam", true);
	}
	
	@Test(expected = RuntimeZootException.class)
	public void shouldThrowIfWrongValueIsAssignedToParameter()
	{
		ControllerAnnotations.setControllerParameter(new Mock2Controller(), "a", "stringValue");
	}
	
	@Test
	public void shouldGetControllerDebugFields()
	{
		List<Field> fields = ControllerAnnotations.getControllerDebugFields(new ControllerWithDebugFieldsMock()); 
		assertEquals(3, fields.size());
		
		assertEquals("intDebugField", fields.get(0).getName());
		assertEquals(int.class, fields.get(0).getType());
		
		assertEquals("floatDebugField", fields.get(1).getName());
		assertEquals(float.class, fields.get(1).getType());
		
		assertEquals("boolDebugField", fields.get(2).getName());
		assertEquals(boolean.class, fields.get(2).getType());
	}
	
	@Test
	public void shouldGetControllerParameterFields()
	{
		List<Field> fields = ControllerAnnotations.getControllerParameterFields(new Mock2Controller());
		assertEquals(3, fields.size());
		
		assertEquals("a", fields.get(0).getName());
		assertEquals(int.class, fields.get(0).getType());
		
		assertEquals("b", fields.get(1).getName());
		assertEquals(float.class, fields.get(1).getType());
		
		assertEquals("c", fields.get(2).getName());
		assertEquals(String.class, fields.get(2).getType());
	}
}
