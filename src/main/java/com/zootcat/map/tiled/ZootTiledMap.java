package com.zootcat.map.tiled;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TiledMapTileSets;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.zootcat.map.ZootMap;

public class ZootTiledMap implements ZootMap
{
	public static final String COLLISION_LAYER_NAME = "Collision";
	public static final String BACKGROUND_COLOR_PROPERTY = "backgroundcolor";
	public static final String TILE_WIDTH_PROPERTY = "tilewidth";
	public static final String TILE_HEIGHT_PROPERTY = "tileheight";
	public static final String MAP_WIDTH_PROPERTY = "width";
	public static final String MAP_HEIGHT_PROPERTY = "height";
		
	private TiledMap tiledMap;
	
	public ZootTiledMap(final TiledMap tiledMap)
	{
		this.tiledMap = tiledMap;
	}
	
	@Override
	public int getTileWidth()
	{		
		return tiledMap.getProperties().get(TILE_WIDTH_PROPERTY, Integer.class);
	}
	
	@Override
	public int getTileHeight()
	{
		return tiledMap.getProperties().get(TILE_HEIGHT_PROPERTY, Integer.class);
	}
	
	@Override
	public Color getBackgroundColor() 
	{
		return Color.valueOf(tiledMap.getProperties().get(BACKGROUND_COLOR_PROPERTY, String.class));
	}
	
	public int getMapWidth()
	{
		return tiledMap.getProperties().get("width", Integer.class);
	}

	public int getMapHeight()
	{
		return tiledMap.getProperties().get("height", Integer.class);
	}
	
	@Override
	public TiledMapTileSets getTilesets()
	{
		return tiledMap.getTileSets();
	}

	public TiledMap getTiledMap()
	{
		return tiledMap;
	}
	
	@Override
	public MapObject getObjectById(int id)
	{
		for(MapLayer layer : tiledMap.getLayers())
		{
			for(MapObject obj : layer.getObjects())
			{
				if(obj.getProperties().get("id", -1, Integer.class) == id)
				{
					return obj;
				}
			}
		}
		return null;
	}
	
	@Override
	public List<MapObject> getAllObjects()
	{
		List<MapObject> result = new ArrayList<MapObject>();
		for(MapLayer layer : tiledMap.getLayers())
		{
			for(MapObject obj : layer.getObjects())
			{
				MapObject objectWithProperties = createMapObjectWithProperties(obj, layer);
				result.add(objectWithProperties);
			}
		}
		return result;
	}

	@Override
	public TiledMapTileLayer getLayer(String layerName)
	{
		return (TiledMapTileLayer) tiledMap.getLayers().get(layerName);		
	}
	
	@Override
	public List<ZootTiledMapCell> getLayerCells(String layerName)
	{
		if(!ClassReflection.isInstance(TiledMapTileLayer.class, tiledMap.getLayers().get(layerName)))
		{
			return Collections.<ZootTiledMapCell>emptyList();
		}
		
		TiledMapTileLayer layer = getLayer(layerName);
		int layerWidth = layer.getWidth();
		int layerHeight = layer.getHeight();
		
		List<ZootTiledMapCell> result = new ArrayList<ZootTiledMapCell>();
		for(int row = 0; row < layerHeight; ++row)
		{
			for(int col = 0; col < layerWidth; ++col)
			{
				Cell cell = layer.getCell(col, row);
				if(cell == null)
				{
					continue;
				}
				result.add(new ZootTiledMapCell(col, row, layer.getTileWidth(), layer.getTileHeight(), cell));
			}
		}
		return result;
	}
	
	@Override
	public void dispose() 
	{
		if(tiledMap != null)
		{
			tiledMap.dispose();
			tiledMap = null;
		}
	}
	
	private MapObject createMapObjectWithProperties(final MapObject obj, final MapLayer layer) 
	{
		boolean isPolygon = ClassReflection.isInstance(PolygonMapObject.class, obj);				
		MapObject result = isPolygon ? new PolygonMapObject(((PolygonMapObject)obj).getPolygon()) : new MapObject();
		
		result.setName(obj.getName());
		result.setColor(obj.getColor());
		result.setOpacity(obj.getOpacity());
		result.setVisible(obj.isVisible());
		
		//add tile properties
		int gid = obj.getProperties().get("gid", -1, Integer.class);
		TiledMapTile tile = tiledMap.getTileSets().getTile(gid);
		if(tile != null)
		{
			result.getProperties().putAll(tile.getProperties());	
		}
		result.getProperties().putAll(obj.getProperties());
				
		return result;
	}
}
