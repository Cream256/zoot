package com.zootcat.scene.tiled;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TiledMapTileSets;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.zootcat.map.tiled.ZootTiledMap;

public class ZootTiledMapTest 
{	
	private TiledMap tiledMapMock;
	private TiledMapTileSets tilesets;
	private MapLayers mapLayers;
	private MapLayer layer1;
	private MapLayer layer2;
	private ZootTiledMap map;
	private MapProperties tiledMapProperties;
	
	@Before
	public void setup()
	{				
		tilesets = new TiledMapTileSets();
		tiledMapMock = mock(TiledMap.class);	
		tiledMapProperties = new MapProperties();
		
		layer1 = new MapLayer();
		layer2 = new MapLayer();
		mapLayers = new MapLayers();
		mapLayers.add(layer1);
		mapLayers.add(layer2);
		
		when(tiledMapMock.getLayers()).thenReturn(mapLayers);
		when(tiledMapMock.getTileSets()).thenReturn(tilesets);
		when(tiledMapMock.getProperties()).thenReturn(tiledMapProperties);
		
		map = new ZootTiledMap(tiledMapMock);
	}
	
	@Test
	public void disposeTest()
	{
		map.dispose();
		assertNull(map.getTiledMap());
		verify(tiledMapMock, times(1)).dispose();
	}
	
	@Test
	public void getTileWidthTest()
	{
		tiledMapProperties.put(ZootTiledMap.TILE_WIDTH_PROPERTY, 32);
		assertEquals(32, map.getTileWidth());
	}
	
	@Test
	public void getTileHeightTest()
	{
		tiledMapProperties.put(ZootTiledMap.TILE_HEIGHT_PROPERTY, 48);
		assertEquals(48, map.getTileHeight());
	}
	
	@Test
	public void getMapWidthTest()
	{
		tiledMapProperties.put(ZootTiledMap.MAP_WIDTH_PROPERTY, 256);
		assertEquals(256, map.getMapWidth());
	}
	
	@Test
	public void getMapHeightTest()
	{
		tiledMapProperties.put(ZootTiledMap.MAP_HEIGHT_PROPERTY, 128);
		assertEquals(128, map.getMapHeight());
	}
	
	@Test
	public void getBackgroundColorTest()
	{		
		tiledMapProperties.put(ZootTiledMap.BACKGROUND_COLOR_PROPERTY, "#FF0000");
		assertEquals(Color.RED, map.getBackgroundColor());
	}
	
	@Test
	public void getTilesetsTest()
	{
		assertEquals(tilesets, map.getTilesets());
	}
	
	@Test
	public void getObjectByIdShouldReturnNullForNotExistingIdTest()
	{
		assertNull(map.getObjectById(0));
	}
	
	@Test
	public void getObjectByIdTest()
	{
		//given
		MapObject obj1 = new MapObject();
		MapObject obj2 = new MapObject();
		MapObject obj3 = new MapObject();
		
		obj1.getProperties().put("id", 1);
		obj2.getProperties().put("id", 2);
		obj3.getProperties().put("id", 3);

		//when
		layer1.getObjects().add(obj1);
		layer2.getObjects().add(obj2);
		layer2.getObjects().add(obj3);
		
		//then
		assertEquals(obj1, map.getObjectById(1));
		assertEquals(obj2, map.getObjectById(2));
		assertEquals(obj3, map.getObjectById(3));
	}
	
	@Test
	public void getAllObjectsShouldReturnValidObjects()
	{
		//given
		MapObject mapObject = new MapObject();
		mapObject.setName("Object");
		mapObject.setColor(Color.RED);
		mapObject.setOpacity(0.5f);
		mapObject.setVisible(false);		
		mapLayers.get(0).getObjects().add(mapObject);
		
		//when
		List<MapObject> mapObjects = map.getAllObjects();
		
		//then
		assertEquals(1, mapObjects.size());
		assertEquals("Object", mapObjects.get(0).getName());
		assertEquals(Color.RED, mapObjects.get(0).getColor());
		assertEquals(0.5f, mapObjects.get(0).getOpacity(), 0.0f);
		assertEquals(false, mapObjects.get(0).isVisible());
	}
	
	@Test
	public void getObjectsShouldReturnObjectWithCombinedProperties()
	{
		//given
		MapObject mapObject = new MapObject();
		mapObject.setName("Obj");
		mapObject.getProperties().put("LocalProp", "1");
		mapObject.getProperties().put("GlobalProp", "A");
		mapObject.getProperties().put("gid", 1);		
		mapLayers.get(0).getObjects().add(mapObject);
		
		TiledMapTile tile = new StaticTiledMapTile(mock(TextureRegion.class));
		tile.setId(1);
		tile.getProperties().put("UniqueProperty", "U");
		tile.getProperties().put("GlobalProp", "B");
		
		TiledMapTileSet tileset = new TiledMapTileSet();
		tileset.putTile(1, tile);
		
		tilesets.addTileSet(tileset);
		
		//when
		List<MapObject> mapObjects = map.getAllObjects();
		
		//then
		assertEquals(1, mapObjects.size());
		
		MapObject obj = mapObjects.get(0);
		assertEquals("1", obj.getProperties().get("LocalProp"));
		assertEquals("Global property should be overriden by local one", "A", obj.getProperties().get("GlobalProp"));
		assertEquals(1, obj.getProperties().get("gid"));
		assertEquals("U", obj.getProperties().get("UniqueProperty"));		
	}

	@Test
	public void getObjectsShouldReturnObjectsFromAllLayers()
	{
		//given
		MapObject obj1 = new MapObject();
		obj1.setName("Obj1");
		
		MapObject obj2 = new MapObject();
		obj2.setName("Obj2");
		
		mapLayers.get(0).getObjects().add(obj1);
		mapLayers.get(1).getObjects().add(obj2);
		
		//when
		List<MapObject> mapObjects = map.getAllObjects();
		
		//then
		assertEquals(2, mapObjects.size());
		assertEquals("Obj1", mapObjects.get(0).getName());
		assertEquals("Obj2", mapObjects.get(1).getName());
	}
}
