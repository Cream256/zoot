package com.zootcat.assets;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.assets.AssetDescriptor;

public class ZootAssetRecognizer
{
	private Map<String, Class<?>> assetTypes = new HashMap<String, Class<?>>();
	
	public void setAssetType(String fileExtension, Class<?> assetType)
	{
		String extension = fileExtension.startsWith(".") ? fileExtension.substring(1) : fileExtension;		
		assetTypes.put(extension.toLowerCase(), assetType);
	}
	
	public Class<?> getAssetType(String filename)
	{
		return assetTypes.get(extractFileExtension(filename).toLowerCase());
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public AssetDescriptor<?> getAssetDescriptor(String filename)
	{
		Class<?> assetType = getAssetType(filename);
		return assetType != null ? new AssetDescriptor(filename, assetType) : null;
	}
		
	private String extractFileExtension(String filename)
	{
		if(filename == null) return "";
		
		int dotIndex = filename.lastIndexOf(".");
		if(dotIndex == -1) return "";
		
		return filename.substring(dotIndex + 1).trim().toLowerCase();
	}
}
