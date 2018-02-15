package com.zootcat.utils;

import java.util.HashMap;
import java.util.Map;

import com.zootcat.exceptions.RuntimeZootException;

public class BitMaskConverter
{	
	public static final int MASK_COLLIDE_WITH_ALL = -1;
	public static final int MASK_COLLIDE_WITH_NONE = 0;
	
	public static final BitMaskConverter Instance = new BitMaskConverter();
	
	private Map<String, Short> values = new HashMap<String, Short>();
	
	private BitMaskConverter()
	{
		//use instance
	}
			
	public short fromString(String mask)
	{				
		if(mask == null || mask.isEmpty())
		{
			return MASK_COLLIDE_WITH_ALL;
		}
		
		short bitMask = MASK_COLLIDE_WITH_NONE;			
		for(String category : mask.split("\\|"))
		{
			short categoryBit = convert(category.trim());
			bitMask |= categoryBit;
		}
		return bitMask;
	}
	
	public void clear()
	{
		values.clear();
	}
	
	private short convert(String str)
	{
		if(!values.containsKey(str))
		{
			double value = Math.pow(2.0f, values.size());
			if(value > Short.MAX_VALUE)
			{
				throw new RuntimeZootException("BitMaskConverter has run out of values on: " + str);
			}
			values.put(str, (short) ZootUtils.trunc(value));				
		}		
		return values.get(str);
	}
}
