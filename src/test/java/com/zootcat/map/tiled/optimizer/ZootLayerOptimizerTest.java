package com.zootcat.map.tiled.optimizer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.zootcat.assets.ZootAssetRecognizer;
import com.zootcat.map.ZootMap;
import com.zootcat.map.tiled.ZootTiledMapLoader;
import com.zootcat.testing.WindowedGdxTestRunner;
import com.zootcat.testing.ZootTestUtils;

@RunWith(WindowedGdxTestRunner.class)
public class ZootLayerOptimizerTest
{	
	@Test
	public void optimizeSingleVerticalTilesTest()
	{
		//given
		String mapFile = ZootTestUtils.getResourcePath("testResources/tiled/SingleVerticalTiles.tmx", this);		
		ZootMap map = new ZootTiledMapLoader(new ZootAssetRecognizer()).load(mapFile);		
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayer("Layer1");
		ZootTiledCellTileComparator comparator = new ZootTiledCellTileComparator();		
		
		//when
		List<ZootLayerRegion> regions = ZootLayerOptimizer.optimize(layer, comparator);
		
		//then		
		assertNotNull(regions);
		assertEquals(1, regions.size());
		assertEquals(3, regions.get(0).x);
		assertEquals(5, regions.get(0).y);
		assertEquals(4, regions.get(0).width);
		assertEquals(1, regions.get(0).height);
		assertEquals(32.0f, regions.get(0).tileWidth, 0.0f);
		assertEquals(32.0f, regions.get(0).tileHeight, 0.0f);
	}
	
	@Test
	public void optimizeBoxTilesTest()
	{
		//given
		String mapFile = ZootTestUtils.getResourcePath("testResources/tiled/BoxTiles.tmx", this);		
		ZootMap map = new ZootTiledMapLoader(new ZootAssetRecognizer()).load(mapFile);		
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayer("Layer1");
		ZootTiledCellTileComparator comparator = new ZootTiledCellTileComparator();		
		
		//when
		List<ZootLayerRegion> regions = ZootLayerOptimizer.optimize(layer, comparator);		
		
		//then
		assertNotNull(regions);
		assertEquals(8, regions.size());
		assertEquals(2, regions.get(0).x);
		assertEquals(3, regions.get(0).y);
		assertEquals(1, regions.get(0).width);
		assertEquals(3, regions.get(0).height);
		assertEquals(32.0f, regions.get(0).tileWidth, 0.0f);
		assertEquals(32.0f, regions.get(0).tileHeight, 0.0f);
		
		//box
		assertEquals(2, regions.get(3).x);
		assertEquals(7, regions.get(3).y);
		assertEquals(3, regions.get(3).width);
		assertEquals(3, regions.get(3).height);	
		assertEquals(32.0f, regions.get(3).tileWidth, 0.0f);
		assertEquals(32.0f, regions.get(3).tileHeight, 0.0f);
	}
	
	@Test
	public void optimizeVerticalTilesTest()
	{
		//given
		String mapFile = ZootTestUtils.getResourcePath("testResources/tiled/VerticalTiles.tmx", this);		
		ZootMap map = new ZootTiledMapLoader(new ZootAssetRecognizer()).load(mapFile);		
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayer("Layer1");
		ZootTiledCellTileComparator comparator = new ZootTiledCellTileComparator();		
		
		//when
		List<ZootLayerRegion> regions = ZootLayerOptimizer.optimize(layer, comparator);
		
		//then		
		assertNotNull(regions);
		assertEquals(6, regions.size());
		
		assertEquals(0, regions.get(0).x);
		assertEquals(0, regions.get(0).y);
		assertEquals(2, regions.get(0).width);
		assertEquals(1, regions.get(0).height);
		assertEquals(32.0f, regions.get(0).tileWidth, 0.0f);
		assertEquals(32.0f, regions.get(0).tileHeight, 0.0f);
		
		assertEquals(4, regions.get(1).x);
		assertEquals(0, regions.get(1).y);
		assertEquals(2, regions.get(1).width);
		assertEquals(1, regions.get(1).height);
		assertEquals(32.0f, regions.get(1).tileWidth, 0.0f);
		assertEquals(32.0f, regions.get(1).tileHeight, 0.0f);
		
		assertEquals(8, regions.get(2).x);
		assertEquals(0, regions.get(2).y);
		assertEquals(2, regions.get(2).width);
		assertEquals(1, regions.get(2).height);
		assertEquals(32.0f, regions.get(2).tileWidth, 0.0f);
		assertEquals(32.0f, regions.get(2).tileHeight, 0.0f);
		
		assertEquals(0, regions.get(3).x);
		assertEquals(3, regions.get(3).y);
		assertEquals(3, regions.get(3).width);
		assertEquals(1, regions.get(3).height);
		assertEquals(32.0f, regions.get(3).tileWidth, 0.0f);
		assertEquals(32.0f, regions.get(3).tileHeight, 0.0f);
		
		assertEquals(3, regions.get(4).x);
		assertEquals(3, regions.get(4).y);
		assertEquals(4, regions.get(4).width);
		assertEquals(1, regions.get(4).height);
		assertEquals(32.0f, regions.get(4).tileWidth, 0.0f);
		assertEquals(32.0f, regions.get(4).tileHeight, 0.0f);
		
		assertEquals(7, regions.get(5).x);
		assertEquals(3, regions.get(5).y);
		assertEquals(3, regions.get(5).width);
		assertEquals(1, regions.get(5).height);
		assertEquals(32.0f, regions.get(5).tileWidth, 0.0f);
		assertEquals(32.0f, regions.get(5).tileHeight, 0.0f);
	}
}