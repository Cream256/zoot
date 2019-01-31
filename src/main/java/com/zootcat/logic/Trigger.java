package com.zootcat.logic;

import java.util.function.Consumer;

public class Trigger
{
	private boolean active;	
	private boolean firstTriggerState;
	private Consumer<Boolean> triggerAction;
	
	public Trigger(Consumer<Boolean> triggerAction)
	{
		this(triggerAction, false);
	}
	
	public Trigger(Consumer<Boolean> triggerAction, boolean active)
	{
		this.active = active;
		this.firstTriggerState = active;
		this.triggerAction = triggerAction;
	}
	
	public void initialize()
	{
		trigger(firstTriggerState);
	}
	
	public boolean isActive()
	{
		return active;
	}
	
	public void switchState()
	{
		setActive(!active);
	}
	
	public void setActive(boolean isActive)
	{
		if(active != isActive) trigger(isActive);		
		active = isActive;	
	}
	
	private void trigger(boolean active)
	{
		triggerAction.accept(active);
	}
}
