package com.zootcat.input;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.zootcat.exceptions.RuntimeZootException;

public class ZootBindableInputProcessorTest 
{
	private int upEvents;
	private int downEvents;	
	ZootBindableInputProcessor processor;
	
	@Before
	public void setup()
	{
		upEvents = 0;
		downEvents = 0;
		processor = new ZootBindableInputProcessor();
	}
	
	@Test
	public void hasDownBindingTest()
	{
		//then
		assertFalse(processor.hasDownBinding(0));
		assertFalse(processor.hasDownBinding(1));
		
		//when
		processor.bindDown(0, () -> true);
		
		//then
		assertTrue(processor.hasDownBinding(0));
		assertFalse(processor.hasDownBinding(1));
	}
	
	@Test
	public void hasUpBindingTest()
	{		
		//then
		assertFalse(processor.hasUpBinding(0));
		assertFalse(processor.hasUpBinding(1));
		
		//when
		processor.bindUp(0, () -> true);
		
		//then
		assertTrue(processor.hasUpBinding(0));
		assertFalse(processor.hasUpBinding(1));
	}
	
	@Test
	public void processorShouldProperlyHandleAllCommandMethodsTest()
	{		
		//when
		processor.bindUp(0, () -> { ++upEvents; return true; } );
		processor.bindDown(0, () -> { ++downEvents; return true; } );		
		
		//then
		assertEquals("Down event should not execute right after binding", 0, downEvents);
		assertEquals("Up event should not execute right after binding", 0, upEvents);
		
		//when
		processor.keyDown(0);
		
		//then
		assertEquals(1, downEvents);
		assertEquals(0, upEvents);
		
		//when
		processor.keyUp(0);
		
		//then
		assertEquals(1, downEvents);
		assertEquals(1, upEvents);
		
		//when
		processor.keyDown(1);
		processor.keyUp(1);
		
		//then nothing changes
		assertEquals(1, downEvents);
		assertEquals(1, upEvents);
	}
	
	@Test(expected = RuntimeZootException.class)
	public void bindDownShouldThrowIfKeycodeAlreadyBindedTest()
	{		
		//when
		processor.bindDown(0, () -> true);
		
		//then
		assertTrue(processor.hasDownBinding(0));
		
		//when
		processor.bindDown(0, () -> true);
	}
	
	@Test(expected = RuntimeZootException.class)
	public void bindUpShouldThrowIfKeycodeAlreadyBindedTest()
	{		
		//when
		processor.bindUp(0, () -> true);
		
		//then
		assertTrue(processor.hasUpBinding(0));
		
		//when
		processor.bindUp(0, () -> true);
	}
	
	@Test
	public void bindDownCommandTest()
	{	
		//when
		processor.bindDown(0, () -> true);
		
		//then
		assertTrue(processor.keyDown(0));
		assertFalse(processor.keyUp(0));
		assertFalse(processor.keyTyped('0'));
	}
		
	@Test
	public void bindUpCommandTest()
	{		
		//when
		processor.bindUp(0, () -> true);
		
		//then
		assertTrue(processor.keyUp(0));
		assertFalse(processor.keyDown(0));
		assertFalse(processor.keyTyped('0'));
	}
	
}
