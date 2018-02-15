package com.zootcat.scene.tiled;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.zootcat.map.tiled.ZootTiledWorldScaleCalculator;

public class ZootTiledWorldScaleCalculatorTest 
{
	@Test
	public void calculateTest()
	{
		assertEquals(1.00f, ZootTiledWorldScaleCalculator.calculate(1.0f, 1.0f), 0.0f);		
		assertEquals(0.0053125f, ZootTiledWorldScaleCalculator.calculate(0.17f, 32.0f), 0.0f);
	}
}
