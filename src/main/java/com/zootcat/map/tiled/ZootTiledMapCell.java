package com.zootcat.map.tiled;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

public class ZootTiledMapCell 
{
	public final int x;
	public final int y;
	public final float width;
	public final float height;
	public final Cell cell;
	
	public ZootTiledMapCell(int x, int y, float width, float height, final Cell cell)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.cell = cell;
	}
}
