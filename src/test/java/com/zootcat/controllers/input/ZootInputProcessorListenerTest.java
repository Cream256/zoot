package com.zootcat.controllers.input;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.zootcat.input.ZootInputProcessorListener;

public class ZootInputProcessorListenerTest 
{
	private ZootInputProcessorListener inputProcessorListener;
	@Mock private InputProcessor inputProcessor;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		inputProcessorListener = new ZootInputProcessorListener(inputProcessor);
	}
	
	@Test
	public void shouldSendKeyDownEventToInputProcessor()
	{
		inputProcessorListener.keyDown(null, Input.Keys.A);
		verify(inputProcessor).keyDown(Input.Keys.A);
		
		inputProcessorListener.keyDown(null, Input.Keys.B);
		verify(inputProcessor).keyDown(Input.Keys.B);
	}
	
	@Test
	public void shouldSendKeyUpEventToInputProcessor()
	{
		inputProcessorListener.keyUp(null, Input.Keys.A);
		verify(inputProcessor).keyUp(Input.Keys.A);
		
		inputProcessorListener.keyUp(null, Input.Keys.B);
		verify(inputProcessor).keyUp(Input.Keys.B);
	}
}
