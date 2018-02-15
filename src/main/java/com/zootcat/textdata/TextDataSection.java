package com.zootcat.textdata;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;

public class TextDataSection
{
    private File file;
    private String startToken = "";  
    private String endToken = "";    
    private Map<String, String> values = new HashMap<String, String>();
    
    public String getStartToken()
    {
    	return startToken;
    }

    public void setStartToken(String startToken)
    {
        this.startToken = startToken;
    }

    public String getEndToken()
    {
        return endToken;
    }

    public void setEndToken(String endToken)
    {
        this.endToken = endToken;
    }
    
    public void set(String key, String value)
    {
        values.put(key.toLowerCase(), value);
    }
    
    public String get(String key)
    {
    	return values.get(key.toLowerCase());
    }
    
    public String getString(String key, String defaultValue)
    {
    	String result = get(key);
    	return result != null ? result : defaultValue;
    }
    
    public int getInt(String key, int defaultValue)
    {
    	String result = get(key);
    	return result != null ? Integer.valueOf(result) : defaultValue;
    }
    
    public boolean getBoolean(String key, boolean defaultValue)
    {
    	String result = get(key);
    	return result != null ? Boolean.valueOf(result) : defaultValue;
    }
        
    public List<String> getAllValues()
    {
        return new ArrayList<String>(values.values());
    }
    
    public List<String> getAllKeys()
    {
        return new ArrayList<String>(values.keySet());
    }

    public File getFile()
    {
        return file;
    }

    public void setFile(File file)
    {
        this.file = file;
    }
    
    public Vector2[] getVectors(String key)
    {  
    	String value = get(key);
    	if(value == null || value.trim().isEmpty())
    	{
    		return new Vector2[0];
    	}
    	
    	String[] pairs = value.split(";");
    	Vector2[] result = new Vector2[pairs.length];
    	for(int i = 0; i < pairs.length; ++i)
    	{
    		String[] numbers = pairs[i].split(",");
    		if(numbers.length != 2)
    		{
    			continue;
    		}
    		int x = Integer.valueOf(numbers[0].trim());
    		int y = Integer.valueOf(numbers[1].trim());    		
    		result[i] = new Vector2(x, y);    		
    	}
		return result;
    }
    
    public int[] getIntArray(String key)
    {
    	String value = get(key);    	
    	
    	if(value == null || value.isEmpty())
    	{
    		return new int[0];
    	}
    	
    	String[] frames = value.split(";");
    	int[] result = new int[frames.length];
    	for(int i = 0; i < frames.length; ++i)
    	{
    		result[i] = Integer.valueOf(frames[i].trim());
    	}
    	return result;
    }
}
