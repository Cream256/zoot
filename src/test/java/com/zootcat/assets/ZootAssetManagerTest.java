package com.zootcat.assets;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessFiles;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.zootcat.gfx.ZootAnimationFile;
import com.zootcat.map.tiled.ZootTiledMap;
import com.zootcat.testing.ZootTestUtils;

public class ZootAssetManagerTest
{
	private ZootAssetManager assetManager;
	
	@Before
	public void setup()
	{
		assetManager = new ZootAssetManager();
		Gdx.files = new HeadlessFiles();
	}
	
	@After
	public void tearDown()
	{
		Gdx.files = null;
	}
	
	@Test
	public void zootSpecificLoadersShouldBePresentTest()
	{
		assertNotNull(assetManager.getLoader(TiledMap.class));
		assertNotNull(assetManager.getLoader(ZootTiledMap.class));
		assertNotNull(assetManager.getLoader(ZootAnimationFile.class));
	}
	
	@Test
	public void shouldReturnNullWhenFilenameIsNullOrEmpty()
	{
		assertNull(assetManager.getOrLoad(null, Integer.class));
		assertNull(assetManager.getOrLoad("", Integer.class));
		assertNull(assetManager.getOrLoad(" ", Integer.class));
	}
	
	@Test
	public void shouldImmediatellyLoadAndReturnResource()
	{
		String animationFilePath = ZootTestUtils.getResourcePath("assets/AnimationFile.anm", this);
		assertNotNull(assetManager.getOrLoad(animationFilePath, ZootAnimationFile.class));
	}
}
