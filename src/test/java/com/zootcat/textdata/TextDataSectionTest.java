package com.zootcat.textdata;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.math.Vector2;

public class TextDataSectionTest
{
    private TextDataSection section;
    
    @Before
    public void setup()
    {
        section = new TextDataSection();
    }
    
    @Test
    public void ctorTest()
    {
        assertTrue(section.getAllKeys().isEmpty());
        assertTrue(section.getAllValues().isEmpty());
        assertEquals("", section.getStartToken());
        assertEquals("", section.getEndToken());
        assertEquals(null, section.getFile());
        assertEquals("", section.getString("xyz", ""));
    }
    
    @Test
    public void getTest()
    {
    	section.set("MyKey", "value");
    	assertEquals("value", section.get("MyKey"));
    	assertEquals(null, section.get("NotExistingKey"));
    }
    
    @Test
    public void setTest()
    {
    	assertNull(section.get("NotExistingKey"));
    	section.set("MyString", "str");
    	
    	assertEquals("str", section.get("MyString"));
    	assertEquals("str", section.get("MYSTRING"));
    	assertEquals("str", section.get("mystring"));
    }
    
    @Test
    public void getIntTest()
    {
    	section.set("MyInt", "123");
    	assertEquals(123, section.getInt("MyInt", 0));
    	assertEquals(256, section.getInt("NotExisting", 256));
    }

    @Test
    public void getBooleanTest()
    {
    	section.set("MyBool", "true");
    	assertEquals(true, section.getBoolean("MyBool", false));
    	assertEquals(true, section.getBoolean("NotExisting", true));
    }
        
    @Test
    public void setFileTest()
    {
    	File file = new File(".");
        section.setFile(file);
        assertEquals(file, section.getFile());
    }
    
    @Test
    public void setTokensTest()
    {
        section.setEndToken("endToken");
        assertEquals("endToken", section.getEndToken());
        
        section.setStartToken("startToken");
        assertEquals("startToken", section.getStartToken());
    }
   
    @Test
    public void getVectorsTest()
    {
    	section.set("Vectors", "1,2; 3,4; 5,6; 7,8; 9,10");    	
    	
    	Vector2[] vectors = section.getVectors("Vectors");    	
    	assertNotNull(vectors);
    	assertEquals(5, vectors.length);
    	assertEquals(1, vectors[0].x, 0.0f);
    	assertEquals(2, vectors[0].y, 0.0f);
    	assertEquals(3, vectors[1].x, 0.0f);
    	assertEquals(4, vectors[1].y, 0.0f);
    	assertEquals(5, vectors[2].x, 0.0f);
    	assertEquals(6, vectors[2].y, 0.0f);
    	assertEquals(7, vectors[3].x, 0.0f);
    	assertEquals(8, vectors[3].y, 0.0f);
    	assertEquals(9, vectors[4].x, 0.0f);
    	assertEquals(10, vectors[4].y, 0.0f);
    }
    
    @Test
    public void getVectorsShouldReturnEmptyArrayOnIfKeyIsNotPresentTest()
    {
    	assertEquals(0, section.getVectors("NotExistingVectors").length);
    }
    
    @Test
    public void getVectorsShouldReturnEmptyArrayOnIfValueIsEmptyTest()
    {
    	section.set("EmptyVectors", "");
    	assertEquals(0, section.getVectors("NotExistingVectors").length);
    }    
    
    @Test
    public void getIntArrayTest()
    {
    	section.set("IntArray", "1;2;3;4;5;6;7;8;9;10");
    	
    	int[] resultArray = section.getIntArray("IntArray");
    	assertNotNull(resultArray);
    	assertEquals(10, resultArray.length);
    	
    	int[] expectedArray = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    	assertArrayEquals(expectedArray, resultArray);
    }
    
    @Test
    public void getIntArrayShouldReturnEmptyArrayIfKeyIsNotPresentTest()
    {
    	assertEquals(0, section.getVectors("NotExistingIntArray").length);
    }
    
    @Test
    public void getIntArrayShouldReturnEmptyArrayIfKeyIsEmptyTest()
    {
    	section.set("EmptyIntArray", "");
    	assertEquals(0, section.getVectors("EmptyIntArray").length);
    }
}
