package com.zootcat.gfx;

import com.badlogic.gdx.math.Vector2;

public class ZootAnimationOffset
{
	public Vector2 right;
	public Vector2 left;
	
	public ZootAnimationOffset()
	{
		right = new Vector2();
		left = new Vector2();
	}
	
	public ZootAnimationOffset(float rightX, float rightY, float leftX, float leftY)
	{
		right = new Vector2(rightX, rightY);
		left = new Vector2(leftX, leftY);
	}
}
