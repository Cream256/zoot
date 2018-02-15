package com.zootcat.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

public class ArgumentParserTest
{                
    @Test
    public void noArgsTest()
    {
        Map<String, Object> result = ArgumentParser.parse(new String[0]);        
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
        
    @Test
    public void booleanTrueTest()
    {
    	Map<String, Object> result = ArgumentParser.parse(new String[]{"myBool = true"});        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get("myBool") instanceof Boolean);
        assertEquals(true, result.get("myBool"));
    }
    
    @Test
    public void booleanFalseTest()
    {
    	Map<String, Object> result = ArgumentParser.parse(new String[]{"myBool = false"});        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get("myBool") instanceof Boolean);
        assertEquals(false, result.get("myBool"));
    }
    
    public void emptyStringArgShouldNotThrowTest()
    {
    	ArgumentParser.parse(new String[]{"   "});        
    }
    
    @Test
    public void stringArgTest()
    {                
        Map<String, Object> result = ArgumentParser.parse(new String[]{"str = myString"});        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get("str") instanceof String);
        assertEquals("myString", result.get("str"));
    }

    @Test
    public void stringWithSpacesTest()
    {                
        Map<String, Object> result = ArgumentParser.parse(new String[]{"str = '  '"});        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get("str") instanceof String);
        assertEquals("  ", result.get("str"));
    }   
                    
    @Test
    public void integerArgTest() 
    {
        Map<String, Object> result = ArgumentParser.parse(new String[]{"myInt = 123"});
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get("myInt") instanceof Integer);
        assertEquals(123, result.get("myInt"));
    }
    
    @Test
    public void negativeIntegerArgTest() 
    {
        Map<String, Object> result = ArgumentParser.parse(new String[]{"myInt = -123"});
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get("myInt") instanceof Integer);
        assertEquals(-123, result.get("myInt"));
    }
    
    @Test
    public void positiveIntegerArgTest() 
    {
        Map<String, Object> result = ArgumentParser.parse(new String[]{"myInt = +123"});
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get("myInt") instanceof Integer);
        assertEquals(123, result.get("myInt"));
    }
        
    @Test
    public void floatArgTest() 
    {
        Map<String, Object> result = ArgumentParser.parse(new String[]{"myFloat = 0.123f"});
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get("myFloat") instanceof Float);
        assertEquals(new Float(0.123f), result.get("myFloat"));
    }
    
    @Test
    public void floatNoLeadingZeroArgTest() 
    {
        Map<String, Object> result = ArgumentParser.parse(new String[]{"myFloat = .123f"});
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get("myFloat") instanceof Float);
        assertEquals(Float.valueOf(0.123f), result.get("myFloat"));
    }
    
    @Test
    public void floatNegativeArgTest() 
    {
        Map<String, Object> result = ArgumentParser.parse(new String[]{"myFloat = -0.123f"});
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get("myFloat") instanceof Float);
        assertEquals(Float.valueOf(-0.123f), result.get("myFloat"));
    }
    
    @Test
    public void doubleArgTest() 
    {
        Map<String, Object> result = ArgumentParser.parse(new String[]{"myDouble = 0.123d"});
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get("myDouble") instanceof Double);
        assertEquals(new Double(0.123), result.get("myDouble"));
    }
                   
    @Test
    public void multiplyArgumentsTest() 
    {        
        String[] arguments = {"arg1=@player1", "arg2=@little bunny", "arg3 = 'DUH'", "arg4=3.14f", "arg5 = 6.14d"};
        Map<String, Object> result = ArgumentParser.parse(arguments); 
        assertNotNull(result);
        assertEquals(5, result.size());
        assertEquals("@player1", result.get("arg1"));
        assertEquals("@little bunny", result.get("arg2"));
        assertEquals("DUH", result.get("arg3"));
        assertEquals(Float.valueOf(3.14f), result.get("arg4"));
        assertEquals(Double.valueOf(6.14), result.get("arg5"));
    }
}

