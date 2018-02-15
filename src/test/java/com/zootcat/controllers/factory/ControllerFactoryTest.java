package com.zootcat.controllers.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.zootcat.controllers.factory.mocks.AbstractController;
import com.zootcat.controllers.factory.mocks.EnumParam;
import com.zootcat.controllers.factory.mocks.InterfaceOnlyController;
import com.zootcat.controllers.factory.mocks.MockDerivedController;
import com.zootcat.controllers.factory.mocks.MockEmptyController;
import com.zootcat.controllers.factory.mocks.MockEnumParamController;
import com.zootcat.controllers.factory.mocks.MockGlobalParamController;
import com.zootcat.controllers.factory.mocks.MockPrimitiveParamsController;
import com.zootcat.controllers.factory.mocks.MockRequiredParamController;
import com.zootcat.controllers.factory.mocks.SimpleController;
import com.zootcat.exceptions.RuntimeZootException;

public class ControllerFactoryTest
{
    private ControllerFactory factory;
	
	@Before
    public void setup()
    {
		factory = new ControllerFactory();
    }
	
	@Test
	public void ctorsTest()
	{
		assertTrue("Default controllers should be added by default", factory.getSize() > 0);
		assertTrue("Default controllers should be added", new ControllerFactory(true).getSize() > 0);
		assertTrue("Default controllers should not be added", new ControllerFactory(false).getSize() == 0);
	}
	
	@Test
	public void addTest()
	{
		ControllerFactory factory = new ControllerFactory(false);
		
		factory.add(InterfaceOnlyController.class);
		assertEquals("Should not add interface", 0, factory.getSize());
		
		factory.add(AbstractController.class);
		assertEquals("Should not add abstract class", 0, factory.getSize());
		
		factory.add(SimpleController.class);
		assertEquals("Should add valid controller", 1, factory.getSize());
	}
	
	@Test
	public void getTest()
	{
		factory.add(SimpleController.class);
		
		assertEquals(SimpleController.class, factory.get("SimpleController"));
		assertEquals("Should recognize name without suffix", SimpleController.class, factory.get("Simple"));
		
		assertEquals("Should be case insensitive", SimpleController.class, factory.get("SIMPLE"));
		assertEquals("Should be case insensitive", SimpleController.class, factory.get("simple"));
		assertEquals("Should be case insensitive", SimpleController.class, factory.get("simplecontroller"));
		assertEquals("Should be case insensitive", SimpleController.class, factory.get("SIMPLECONTROLLER"));
	}
	
	@Test
	public void containsTest()
	{
		ControllerFactory factory = new ControllerFactory(false);		
		assertFalse(factory.contains(SimpleController.class));
		assertFalse("Should be case insensitive", factory.contains("SimpleController"));
		assertFalse("Should be case insensitive", factory.contains("SIMPLECONTROLLER"));
		assertFalse("Should be case insensitive", factory.contains("simplecontroller"));
		assertFalse("Should be case insensitive", factory.contains("Simple"));
		assertFalse("Should be case insensitive", factory.contains("simple"));
		assertFalse("Should be case insensitive", factory.contains("SIMPLE"));
		
		factory.add(SimpleController.class);
		assertTrue(factory.contains(SimpleController.class));		
		assertTrue("Should be case insensitive", factory.contains("SimpleController"));
		assertTrue("Should be case insensitive", factory.contains("SIMPLECONTROLLER"));
		assertTrue("Should be case insensitive", factory.contains("simplecontroller"));
		assertTrue("Should be case insensitive", factory.contains("Simple"));
		assertTrue("Should be case insensitive", factory.contains("simple"));
		assertTrue("Should be case insensitive", factory.contains("SIMPLE"));
	}
	
	@Test
	public void addFromPackageTest()
	{
		ControllerFactory factory = new ControllerFactory(false);
				
		factory.addFromPackage("com.zootcat.controllers.factory.mocks", false);
		assertEquals("Should only include package without subpackages", 11, factory.getSize());
		
		factory.addFromPackage("com.zootcat.controllers.factory.mocks", true);
		assertEquals("Should include package and subpackages", 12, factory.getSize());
	}
	
	@Test
    public void controllerWithNoParamsTest()
    {
        //given
        ControllerFactory factory = new ControllerFactory(false);
        
        //then
        assertEquals(0, factory.getSize());
        
        //when
        factory.add(MockEmptyController.class);
        
        //then
        assertEquals(1, factory.getSize());
        
        //when
        Object created = factory.create(MockEmptyController.class.getSimpleName(), new HashMap<String, Object>());        
        
        //then
        assertTrue(created instanceof MockEmptyController);
        assertEquals(MockEmptyController.class, factory.get(MockEmptyController.class.getSimpleName()));
    }
    
	@Test
	public void controllerWithPrimitiveParamsTest()
	{
		//given
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("intParam", 1);
		params.put("floatParam", 2.0f);
		params.put("doubleParam", 3.0);
		params.put("stringParam", "test");
		params.put("booleanParam", true);
		
		//when
		factory.add(MockPrimitiveParamsController.class);
		Object created = factory.create(MockPrimitiveParamsController.class.getSimpleName(), params);
		
		//then
		assertNotNull(created);
		assertTrue(created instanceof MockPrimitiveParamsController);
		
		MockPrimitiveParamsController ctrl = (MockPrimitiveParamsController)created;
		assertEquals(1, ctrl.getIntParam());
		assertEquals(2.0f, ctrl.getFloatParam(), 0.0f);
		assertEquals(3.0, ctrl.getDoubleParam(), 0.0f);
		assertEquals("test", ctrl.getStringParam());
		assertEquals(true, ctrl.getBooleanParam());
	}
    
	@Test
	public void requiredParamShouldBeProcessedCorrectlyTest()
	{
		//given
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("optional", 1);
		params.put("required", 2);
		
		//when
		factory.add(MockRequiredParamController.class);
		Object created = factory.create(MockRequiredParamController.class.getSimpleName(), params);
		
		//then
		assertNotNull(created);
		assertTrue(created instanceof MockRequiredParamController);
		
		MockRequiredParamController ctrl = (MockRequiredParamController)created;
		assertEquals(1, ctrl.getOptional());
		assertEquals(2, ctrl.getRequired());
	}
	
	@Test(expected = RuntimeZootException.class)
	public void requiredParamShouldThrowIfNotPresentTest()
	{
		//given
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("optional", 1);
		
		//when
		factory.add(MockRequiredParamController.class);
		
		//then
		factory.create(MockRequiredParamController.class.getSimpleName(), params);
	}
    
	@Test
	public void globalParamShouldBeProcessedCorrectlyTest()
	{
		//given
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("local", 1);
		
		//when
		factory.add(MockGlobalParamController.class);
		factory.addGlobalParameter("global", 2);
		Object created = factory.create(MockGlobalParamController.class.getSimpleName(), params);
		
		//then
		assertNotNull(created);
		assertTrue(created instanceof MockGlobalParamController);
		
		MockGlobalParamController ctrl = (MockGlobalParamController)created;
		assertEquals(1, ctrl.getLocal());
		assertEquals(2, ctrl.getGlobal());		
	}
	
	@Test(expected = RuntimeZootException.class)
	public void globalParamShouldThrowIfNotPresentTest()
	{
		//given
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("local", 1);
		
		//when
		factory.add(MockGlobalParamController.class);
		factory.create(MockGlobalParamController.class.getSimpleName(), params);		
	}
    
  
    @Test(expected=RuntimeZootException.class)
    public void shouldThrowOnInvalidClassName()
    {
        ControllerFactory factory = new ControllerFactory();
        factory.create("ClassNotInContainer", new HashMap<String, Object>());
    }
    
    @Test
    public void addGlobalParameterShouldNotAddDuplicatesTest()
    {
    	//given
    	Integer integerParam = new Integer(1);
    	
    	//when
    	factory.addGlobalParameter("Int", integerParam);
    	factory.addGlobalParameter("Int", integerParam);
    	
    	//then
    	assertEquals(1, factory.getGlobalParameters().size());
    	assertTrue(factory.getGlobalParameters().containsKey("Int"));
    }
    
    @Test
    public void removeGlobalParameterTest()
    {
    	//when
    	factory.addGlobalParameter("Int", 1);
    	factory.addGlobalParameter("Bool", true);
    	
    	//then
    	assertEquals(2, factory.getGlobalParameters().size());
    	
    	//when
    	factory.removeGlobalParameter("Int");
    	
    	//then
    	assertEquals(1, factory.getGlobalParameters().size());
    	assertEquals(true, factory.getGlobalParameters().get("Bool"));
    	
    	//when
    	factory.removeGlobalParameter("Bool");
    	
    	//then
    	assertEquals(0, factory.getGlobalParameters().size());
    }
    
    @Test
    public void enumParameterTest()
    {
		//given
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("enumParam", EnumParam.ENUM_VALUE_2.toString());
		
		//when
		factory.add(MockEnumParamController.class);
		Object created = factory.create(MockEnumParamController.class.getSimpleName(), params);
		
		//then
		assertNotNull(created);
		assertTrue(created instanceof MockEnumParamController);
		
		MockEnumParamController ctrl = (MockEnumParamController)created;
		assertEquals(EnumParam.ENUM_VALUE_2, ctrl.getEnum());    	
    }
    
    @Test
    public void nullParameterShouldBeSetToDefaultTest()
    {
		//given
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("intParam", null);
		
		//when
		factory.add(MockPrimitiveParamsController.class);
		Object created = factory.create(MockPrimitiveParamsController.class.getSimpleName(), params);
		
		//then
		assertNotNull(created);
		assertTrue(created instanceof MockPrimitiveParamsController);
		
		MockPrimitiveParamsController ctrl = (MockPrimitiveParamsController)created;
		assertEquals(0, ctrl.getIntParam());
    }
    
    @Test
    public void parameterInheritanceTest()
    {
		//given
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("baseParam", 1);
		params.put("derivedParam", 2);
		
		//when
		factory.add(MockDerivedController.class);
		Object created = factory.create(MockDerivedController.class.getSimpleName(), params);
		
		//then
		assertNotNull(created);
		assertTrue(created instanceof MockDerivedController);
		
		MockDerivedController ctrl = (MockDerivedController)created;
		assertEquals(1, ctrl.getBaseParam());
		assertEquals(2, ctrl.getDerivedParam());
    }
}
