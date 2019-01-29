package com.zootcat.input;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.badlogic.gdx.InputAdapter;
import com.zootcat.exceptions.RuntimeZootException;

public class ZootBindableInputProcessor extends InputAdapter
{
	private Map<Integer, Supplier<Boolean>> keyUpBindings = new HashMap<Integer, Supplier<Boolean>>();
	private Map<Integer, Supplier<Boolean>> keyDownBindings = new HashMap<Integer, Supplier<Boolean>>();
	private Map<Integer, ZootBindableTouchCommand> touchUpBindings = new HashMap<Integer, ZootBindableTouchCommand>();
	private Map<Integer, ZootBindableTouchCommand> touchDownBindings = new HashMap<Integer, ZootBindableTouchCommand>();
	
	@Override
	public boolean keyDown (int keycode) 
	{
		if(keyDownBindings.containsKey(keycode))
		{
			return keyDownBindings.get(keycode).get();
		}		
		return false;
	}
	
	@Override
	public boolean keyUp (int keycode) 
	{		
		if(keyUpBindings.containsKey(keycode))
		{
			return keyUpBindings.get(keycode).get();
		}
		return false;
	}
					
	public void bindKeyDown(int keycode, Supplier<Boolean> command)
	{
		validateNewKeyBinding(keycode, true);
		keyDownBindings.put(keycode, command);
	}
	
	public void bindKeyUp(int keycode, Supplier<Boolean> command)
	{
		validateNewKeyBinding(keycode, false);
		keyUpBindings.put(keycode, command);
	}

	private void validateNewKeyBinding(int keyCode, boolean validateDownBinding)
	{
		boolean hasBinding = validateDownBinding ? hasKeyDownBinding(keyCode) : hasKeyUpBinding(keyCode);
		if(hasBinding) throw new RuntimeZootException("Key binding already present for key code " + keyCode);
	}
	
	public boolean hasKeyDownBinding(int keycode)
	{
		return keyDownBindings.containsKey(keycode);
	}
	
	public boolean hasKeyUpBinding(int keycode)
	{
		return keyUpBindings.containsKey(keycode);
	}
	
	@Override
	public boolean touchDown (int screenX, int screenY, int pointer, int button) 
	{
		if(touchDownBindings.containsKey(button))
		{
			return touchDownBindings.get(button).apply(screenX, screenY, pointer);
		}
		return false;
	}

	@Override
	public boolean touchUp (int screenX, int screenY, int pointer, int button) 
	{
		if(touchUpBindings.containsKey(button))
		{
			return touchUpBindings.get(button).apply(screenX, screenY, pointer);
		}
		return false;
	}
	
	public void bindTouchDown(int buttonCode, ZootBindableTouchCommand command)
	{
		validateNewTouchBinding(buttonCode, true);
		touchDownBindings.put(buttonCode, command);
	}
	
	public void bindTouchUp(int buttonCode, ZootBindableTouchCommand command)
	{
		validateNewTouchBinding(buttonCode, false);
		touchUpBindings.put(buttonCode, command);
	}
	
	private void validateNewTouchBinding(int buttonCode, boolean validateDownBinding)
	{
		boolean hasBinding = validateDownBinding ? hasTouchDownBinding(buttonCode) : hasTouchUpBinding(buttonCode);
		if(hasBinding) throw new RuntimeZootException("Mouse binding already present for button code " + buttonCode);
	}
	
	public boolean hasTouchDownBinding(int buttonCode)
	{
		return touchDownBindings.containsKey(buttonCode);
	}
	
	public boolean hasTouchUpBinding(int buttonCode)
	{
		return touchUpBindings.containsKey(buttonCode);
	}
}
