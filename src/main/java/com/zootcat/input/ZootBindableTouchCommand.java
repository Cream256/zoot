package com.zootcat.input;

@FunctionalInterface
public interface ZootBindableTouchCommand
{
	public boolean apply(int screenX, int screenY, int pointer);	
}
