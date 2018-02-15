package com.zootcat.controllers.factory.mocks;

import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.controllers.factory.CtrlParam;

public class MockEnumParamController extends ControllerAdapter 
{
	@CtrlParam private EnumParam enumParam;
	
	public EnumParam getEnum()
	{
		return enumParam;
	}
	
}
