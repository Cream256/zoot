package com.zootcat.gfx;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

//Created mainly for testing purposes, so it would be easier to
//decouple tests from GDX implementation
public class ZootGraphicsFactory
{	
	public SpriteBatch createSpriteBatch()
	{
		return new SpriteBatch();
	}	
	
	public ShapeRenderer createShapeRenderer()
	{
		return new ShapeRenderer();
	}
}