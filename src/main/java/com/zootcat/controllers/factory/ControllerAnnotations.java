package com.zootcat.controllers.factory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.zootcat.controllers.Controller;
import com.zootcat.exceptions.RuntimeZootException;

public class ControllerAnnotations
{
	public static List<Field> getControllerParameterFields(Controller controller)
	{
		return getAllFields(controller).stream().filter(field -> field.isAnnotationPresent(CtrlParam.class)).collect(Collectors.toList());
	}
	
	public static List<Field> getControllerDebugFields(Controller controller)
	{		
		return getAllFields(controller)
				.stream()
				.filter((field -> (field.isAnnotationPresent(CtrlParam.class) && field.getAnnotation(CtrlParam.class).debug()) 
						|| field.isAnnotationPresent(CtrlDebug.class)))
				.collect(Collectors.toList());
	}
	
	public static void setControllerParameter(Controller controller, String paramName, Object value)
	{
		try
		{
			Field field = getControllerParameterFields(controller)
				.stream().filter(f -> f.getName().equalsIgnoreCase(paramName))
				.findFirst().orElseThrow(() -> new RuntimeZootException("Controller param not found: " + paramName));	
			field.setAccessible(true);
			field.set(controller, value);
		} 
		catch (IllegalArgumentException | IllegalAccessException e)
		{
			throw new RuntimeZootException(e);
		}
	}
	
	private static List<Field> getAllFields(Controller controller)
	{
		List<Field> allClassFields = new ArrayList<Field>();    	
		Class<?> currentClass = controller.getClass();
		while(currentClass != null)
		{
			allClassFields.addAll(Arrays.asList(currentClass.getDeclaredFields()));
			currentClass = currentClass.getSuperclass();
		}	
		return allClassFields;
	}
}
