package com.zootcat.controllers.factory.mocks;

import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.controllers.factory.CtrlParam;

public class MockRequiredParamController extends ControllerAdapter
{
	@CtrlParam private int optional;
	@CtrlParam(required=true) private int required;
	
	public int getOptional()
	{
		return optional;
	}
	
	public int getRequired()
	{
		return required;
	}
	
}
