package com.zootcat.controllers;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ControllerComparatorTest
{
	private static final int HIGHEST = 100;
	private static final int MEDIUM = 50;
	private static final int LOWEST = 0;
	
	@Mock private Controller ctrl1;
	@Mock private Controller ctrl2;
	@Mock private Controller ctrl3;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		when(ctrl1.getPriority()).thenReturn(HIGHEST);
		when(ctrl2.getPriority()).thenReturn(MEDIUM);
		when(ctrl3.getPriority()).thenReturn(LOWEST);
	}
	
	@Test
	public void shouldProvideDescedingOrder()
	{
		assertTrue(ControllerComparator.Instance.compare(ctrl1, ctrl2) < 0);
		assertTrue(ControllerComparator.Instance.compare(ctrl2, ctrl3) < 0);
		assertTrue(ControllerComparator.Instance.compare(ctrl1, ctrl3) < 0);		
		assertTrue(ControllerComparator.Instance.compare(ctrl2, ctrl1) > 0);
		assertTrue(ControllerComparator.Instance.compare(ctrl3, ctrl2) > 0);
		assertTrue(ControllerComparator.Instance.compare(ctrl3, ctrl1) > 0);
	}
	
	@Test
	public void shouldProvideEqualityWhenComparingTheSamePriority()
	{
		assertTrue(ControllerComparator.Instance.compare(ctrl1, ctrl1) == 0);
		assertTrue(ControllerComparator.Instance.compare(ctrl2, ctrl2) == 0);
		assertTrue(ControllerComparator.Instance.compare(ctrl3, ctrl3) == 0);
	}
	
	
}
