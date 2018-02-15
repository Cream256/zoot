package com.zootcat.map.tiled.optimizer;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

public class ZootTiledCellTileComparator implements ZootTiledCellComparator
{
	@Override
	public boolean areEqual(Cell a, Cell b)
	{
		return a.getTile() == b.getTile();
	}
}
