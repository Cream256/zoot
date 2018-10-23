package com.zootcat.input;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;

public class ZootInputManager extends InputAdapter 
{
	private boolean enabled;
	private InputMultiplexer multiplexer;	
	private Set<Integer> pressedKeys = new HashSet<Integer>();
		
	public ZootInputManager()
	{
		this(new InputMultiplexer());
	}
	
	public ZootInputManager(InputMultiplexer multiplexer)
	{
		this.enabled = true;
		this.multiplexer = multiplexer;
	}
	
	@Override
	public boolean keyDown (int keycode) 
	{
		if(!enabled) return false;
		
		pressedKeys.add(keycode);		
		return multiplexer.keyDown(keycode);
	}

	@Override
	public boolean keyUp (int keycode) 
	{
		if(!enabled) return false;
		
		pressedKeys.remove(keycode);
		return multiplexer.keyUp(keycode);
	}
	
	@Override
	public boolean keyTyped (char character) 
	{
		return enabled ? multiplexer.keyTyped(character) : false;
	}

	@Override
	public boolean touchDown (int screenX, int screenY, int pointer, int button) 
	{
		return enabled ? multiplexer.touchDown(screenX, screenY, pointer, button) : false;
	}

	@Override
	public boolean touchUp (int screenX, int screenY, int pointer, int button) 
	{
		return enabled ? multiplexer.touchUp(screenX, screenY, pointer, button) : false;
	}

	@Override
	public boolean touchDragged (int screenX, int screenY, int pointer) 
	{
		return enabled ? multiplexer.touchDragged(screenX, screenY, pointer) : false;
	}

	@Override
	public boolean mouseMoved (int screenX, int screenY) 
	{
		return enabled ? multiplexer.mouseMoved(screenX, screenY) : false;
	}

	@Override
	public boolean scrolled (int amount)
	{
		return enabled ? multiplexer.scrolled(amount) : false;
	}
	
	public void processPressedKeys(float delta)
	{
		pressedKeys.forEach((key) -> keyDown(key));
	}
	
	public void clearPressedKeys()
	{
		pressedKeys.clear();
	}

	public void addProcessor(InputProcessor inputProcessor) 
	{
		multiplexer.addProcessor(inputProcessor);
	}
	
	public void removeProcessor(InputProcessor inputProcessor)
	{
		multiplexer.removeProcessor(inputProcessor);
	}
	
	public void removeAllProcessors()
	{
		multiplexer.clear();
	}
	
	public int getProcessorsCount()
	{
		return multiplexer.size();
	}
	
	public void enable(boolean value)
	{
		enabled = value;
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}
}
