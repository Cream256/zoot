package com.zootcat.controllers.factory.mocks;

import com.zootcat.controllers.factory.CtrlParam;

public class MockDerivedController extends MockBaseController 
{
	@CtrlParam private int derivedParam = 0;
	
	public int getDerivedParam()
	{
		return derivedParam;
	}
	
}
