package com.zootcat.controllers;

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
}
