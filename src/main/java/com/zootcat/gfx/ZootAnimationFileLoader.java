package com.zootcat.gfx;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.zootcat.assets.ZootAssetRecognizer;
import com.zootcat.exceptions.ZootException;

public class ZootAnimationFileLoader extends AsynchronousAssetLoader<ZootAnimationFile, ZootAnimationFileLoader.Parameters>
{	
	private ZootAnimationFile animationFile;
	private ZootAssetRecognizer assetRecognizer;
	
	public ZootAnimationFileLoader(ZootAssetRecognizer assetRecognizer)
	{
		this(assetRecognizer, new InternalFileHandleResolver());
	}
	
	public ZootAnimationFileLoader(ZootAssetRecognizer assetRecognizer, FileHandleResolver resolver)
	{
		super(resolver);
		this.assetRecognizer = assetRecognizer;
	}

	@Override
	public void loadAsync(AssetManager manager, String fileName, FileHandle file, Parameters parameter)
	{
		animationFile = animationFile == null ? loadFromFile(file) : animationFile; 
	}

	@Override
	public ZootAnimationFile loadSync(AssetManager manager, String fileName, FileHandle file, Parameters parameter)
	{
		ZootAnimationFile result = animationFile;
		animationFile = null;
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, Parameters parameter)
	{
		animationFile = loadFromFile(file);
		Array<AssetDescriptor> dependencies = new Array<AssetDescriptor>(1);				
		
		animationFile.getSpriteSheets().values().forEach(spriteSheet -> 
		{
			String relativeSpriteSheetPath = file.parent().path() + "/" + spriteSheet;		
			dependencies.add(assetRecognizer.getAssetDescriptor(relativeSpriteSheetPath));	
		});
		return dependencies;
	}
	
	private ZootAnimationFile loadFromFile(FileHandle fileHandle)
	{
		try
		{
			return new ZootAnimationFile(fileHandle.file());
		}
		catch (ZootException e)
		{
			return null;
		}
	}
	
	static public class Parameters extends AssetLoaderParameters<ZootAnimationFile>
	{
		//noop
	}
}
