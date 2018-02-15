package com.zootcat.utils;

import java.io.File;
import java.util.Arrays;
import java.util.Random;

import com.zootcat.exceptions.RuntimeZootException;

public class ZootUtils
{    
    public static int trunc(float f)
    {        
        return (int)f;
    }
    
    public static double trunc(double d)
    {
    	return (int)d;
    }
    
    public static float lerp(float time, float start, float finish)
    {
        float timeNormalized = clipToRange(time, 0.0f, 1.0f);
        return (1.0f - timeNormalized) * start + timeNormalized * finish;
    }
    
    public static float clipToRange(float value, float min, float max)
    {
        float localMin = Math.min(min, max);
        float localMax = Math.max(min, max);        
        return value < localMin ? localMin : value > localMax ? localMax : value;
    }

    public static int clipToRange(int value, int min, int max)
    {
        int localMin = Math.min(min, max);
        int localMax = Math.max(min, max);        
        return value < localMin ? localMin : value > localMax ? localMax : value;
    }
    
    public static boolean inRange(float value, float min, float max)
    {
        return value >= min && value <= max;
    }
    
    public static String unquoteString(String quotedString)
    {
        if (quotedString != null
        && ((quotedString.startsWith("\"") && quotedString.endsWith("\""))
        || (quotedString.startsWith("'") && quotedString.endsWith("'")))) 
        {
            return quotedString.substring(1, quotedString.length() - 1);
        }
        return quotedString;
    }
    
    public static String getWorkingDirectory()
    {
        return System.getProperty("user.dir") + File.separator;
    }
    
    public static int random(int min, int max)
    {
        return new Random().nextInt((max - min) + 1) + min;        
    }

	public static int wrapValue(int value, int min, int max) 
	{
		if(value > max)
		{
			return min;
		}
		if(value < min)
		{
			return max;
		}
		return value;
	}	
	
	public static <T extends Enum<?>> T searchEnum(Class<T> enumeration, String value) 
	{
	    return Arrays.stream(enumeration.getEnumConstants())
	    			 .filter(enm -> enm.name().compareToIgnoreCase(value) == 0)
	    			 .findFirst()
	    			 .orElseThrow(() -> new RuntimeZootException("Enum '" + value + "' not found for " + enumeration.getClass().getSimpleName()));
	}

}
