package com.zootcat.input;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;

public class ZootInputManager extends InputAdapter 
{
	private Set<Integer> pressedKeys = new HashSet<Integer>();
	private InputMultiplexer multiplexer = new InputMultiplexer();
	
	@Override
	public boolean keyDown (int keycode) 
	{
		pressedKeys.add(keycode);		
		return multiplexer.keyDown(keycode);
	}

	@Override
	public boolean keyUp (int keycode) 
	{
		pressedKeys.remove(keycode);
		return multiplexer.keyUp(keycode);
	}
	
	@Override
	public boolean keyTyped (char character) 
	{
		return multiplexer.keyTyped(character);
	}

	@Override
	public boolean touchDown (int screenX, int screenY, int pointer, int button) 
	{
		return multiplexer.touchDown(screenX, screenY, pointer, button);
	}

	@Override
	public boolean touchUp (int screenX, int screenY, int pointer, int button) 
	{
		return multiplexer.touchUp(screenX, screenY, pointer, button);
	}

	@Override
	public boolean touchDragged (int screenX, int screenY, int pointer) 
	{
		return multiplexer.touchDragged(screenX, screenY, pointer);
	}

	@Override
	public boolean mouseMoved (int screenX, int screenY) 
	{
		return multiplexer.mouseMoved(screenX, screenY);
	}

	@Override
	public boolean scrolled (int amount)
	{
		return multiplexer.scrolled(amount);
	}
	
	public void processPressedKeys(float delta)
	{
		pressedKeys.forEach((key) -> keyDown(key));
	}

	public void addProcessor(InputProcessor inputProcessor) 
	{
		multiplexer.addProcessor(inputProcessor);
	}
	
	public void removeProcessor(InputProcessor inputProcessor)
	{
		multiplexer.removeProcessor(inputProcessor);
	}
}
