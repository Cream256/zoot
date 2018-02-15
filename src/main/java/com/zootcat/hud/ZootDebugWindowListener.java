package com.zootcat.hud;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.zootcat.scene.ZootActor;

public class ZootDebugWindowListener extends InputListener
{
	private ZootDebugHud debugHud;

	public ZootDebugWindowListener(ZootDebugHud debugHud)
	{
		this.debugHud = debugHud;
	}
	
	@Override
	public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) 
	{
		if(button == Input.Buttons.RIGHT)
		{
			debugHud.setDebugActor(null);
			return true;
		}
		
		Actor actor = event.getTarget();			
		if(ClassReflection.isInstance(ZootActor.class, actor))
		{
			debugHud.setDebugActor((ZootActor) actor);
			return true;
		}
		return false;
	}
	
}
