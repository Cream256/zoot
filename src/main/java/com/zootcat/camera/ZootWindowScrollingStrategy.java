package com.zootcat.camera;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.zootcat.scene.ZootActor;

public class ZootWindowScrollingStrategy implements ZootCameraScrollingStrategy
{	
	private static final float DEFAULT_SCROLLING_SPEED = 4.0f;

	private Vector2 offset = new Vector2();
	private float left, right, top, bottom;
	private float scrollingSpeed = DEFAULT_SCROLLING_SPEED;
		
	public ZootWindowScrollingStrategy(float leftBorder, float rightBorder, float topBorder, float bottomBorder)
	{
		left = leftBorder;
		right = rightBorder;
		top = topBorder;
		bottom = bottomBorder;
	}
	
	public void setOffset(float x, float y)
	{
		offset.x = x;
		offset.y = y;
	}
	
	public Vector2 getOffsetCopy()
	{
		return offset.cpy();
	}
	
	public void setScrollingSpeed(float speed)
	{
		scrollingSpeed = speed;
	}
	
	public float getScrollingSpeed()
	{
		return scrollingSpeed;
	}
		
	@Override
	public void scrollCamera(ZootCamera camera, float delta)
	{
		ZootActor actor = camera.getTarget();
		if(actor == null) return;
								
		//calculate window boundary		
		Vector3 cameraPositionRef = camera.getPosition();
		float topLeftX = cameraPositionRef.x - left + offset.x;
		float topLeftY = cameraPositionRef.y + top + offset.y;		
		float bottomRightX = cameraPositionRef.x + right + offset.x;
		float bottomRightY = cameraPositionRef.y - bottom + offset.y;
						
		//check if actor is outside of boundary	
		float actorLeft = actor.getX();
		float actorRight = actorLeft + actor.getWidth();
		float actorBottom = actor.getY();
		float actorTop = actorBottom + actor.getHeight(); 
		
		float lookAtX = cameraPositionRef.x;
		float lookAtY = cameraPositionRef.y;
		if(actorRight > bottomRightX)
		{
			lookAtX += actorRight - bottomRightX;			
		}
		else if(actorLeft < topLeftX)
		{
			lookAtX -= topLeftX - actorLeft;
		}
		if(actorBottom < bottomRightY)
		{
			lookAtY += actorBottom - bottomRightY;
		}
		else if(actorTop > topLeftY)
		{
			lookAtY -= topLeftY - actorTop;
		}
				
		//lerp to new position
		float dt = Math.min(1.0f, delta);	//so the camera won't wonder into space at first call		
		float lerpProgress = Math.min(scrollingSpeed * dt, 1.0f);		
		float newX = MathUtils.lerp(cameraPositionRef.x, lookAtX, lerpProgress);
		float newY = MathUtils.lerp(cameraPositionRef.y, lookAtY, lerpProgress);		
		camera.setPosition(newX, newY);
	}
}
