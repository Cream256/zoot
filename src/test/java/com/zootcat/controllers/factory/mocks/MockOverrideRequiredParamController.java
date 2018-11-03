package com.zootcat.controllers.factory.mocks;

import com.zootcat.controllers.factory.CtrlParam;

public class MockOverrideRequiredParamController extends MockRequiredParamController
{
	@CtrlParam(required = false) private int required;
}
