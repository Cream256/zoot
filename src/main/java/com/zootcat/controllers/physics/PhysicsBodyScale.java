package com.zootcat.controllers.physics;

import com.badlogic.gdx.utils.reflect.ClassReflection;

public class PhysicsBodyScale
{
	public final float scaleX;
	public final float scaleY;
	public final float radiusScale;
	public final boolean scaleSensors;
	
	public PhysicsBodyScale()
	{
		this(1.0f, 1.0f, 1.0f, true);
	}
	
	public PhysicsBodyScale(float scaleX, float scaleY, float radiusScale, boolean scaleSensors)
	{
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		this.radiusScale = radiusScale;
		this.scaleSensors = scaleSensors;
	}
	
	public PhysicsBodyScale invert()
	{
		float newScaleX = 1.0f / scaleX;
		float newScaleY = 1.0f / scaleY;
		float newRadiusScale = 1.0f / radiusScale;
		return new PhysicsBodyScale(newScaleX, newScaleY, newRadiusScale, scaleSensors);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj) return true;
		if(!ClassReflection.isInstance(PhysicsBodyScale.class, obj)) return false;
		
		PhysicsBodyScale other = (PhysicsBodyScale)obj;
		return scaleX == other.scaleX 
				&& scaleY == other.scaleY 
				&& radiusScale == other.radiusScale
				&& scaleSensors == other.scaleSensors;
	}
	
	@Override
	public int hashCode()
	{
		int result = PhysicsBodyScale.class.hashCode();
		int c = Float.floatToIntBits(scaleX) + Float.floatToIntBits(scaleY) + Float.floatToIntBits(radiusScale) + (scaleSensors ? 1 : 0);		
		return 37 * result + c;
	}
}
