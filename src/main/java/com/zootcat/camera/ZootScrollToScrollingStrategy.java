package com.zootcat.camera;

import com.badlogic.gdx.math.MathUtils;
import com.zootcat.scene.ZootActor;

public class ZootScrollToScrollingStrategy implements ZootCameraScrollingStrategy
{	
	private float startX;
	private float startY;
	private float endX;
	private float endY;
	private float time;
	private float currentTime = 0.0f;
	private boolean startSet = false;
		
	public ZootScrollToScrollingStrategy(ZootActor actor, float duration)
	{
		endX = actor.getX() + actor.getWidth() * 0.5f;
		endY = actor.getY() + actor.getHeight() * 0.5f;
		time = duration;
	}
	
	public ZootScrollToScrollingStrategy(float x, float y, float duration)
	{
		endX = x;
		endY = y;
		time = duration;
	}
		
	@Override
	public void scrollCamera(ZootCamera camera, float delta)
	{
		if(!startSet)
		{
			currentTime = 0.0f;
			startX = camera.getPosition().x;
			startY = camera.getPosition().y;
			startSet = true;
		}
		
		currentTime += Math.min(delta, 1.0f);				
		float lerpProgress = Math.min(currentTime / time, 1.0f);		
		float newX = MathUtils.lerp(startX, endX, lerpProgress);
		float newY = MathUtils.lerp(startY, endY, lerpProgress);		
		camera.setPosition(newX, newY);
	}

	@Override
	public void reset()
	{
		startSet = false;
	}
}
