package com.zootcat.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.zootcat.gfx.ZootAnimationFile;
import com.zootcat.gfx.ZootAnimationFileLoader;
import com.zootcat.map.tiled.ZootTiledMap;
import com.zootcat.map.tiled.ZootTiledMapLoader;

public class ZootAssetManager extends AssetManager
{
	private ZootAssetRecognizer assetRecognizer;
	
	public ZootAssetManager()
	{
		assetRecognizer = new ZootAssetRecognizer();
		assetRecognizer.setAssetType(".png", Texture.class);
		assetRecognizer.setAssetType(".jpg", Texture.class);
		assetRecognizer.setAssetType(".bmp", Texture.class);
		assetRecognizer.setAssetType(".wav", Sound.class);		
		assetRecognizer.setAssetType(".ogg", Music.class);
		assetRecognizer.setAssetType(".mp3", Music.class);
		assetRecognizer.setAssetType(".anm", ZootAnimationFile.class);
		
		setLoader(TiledMap.class, new TmxMapLoader());
		setLoader(ZootTiledMap.class, new ZootTiledMapLoader(assetRecognizer));
		setLoader(ZootAnimationFile.class, new ZootAnimationFileLoader(assetRecognizer));
	}
	
	public <T> T getOrLoad(String filename, Class<T> clazz)
	{
		if(filename == null || filename.isEmpty())
		{
			return null;
		}
		
		if(!isLoaded(filename, clazz))
		{
			load(filename, clazz);
			finishLoading();
		}
		return get(filename, clazz);
	}
}