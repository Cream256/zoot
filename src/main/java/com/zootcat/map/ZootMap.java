package com.zootcat.map;

import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSets;
import com.badlogic.gdx.utils.Disposable;
import com.zootcat.map.tiled.ZootTiledMapCell;

public interface ZootMap extends Disposable
{
	int getTileWidth();
	int getTileHeight();
	Color getBackgroundColor();	
	
	MapLayer getLayer(String layerName);
	TiledMapTileSets getTilesets();
	
	MapObject getObjectById(int id);
	List<MapObject> getAllObjects();
	List<ZootTiledMapCell> getLayerCells(String layerName);	
}