package com.zootcat.controllers.sfx;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.controllers.logic.OnEnterStateController;
import com.zootcat.scene.ZootActor;

public class PlaySoundOnEnterStateController extends OnEnterStateController
{
	@CtrlParam(required = true) private String soundFile;
	@CtrlParam(global = true) private AssetManager assetManager;
	
	private Sound sound;
	
	@Override
	public void init(ZootActor actor) 
	{
		sound = assetManager.get(soundFile, Sound.class);
	}
	
	
	public void onEnterState(ZootActor actor)
	{
		sound.play();
	}
	
}
