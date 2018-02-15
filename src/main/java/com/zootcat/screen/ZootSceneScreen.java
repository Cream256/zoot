package com.zootcat.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.zootcat.hud.ZootDebugHud;
import com.zootcat.hud.ZootDebugWindowListener;
import com.zootcat.input.ZootInputManager;
import com.zootcat.scene.ZootScene;

public class ZootSceneScreen implements Screen
{
	private ZootScene scene;
	private ZootDebugHud debugHud;
	private boolean paused = false;
	private ZootInputManager inputManager;
	
	public ZootSceneScreen(ZootScene scene)
	{
		this.scene = scene;
		debugHud = new ZootDebugHud();
		inputManager = new ZootInputManager();
	}
			
	@Override
	public void resize (int width, int height) 
	{
		scene.resize(width, height);
		debugHud.resize(width, height);
	}
    
	@Override
	public void pause () 
	{
		paused = true;
	}

	@Override
	public void resume () 
	{
		paused = false;
	}
	
	@Override
	public void dispose() 
	{
		if(scene != null)
		{		
			scene.dispose();
			scene = null;
		}
	}
	
	@Override
	public void show()
	{			
		assignInput();
	}
	
	@Override
	public void hide()
	{
		deassignInput();
		dispose();		
	}

	@Override
	public void render(float delta)
	{
        if(isPaused() || !hasScene())
        {
        	return;
        }
    	
        //update
        onUpdate(delta);
            	
        //render
        onRender(delta);
	}
	
	public void onRender(float delta)
	{
		scene.render(delta);
    	debugHud.render(delta);
	}

	public void onUpdate(float delta)
	{
        inputManager.processPressedKeys(delta);
        scene.update(delta);
	}

	public ZootScene getScene()
	{
		return scene;
	}
	
	public boolean hasScene()
	{
		return scene != null;
	}
	
	public boolean isPaused()
	{
		return paused;
	}
	
	public void reloadScene()
	{
		deassignInput();
		scene.reload();
		assignInput();
	}
	
	public ZootInputManager getInputManager()
	{
		return inputManager;
	}
	
	public ZootDebugHud getDebugHud()
	{
		return debugHud;
	}
	
	protected void assignInput()
	{
		inputManager.addProcessor(getDebugHud().getInputProcessor());
		scene.addListener(new ZootDebugWindowListener(debugHud));
		
		Gdx.input.setInputProcessor(inputManager);
	}
	
	protected void deassignInput()
	{
		Gdx.input.setInputProcessor(null);
	}
}
