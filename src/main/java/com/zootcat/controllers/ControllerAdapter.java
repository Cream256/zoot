package com.zootcat.controllers;

import com.zootcat.scene.ZootActor;

public class ControllerAdapter implements Controller 
{
	private boolean enabled = true;
	
	@Override
	public void init(ZootActor actor) 
	{
		//noop
	}

	@Override
	public void onAdd(ZootActor actor) 
	{
		//noop
	}

	@Override
	public void onRemove(ZootActor actor) 
	{
		//noop
	}

	@Override
	public void onUpdate(float delta, ZootActor actor)
	{
		//noop
	}

	@Override
	public boolean isEnabled()
	{
		return enabled;
	}
	
	@Override
	public void setEnabled(boolean value)
	{
		enabled = value;
	}
	
	@Override
	public int hashCode()
	{
		return Controller.getControllerId(this);
	}
	
	@Override
	public boolean equals(Object object)
	{
		if(object == this) return true;
		if(!(object instanceof Controller)) return false;
		return Controller.areEqual(this, (Controller)object);
	}
}
