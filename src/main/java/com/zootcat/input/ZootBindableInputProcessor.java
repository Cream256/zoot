package com.zootcat.input;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.badlogic.gdx.InputAdapter;
import com.zootcat.exceptions.RuntimeZootException;

public class ZootBindableInputProcessor extends InputAdapter
{
	private Map<Integer, Supplier<Boolean>> bindingsUp = new HashMap<Integer, Supplier<Boolean>>();
	private Map<Integer, Supplier<Boolean>> bindingsDown = new HashMap<Integer, Supplier<Boolean>>();
		
	@Override
	public boolean keyDown (int keycode) 
	{
		if(bindingsDown.containsKey(keycode))
		{
			return bindingsDown.get(keycode).get();
		}		
		return false;
	}
	
	@Override
	public boolean keyUp (int keycode) 
	{		
		if(bindingsUp.containsKey(keycode))
		{
			return bindingsUp.get(keycode).get();
		}
		return false;
	}
				
	public void bindDown(int keycode, Supplier<Boolean> command)
	{
		validateNewBinding(keycode, true);
		bindingsDown.put(keycode, command);
	}
	
	public void bindUp(int keycode, Supplier<Boolean> command)
	{
		validateNewBinding(keycode, false);
		bindingsUp.put(keycode, command);
	}
	
	public boolean hasDownBinding(int keycode)
	{
		return bindingsDown.containsKey(keycode);
	}
	
	public boolean hasUpBinding(int keycode)
	{
		return bindingsUp.containsKey(keycode);
	}
	
	private void validateNewBinding(int keycode, boolean validateDownBinding)
	{
		boolean hasBinding = validateDownBinding ? hasDownBinding(keycode) : hasUpBinding(keycode);
		if(hasBinding)
		{
			throw new RuntimeZootException("Binding already present for keycode " + keycode);
		}
	}
}
