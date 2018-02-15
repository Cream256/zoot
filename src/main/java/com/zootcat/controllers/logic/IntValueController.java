package com.zootcat.controllers.logic;

import com.badlogic.gdx.math.MathUtils;
import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.scene.ZootActor;

public class IntValueController extends ControllerAdapter
{
	@CtrlParam(debug = true) private int value = 0;
	@CtrlParam(debug = true) private int maxValue = 0;
	@CtrlParam(debug = true) private int minValue = 0;
	
	@Override
	public void init(ZootActor actor) 
	{		
		int fixedMax = Math.max(minValue, maxValue);
		int fixedMin = Math.min(minValue, maxValue);
				
		maxValue = fixedMax; 
		minValue = fixedMin;
		value = MathUtils.clamp(value, minValue, maxValue);
	}
	
	public int getValue()
	{
		return value;
	}

	public int getMaxValue()
	{
		return maxValue;
	}
	
	public int getMinValue()
	{
		return minValue;
	}

	public void addToValue(int value)
	{
		setValue(getValue() + value);
	}

	public void addToMaxValue(int value)
	{
		setMaxValue(getMaxValue() + value);
	}

	public void addToMinValue(int value)
	{
		setMinValue(getMinValue() + value);
	}
	
	public void setValue(int newValue)
	{
		value = MathUtils.clamp(newValue, minValue, maxValue);
	}

	public void setMaxValue(int newValue)
	{
		maxValue = MathUtils.clamp(newValue, minValue, Integer.MAX_VALUE);
		value = MathUtils.clamp(value, minValue, maxValue);
	}
	
	public void setMinValue(int newValue)
	{
		minValue = MathUtils.clamp(newValue, Integer.MIN_VALUE, maxValue);
		value = MathUtils.clamp(value, minValue, maxValue);
	}
}