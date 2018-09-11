package com.zootcat.screen;

import com.badlogic.gdx.Gdx;
import com.zootcat.game.ZootGame;
import com.zootcat.input.ZootInputManager;
import com.zootcat.scene.ZootScene;

public class ZootSceneScreen implements ZootScreen
{
	private ZootGame game;
	private ZootScene scene;
	private boolean paused = false;
	private ZootInputManager inputManager;
	
	public ZootSceneScreen(ZootGame game, ZootScene scene)
	{
		this.game = game;
		this.scene = scene;
		inputManager = new ZootInputManager();
	}
			
	@Override
	public void resize(int width, int height) 
	{
		scene.resize(width, height);
	}
    
	@Override
	public void pause() 
	{
		paused = true;
	}

	@Override
	public void resume() 
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
		createHud();
		assignInput();
	}
	
	protected void createHud()
	{
		//noop
	}

	@Override
	public void hide()
	{
		deassignInput();	
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
	
	public ZootGame getGame()
	{
		return game;
	}
	
	public void onRender(float delta)
	{
		scene.render(delta);
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
		
	public ZootInputManager getInputManager()
	{
		return inputManager;
	}
		
	protected void assignInput()
	{
		Gdx.input.setInputProcessor(inputManager);
	}
	
	protected void deassignInput()
	{
		inputManager.clear();
		inputManager.clearPressedKeys();
		Gdx.input.setInputProcessor(null);
	}
}
