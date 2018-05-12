package com.zootcat.scene.tiled;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Vector2;
import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.map.tiled.ZootTiledMap;
import com.zootcat.map.tiled.ZootTiledSceneActorFactory;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootSceneActorSpawner;

public class ZootTiledSceneActorSpawner implements ZootSceneActorSpawner
{
	private ZootTiledMap map;
	private ZootTiledSceneActorFactory actorFactory;
		
	public ZootTiledSceneActorSpawner(ZootTiledMap map, ZootTiledSceneActorFactory factory)
	{
		this.map = map;
		this.actorFactory = factory;
	}
	
	@Override
	public ZootActor spawn(String tilesetName, int tileId, final Vector2 position)
	{
		return spawn(tilesetName, tileId, position, null);
	}
	
	@Override
	public ZootActor spawn(String tilesetName, int tileId, final Vector2 position, final Vector2 velocity)
	{				
		TiledMapTile tile = map.getTile(tilesetName, tileId);
		if(tile == null)
		{
			throw new RuntimeZootException("Unable to spawn actor with tile id " + tileId);
		}
		
		ZootActor spawnedActor = actorFactory.createFromTile(tile); 		
		spawnedActor.controllerAction(PhysicsBodyController.class, ctrl -> 
		{				
			if(position != null) ctrl.setPosition(position.x, position.y);
			if(velocity != null) ctrl.setVelocity(velocity.x, velocity.y);
		});
		return spawnedActor;
	}
}