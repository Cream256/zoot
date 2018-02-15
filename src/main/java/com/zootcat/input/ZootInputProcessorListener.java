package com.zootcat.input;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class ZootInputProcessorListener extends InputListener 
{
	private InputProcessor inputProcessor;
	
	public ZootInputProcessorListener(InputProcessor inputProcessor)
	{
		this.inputProcessor = inputProcessor;
	}
	
	public boolean keyDown (InputEvent event, int keycode) 
	{
		return inputProcessor.keyDown(keycode);
	}

	public boolean keyUp (InputEvent event, int keycode) 
	{
		return inputProcessor.keyUp(keycode);
	}
}
