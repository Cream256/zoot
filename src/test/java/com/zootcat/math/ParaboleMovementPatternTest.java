package com.zootcat.math;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.math.Vector2;

public class ParaboleMovementPatternTest
{
	//parabole constructed similiar to http://www.matmana6.pl/tablice_matematyczne/liceum/funkcja_kwadratowa/75-wyznaczanie_wzoru_funkcji_kwadratowej
	private static final Vector2 PEAK = new Vector2(3.0f, -6.0f);
	private static final Vector2 POINT_ON_PARABOLE = new Vector2(0.0f, 3.0f);
	
	private ParaboleMovementPattern parabole;
	
	@Before
	public void setup()
	{
		parabole = new ParaboleMovementPattern(PEAK, POINT_ON_PARABOLE);
	}
	
	@Test
	public void shuoldReturnPeakPoint()
	{
		assertEquals(PEAK, parabole.at(PEAK.x));
	}
	
	@Test
	public void shouldReturnPointOnParabole()
	{
		assertEquals(POINT_ON_PARABOLE.x, parabole.at(POINT_ON_PARABOLE.x).x, 0.0f);
		assertEquals(POINT_ON_PARABOLE.y, parabole.at(POINT_ON_PARABOLE.x).y, 0.01f);
	}
	
	@Test
	public void shouldReturnProperPoints()
	{
		assertEquals(-2.0f, parabole.at(1.0f).y, 0.0f);
		assertEquals(-5.0f, parabole.at(2.0f).y, 0.0f);
		assertEquals(-2.0f, parabole.at(5.0f).y, 0.0f);
		assertEquals(3.0f, parabole.at(6.0f).y, 0.0f);		
	}
}
