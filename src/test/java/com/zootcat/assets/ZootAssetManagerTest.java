package com.zootcat.assets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.headless.HeadlessFiles;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.zootcat.gfx.ZootAnimationFile;
import com.zootcat.map.tiled.ZootTiledMap;
import com.zootcat.testing.ZootTestUtils;
import com.zootcat.tools.physicsbodyeditor.PhysicsBodyEditorModel;

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
	public void shouldHaveLoaders()
	{
		assertNotNull(assetManager.getLoader(TiledMap.class));
		assertNotNull(assetManager.getLoader(ZootTiledMap.class));
		assertNotNull(assetManager.getLoader(ZootAnimationFile.class));
		assertNotNull(assetManager.getLoader(PhysicsBodyEditorModel.class));
	}
	
	@Test
	public void shouldRecognizeFileExtensions()
	{
		ZootAssetRecognizer assetRecognizer = assetManager.getAssetRecognizer(); 
		
		assertNotNull(assetRecognizer);
		assertEquals(Texture.class, assetRecognizer.getAssetType("texture.png"));
		assertEquals(Texture.class, assetRecognizer.getAssetType("texture.jpg"));
		assertEquals(Texture.class, assetRecognizer.getAssetType("texture.bmp"));
		assertEquals(Sound.class, assetRecognizer.getAssetType("sound.wav"));
		assertEquals(Music.class, assetRecognizer.getAssetType("music.ogg"));
		assertEquals(Music.class, assetRecognizer.getAssetType("music.mp3"));
		assertEquals(ZootAnimationFile.class, assetRecognizer.getAssetType("animation.anm"));
		assertEquals(PhysicsBodyEditorModel.class, assetRecognizer.getAssetType("fixtureModel.json"));
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
