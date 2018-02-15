package com.zootcat.physics;

import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;

public class ZootDefaultContactFilter
{		
	public static boolean shouldCollide(Fixture fixtureA, Fixture fixtureB)
	{
		return shouldCollide(fixtureA.getFilterData(), fixtureB.getFilterData());
	}
	
	public static boolean shouldCollide(Filter filterA, Filter filterB)
	{	
		if (filterA.groupIndex == filterB.groupIndex && filterA.groupIndex != 0)
		{
			return filterA.groupIndex > 0;
		}
	
		boolean collide = (filterA.maskBits & filterB.categoryBits) != 0 && (filterA.categoryBits & filterB.maskBits) != 0;
		return collide;
	}
}