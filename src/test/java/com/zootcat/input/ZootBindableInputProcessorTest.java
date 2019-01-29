package com.zootcat.input;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.zootcat.exceptions.RuntimeZootException;

public class ZootBindableInputProcessorTest 
{
	private int keyUpEvents;
	private int keyDownEvents;	
	private int touchUpEvents;
	private int touchDownEvents;	
	ZootBindableInputProcessor processor;
	
	@Before
	public void setup()
	{
		keyUpEvents = 0;
		keyDownEvents = 0;
		touchUpEvents = 0;
		touchDownEvents = 0;
		processor = new ZootBindableInputProcessor();
	}
	
	@Test
	public void hasKeyDownBindingTest()
	{
		//then
		assertFalse(processor.hasKeyDownBinding(0));
		assertFalse(processor.hasKeyDownBinding(1));
		
		//when
		processor.bindKeyDown(0, () -> true);
		
		//then
		assertTrue(processor.hasKeyDownBinding(0));
		assertFalse(processor.hasKeyDownBinding(1));
	}
	
	@Test
	public void hasKeyUpBindingTest()
	{		
		//then
		assertFalse(processor.hasKeyUpBinding(0));
		assertFalse(processor.hasKeyUpBinding(1));
		
		//when
		processor.bindKeyUp(0, () -> true);
		
		//then
		assertTrue(processor.hasKeyUpBinding(0));
		assertFalse(processor.hasKeyUpBinding(1));
	}
	
	@Test
	public void processorShouldProperlyHandleAllCommandMethodsTest()
	{		
		//when
		processor.bindKeyUp(0, () -> { ++keyUpEvents; return true; } );
		processor.bindKeyDown(0, () -> { ++keyDownEvents; return true; } );		
		processor.bindTouchUp(1, (sx, sy, p) -> { ++touchUpEvents; return true; });
		processor.bindTouchDown(1, (sx, sy, p) -> { ++touchDownEvents; return true; });
		
		//then
		assertEquals("Down key event should not execute right after binding", 0, keyDownEvents);
		assertEquals("Up key event should not execute right after binding", 0, keyUpEvents);
		assertEquals("Down touch event should not execute right after binding", 0, touchDownEvents);
		assertEquals("Up touch event should not execute right after binding", 0, touchUpEvents);
		
		//when
		processor.keyDown(0);
		processor.touchDown(0, 0, 0, 1);
		
		//then
		assertEquals(1, keyDownEvents);
		assertEquals(0, keyUpEvents);
		assertEquals(1, touchDownEvents);
		assertEquals(0, touchUpEvents);
		
		//when
		processor.keyUp(0);
		processor.touchUp(0, 0, 0, 1);
		
		//then
		assertEquals(1, keyDownEvents);
		assertEquals(1, keyUpEvents);
		assertEquals(1, touchDownEvents);
		assertEquals(1, touchUpEvents);
		
		//when
		processor.keyDown(2);
		processor.keyUp(2);
		processor.touchDown(0, 0, 0, 2);
		processor.touchUp(0, 0, 0, 2);
		
		//then nothing changes
		assertEquals(1, keyDownEvents);
		assertEquals(1, keyUpEvents);
		assertEquals(1, touchDownEvents);
		assertEquals(1, touchUpEvents);
	}
	
	@Test(expected = RuntimeZootException.class)
	public void bindKeyDownShouldThrowIfKeycodeAlreadyBindedTest()
	{		
		//when
		processor.bindKeyDown(0, () -> true);
		
		//then
		assertTrue(processor.hasKeyDownBinding(0));
		
		//when
		processor.bindKeyDown(0, () -> true);
	}
	
	@Test(expected = RuntimeZootException.class)
	public void bindKeyUpShouldThrowIfKeycodeAlreadyBindedTest()
	{		
		//when
		processor.bindKeyUp(0, () -> true);
		
		//then
		assertTrue(processor.hasKeyUpBinding(0));
		
		//when
		processor.bindKeyUp(0, () -> true);
	}
	
	@Test(expected = RuntimeZootException.class)
	public void bindtouchUpShouldThrowIfButtoncodeAlreadyBindedTest()
	{		
		//when
		processor.bindTouchUp(0, (sx, sy, p) -> true);
		
		//then
		assertTrue(processor.hasTouchUpBinding(0));
		
		//when
		processor.bindTouchUp(0, (sx, sy, p) -> true);
	}
	
	@Test(expected = RuntimeZootException.class)
	public void bindtouchDownShouldThrowIfButtoncodeAlreadyBindedTest()
	{		
		//when
		processor.bindTouchDown(0, (sx, sy, p) -> true);
		
		//then
		assertTrue(processor.hasTouchDownBinding(0));
		
		//when
		processor.bindTouchDown(0, (sx, sy, p) -> true);
	}
	
	@Test
	public void bindDownCommandTest()
	{	
		//when
		processor.bindKeyDown(0, () -> true);
		
		//then
		assertTrue(processor.keyDown(0));
		assertFalse(processor.keyUp(0));
		assertFalse(processor.keyTyped('0'));
	}
		
	@Test
	public void bindUpCommandTest()
	{		
		//when
		processor.bindKeyUp(0, () -> true);
		
		//then
		assertTrue(processor.keyUp(0));
		assertFalse(processor.keyDown(0));
		assertFalse(processor.keyTyped('0'));
	}
	
	@Test
	public void hasTouchDownBindingTest()
	{
		//then
		assertFalse(processor.hasTouchDownBinding(0));
		assertFalse(processor.hasTouchDownBinding(1));
		
		//when
		processor.bindTouchDown(0, (sx, sy, p) -> true);
		
		//then
		assertTrue(processor.hasTouchDownBinding(0));
		assertFalse(processor.hasTouchDownBinding(1));
	}
	
	@Test
	public void hasTouchUpBindingTest()
	{		
		//then
		assertFalse(processor.hasTouchUpBinding(0));
		assertFalse(processor.hasTouchUpBinding(1));
		
		//when
		processor.bindTouchUp(0, (sx, sy, p) -> true);
		
		//then
		assertTrue(processor.hasTouchUpBinding(0));
		assertFalse(processor.hasTouchUpBinding(1));
	}
	
}
