package com.zootcat.input;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;

public class ZootInputManagerTest
{
	@Mock private InputMultiplexer multiplexer;
	private ZootInputManager inputManager;
			
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		inputManager = new ZootInputManager(multiplexer);
	}
	
	@Test
	public void shouldCreateDefaultManager()
	{
		inputManager = new ZootInputManager();
		assertEquals(0, inputManager.getProcessorsCount());
	}
	
	@Test
	public void shouldProcessKeyDown()
	{
		when(multiplexer.keyDown(Input.Keys.A)).thenReturn(true);
		when(multiplexer.keyDown(Input.Keys.B)).thenReturn(false);
		
		assertTrue(inputManager.keyDown(Input.Keys.A));
		assertFalse(inputManager.keyDown(Input.Keys.B));
	}
	
	@Test
	public void shouldProcessKeyUp()
	{
		when(multiplexer.keyUp(Input.Keys.A)).thenReturn(true);
		when(multiplexer.keyUp(Input.Keys.B)).thenReturn(false);
		
		assertTrue(inputManager.keyUp(Input.Keys.A));
		assertFalse(inputManager.keyUp(Input.Keys.B));
	}
	
	@Test
	public void shouldProcessKeyTyped()
	{
		when(multiplexer.keyTyped('A')).thenReturn(true);
		when(multiplexer.keyTyped('B')).thenReturn(false);
		
		assertTrue(inputManager.keyTyped('A'));
		assertFalse(inputManager.keyTyped('B'));
	}
	
	@Test
	public void shouldProcessTouchDown()
	{
		when(multiplexer.touchDown(1, 2, 3, 4)).thenReturn(true);
		when(multiplexer.touchDown(4, 3, 2, 1)).thenReturn(false);
		
		assertTrue(inputManager.touchDown(1, 2, 3, 4));
		assertFalse(inputManager.touchDown(4, 3, 2, 1));		
	}
	
	@Test
	public void shouldProcessTouchUp()
	{
		when(multiplexer.touchUp(1, 2, 3, 4)).thenReturn(true);
		when(multiplexer.touchUp(4, 3, 2, 1)).thenReturn(false);
		
		assertTrue(inputManager.touchUp(1, 2, 3, 4));
		assertFalse(inputManager.touchUp(4, 3, 2, 1));		
	}	
	
	@Test
	public void shouldProcessTouchDragged()
	{
		when(multiplexer.touchDragged(1, 2, 3)).thenReturn(true);
		when(multiplexer.touchDragged(4, 3, 2)).thenReturn(false);
		
		assertTrue(inputManager.touchDragged(1, 2, 3));
		assertFalse(inputManager.touchDragged(4, 3, 2));		
	}
	
	@Test
	public void shouldProcessScrolled()
	{
		when(multiplexer.scrolled(100)).thenReturn(true);
		when(multiplexer.scrolled(200)).thenReturn(false);
		
		assertTrue(inputManager.scrolled(100));
		assertFalse(inputManager.scrolled(200));
	}
	
	@Test
	public void shouldProcessMouseMovement()
	{
		when(multiplexer.mouseMoved(1, 2)).thenReturn(true);
		when(multiplexer.mouseMoved(3, 4)).thenReturn(false);
		
		assertTrue(inputManager.mouseMoved(1, 2));
		assertFalse(inputManager.mouseMoved(3, 4));
	}
	
	@Test
	public void shouldProcessPressedKeys()
	{
		when(multiplexer.keyDown(Input.Keys.A)).thenReturn(true);
		when(multiplexer.keyDown(Input.Keys.B)).thenReturn(true);
		when(multiplexer.keyDown(Input.Keys.C)).thenReturn(true);
		verify(multiplexer, times(0)).keyDown(Input.Keys.A);
		verify(multiplexer, times(0)).keyDown(Input.Keys.B);
		verify(multiplexer, times(0)).keyDown(Input.Keys.C);
		
		inputManager.keyDown(Input.Keys.A);
		inputManager.keyDown(Input.Keys.B);
		inputManager.keyDown(Input.Keys.C);
		verify(multiplexer).keyDown(Input.Keys.A);
		verify(multiplexer).keyDown(Input.Keys.B);
		verify(multiplexer).keyDown(Input.Keys.C);
		verify(multiplexer, times(0)).keyDown(Input.Keys.D);
		
		inputManager.processPressedKeys(1.0f);
		verify(multiplexer, times(2)).keyDown(Input.Keys.A);
		verify(multiplexer, times(2)).keyDown(Input.Keys.B);
		verify(multiplexer, times(2)).keyDown(Input.Keys.C);
		verify(multiplexer, times(0)).keyDown(Input.Keys.D);
	}
	
	@Test
	public void shouldClearPressedKeys()
	{
		when(multiplexer.keyDown(Input.Keys.A)).thenReturn(true);
		
		inputManager.keyDown(Input.Keys.A);
		inputManager.processPressedKeys(1.0f);		
		verify(multiplexer, times(2)).keyDown(Input.Keys.A);
		
		inputManager.clearPressedKeys();
		verifyNoMoreInteractions(multiplexer);
	}
	
	@Test
	public void shouldDetectWhenKeyWasPressed()
	{
		inputManager.keyDown(Input.Keys.A);		
		inputManager.processPressedKeys(1.0f);		
		verify(multiplexer, times(2)).keyDown(Input.Keys.A);
	}
	
	@Test
	public void shouldDetectWhenKeyWasUnpressed()
	{
		inputManager.keyDown(Input.Keys.A);
		inputManager.keyUp(Input.Keys.A);
		inputManager.processPressedKeys(1.0f);		
		verify(multiplexer).keyDown(Input.Keys.A);
	}
	
	@Test
	public void shouldAddProcessor()
	{
		InputProcessor processor = mock(InputProcessor.class);		
		inputManager.addProcessor(processor);
		
		verify(multiplexer).addProcessor(processor);
	}
	
	@Test
	public void shouldRemoveProcessor()
	{
		InputProcessor processor = mock(InputProcessor.class);		
		inputManager.removeProcessor(processor);
		
		verify(multiplexer).removeProcessor(processor);
	}
	
	@Test
	public void shouldReturnProcessorCount()
	{
		when(multiplexer.size()).thenReturn(123);
		assertEquals(123, inputManager.getProcessorsCount());
	}
	
	@Test
	public void shouldClearAllProcessors()
	{
		inputManager.clear();
		verify(multiplexer).clear();
	}
}
