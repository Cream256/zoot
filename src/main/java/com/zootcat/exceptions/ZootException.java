package com.zootcat.exceptions;

public class ZootException extends Exception
{
	private static final long serialVersionUID = -726221293312046669L;

	public ZootException(String msg)
	{
		super(msg);
	}

	public ZootException(String msg, Exception e)
	{
		super(msg, e);
	}
}
