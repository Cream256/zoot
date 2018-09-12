package com.zootcat.controllers.input;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class InputProcessorControllerTest 
{
	private InputProcessorController ctrl;
	private InputProcessor inputProcessor;
	
	@Before
	public void setup()
	{
		inputProcessor = mock(InputProcessor.class);
		ctrl = new InputProcessorController(inputProcessor);
	}
	
	@Test
	public void shouldProcessKeyDown()
	{
		//given
		InputEvent event = mock(InputEvent.class);
		when(inputProcessor.keyDown(Input.Keys.UP)).thenReturn(true);
		when(inputProcessor.keyDown(Input.Keys.DOWN)).thenReturn(false);
		
		//then
		assertTrue(ctrl.keyDown(event, Input.Keys.UP));
		assertFalse(ctrl.keyDown(event, Input.Keys.DOWN));
	}
	
	@Test
	public void shouldProcessKeyUp()
	{
		//given
		InputEvent event = mock(InputEvent.class);
		when(inputProcessor.keyUp(Input.Keys.UP)).thenReturn(true);
		when(inputProcessor.keyUp(Input.Keys.DOWN)).thenReturn(false);
		
		//then
		assertTrue(ctrl.keyUp(event, Input.Keys.UP));
		assertFalse(ctrl.keyUp(event, Input.Keys.DOWN));
	}
	
	@Test
	public void shouldProcessKeyTyped()
	{
		//given
		InputEvent event = mock(InputEvent.class);
		when(inputProcessor.keyTyped('A')).thenReturn(true);
		when(inputProcessor.keyTyped('B')).thenReturn(false);
		
		//then
		assertTrue(ctrl.keyTyped(event, 'A'));
		assertFalse(ctrl.keyTyped(event, 'B'));
	}
	
	@Test
	public void shouldBeEnabledByDefault()
	{
		assertTrue(ctrl.isEnabled());
	}
	
	@Test
	public void shouldSetEnabled()
	{
		ctrl.setEnabled(false);
		assertFalse(ctrl.isEnabled());
		
		ctrl.setEnabled(true);
		assertTrue(ctrl.isEnabled());
	}
	
	@Test
	public void shouldNotProcessKeysWhenDisabled()
	{
		//given
		InputEvent event = mock(InputEvent.class);
		when(inputProcessor.keyDown(Input.Keys.UP)).thenReturn(true);
		when(inputProcessor.keyUp(Input.Keys.UP)).thenReturn(true);
		when(inputProcessor.keyTyped('A')).thenReturn(true);
		
		//when		
		ctrl.setEnabled(false);
				
		//then
		assertFalse(ctrl.keyDown(event, Input.Keys.UP));
		assertFalse(ctrl.keyUp(event, Input.Keys.UP));
		assertFalse(ctrl.keyTyped(event, 'A'));		
	}
	
}
