package com.zootcat.controllers.input;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class InputProcessorController extends InputController
{
	private InputProcessor inputProcessor;
	
	public InputProcessorController(InputProcessor inputProcessor)
	{
		this.inputProcessor = inputProcessor;
	}
		
	@Override
	public boolean keyDown (InputEvent event, int keycode) 
	{
		return inputProcessor.keyDown(keycode);
	}

	@Override
	public boolean keyUp (InputEvent event, int keycode) 
	{
		return inputProcessor.keyUp(keycode);
	}

	@Override
	public boolean keyTyped (InputEvent event, char character) 
	{
		return inputProcessor.keyTyped(character);
	}

}
