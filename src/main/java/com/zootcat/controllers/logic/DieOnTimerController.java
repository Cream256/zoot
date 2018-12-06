package com.zootcat.controllers.logic;

import com.zootcat.actions.ZootActions;
import com.zootcat.scene.ZootActor;

public class DieOnTimerController extends OnTimerController
{	
	@Override
	public void onTimer(float delta, ZootActor actor)
	{
		actor.addAction(ZootActions.killActor(actor));
	}
}
