package com.zootcat.scene.tiled;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TiledMapTileSets;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.map.tiled.ZootTiledMap;
import com.zootcat.scene.ZootActor;

public class ZootTiledSceneActorSpawnerTest
{		
	@Mock private ZootTiledMap zootTiledMap;
	@Mock private ZootTiledSceneActorFactory actorFactory;
		
	private ZootTiledSceneActorSpawner spawner;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);		
		
		spawner = new ZootTiledSceneActorSpawner(zootTiledMap, actorFactory);
	}
	
	@Test
	public void shouldThrowIfTileIdIsNotFoundInTileset()
	{
		try
		{		
			//given
			final String tilesetName = "Existing tileset";
			TiledMapTileSets tilesets = mock(TiledMapTileSets.class);
			
			//when
			when(zootTiledMap.getTilesets()).thenReturn(tilesets);
			when(tilesets.getTileSet(tilesetName)).thenReturn(mock(TiledMapTileSet.class));						
			spawner.spawn(tilesetName, 0, new Vector2());
		}
		catch(RuntimeZootException e)
		{
			//then
			assertEquals("Unable to spawn actor with tile id 0", e.getMessage());			
		}
	}
	
	@Test
	public void shouldSpawnActor()
	{
		//given
		final int expectedTileId = 1;
		final String tilesetName = "Tileset";
		
		//when
		StaticTiledMapTile tile = new StaticTiledMapTile(mock(TextureRegion.class));
		when(zootTiledMap.getTile(tilesetName, expectedTileId)).thenReturn(tile);
		when(actorFactory.createFromTile(tile)).thenReturn(new ZootActor());
				
		//then
		ZootActor actor = spawner.spawn(tilesetName, expectedTileId, new Vector2());
		assertNotNull(actor);
	}
}
