package com.zootcat.utils;

import java.util.Arrays;

import com.badlogic.gdx.utils.Array;

public class CollisionMask
{
	private Array<String> masks;
	private int masksLength;
	
	public CollisionMask()
	{
		masks = new Array<String>();
		masksLength = 0;
	}
	
	public CollisionMask(String mask)
	{
		String[] masksFromString = mask.split("\\|");
		
		masks = new Array<String>(masksFromString.length);		
		Arrays.stream(masksFromString).forEach(m -> add(m));
	}
	
	public CollisionMask add(String value)
	{
		String sanitizedValue = value.trim().toUpperCase();
		if(sanitizedValue.isEmpty()) return this;
		
		if(!masks.contains(sanitizedValue, false))
		{
			masks.add(sanitizedValue);	
			masksLength += sanitizedValue.length();
		}
		
		return this;
	}
	
	public CollisionMask remove(String value)
	{
		masks.removeValue(value, false);
		return this;
	}
	
	public short toBitMask()
	{
		return BitMaskConverter.Instance.fromString(toString());
	}
	
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder(masksLength + masks.size);
		for(int i = 0; i < masks.size; ++i)
		{
			builder.append(masks.get(i));
			if(i < masks.size - 1) builder.append("|");			
		}		
		return builder.toString();
	}
}
