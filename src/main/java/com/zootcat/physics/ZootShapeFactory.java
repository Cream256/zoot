package com.zootcat.physics;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class ZootShapeFactory
{	
	public static PolygonShape createBox(float width, float height)
	{
		PolygonShape boxPoly = new PolygonShape();
		boxPoly.setAsBox(width / 2.0f, height / 2.0f);
		return boxPoly;
	}
	
	public static PolygonShape createBox(float width, float height, float x, float y)
	{
		PolygonShape boxPoly = new PolygonShape();
		boxPoly.setAsBox(width / 2.0f, height / 2.0f, new Vector2(x, y), 0.0f);
		return boxPoly;
	}
	
	public static CircleShape createCircle(float radius) 
	{
		CircleShape circle = new CircleShape();
		circle.setRadius(radius);
		return circle;
	}
	
	public static PolygonShape createSlope(float width, float height, boolean leftSlope) 
	{
		PolygonShape slope = new PolygonShape();
		
		float x = -width / 2.0f;
		float y = -height / 2.0f;
		
		Vector2[] vertices = new Vector2[3];
		if(leftSlope)
		{
			vertices[0] = new Vector2(x, y);
			vertices[1] = new Vector2(x + width, y);
			vertices[2] = new Vector2(x + width, y + height);
		} 
		else
		{
			vertices[0] = new Vector2(x, y);
			vertices[1] = new Vector2(x + width, y);
			vertices[2] = new Vector2(x, y + height);
		}
		slope.set(vertices);			
		return slope;	
	}
	
	public static PolygonShape createPolygon(Polygon polygon, float actorX, float actorY, float unitScale) 
	{
		Rectangle boundingRect = polygon.getBoundingRectangle();
		float polyWidth = boundingRect.getWidth() * unitScale;
		float polyHeight = boundingRect.getHeight() * unitScale;
								
		float[] polygonVertices = polygon.getTransformedVertices();			
		float[] vertices = new float[polygonVertices.length];
		System.arraycopy(polygonVertices, 0, vertices, 0, vertices.length);			
		
		for(int i = 0; i < vertices.length; ++i)
		{								
			//scale to world
			vertices[i] *= unitScale;
			
			//translate by actor position
			boolean isX = i % 2 == 0;
			if(isX) vertices[i] -= actorX;
			else vertices[i] -= actorY;
							
			//translate by actor half size
			if(isX) vertices[i] -= polyWidth / 2;
			else vertices[i] -= polyHeight / 2;
		}
					
		PolygonShape polygonShape = new PolygonShape();
		polygonShape.set(vertices);
		return polygonShape;
	}
}
