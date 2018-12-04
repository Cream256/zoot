package com.zootcat.controllers.logic;

import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.scene.ZootActor;

//TODO remake tests
public class DieOnCollideAfterTimeController extends DieOnCollideController
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
	public void postUpdate(float delta, ZootActor actor)	
	{
		super.onUpdate(delta, actor);
		
		if(!timerStarted || done)
		{
			return;
		}
		
		elapsedTime += delta;
		if(elapsedTime >= time)
		{
			super.die();
			done = true;
		}
	}
}
