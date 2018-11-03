package com.zootcat.controllers.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.Test;

import com.zootcat.controllers.factory.mocks.ControllerWithDebugFieldsMock;
import com.zootcat.controllers.factory.mocks.Mock2Controller;
import com.zootcat.controllers.factory.mocks.MockGlobalParamController;
import com.zootcat.controllers.factory.mocks.MockOverrideRequiredParamController;
import com.zootcat.controllers.factory.mocks.MockRequiredParamController;
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
	public void shouldReturnValidGlobalParameters()
	{
		List<ControllerParameter> params = ControllerAnnotations.getControllerParameterFields(new MockGlobalParamController());
		
		assertEquals(2, params.size());
		assertFalse(params.get(0).global);
		assertTrue(params.get(1).global);
	}
	
	@Test
	public void shouldReturnValidRequiredParameters()
	{
		List<ControllerParameter> params = ControllerAnnotations.getControllerParameterFields(new MockRequiredParamController());
		
		assertEquals(2, params.size());
		assertFalse(params.get(0).required);
		assertTrue(params.get(1).required);
	}
	
	@Test
	public void shouldGetControllerParameterFieldsInOrder()
	{
		List<ControllerParameter> params = ControllerAnnotations.getControllerParameterFields(new Mock2Controller());
		assertEquals(3, params.size());
		
		assertEquals("a", params.get(0).field.getName());
		assertEquals(int.class, params.get(0).field.getType());
		
		assertEquals("b", params.get(1).field.getName());
		assertEquals(float.class, params.get(1).field.getType());
		
		assertEquals("c", params.get(2).field.getName());
		assertEquals(String.class, params.get(2).field.getType());
	}
		
	@Test
	public void shouldOverrideParameterFields()
	{
		List<ControllerParameter> params = ControllerAnnotations.getControllerParameterFields(new MockOverrideRequiredParamController());
		assertEquals(2, params.size());
		
		assertEquals("required", params.get(0).field.getName());
		assertEquals(int.class, params.get(0).field.getType());
		assertFalse(params.get(0).global);
		assertFalse(params.get(0).required);
		
		assertEquals("optional", params.get(1).field.getName());
		assertEquals(int.class, params.get(1).field.getType());
		assertFalse(params.get(1).global);
		assertFalse(params.get(1).required);
	}
}
