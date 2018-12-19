package com.zootcat.exceptions;

public class ZootControllerNotFoundException extends RuntimeZootException
{
	private static final long serialVersionUID = -1491375900591308102L;

	public ZootControllerNotFoundException(String controllerName, String actorName)
	{
		super("Controller " + controllerName + " not found for " + actorName + " actor.");
	}
}