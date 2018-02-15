package com.zootcat.scene.tiled;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.zootcat.camera.ZootCamera;
import com.zootcat.controllers.factory.ControllerFactory;
import com.zootcat.gfx.ZootRender;
import com.zootcat.map.ZootMap;
import com.zootcat.map.tiled.ZootTiledMap;
import com.zootcat.map.tiled.ZootTiledMapActorFactory;
import com.zootcat.map.tiled.ZootTiledMapRender;
import com.zootcat.map.tiled.ZootTiledMapRenderConfig;
import com.zootcat.map.tiled.ZootTiledWorldScaleCalculator;
import com.zootcat.map.tiled.optimizer.ZootLayerOptimizer;
import com.zootcat.map.tiled.optimizer.ZootLayerRegion;
import com.zootcat.map.tiled.optimizer.ZootTiledCellTileComparator;
import com.zootcat.physics.ZootPhysics;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;

public class ZootTiledScene implements ZootScene
{
	private static final float FIXED_TIME_STEP = 1.0f / 60.0f;
	private static final float MIN_TIME_STEP = 1.0f / 4.0f;
		
	private Stage stage;
	private ZootTiledMap map;
	private ZootPhysics physics;	
	private AssetManager assetManager;
	private ZootCamera camera;
	private ZootTiledMapRender mapRender;
	private ControllerFactory ctrlFactory;	
	
	private float unitScale;
	private float worldUnitPerTile;
	private float viewportWidth;
	private float viewportHeight;
	private float timeAccumulator = 0.0f;	
	
	private boolean isDebugMode;
	private Box2DDebugRenderer debugRender;
	
	public ZootTiledScene(ZootTiledMap map, AssetManager assetManager, ControllerFactory factory, float viewportWidth, float viewportHeight, float worldUnitPerTile)
	{						
    	this.worldUnitPerTile = worldUnitPerTile;
		this.unitScale = ZootTiledWorldScaleCalculator.calculate(worldUnitPerTile, map.getTileWidth());
    	this.viewportWidth = viewportWidth;
    	this.viewportHeight = viewportHeight;
    	this.assetManager = assetManager;
    	this.ctrlFactory = factory;
    	this.map = map;    	
    	createScene();
	}
	
	@Override
	public ZootCamera getCamera()
	{
		return camera;
	}
	
	@Override
	public ZootPhysics getPhysics()
	{
		return physics;
	}
	
	@Override
	public ZootRender getRender() 
	{
		return mapRender;
	}
	
	@Override
	public ZootMap getMap()
	{
		return map;
	}
	
	@Override
	public void addActor(ZootActor zootActor)
	{
		stage.addActor(zootActor);
	}
	
	@Override
	public void removeActor(ZootActor actor) 
	{
		if(actor.getParent().equals(stage))
		{
			actor.remove();
		}
	}

	@Override
	public List<ZootActor> getActors() 
	{		
		return getActors((act) -> true);
	}
	
	@Override
	public List<ZootActor> getActors(Predicate<Actor> filter) 
	{				
		return StreamSupport.stream(stage.getActors().spliterator(), false)
							 .filter(filter)
							 .map((act) -> (ZootActor)act)
							 .collect(Collectors.toList());
	}
	
	@Override
	public void update(float delta)
	{		
		timeAccumulator += Math.min(MIN_TIME_STEP, delta);       
		while(timeAccumulator >= FIXED_TIME_STEP)
		{
			stage.act(FIXED_TIME_STEP);
			physics.step(FIXED_TIME_STEP);
			timeAccumulator -= FIXED_TIME_STEP;
		}
		camera.update(delta, true);
	}
	
	@Override
	public void render(float delta)
	{			
		mapRender.setView((OrthographicCamera)getCamera());
		mapRender.render(delta);
		stage.draw();
		
		if(isDebugMode())
		{
			debugRender.setDrawAABBs(false);
			debugRender.setDrawBodies(true);
			debugRender.setDrawInactiveBodies(true);
			debugRender.setDrawJoints(true);
			debugRender.render(physics.getWorld(), getCamera().combined);
		}
	}

	@Override
	public void dispose() 
	{
		disposeSceneResources();
		
		if(map != null)
		{
			map.dispose();
			map = null;
		}
	}

	@Override
	public boolean isDebugMode() 
	{
		return isDebugMode;
	}
	
	@Override
	public void setDebugMode(boolean debug)
	{
		isDebugMode = debug;
	}

	@Override
	public void setFocusedActor(ZootActor actor)
	{
		stage.setKeyboardFocus(actor);
	}

	@Override
	public void resize(int width, int height) 
	{		
		//noop
	}

	@Override
	public void addListener(EventListener listener) 
	{
		stage.addListener(listener);
	}

	@Override
	public void removeListener(EventListener listener) 
	{
		stage.removeListener(listener);
	}

	@Override
	public InputProcessor getInputProcessor() 
	{
		return stage;
	}

	@Override
	public Viewport getViewport()
	{
		return stage.getViewport();
	}
	
	@Override
	public float getUnitScale()
	{
		return unitScale;
	}
	
	@Override
	public void reload()
	{
		disposeSceneResources();
		createScene();
	}
	
	private void createScene()
	{
		//physics
    	physics = new ZootPhysics();
    	
		//render
    	ZootTiledMapRenderConfig renderConfig = new ZootTiledMapRenderConfig();
		renderConfig.renderRectangleObjects = false;
		renderConfig.renderTextureObjects = false;	
		renderConfig.unitScale = unitScale;
		mapRender = new ZootTiledMapRender(map, renderConfig);
				
		//stage
		camera = new ZootCamera(map.getMapWidth() * worldUnitPerTile, map.getMapHeight() * worldUnitPerTile);		
		Viewport viewport = new StretchViewport(viewportWidth, viewportHeight, camera);
		stage = new Stage(viewport);
		
		//cell actors
    	ZootTiledMapActorFactory actorFactory = new ZootTiledMapActorFactory(this, ctrlFactory, assetManager);		
    	TiledMapTileLayer collisionLayer = map.getLayer(ZootTiledMap.COLLISION_LAYER_NAME);
    	
    	List<ZootLayerRegion> cellRegions = ZootLayerOptimizer.optimize(collisionLayer, new ZootTiledCellTileComparator());			
		List<ZootActor> cellActors = actorFactory.createFromLayerRegions(cellRegions);		
		cellActors.forEach(cellActor -> stage.addActor(cellActor));
		
		//object actors
		List<ZootActor> actors = actorFactory.createFromMapObjects(map.getAllObjects());		
		actors.forEach(actor -> stage.addActor(actor));
		
		//debug
		isDebugMode = false;
		debugRender = new Box2DDebugRenderer();
	}
	
	private void disposeSceneResources()
	{
		if(mapRender != null)
		{
			mapRender.dispose();
			mapRender = null;
		}
		
		if(stage != null)
		{
			stage.dispose();
			stage = null;
		}
		
		if(debugRender != null)
		{
			debugRender.dispose();
			debugRender = null;
		}
		
		if(physics != null)
		{
			physics.dispose();
			physics = null;
		}
	}
}
