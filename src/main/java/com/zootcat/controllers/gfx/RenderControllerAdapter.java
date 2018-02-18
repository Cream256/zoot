package com.zootcat.controllers.gfx;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.scene.ZootActor;

public class RenderControllerAdapter extends ControllerAdapter implements RenderController
{
	private float offsetX = 0.0f;
	private float offsetY = 0.0f;
	
	@Override
	public void onRender(Batch batch, float parentAlpha, ZootActor actor, float delta)
	{
		//noop
	}

	@Override
	public void setOffset(float x, float y)
	{
		this.offsetX = x;
		this.offsetY = y;
	}
	
	public float getOffsetX()
	{
		return offsetX;
	}
	
	public float getOffsetY()
	{
		return offsetY;
	}
}
