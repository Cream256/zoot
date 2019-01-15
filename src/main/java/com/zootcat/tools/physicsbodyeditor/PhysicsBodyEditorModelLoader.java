package com.zootcat.tools.physicsbodyeditor;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.zootcat.exceptions.RuntimeZootException;

public class PhysicsBodyEditorModelLoader extends AsynchronousAssetLoader<PhysicsBodyEditorModel, PhysicsBodyEditorModelLoader.Parameters>
{
	private PhysicsBodyEditorModel loadedModel;
	
	public PhysicsBodyEditorModelLoader()
	{
		super(new InternalFileHandleResolver());
	}
	
	public PhysicsBodyEditorModelLoader(FileHandleResolver resolver)
	{
		super(resolver);
	}

	@Override
	public void loadAsync(AssetManager manager, String fileName, FileHandle file, Parameters parameter)
	{
		loadedModel = loadedModel == null ? loadFromFile(file) : loadedModel;
	}

	@Override
	public PhysicsBodyEditorModel loadSync(AssetManager manager, String fileName, FileHandle file, Parameters parameter)
	{
		PhysicsBodyEditorModel result = loadedModel;
		loadedModel = null;
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, Parameters parameter)
	{
		return null;
	}
	
	private PhysicsBodyEditorModel loadFromFile(FileHandle fileHandle)
	{
		try
		{
			PhysicsBodyEditorModelFile file = new PhysicsBodyEditorModelFile(fileHandle);
			return file.getModel();
		}
		catch (RuntimeZootException e)
		{
			return null;
		}
	}
	
	static public class Parameters extends AssetLoaderParameters<PhysicsBodyEditorModel>
	{
		//noop
	}
}
