package com.zootcat.game;

import java.util.function.BiFunction;
import java.util.function.Function;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.zootcat.assets.ZootAssetManager;
import com.zootcat.controllers.factory.ControllerFactory;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.gfx.ZootGraphicsFactory;
import com.zootcat.input.ZootInputManager;
import com.zootcat.map.tiled.ZootTiledMap;
import com.zootcat.scene.ZootScene;
import com.zootcat.scene.tiled.ZootTiledScene;
import com.zootcat.screen.ZootLoadingScreen;
import com.zootcat.screen.ZootSceneScreen;
import com.zootcat.screen.ZootScreen;

public abstract class ZootGame extends Game
{		
	private float unitPerTile = 1.0f;
	private float viewportWidth = 16.0f;
	private float viewportHeight = 9.0f;
	private ZootScreen previousScreen;
	private String currentLevelPath;	
	private ZootAssetManager assetManager;
	private ZootInputManager inputManager;
	private ZootGraphicsFactory graphicsFactory;
	private ControllerFactory controllerFactory;
	private Function<ZootGame, ZootLoadingScreen> loadingScreenSupplier;
	private BiFunction<ZootGame, ZootScene, ZootSceneScreen> sceneScreenSupplier;
	
	public ZootGame()
	{
		Box2D.init();
		assetManager = new ZootAssetManager();		
		inputManager = new ZootInputManager();
		graphicsFactory = new ZootGraphicsFactory();
		
		controllerFactory = new ControllerFactory();
		controllerFactory.addGlobalParameter("game", this);
		controllerFactory.addGlobalParameter("graphicsFactory", new ZootGraphicsFactory());
		
		loadingScreenSupplier = (game) -> new ZootLoadingScreen(game);
		sceneScreenSupplier = (game, scene) -> new ZootSceneScreen(game, scene);
	}
	
	@Override
	public final void create()
	{
		Gdx.input.setInputProcessor(inputManager);
		onCreate();
	}
	
	public abstract void onCreate();
	    
    @Override
    public final void dispose()
    {
    	onDispose();
    	super.dispose();
    	
    	if(previousScreen != null) getPreviousScreen().dispose();
    	previousScreen = null;
    	
    	if(super.screen != null) getScreen().dispose();    	
    	super.screen = null;
    	
    	assetManager.dispose();
    	assetManager = null;
    	
    	inputManager = null;
    	Gdx.input.setInputProcessor(null);
    }
    
    public abstract void onDispose();
    
    @Override
	public void setScreen (Screen screen) 
    {
		if(!(screen instanceof ZootScreen))
		{
			throw new RuntimeZootException("Screen must be instance of ZootScreen");
		}
    	
    	previousScreen = (ZootScreen)getScreen();
    	super.setScreen(screen);
	}
    
    public ZootScreen getPreviousScreen()
    {
    	return previousScreen;
    }
    
    public void restartCurrentLevel()
    {
    	if(currentLevelPath == null)
    	{
    		return;
    	}
    	
    	Gdx.app.debug("ZootGame", "Restarting level " + currentLevelPath);
    	
    	getAssetManager().unload(currentLevelPath);    	
    	getInputManager().removeAllProcessors();
    	getInputManager().clearPressedKeys();
    	
    	getScreen().hide();
    	loadLevel(currentLevelPath);
    }
    
    public void loadLevel(String levelFile)
    {
    	Gdx.app.debug("ZootGame", "Loading level " + levelFile);
    	
    	currentLevelPath = levelFile;
    	
    	ZootLoadingScreen loadingScreen = createLoadingScreen(); 	
    	loadingScreen.addTask((assetManager) -> assetManager.load(levelFile, ZootTiledMap.class));
    	
    	loadingScreen.onFinishLoading((game) -> 
    	{
    		ZootTiledMap tiledMap = getAssetManager().get(levelFile, ZootTiledMap.class);
    		
    		//TODO create a sceneSupplier?
    		Viewport viewport = new StretchViewport(viewportWidth, viewportHeight);    		
    		ZootTiledScene scene = new ZootTiledScene(tiledMap, getAssetManager(), getControllerFactory(), viewport);
    		
    		/*ZootTiledScene scene = new ZootTiledScene(
    				tiledMap, 
    				getAssetManager(), 
    				getControllerFactory(), 
    				game.getViewportWidth(), 
    				game.getViewportHeight(), 
    				game.getUnitPerTile());
    				*/
    		game.setScreen(createSceneScreen(scene));
    	});
    	
    	setScreen(loadingScreen);
    }
    
    public ZootAssetManager getAssetManager()
    {
    	return assetManager;
    }
    
    public ControllerFactory getControllerFactory()
    {
    	return controllerFactory;
    }
    
    public ZootGraphicsFactory getGraphicsFactory()
    {
    	return graphicsFactory;
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
    
    public ZootLoadingScreen createLoadingScreen()
    {
    	return loadingScreenSupplier.apply(this);
    }
    
    public void setSceneScreenSupplier(BiFunction<ZootGame, ZootScene, ZootSceneScreen> supplier)
    {
    	sceneScreenSupplier = supplier;
    }    
    
    public ZootSceneScreen createSceneScreen(ZootScene scene)
    {
		return sceneScreenSupplier.apply(this, scene);
    }
    
    public ZootInputManager getInputManager()
    {
    	return inputManager;
    }
}
