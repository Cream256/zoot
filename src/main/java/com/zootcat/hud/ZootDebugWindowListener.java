package com.zootcat.hud;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.zootcat.scene.ZootActor;

public class ZootDebugWindowListener extends InputListener
{
	private ZootDebugWindow debugWindow;

	public ZootDebugWindowListener(ZootDebugWindow debugWindow)
	{
		this.debugWindow = debugWindow;
	}
	
	@Override
	public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) 
	{
		if(button == Input.Buttons.RIGHT)
		{
			debugWindow.setDebugActor(null);
			return true;
		}
		
		Actor actor = event.getTarget();			
		if(ClassReflection.isInstance(ZootActor.class, actor))
		{
			debugWindow.setDebugActor((ZootActor) actor);
			return true;
		}
		return false;
	}
}
