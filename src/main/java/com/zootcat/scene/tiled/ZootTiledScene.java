package com.zootcat.scene.tiled;

import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.zootcat.camera.ZootCamera;
import com.zootcat.controllers.factory.ControllerFactory;
import com.zootcat.hud.ZootHud;
import com.zootcat.map.tiled.ZootTiledMap;
import com.zootcat.map.tiled.ZootTiledMapRender;
import com.zootcat.map.tiled.ZootTiledMapRenderConfig;
import com.zootcat.map.tiled.ZootTiledSceneActorFactory;
import com.zootcat.map.tiled.ZootTiledWorldScaleCalculator;
import com.zootcat.map.tiled.optimizer.ZootLayerOptimizer;
import com.zootcat.map.tiled.optimizer.ZootLayerRegion;
import com.zootcat.map.tiled.optimizer.ZootTiledCellTileComparator;
import com.zootcat.physics.ZootPhysics;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootStageScene;

public class ZootTiledScene extends ZootStageScene
{
	private static final float FIXED_TIME_STEP = 1.0f / 60.0f;
	private static final float MIN_TIME_STEP = 1.0f / 4.0f;
		
	private ZootTiledMap map;	
	private AssetManager assetManager;
	private ControllerFactory ctrlFactory;
	//private ZootTiledSceneActorSpawner spawner;
	
	private float unitScale;
	private float timeAccumulator = 0.0f;	
	
	private Box2DDebugRenderer debugRender;
	
	public ZootTiledScene(ZootTiledMap map, AssetManager assetManager, ControllerFactory factory, Viewport viewport)
	{						
		super(viewport);
		this.map = map;
		this.ctrlFactory = factory;
		this.assetManager = assetManager;
		this.unitScale = ZootTiledWorldScaleCalculator.calculate(map.getUnitPerTile(), map.getTileWidth());
    	createScene();
	}
	
	private void createScene()
	{
		//hud
		setHud(new ZootHud());
		
		//physics
		setPhysics(new ZootPhysics());
    	
		//render
    	ZootTiledMapRenderConfig renderConfig = new ZootTiledMapRenderConfig();
		renderConfig.renderRectangleObjects = false;
		renderConfig.renderTextureObjects = false;	
		renderConfig.unitScale = unitScale;
		setRender(new ZootTiledMapRender(map, renderConfig));
				
		//stage
		ZootCamera camera = new ZootCamera(map.getMapWidth() * map.getUnitPerTile(), map.getMapHeight() * map.getUnitPerTile());
		camera.setViewportSize(getViewport().getWorldWidth(), getViewport().getWorldHeight());
		setCamera(camera);
				
		getViewport().setCamera(camera);
		
		//actor factory
    	ZootTiledSceneActorFactory actorFactory = new ZootTiledSceneActorFactory(this);
    	
		//cell actors
    	TiledMapTileLayer collisionLayer = map.getLayer(ZootTiledMap.COLLISION_LAYER_NAME);
    	
    	List<ZootLayerRegion> cellRegions = ZootLayerOptimizer.optimize(collisionLayer, new ZootTiledCellTileComparator());			
		List<ZootActor> cellActors = actorFactory.createFromLayerRegions(cellRegions);		
		cellActors.forEach(cellActor -> getStage().addActor(cellActor));
		
		//object actors
		List<ZootActor> actors = actorFactory.createFromMapObjects(map.getAllObjects());		
		actors.forEach(actor -> addActor(actor));
		
    	//actor spawner for spawning actors after scene have been created
    	//spawner = new ZootTiledSceneActorSpawner(map, actorFactory);
		
		//debug
		debugRender = new Box2DDebugRenderer();
	}
		
	public ZootTiledMap getTiledMap()
	{
		return map;
	}
	
	@Override
	public void update(float delta)
	{		
		timeAccumulator += Math.min(MIN_TIME_STEP, delta);       
		while(timeAccumulator >= FIXED_TIME_STEP)
		{
			getStage().act(FIXED_TIME_STEP);
			getPhysics().step(FIXED_TIME_STEP);
			timeAccumulator -= FIXED_TIME_STEP;
		}
		getCamera().update(delta, true);
		getHud().update(delta);
	}
	
	@Override
	public void render(float delta)
	{			
		getRender().setView((OrthographicCamera)getCamera());
		getRender().render(delta);
		getStage().draw();
		
		if(isDebugMode())
		{
			debugRender.setDrawAABBs(false);
			debugRender.setDrawBodies(true);
			debugRender.setDrawInactiveBodies(true);
			debugRender.setDrawJoints(true);
			debugRender.render(getPhysics().getWorld(), getCamera().combined);
		}
		
		getHud().render();
	}

	@Override
	public void dispose() 
	{		
		if(map != null)
		{
			map.dispose();
			map = null;
		}
		super.dispose();
	}

	
	@Override
	public float getUnitScale()
	{
		return unitScale;
	}
			
	public ControllerFactory getControllerFactory()
	{
		return ctrlFactory;
	}
	
	public AssetManager getAssetManager()
	{
		return assetManager;
	}

	/*
	@Override
	public ZootSceneActorSpawner getActorSpawner()
	{
		return spawner;
	}
	*/
}
