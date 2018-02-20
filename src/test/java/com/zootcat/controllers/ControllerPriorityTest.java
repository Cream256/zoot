package com.zootcat.controllers;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ControllerPriorityTest
{	
	@Test
	public void shouldGiveProperPriorities()
	{
		assertTrue(ControllerPriority.Low.getValue() < ControllerPriority.Normal.getValue());
		assertTrue(ControllerPriority.Normal.getValue() < ControllerPriority.High.getValue());
		assertTrue(ControllerPriority.High.getValue() < ControllerPriority.Critical.getValue());
	}
}
