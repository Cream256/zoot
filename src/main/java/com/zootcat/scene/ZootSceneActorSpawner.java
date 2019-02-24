package com.zootcat.scene;

import com.badlogic.gdx.math.Vector2;

//TODO REFACTOR, this should be refactored to be more generic than Tiled specific 
public interface ZootSceneActorSpawner
{
	ZootActor spawn(String tilesetName, int tileId, final Vector2 position);
	ZootActor spawn(String tilesetName, int tileId, final Vector2 position, final Vector2 velocity);	
}