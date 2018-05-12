package com.zootcat.scene;

import com.badlogic.gdx.math.Vector2;

public interface ZootSceneActorSpawner
{
	ZootActor spawn(String tilesetName, int tileId, final Vector2 position);
	ZootActor spawn(String tilesetName, int tileId, final Vector2 position, final Vector2 velocity);	
}