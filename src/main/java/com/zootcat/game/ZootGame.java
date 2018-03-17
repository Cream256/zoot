package com.zootcat.game;

import java.util.function.BiFunction;
import java.util.function.Function;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.zootcat.assets.ZootAssetManager;
import com.zootcat.controllers.factory.ControllerFactory;
import com.zootcat.map.tiled.ZootTiledMap;
import com.zootcat.scene.ZootScene;
import com.zootcat.scene.tiled.ZootTiledScene;
import com.zootcat.screen.ZootLoadingScreen;
import com.zootcat.screen.ZootSceneScreen;

public abstract class ZootGame extends Game
{		
	private float unitPerTile = 1.0f;
	private float viewportWidth = 16.0f;
	private float viewportHeight = 9.0f;
	private String currentLevelPath;
	private ZootAssetManager assetManager;
	private ControllerFactory controllerFactory;
	private Function<ZootGame, ZootLoadingScreen> loadingScreenSupplier;
	private BiFunction<ZootGame, ZootScene, ZootSceneScreen> sceneScreenSupplier;
		
	public ZootGame()
	{
		assetManager = new ZootAssetManager();
		controllerFactory = new ControllerFactory();
		loadingScreenSupplier = (game) -> new ZootLoadingScreen(game);
		sceneScreenSupplier = (game, scene) -> new ZootSceneScreen(game, scene);
	}
	    
    @Override
    public void dispose()
    {
    	super.dispose();
    	
    	assetManager.dispose();
    	assetManager = null;
    }
    
    public void restartCurrentLevel()
    {
    	if(currentLevelPath == null)
    	{
    		return;
    	}
    	
    	Gdx.app.debug("ZootGame", "Restarting level " + currentLevelPath);
    	
    	getAssetManager().unload(currentLevelPath);    	
    	getScreen().hide();
    	loadLevel(currentLevelPath);
    }
    
    public void loadLevel(String levelFile)
    {
    	currentLevelPath = levelFile;
    	
    	ZootLoadingScreen loadingScreen = loadingScreenSupplier.apply(this); 	
    	loadingScreen.addTask((Void) -> getAssetManager().load(levelFile, ZootTiledMap.class));
    	
    	loadingScreen.onFinishLoading((game) -> 
    	{
    		ZootTiledMap tiledMap = getAssetManager().get(levelFile, ZootTiledMap.class);
    		ZootTiledScene scene = new ZootTiledScene(
    				tiledMap, 
    				getAssetManager(), 
    				getControllerFactory(), 
    				game.getViewportWidth(), 
    				game.getViewportHeight(), 
    				game.getUnitPerTile());
    		game.setScreen(sceneScreenSupplier.apply(this, scene));
    	});
    	
    	setScreen(loadingScreen);
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
    
    public void setLoadingScreenSupplier(Function<ZootGame, ZootLoadingScreen> supplier)
    {
    	loadingScreenSupplier = supplier;
    }
    
    public void setSceneScreenSupplier(BiFunction<ZootGame, ZootScene, ZootSceneScreen> supplier)
    {
    	sceneScreenSupplier = supplier;
    }    
}
