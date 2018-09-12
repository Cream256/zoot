package com.zootcat.controllers.input;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class InputProcessorController extends InputController
{
	private boolean enabled;
	private InputProcessor inputProcessor;
		
	public InputProcessorController(InputProcessor inputProcessor)
	{
		this.inputProcessor = inputProcessor;
		this.enabled = true;
	}
		
	@Override
	public boolean keyDown (InputEvent event, int keycode) 
	{
		return enabled ? inputProcessor.keyDown(keycode) : false;
	}

	@Override
	public boolean keyUp (InputEvent event, int keycode) 
	{
		return enabled ? inputProcessor.keyUp(keycode) : false;
	}

	@Override
	public boolean keyTyped (InputEvent event, char character) 
	{
		return enabled ? inputProcessor.keyTyped(character) : false;
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}
	
	public void setEnabled(boolean value)
	{
		enabled = value;
	}

}
