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
import com.zootcat.assets.ZootAssetManager;
import com.zootcat.controllers.factory.ControllerFactory;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.map.tiled.ZootTiledMap;
import com.zootcat.scene.ZootActor;

public class ZootTiledSceneActorSpawnerTest
{		
	@Mock private ZootTiledScene zootTiledScene;
	@Mock private ZootTiledMap zootTiledMap;
	@Mock private ZootAssetManager zootAssetManager;
	@Mock private ControllerFactory ctrlFactory;
		
	private ZootTiledSceneActorSpawner spawner;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);		
		when(zootTiledScene.getAssetManager()).thenReturn(zootAssetManager);
		when(zootTiledScene.getControllerFactory()).thenReturn(ctrlFactory);
		when(zootTiledScene.getUnitScale()).thenReturn(1.0f);
		when(zootTiledScene.getMap()).thenReturn(zootTiledMap);
		
		spawner = new ZootTiledSceneActorSpawner(zootTiledScene);
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
			spawner.spawn(tilesetName, 0);
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
				
		//then
		ZootActor actor = spawner.spawn(tilesetName, expectedTileId);
		assertNotNull(actor);
	}
}
