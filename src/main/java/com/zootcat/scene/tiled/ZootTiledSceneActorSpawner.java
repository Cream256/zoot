package com.zootcat.scene.tiled;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.map.tiled.ZootTiledSceneActorFactory;
import com.zootcat.scene.ZootActor;

public class ZootTiledSceneActorSpawner
{
	private ZootTiledScene scene;
	private ZootTiledSceneActorFactory actorFactory;
		
	public ZootTiledSceneActorSpawner(ZootTiledScene scene)
	{
		this.scene = scene;
		actorFactory = new ZootTiledSceneActorFactory(scene);
	}
	
	public ZootActor spawn(String tilesetName, int tileId)
	{				
		TiledMapTile tile = scene.getMap().getTile(tilesetName, tileId);
		if(tile == null)
		{
			throw new RuntimeZootException("Unable to spawn actor with tile id " + tileId);
		}
		
		return actorFactory.createFromTile(tile);
	}
}
