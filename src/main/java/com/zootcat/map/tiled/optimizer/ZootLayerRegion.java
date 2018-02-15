package com.zootcat.map.tiled.optimizer;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

public class ZootLayerRegion
{
	public int x;
	public int y;
	public int width;
	public int height;
	public float tileWidth;
	public float tileHeight;
	public Cell cell;
	
	public ZootLayerRegion(int x, int y, float tileWidth, float tileHeight, Cell cell)
	{
		this.x = x;
		this.y = y;
		this.width = 1;
		this.height = 1;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.cell = cell;
	}
}
