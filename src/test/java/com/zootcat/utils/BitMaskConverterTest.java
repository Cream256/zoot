package com.zootcat.utils;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.zootcat.exceptions.RuntimeZootException;

public class BitMaskConverterTest 
{
	private BitMaskConverter converter;
	
	@Before
	public void setup()
	{
		converter = BitMaskConverter.Instance;
		converter.clear();
	}
	
	@After
	public void tearDown()
	{
		converter.clear();
	}
	
	@Test
	public void shouldProduceValidValuesTest()
	{
		assertEquals(0x0001, converter.fromString("first"));
		assertEquals(0x0002, converter.fromString("second"));
		assertEquals(0x0004, converter.fromString("third"));
		assertEquals(0x0008, converter.fromString("fourth"));
	}
	
	@Test
	public void shouldGiveTheSameResultForTheSamStringTest()
	{
		assertEquals(0x0001, converter.fromString("first"));
		assertEquals(0x0002, converter.fromString("second"));
		assertEquals(0x0001, converter.fromString("first"));
		assertEquals(0x0002, converter.fromString("second"));
		assertEquals(0x0004, converter.fromString("third"));
		assertEquals(0x0004, converter.fromString("third"));
	}
	
	@Test
	public void shouldBeCaseSensitiveTest()
	{
		assertEquals(0x0001, converter.fromString("text"));
		assertEquals(0x0002, converter.fromString("TEXT"));
		assertEquals(0x0004, converter.fromString("TexT"));
	}
	
	@Test(expected = RuntimeZootException.class)
	public void shouldThrowWhenCalculatedValueIsGreaterThanShortMaxValueTest()
	{
		for(int i = 0; i < 15; ++i)
		{
			assertTrue(Short.MAX_VALUE > converter.fromString(String.valueOf(i)));
		}
		converter.fromString("this should throw");
	}
	
	@Test
	public void shouldConvertToMaskContainingMultiplyValuesTest()
	{
		//given
		short mask1 = converter.fromString("A");		
		short mask2 = converter.fromString("B");
		short mask3 = converter.fromString("C");
		
		//when
		short mask4 = converter.fromString("A | B | C");
		
		//then
		assertEquals(mask1 | mask2 | mask3, mask4);
	}
	
	@Test
	public void shouldReturnMaskThatCollidesWithAllWhenNoParamsAreGivenTest()
	{
		assertEquals(BitMaskConverter.MASK_COLLIDE_WITH_ALL, converter.fromString(null));
		assertEquals(BitMaskConverter.MASK_COLLIDE_WITH_ALL, converter.fromString(""));
	}
}
