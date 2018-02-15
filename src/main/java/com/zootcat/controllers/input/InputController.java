package com.zootcat.controllers.input;

import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.zootcat.controllers.Controller;
import com.zootcat.scene.ZootActor;

public class InputController extends InputListener implements Controller
{	
	@Override
	public void init(ZootActor actor)
	{
		//noop
	}
	
	@Override
	public void onAdd(ZootActor actor) 
	{
		actor.addListener(this);
	}

	@Override
	public void onRemove(ZootActor actor) 
	{
		actor.removeListener(this);
	}

	@Override
	public void onUpdate(float delta, ZootActor actor) 
	{
		//noop
	}
}
