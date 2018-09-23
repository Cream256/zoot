package com.zootcat.controllers.factory.mocks;

import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.controllers.factory.CtrlDebug;
import com.zootcat.controllers.factory.CtrlParam;

public class ControllerWithDebugFieldsMock extends ControllerAdapter 
{
	@CtrlDebug private int intDebugField;
	@CtrlDebug private float floatDebugField;
	@CtrlParam(debug = true) private boolean boolDebugField;
}
