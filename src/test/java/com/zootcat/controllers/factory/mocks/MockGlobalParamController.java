package com.zootcat.controllers.factory.mocks;

import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.controllers.factory.CtrlParam;

public class MockGlobalParamController extends ControllerAdapter
{
	@CtrlParam private int local = 0;
	@CtrlParam(global = true) private int global;
	
	public int getLocal()
	{
		return local;
	}
	
	public int getGlobal()
	{
		return global;
	}
	
}
