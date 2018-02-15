package com.zootcat.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.zootcat.assets.ZootAssetManager;
import com.zootcat.controllers.factory.ControllerFactory;

public abstract class ZootGame extends Game
{		
	private float unitPerTile = 1.0f;
	private float viewportWidth = 16.0f;
	private float viewportHeight = 9.0f;
	private ZootAssetManager assetManager;
	private ControllerFactory controllerFactory;
	
	public ZootGame()
	{
		assetManager = new ZootAssetManager();
		controllerFactory = new ControllerFactory();
	}
	    
    @Override
    public void dispose()
    {
    	super.dispose();
    	
    	assetManager.dispose();
    	assetManager = null;
    }
    
    public AssetManager getAssetManager()
    {
    	return assetManager;
    }
    
    public ControllerFactory getControllerFactory()
    {
    	return controllerFactory;
    }
    
    public void setViewportWidth(float width)
    {
    	viewportWidth = width;
    }
    
    public float getViewportWidth()
    {
    	return viewportWidth;
    }
    
    public void setViewportHeight(float height)
    {
    	viewportHeight = height;
    }
    
    public float getViewportHeight()
    {
    	return viewportHeight;
    }
    
    public void setUnitPerTile(float unitPerTile)
    {
    	this.unitPerTile = unitPerTile;
    }
    
    public float getUnitPerTile()
    {
    	return unitPerTile;
    }
}
