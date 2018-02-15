package com.zootcat.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.physics.ZootBodyShape;

public class ZootUtilsTest 
{
    @Test
    public void truncTest()
    {
        assertEquals(0, ZootUtils.trunc(0.0f));
        assertEquals(0, ZootUtils.trunc(0.99f));
        assertEquals(0, ZootUtils.trunc(-0.99f));
        assertEquals(1, ZootUtils.trunc(1.00f));
        assertEquals(-1, ZootUtils.trunc(-1.00f));
        assertEquals(1, ZootUtils.trunc(1.99f));
        assertEquals(-1, ZootUtils.trunc(-1.99f));
        assertEquals(123, ZootUtils.trunc(123.45f));
    }
    
	@Test
	public void lerpTest()
	{
	    assertEquals(0.0f, ZootUtils.lerp(0.0f, 0.0f, 0.0f), 0.0f);
	    assertEquals(0.0f, ZootUtils.lerp(1.0f, 0.0f, 0.0f), 0.0f);
	    assertEquals(0.0f, ZootUtils.lerp(0.0f, 0.0f, 1.0f), 0.0f);
	    assertEquals(0.5f, ZootUtils.lerp(0.5f, 0.0f, 1.0f), 0.0f);
	    assertEquals(1.0f, ZootUtils.lerp(1.0f, 0.0f, 1.0f), 0.0f);
	    assertEquals(1.0f, ZootUtils.lerp(2.0f, 0.0f, 1.0f), 0.0f);
	    assertEquals(0.0f, ZootUtils.lerp(-1.0f, 0.0f, 1.0f), 0.0f);
	}
    
    @Test
	public void clipToRangeFloatTest()
	{
		assertEquals(0.0f, ZootUtils.clipToRange(0.0f, 0.0f, 0.0f), 0.01f);
		assertEquals(0.0f, ZootUtils.clipToRange(1.0f, 0.0f, 0.0f), 0.01f);
		assertEquals(1.0f, ZootUtils.clipToRange(1.0f, 1.0f, 2.0f), 0.01f);		
		assertEquals(-0.5f, ZootUtils.clipToRange(-0.5f, -0.5f, 0.0f), 0.01f);
		assertEquals(-0.5f, ZootUtils.clipToRange(-0.5f, -1.5f, 1.5f), 0.01f);		
		assertEquals(10.0f, ZootUtils.clipToRange(20.0f, 0.0f, 10.0f), 0.01f);
		assertEquals(-5.0f, ZootUtils.clipToRange(-8.0f, -5.0f, 5.0f), 0.01f);		
		assertEquals(0.0f, ZootUtils.clipToRange(0.0f, 0.0f, -5.0f), 0.01f);
		assertEquals(0.0f, ZootUtils.clipToRange(0.0f, 5.0f, 0.0f), 0.01f);
	}
	
	@Test
	public void clipToRangeIntTest()
	{
	    assertEquals(0, ZootUtils.clipToRange(0, 0, 0));
        assertEquals(0, ZootUtils.clipToRange(1, 0, 0));
        assertEquals(1, ZootUtils.clipToRange(1, 1, 2));     
        assertEquals(-5, ZootUtils.clipToRange(-5, -5, 0));
        assertEquals(-5, ZootUtils.clipToRange(-5, -15, 15));      
        assertEquals(10, ZootUtils.clipToRange(20, 0, 10));
        assertEquals(-5, ZootUtils.clipToRange(-8, -5, 5));
        assertEquals(0, ZootUtils.clipToRange(0, 0, -5));
        assertEquals(0, ZootUtils.clipToRange(0, 5, 0));
	}
	
	@Test
	public void unquoteStringTest()
	{
	    assertEquals(null, ZootUtils.unquoteString(null));
	    assertEquals(" ", ZootUtils.unquoteString(" "));
	    assertEquals("aBc", ZootUtils.unquoteString("aBc"));
	    assertEquals("zxc zxc", ZootUtils.unquoteString("zxc zxc"));
	    assertEquals("\"zxc zxc", ZootUtils.unquoteString("\"zxc zxc"));
	    assertEquals("zxc zxc\"", ZootUtils.unquoteString("zxc zxc\""));
	    assertEquals("quoted", ZootUtils.unquoteString("\"quoted\""));
	    assertEquals("'Singlequoted", ZootUtils.unquoteString("'Singlequoted"));
	    assertEquals("Singlequoted'", ZootUtils.unquoteString("Singlequoted'"));
	    assertEquals("Singlequoted", ZootUtils.unquoteString("'Singlequoted'"));
	}
	
	@Test
	public void wrapValueTest()
	{
		assertEquals(0, ZootUtils.wrapValue(0, 0, 0));
		assertEquals(50, ZootUtils.wrapValue(-51, -50, 50));
		assertEquals(-50, ZootUtils.wrapValue(51, -50, 50));
	}
	
	@Test
	public void inRangeTest()
	{
		assertTrue(ZootUtils.inRange(0.0f, 0.0f, 1.0f));
		assertTrue(ZootUtils.inRange(0.5f, 0.0f, 1.0f));
		assertTrue(ZootUtils.inRange(1.0f, 0.0f, 1.0f));
		assertFalse(ZootUtils.inRange(1.1f, 0.0f, 1.0f));
		assertFalse(ZootUtils.inRange(-0.1f, 0.0f, 1.0f));
	}
	
	@Test
	public void getWorkingDirectoryTest()
	{
	    File workingDir = new File(System.getProperty("user.dir"));
	    File workingDirFromUtil = new File(ZootUtils.getWorkingDirectory());
	    assertEquals(workingDir.getPath(), workingDirFromUtil.getPath());
	}
	
	@Test
	public void searchEnumTest()
	{
		assertEquals(ZootBodyShape.BOX, ZootUtils.searchEnum(ZootBodyShape.class, "BOX"));
		assertEquals(ZootBodyShape.BOX, ZootUtils.searchEnum(ZootBodyShape.class, "box"));
		assertEquals(ZootBodyShape.BOX, ZootUtils.searchEnum(ZootBodyShape.class, "BoX"));
		assertEquals(ZootBodyShape.CIRCLE, ZootUtils.searchEnum(ZootBodyShape.class, "CIRCLE"));
		assertEquals(ZootBodyShape.CIRCLE, ZootUtils.searchEnum(ZootBodyShape.class, "circle"));
		assertEquals(ZootBodyShape.CIRCLE, ZootUtils.searchEnum(ZootBodyShape.class, "CiRclE"));
	}
	
	@Test(expected=RuntimeZootException.class)
	public void searchEnumShouldThrowOnInvalidInput()
	{
		ZootUtils.searchEnum(ZootBodyShape.class, "invalid");
	}
	
	@Test
	public void randomTest()
	{
		final int retryCount = 10000;
		final int bounds[] = {-50, 50, 0, 100, 33, 33, 1, 2};		
		for(int i = 0; i < retryCount; ++i)
		{
			for(int boundIndex = 0; boundIndex < bounds.length / 2; boundIndex += 2)
			{
				int upperBound = bounds[boundIndex + 1];
				int lowerBound = bounds[boundIndex];
				int value = ZootUtils.random(lowerBound, upperBound); 
				assertTrue(value >= lowerBound && value <= upperBound);	
			}
		}
	}
}