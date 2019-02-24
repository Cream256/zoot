package com.zootcat.physics;

import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;
import com.zootcat.scene.tiled.ZootTiledScene;
import com.zootcat.utils.BitMaskConverter;
import com.zootcat.utils.CollisionMask;

public class ZootFixtureDefBuilder
{
	public enum FixtureDimensions { Provided, Actor, ActorScaled };
	
	private float density;
	private float friction;
	private float restitution;
	private float offsetX;
	private float offsetY;
	private float width;
	private float height;
	private boolean sensor;
	private ZootBodyShape shape;
	private FixtureDimensions dimensions;
	private String category;
	private String mask;
	private ZootScene scene;
		
	public ZootFixtureDefBuilder(ZootScene scene)
	{
		this.scene = scene;
		reset();
	}
	
	public FixtureDef build(ZootActor actor) 
	{
		validate(actor);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = density;
		fixtureDef.friction = friction;
		fixtureDef.restitution = restitution;
		fixtureDef.isSensor = sensor;
		fixtureDef.shape = createShape(actor, shape);
		setupFilter(fixtureDef.filter);				
		reset();
		return fixtureDef;
	}
	
	private void validate(ZootActor actor)
	{
		if(shape == ZootBodyShape.NONE) return;
		if(shape == ZootBodyShape.UNKNOWN) throw new RuntimeZootException("Cannot create unknown shape for " + actor.getName());
		
		float providedArea = width * height;
		if(dimensions == FixtureDimensions.Provided && providedArea <= 0.0f) throw new RuntimeZootException("Provided fixture area for " + actor.getName() + " must be > 0");
				
		float actorArea = actor.getWidth() * actor.getHeight();
		if(dimensions == FixtureDimensions.Actor && actorArea <= 0.0f) throw new RuntimeZootException("Actor area for " + actor.getName() + " must be > 0");
		
		float scaledActorArea = (actor.getWidth() * width) * (actor.getHeight() * height);
		if(dimensions == FixtureDimensions.ActorScaled && scaledActorArea <= 0.0f) throw new RuntimeZootException("Scaled actor area for " + actor.getName() + " must be > 0");
	}
	
	public ZootFixtureDefBuilder reset()
	{
		density = 1.0f;
		friction = 0.2f;
		restitution = 0.0f;
		offsetX = 0.0f;
		offsetY = 0.0f;
		width = 1.0f;
		height = 1.0f;
		sensor = false;
		shape = ZootBodyShape.BOX;
		category = "";
		mask = "";		
		dimensions = FixtureDimensions.Provided;
		return this;
	}
	
	public ZootFixtureDefBuilder setDensity(float value)
	{
		density = value;
		return this;
	}
	
	public float getDensity()
	{
		return density;
	}
	
	public ZootFixtureDefBuilder setFriction(float value)
	{
		friction = value;
		return this;
	}
	
	public float getFriction()
	{
		return friction;
	}
	
	public ZootFixtureDefBuilder setRestitution(float value)
	{
		restitution = value;
		return this;
	}
	
	public float getRestitution()
	{
		return restitution;
	}
	
	public ZootFixtureDefBuilder setOffsetX(float value)
	{
		offsetX = value;
		return this;
	}
	
	public float getOffsetX()
	{
		return offsetX;
	}
	
	public ZootFixtureDefBuilder setOffsetY(float value)
	{
		offsetY = value;
		return this;
	}
	
	public float getOffsetY()
	{
		return offsetY;
	}
	
	public ZootFixtureDefBuilder setWidth(float value)
	{
		width = value;
		return this;
	}
	
	public float getWidth()
	{
		return width;
	}
	
	public ZootFixtureDefBuilder setHeight(float value)
	{
		height = value;
		return this;
	}
	
	public float getHeight()
	{
		return height;
	}
	
	public ZootFixtureDefBuilder setSensor(boolean value)
	{
		sensor = value;
		return this;
	}
	
	public boolean getSensor()
	{
		return sensor;
	}
	
	public ZootFixtureDefBuilder setShape(ZootBodyShape value)
	{
		shape = value;
		return this;
	}
	
	public ZootBodyShape getShape()
	{
		return shape;
	}
	
	public ZootFixtureDefBuilder setCategory(String value)
	{
		category = value;
		return this;
	}

	public String getCategory()
	{
		return category;
	}
	
	public ZootFixtureDefBuilder setMask(String value)
	{
		mask = value;
		return this;
	}
	
	public String getMask()
	{
		return mask;
	}
	
	public ZootFixtureDefBuilder setDimensions(FixtureDimensions value)
	{
		dimensions = value;
		return this;
	}
	
	public FixtureDimensions getFixtureDimensions()
	{
		return dimensions;
	}
			
	protected Shape createShape(ZootActor actor, ZootBodyShape shape)
	{		
		switch(shape)
		{
		case BOX:
			return ZootShapeFactory.createBox(
					getFixtureWidth(actor), 
					getFixtureHeight(actor), 
					getFixtureX(actor),
					getFixtureY(actor));
			
		case CIRCLE:
			return ZootShapeFactory.createCircle(getFixtureWidth(actor));
			
		case SLOPE_LEFT:
		case SLOPE_RIGHT:
			return ZootShapeFactory.createSlope(getFixtureWidth(actor), getFixtureHeight(actor), shape == ZootBodyShape.SLOPE_LEFT);
			
		case POLYGON:
			PolygonMapObject polygonObj = (PolygonMapObject) ((ZootTiledScene)scene).getTiledMap().getObjectById(actor.getId());
			return ZootShapeFactory.createPolygon(polygonObj.getPolygon(), actor.getX(), actor.getY(), scene.getUnitScale());

		case NONE:
			return null;
			
		default:
			throw new RuntimeZootException("Unknown fixture shape type for for actor: " + actor);
		}
	}
	
	private float getFixtureWidth(ZootActor actor)
	{
		switch(dimensions)
		{
		case Actor:
			return actor.getWidth();
		case ActorScaled:
			return actor.getWidth() * width;		
		case Provided:
		default:		
			return width * scene.getUnitScale(); 		
		}
	}
	
	private float getFixtureHeight(ZootActor actor)
	{
		switch(dimensions)
		{
		case Actor:
			return actor.getHeight();
		case ActorScaled:
			return actor.getHeight() * height;		
		case Provided:
		default:		
			return height * scene.getUnitScale(); 		
		}
	}
	
	private float getFixtureX(ZootActor actor)
	{
		switch(dimensions)
		{
		case ActorScaled:
			return actor.getWidth() * offsetX;			
		default:
			return offsetX * scene.getUnitScale();			
		}		
	}
	
	private float getFixtureY(ZootActor actor)
	{
		switch(dimensions)
		{
		case ActorScaled:
			return actor.getHeight() * offsetY;			
		default:
			return offsetY * scene.getUnitScale();			
		}	
	}
		
	private void setupFilter(Filter filter)
	{
		if(category != null && !category.isEmpty())
		{
			filter.categoryBits = BitMaskConverter.Instance.fromString(category);	
		}
		
		if(mask != null && !mask.isEmpty())
		{
			filter.maskBits = new CollisionMask(mask).toBitMask();
		}
	}
}
