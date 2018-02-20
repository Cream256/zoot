package com.zootcat.controllers;

public enum ControllerPriority
{
	Low(-1), Normal(0), High(1), Critical(2);
	
	private int value;
	
	private ControllerPriority(int value)
	{
		this.value = value;
	}
	
	public int getValue()
	{
		return value;
	}
}
