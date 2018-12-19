package com.zootcat.controllers;

import com.zootcat.controllers.gfx.RenderController;
import com.zootcat.scene.ZootActor;

/**
 * Controllers are a entity classes used for specific game logic.
 * Each {@link ZootActor} is a container that holds a list of controllers.
 * They are used to control actor behaviour and state. Thanks to that
 * the game logic is separated into many smaller classes. 
 * @author Cream
 * @see RenderController
 */
public interface Controller 
{
	void init(ZootActor actor);
	void onAdd(ZootActor actor);
	void onRemove(ZootActor actor);	
	void onUpdate(float delta, ZootActor actor);
	void setEnabled(boolean value);
	boolean isEnabled();
		
	default ControllerPriority getPriority() 
	{ 
		return ControllerPriority.Normal; 
	}
	
	static int getControllerId(Controller ctrl)
	{
		return ctrl.getClass().hashCode();
	}

	static int getControllerId(Class<? extends Controller> ctrlClass)
	{
		return ctrlClass.hashCode();
	}
	
	static boolean areEqual(Class<? extends Controller> ctrlClass1, Class<? extends Controller> ctrlClass2)
	{
		return getControllerId(ctrlClass1) == getControllerId(ctrlClass2);
	}
	
	static boolean areEqual(Controller ctrl1, Controller ctrl2)
	{
		return getControllerId(ctrl1) == getControllerId(ctrl2);
	}
}
