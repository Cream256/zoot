package com.zootcat.controllers.logic;

import com.badlogic.gdx.physics.box2d.Contact;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.controllers.physics.OnCollideController;
import com.zootcat.scene.ZootActor;

/**
 * Switch Controller - used for actors that are switches. When collision
 * happens, the switch changes it's state and invokes the trigger method.
 * 
 * You should override trigger method to perform custom actions.
 * @author Cream
 * @see OnCollideController
 */
public abstract class SwitchController extends OnCollideController
{
	@CtrlParam(debug = true) private boolean active = false;
			
	@Override
	public void onAdd(ZootActor actor) 
	{
		super.onAdd(actor);
		trigger(active);
	}
	
	@Override
	public void onEnter(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		switchState();
	}

	@Override
	public void onLeave(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		//noop
	}
	
	public boolean isActive()
	{
		return active;
	}
	
	public void setActive(boolean isActive)
	{
		if(active != isActive) trigger(isActive);		
		active = isActive;		
	}
	
	public void switchState()
	{
		setActive(!active);
	}
	
	public abstract void trigger(boolean active);
}
