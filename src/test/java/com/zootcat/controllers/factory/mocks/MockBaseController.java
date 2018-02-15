package com.zootcat.controllers.factory.mocks;

import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.controllers.factory.CtrlParam;

public class MockBaseController extends ControllerAdapter 
{
	@CtrlParam private int baseParam = 0;
	
	public int getBaseParam()
	{
		return baseParam;
	}
	
}
