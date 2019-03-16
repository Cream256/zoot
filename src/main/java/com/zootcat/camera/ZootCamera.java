package com.zootcat.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;

public class ZootCamera extends OrthographicCamera
{
	private ZootScene scene;
	private ZootActor target;
	private float worldWidth;
	private float worldHeight;
	private boolean clipToLevel = false;	
	private ZootCameraScrollingStrategy scrollingStrategy = ZootNullScrollingStrategy.Instance;
	
	public ZootCamera(float worldWidth, float worldHeight)
	{
		this.worldWidth = worldWidth;
		this.worldHeight = worldHeight;
	}
	
	public void setScene(ZootScene scene)
	{
		this.scene = scene;
	}
	
	public ZootScene getScene()
	{
		return scene;
	}
	
	public ZootActor getTarget()
	{
		return target;
	}
	
	public void setTarget(ZootActor target)
	{
		this.target = target;
	}
	
	public void zoom(float amount)
	{
		zoom += amount;
	}
	
	public void setZoom(float newZoom)
	{
		zoom = newZoom;
	}
	
	public float getZoom()
	{
		return zoom;
	}
		
	public void update(float delta, boolean updateFrustum)
	{
		scrollingStrategy.scrollCamera(this, delta);				
		super.update(updateFrustum);
	}
		
	public void setScrollingStrategy(ZootCameraScrollingStrategy strategy)
	{
		scrollingStrategy = strategy != null ? strategy : ZootNullScrollingStrategy.Instance;
	}
	
	public ZootCameraScrollingStrategy getScrollingStrategy()
	{
		return scrollingStrategy;
	}
	
	public void setEdgeSnapping(boolean value)
	{
		clipToLevel = value;
	}
	
	public boolean isEdgeSnapping()
	{
		return clipToLevel;
	}
	
	public void setViewportSize(float width, float height)
	{
		viewportWidth = width;
		viewportHeight = height;
	}
	
	public float getViewportWidth()
	{
		return viewportWidth;
	}
	
	public float getViewportHeight()
	{
		return viewportHeight;
	}
	
	public float getWorldWidth()
	{
		return worldWidth;
	}
	
	public float getWorldHeight()
	{
		return worldHeight;
	}
	
	public void setPosition(float x, float y)
	{
		position.x = x;
		position.y = y;
		if(isEdgeSnapping()) snapToEdges();
	}
	
	public Vector3 getPosition()
	{
		return position;
	}
				
	private void snapToEdges()
	{		
		float minX = zoom * (viewportWidth / 2.0f);
		float maxX = worldWidth - minX;		
		float minY = zoom * (viewportHeight / 2.0f);
		float maxY = worldHeight - minY;
		
		position.x = MathUtils.clamp(position.x, minX, maxX);
		position.y = MathUtils.clamp(position.y, minY, maxY);		
	}
}
