package com.zootcat.scene.tiled;

import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
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
	private ZootTiledMap map;	
	private AssetManager assetManager;
	private ControllerFactory ctrlFactory;	
	private float unitScale;	
	//private ZootTiledSceneActorSpawner spawner;
		
	public ZootTiledScene(ZootTiledMap map, AssetManager assetManager, ControllerFactory factory, Viewport viewport)
	{						
		super(viewport);
		this.map = map;
		this.ctrlFactory = factory;
		this.assetManager = assetManager;
		this.unitScale = ZootTiledWorldScaleCalculator.calculate(map.getPhysicsUnitPerTile(), map.getTileWidth());
    	createScene();
	}
	
	private void createScene()
	{
		//hud
		setHud(new ZootHud());
		
		//physics
		setPhysics(new ZootPhysics());
    	
		//tiled map render
    	ZootTiledMapRenderConfig renderConfig = new ZootTiledMapRenderConfig();
		renderConfig.renderRectangleObjects = false;
		renderConfig.renderTextureObjects = false;	
		renderConfig.unitScale = unitScale;
		setRender(new ZootTiledMapRender(map, renderConfig));
				
		//camera
		float physicalWorldWidth = map.getMapWidth() * map.getPhysicsUnitPerTile();
		float physicalWorldHeight = map.getMapHeight() * map.getPhysicsUnitPerTile();		
		ZootCamera camera = new ZootCamera(physicalWorldWidth, physicalWorldHeight);				
		setCamera(camera);
		
		//viewport
		camera.setViewportSize(getViewport().getWorldWidth(), getViewport().getWorldHeight());
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
		
		Box2DDebugRenderer debugRender = new Box2DDebugRenderer();
		debugRender.setDrawAABBs(false);
		debugRender.setDrawBodies(true);
		debugRender.setDrawInactiveBodies(true);
		debugRender.setDrawJoints(true);
		setDebugRender(debugRender);
	}
		
	public ZootTiledMap getTiledMap()
	{
		return map;
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
