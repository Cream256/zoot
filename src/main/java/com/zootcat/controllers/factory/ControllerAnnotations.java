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
	public static List<ControllerParameter> getControllerParameterFields(Controller controller)
	{
		return getAllFields(controller).stream()
									   .filter(field -> field.isAnnotationPresent(CtrlParam.class))
									   .map(field -> new ControllerParameter(field))
									   .collect(Collectors.toList());
	}
	
	public static List<Field> getControllerDebugFields(Controller controller)
	{		
		return getAllFields(controller)
				.stream()
				.filter((field -> field.isAnnotationPresent(CtrlParam.class) || field.isAnnotationPresent(CtrlDebug.class)))
				.collect(Collectors.toList());
	}
	
	public static void setControllerParameter(Controller controller, String paramName, Object value)
	{
		try
		{
			ControllerParameter parameterField = getControllerParameterFields(controller)
				.stream()
				.filter(f -> f.field.getName().equalsIgnoreCase(paramName))
				.findFirst()
				.orElseThrow(() -> new RuntimeZootException("Controller param not found: " + paramName));	
			parameterField.field.setAccessible(true);
			parameterField.field.set(controller, value);
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
			Field[] currentClassFields = currentClass.getDeclaredFields();
			Arrays.stream(currentClassFields)
				.filter(newField -> !allClassFields.stream().anyMatch(existingField -> existingField.getName().equals(newField.getName())))
				.forEach(newField -> allClassFields.add(newField));			
			
			currentClass = currentClass.getSuperclass();
		}	
		
		return allClassFields;
	}
}
