package com.zootcat.map.tiled;

public class ZootTiledWorldScaleCalculator 
{
	public static float calculate(float tilePerUnit, float tileSize)
	{		
		float singleTileScale = 1.0f / tileSize;
		return singleTileScale * tilePerUnit;
	}
}
