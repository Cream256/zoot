package com.zootcat.controllers;

import java.util.HashMap;
import java.util.Map;

/**
 * UnUniquely identifies a {@link Controller} sub-class. You cannot instantiate a ComponentType. 
 * They can only be accessed via {@link #getFor(Class<? extends Controller>)}. 
 * Each controller class will always return the same instance of ComponentType.
 * @author Cream
 * 
 * @remarks based on: https://github.com/libgdx/ashley/blob/master/ashley/src/com/badlogic/ashley/core/ComponentType.java
 */
public class ControllerType
{
	private static final Map<Class<? extends Controller>, ControllerType> types = new HashMap<Class<? extends Controller>, ControllerType>();
	private static int typeIndex = 0;
	
	private final int id;
	
	private ControllerType()
	{
		id = ++typeIndex;
	}
	
	public int getId()
	{
		return id;
	}
	
	@Override
	public int hashCode() 
	{
		return id;
	}

	@Override
	public boolean equals (Object obj) 
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ControllerType other = (ControllerType)obj;
		return id == other.id;
	}
	
	public static ControllerType forClass(Class<? extends Controller> controllerClass)
	{
		ControllerType type = types.get(controllerClass);
		if(type == null)
		{
			type = new ControllerType();
			types.put(controllerClass, type);
		}		
		return type;
	}
}
