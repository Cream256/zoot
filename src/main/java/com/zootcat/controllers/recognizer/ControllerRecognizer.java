package com.zootcat.controllers.recognizer;

import com.zootcat.controllers.Controller;

public interface ControllerRecognizer
{
	boolean areEqual(Controller ctrl1, Controller ctrl2);
	boolean areEqual(Class<? extends Controller> ctrlClass1, Class<? extends Controller> ctrlClass2);	
}