package com.zootcat.exceptions;

public class ZootDuplicatedControllerException extends RuntimeZootException
{
	private static final long serialVersionUID = 4098675275127395098L;

	public ZootDuplicatedControllerException(String controllerName, String actorName)
	{
		super("Duplicated controller " + controllerName + " found for actor " + actorName);
	}
	
	public ZootDuplicatedControllerException(String message)
	{
		super(message);
	}
}