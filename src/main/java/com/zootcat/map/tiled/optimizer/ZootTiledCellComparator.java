package com.zootcat.map.tiled.optimizer;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

public interface ZootTiledCellComparator
{
	boolean areEqual(Cell a, Cell b);
}
