package com.zootcat.controllers.sfx;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.controllers.logic.OnStateChangeController;
import com.zootcat.fsm.ZootState;
import com.zootcat.scene.ZootActor;

public class PlaySoundOnEnterStateController extends OnStateChangeController
{
	@CtrlParam(required = true) private String stateName;
	@CtrlParam(required = true) private String soundFile;
	@CtrlParam(global = true) private AssetManager assetManager;
	
	private Sound sound;
	
	@Override
	public void init(ZootActor actor) 
	{
		sound = assetManager.get(soundFile, Sound.class);
	}
	
	@Override
	public void onEnterState(ZootActor actor, ZootState state)
	{
		if(state.getName().equalsIgnoreCase(stateName))
		{		
			sound.play();
		}		
	}

	@Override
	public void onLeaveState(ZootActor actor, ZootState state)
	{
		//noop
	}
}