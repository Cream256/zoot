package com.zootcat.controllers.logic;

import com.badlogic.gdx.physics.box2d.Contact;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.scene.ZootActor;

public class DieOnCollideFromAboveAfterTimeController extends DieOnCollideFromAboveController
{
	@CtrlParam private float time = 1.0f;
	
	private float elapsedTime = 0.0f;
	private boolean done = false;
	private boolean timerStarted = false;
	
	@Override
	public void init(ZootActor actor)
	{
		super.init(actor);		
		done = false;
		timerStarted = false;
		elapsedTime = 0.0f;
	}
	
	@Override
	public void onUpdate(float delta, ZootActor actor)
	{
		super.onUpdate(delta, actor);
		
		if(!timerStarted || done)
		{
			return;
		}
		
		elapsedTime += delta;
		if(elapsedTime >= time)
		{
			super.killControllerActor();
			done = true;
		}
	}
	
	@Override
	public void onCollidedFromAbove(ZootActor actorA, ZootActor actorB, Contact contact)
	{				
		timerStarted = true;
	}	
}
