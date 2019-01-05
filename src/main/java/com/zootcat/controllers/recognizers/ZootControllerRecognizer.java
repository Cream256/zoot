package com.zootcat.controllers.recognizers;

import com.zootcat.controllers.Controller;

public interface ZootControllerRecognizer
{
	boolean isControllerExact(Controller ctrl, Class<? extends Controller> clazz);
}