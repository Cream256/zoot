package com.zootcat.controllers.factory.mocks;

import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.controllers.factory.CtrlParam;

public class MockPrimitiveParamsController extends ControllerAdapter	
{
	@CtrlParam private int intParam = 0;
	@CtrlParam private float floatParam = 0.0f;
	@CtrlParam private double doubleParam = 0.0;
	@CtrlParam private String stringParam = "";
	@CtrlParam private boolean booleanParam = false;
	
	public int getIntParam()
	{
		return intParam;
	}
	
	public float getFloatParam()
	{
		return floatParam;
	}
	
	public double getDoubleParam()
	{
		return doubleParam;
	}
	
	public String getStringParam()
	{
		return stringParam;
	}
	
	public boolean getBooleanParam()
	{
		return booleanParam;
	}
}
