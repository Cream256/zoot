package com.zootcat.controllers.factory;

import java.lang.reflect.Field;

public class ControllerParameter
{
	public final Field field;
	public final boolean required;
	public final boolean global;
	
	ControllerParameter(Field field)
	{
		this.field = field;		
		this.required = field.getAnnotation(CtrlParam.class).required();		
		this.global = field.getAnnotation(CtrlParam.class).global();
	}
	
	@Override
	public String toString()
	{
		return field.getName();
	}
}
