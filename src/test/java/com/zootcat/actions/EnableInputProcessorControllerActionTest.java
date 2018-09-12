package com.zootcat.actions;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.InputProcessor;
import com.zootcat.controllers.input.InputProcessorController;
import com.zootcat.scene.ZootActor;

public class EnableInputProcessorControllerActionTest
{
	private ZootActor targetActor;
	private InputProcessorController inputProcessorCtrl;
	private EnableInputProcessorControllerAction action;
	
	@Before
	public void setup()
	{
		inputProcessorCtrl = new InputProcessorController(mock(InputProcessor.class));
		targetActor = new ZootActor();
		targetActor.addController(inputProcessorCtrl);		
		
		action = new EnableInputProcessorControllerAction();
		action.setTarget(targetActor);
	}
	
	@Test
	public void shouldReturnTrue()
	{
		assertTrue(action.act(1.0f));
	}
	
	@Test
	public void shouldReturnIfControllerWillBeEnabled()
	{
		action.setControllerEnabled(true);
		assertTrue(action.getControllerEnabled());
		
		action.setControllerEnabled(false);
		assertFalse(action.getControllerEnabled());
	}
	
	@Test
	public void shouldEnableInputProcessorControllerByDefault()
	{		
		//given
		inputProcessorCtrl.setEnabled(false);
		assertFalse(inputProcessorCtrl.isEnabled());
		
		//when		
		action.act(1.0f);
		
		//then
		assertTrue(inputProcessorCtrl.isEnabled());
	}
	
	@Test
	public void shouldDisableInputProcessorController()
	{
		//given
		inputProcessorCtrl.setEnabled(true);
		assertTrue(inputProcessorCtrl.isEnabled());
		
		//when		
		action.setControllerEnabled(false);
		action.act(1.0f);
		
		//then
		assertFalse(inputProcessorCtrl.isEnabled());		
	}
	
	@Test
	public void shouldEnableInputProcessorController()
	{
		//given
		inputProcessorCtrl.setEnabled(false);
		assertFalse(inputProcessorCtrl.isEnabled());
		
		//when		
		action.setControllerEnabled(true);
		action.act(1.0f);
		
		//then
		assertTrue(inputProcessorCtrl.isEnabled());		
	}
}
