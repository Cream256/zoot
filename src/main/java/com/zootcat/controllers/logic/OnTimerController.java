package com.zootcat.controllers.logic;

import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.scene.ZootActor;

public abstract class OnTimerController extends ControllerAdapter
{
	@CtrlParam(debug = true) private boolean repeat = false;
	@CtrlParam(debug = true, required = true) private float interval; 
		
	private float time = 0.0f;
	private boolean done = false;
	
	@Override
	public void init(ZootActor actor)
	{
		done = false;
		time = 0.0f;
	}
	
	@Override
	public void onUpdate(float delta, ZootActor actor)
	{
		if(done)
		{
			return;
		}
		
		time += delta;
		if(time >= interval)
		{
			onTimer(delta, actor);
			time = repeat ? 0.0f : time;
			done = !repeat;
		}
	}
	
	public abstract void onTimer(float delta, ZootActor actor);
}
