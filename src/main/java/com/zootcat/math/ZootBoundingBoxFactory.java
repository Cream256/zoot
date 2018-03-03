package com.zootcat.math;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.zootcat.exceptions.RuntimeZootException;

public class ZootBoundingBoxFactory
{	
	public static BoundingBox create(Fixture fixture)
	{
		BoundingBox box = new BoundingBox();
		createAtRef(fixture, box);
		return box;
	}
	
	public static BoundingBox createAtRef(Fixture fixture, BoundingBox out)
	{
		switch(fixture.getShape().getType())
		{
		case Polygon:
			setPolygonBoundingBox((PolygonShape) fixture.getShape(), out);
			return out;
			
		case Circle:
			setCircleBoundingBox((CircleShape) fixture.getShape(), out);
			return out;
			
		default:
			throw new RuntimeZootException("BoundingBox not implemented for shape: " + fixture.getShape());
		}
	}
	
	private static void setPolygonBoundingBox(PolygonShape polygon, BoundingBox out)
	{		
		Vector3 min = new Vector3();
		Vector3 max = new Vector3();
	
		Vector2 vertex = new Vector2();
		for(int i = 0; i < polygon.getVertexCount(); ++i)
		{			
			polygon.getVertex(i, vertex);				
			min.x = Math.min(min.x, vertex.x);
			min.y = Math.min(min.y, vertex.y);
			max.x = Math.max(max.x, vertex.x);
			max.y = Math.max(max.y, vertex.y);
		}		
				
		out.set(min, max);
	}

	private static void setCircleBoundingBox(CircleShape circle, BoundingBox out)
	{
		float radius = circle.getRadius();
		Vector2 pos = circle.getPosition();
		
		Vector3 min = new Vector3(pos.x - radius, pos.y - radius, 0.0f);
		Vector3 max = new Vector3(pos.x + radius, pos.y + radius, 0.0f);		
		
		out.set(min, max);
	}
}
