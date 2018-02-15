package com.zootcat.physics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.Fixture;

public class ZootContactFilter implements ContactFilter
{
	private static final int DEFAULT_FILTER_LIST_SIZE = 3;
	
	private Map<Fixture, List<ContactFilter>> fixtureFilters = new HashMap<Fixture, List<ContactFilter>>();
	
	public void addFixtureFilter(Fixture fixture, ContactFilter contactFilter)
	{	
		List<ContactFilter> filters = fixtureFilters.get(fixture);
		if(filters == null)
		{
			filters = new ArrayList<ContactFilter>(DEFAULT_FILTER_LIST_SIZE);
		}
		filters.add(contactFilter);
		fixtureFilters.put(fixture, filters);
	}
	
	public void removeFixtureFilter(Fixture fixture, ContactFilter filter)
	{
		getFixtureFilters(fixture).remove(filter);
	}
	
	public void removeFixtureFilters(Fixture fixture)
	{
		fixtureFilters.remove(fixture);
	}
	
	@Override
	public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB)
	{
		List<ContactFilter> filtersA = getFixtureFilters(fixtureA);
		List<ContactFilter> filtersB = getFixtureFilters(fixtureB);		
		if(filtersA.size() == 0 && filtersB.size() == 0)
		{
			return ZootDefaultContactFilter.shouldCollide(fixtureA, fixtureB);	
		}
	
		boolean fixtureACollided = filtersA.stream().allMatch(filter -> filter.shouldCollide(fixtureA, fixtureB));
		boolean fixtureBCollided = filtersB.stream().allMatch(filter -> filter.shouldCollide(fixtureA, fixtureB));
		return fixtureACollided && fixtureBCollided;
	}
			
	private List<ContactFilter> getFixtureFilters(Fixture fixture)
	{
		List<ContactFilter> filters = fixtureFilters.get(fixture);
		return filters != null ? filters : Collections.emptyList();
	}
}

