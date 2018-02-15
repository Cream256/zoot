package com.zootcat.assets;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.zootcat.gfx.ZootAnimationFile;
import com.zootcat.map.tiled.ZootTiledMap;

public class ZootAssetManagerTest
{
	@Test
	public void zootSpecificLoadersShouldBePresentTest()
	{
		ZootAssetManager assetManager = new ZootAssetManager();
		
		assertNotNull(assetManager.getLoader(TiledMap.class));
		assertNotNull(assetManager.getLoader(ZootTiledMap.class));
		assertNotNull(assetManager.getLoader(ZootAnimationFile.class));
	}
}
