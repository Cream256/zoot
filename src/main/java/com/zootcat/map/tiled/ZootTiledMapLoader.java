package com.zootcat.map.tiled;

import java.util.Map;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.zootcat.assets.ZootAssetRecognizer;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.map.ZootMap;
import com.zootcat.utils.ArgumentParser;

public class ZootTiledMapLoader extends AsynchronousAssetLoader<ZootTiledMap, ZootTiledMapLoader.Parameters>
{
	private ZootAssetRecognizer assetRecognizer;
	private TmxMapLoader tmxMapLoader = new TmxMapLoader();
	private ZootTiledMap map;
	
	public ZootTiledMapLoader(ZootAssetRecognizer assetRecognizer)
	{
		this(assetRecognizer, new InternalFileHandleResolver());
	}

	public ZootTiledMapLoader(ZootAssetRecognizer assetRecognizer, FileHandleResolver resolver)
	{
		super(resolver);
		this.assetRecognizer = assetRecognizer;
	}
	
	public ZootMap load(String fileName)
	{
		TiledMap tiledMap = tmxMapLoader.load(fileName);
		return new ZootTiledMap(tiledMap);
	}
	
	@Override
	public void loadAsync(AssetManager manager, String fileName, FileHandle file, Parameters parameter)
	{
		tmxMapLoader.loadAsync(manager, fileName, file, toTmxParams(parameter));
		TiledMap tiledMap = tmxMapLoader.loadSync(manager, fileName, file, toTmxParams(parameter));
		map = new ZootTiledMap(tiledMap);
	}

	@Override
	public ZootTiledMap loadSync(AssetManager manager, String fileName, FileHandle file, Parameters parameter)
	{
		ZootTiledMap result = map;
		map = null;
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, Parameters parameter)
	{
		Array<AssetDescriptor> dependencies = tmxMapLoader.getDependencies(fileName, file, toTmxParams(parameter)); 		
		
		//add dependencies from this map
		Element tilemapRoot = new XmlReader().parse(file);
		addDependenciesFromXml(tilemapRoot, dependencies);
		
		//add dependencies from external tilesets
		for(Element tileset : tilemapRoot.getChildrenByNameRecursively("tileset"))
		{
			String tilesetName = tileset.getAttribute("source", null);
			if(tilesetName == null || tilesetName.isEmpty()) continue;
			
			FileHandle tilesetFile = resolve(file.parent().path() + "/" + tilesetName);
			Element tilesetRoot = new XmlReader().parse(tilesetFile);
			addDependenciesFromXml(tilesetRoot, dependencies);
		}
		
		return dependencies;
	}

	@SuppressWarnings("rawtypes")
	public void addDependenciesFromXml(Element root, Array<AssetDescriptor> dependencies)
	{			
		for(Element property : root.getChildrenByNameRecursively("property"))
		{			
			try
			{
				Map<String, Object> arguments = ArgumentParser.parse(property.getAttribute("value").split(","));				
				arguments.values().stream()
								.filter(value -> value != null)  				  
								.map(value -> value.toString())
				  				.filter(value -> assetRecognizer.getAssetType(value) != null)
				  				.map(filename -> assetRecognizer.getAssetDescriptor(filename))
				  				.forEach(dependency -> dependencies.add(dependency));
			}
			catch (RuntimeZootException e)
			{
				continue;
			}
		}
	}
	
	private TmxMapLoader.Parameters toTmxParams(Parameters zootParams)
	{
		if(zootParams == null)
		{
			return null;
		}
		
		TmxMapLoader.Parameters params = new TmxMapLoader.Parameters();
		params.convertObjectToTileSpace = zootParams.convertObjectToTileSpace;
		params.flipY = zootParams.flipY;
		params.generateMipMaps = zootParams.generateMipMaps;
		params.textureMagFilter = zootParams.textureMagFilter;
		params.textureMinFilter = zootParams.textureMinFilter;			
		return params;
	}
	
	//copy of TiledMapLoader.Parameters class
	public static class Parameters extends AssetLoaderParameters<ZootTiledMap> 
	{
		public boolean generateMipMaps = false;
		public TextureFilter textureMinFilter = TextureFilter.Nearest;
		public TextureFilter textureMagFilter = TextureFilter.Nearest;
		public boolean convertObjectToTileSpace = false;
		public boolean flipY = true;
	}
}
