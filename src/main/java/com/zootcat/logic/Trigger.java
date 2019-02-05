package com.zootcat.logic;

import java.util.function.Consumer;

public class Trigger
{
	private boolean active;	
	private boolean canRevert;
	private boolean firstTriggerDone;
	private boolean firstTriggerState;	
	private Consumer<Boolean> triggerAction;
	
	public Trigger(Consumer<Boolean> triggerAction)
	{
		this(triggerAction, false, true);
	}
	
	public Trigger(Consumer<Boolean> triggerAction, boolean active)
	{
		this(triggerAction, active, true);
	}
	
	public Trigger(Consumer<Boolean> triggerAction, boolean active, boolean canRevert)
	{
		this.active = active;
		this.firstTriggerDone = false;
		this.firstTriggerState = active;		
		this.triggerAction = triggerAction;
		this.canRevert = canRevert;		
	}
	
	public void initialize()
	{
		trigger(firstTriggerState);
		firstTriggerDone = false;
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
		if(firstTriggerDone && !canRevert) return;
		
		if(active != isActive) trigger(isActive);		
		active = isActive;	
	}
	
	private void trigger(boolean active)
	{
		triggerAction.accept(active);
		firstTriggerDone = true;
	}
}
