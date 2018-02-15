package com.zootcat.controllers.factory.mocks;

import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.controllers.factory.CtrlParam;

public class Mock2Controller extends ControllerAdapter 
{
	@CtrlParam public int a;
	@CtrlParam public float b;
	@CtrlParam public String c;
}
