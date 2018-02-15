package com.zootcat.physics;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;

public class ZootContactFilterTest
{
	@Mock private Fixture fixtureA;
	@Mock private Fixture fixtureB;
	@Mock private ContactFilter filterTrue;
	@Mock private ContactFilter filterFalse;	
	private Filter filterA;
	private Filter filterB;
	private ZootContactFilter contactFilter;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		filterA = new Filter();
		filterB = new Filter();
		when(fixtureA.getFilterData()).thenReturn(filterA);
		when(fixtureB.getFilterData()).thenReturn(filterB);
		when(filterTrue.shouldCollide(anyObject(), anyObject())).thenReturn(true);
		when(filterFalse.shouldCollide(anyObject(), anyObject())).thenReturn(false);		
		contactFilter = new ZootContactFilter();
	}
	
	@Test
	public void addFixtureFilterTest()
	{
		contactFilter.addFixtureFilter(fixtureA, filterTrue);
		assertTrue(contactFilter.shouldCollide(fixtureA, fixtureB));
		
		contactFilter.addFixtureFilter(fixtureA, filterTrue);
		assertTrue(contactFilter.shouldCollide(fixtureA, fixtureB));
		
		contactFilter.addFixtureFilter(fixtureA, filterFalse);
		assertFalse("Should collide only when all filters return true", contactFilter.shouldCollide(fixtureA, fixtureB));
	}
	
	@Test
	public void addFixtureFilterForBothFixturesShouldCombineResultsTest()
	{
		contactFilter.addFixtureFilter(fixtureA, filterTrue);
		contactFilter.addFixtureFilter(fixtureB, filterTrue);
		assertTrue(contactFilter.shouldCollide(fixtureA, fixtureB));
		assertTrue(contactFilter.shouldCollide(fixtureB, fixtureA));
		
		contactFilter.addFixtureFilter(fixtureB, filterFalse);
		assertFalse("Should use filters from both fixtures", contactFilter.shouldCollide(fixtureA, fixtureB));
		assertFalse("Should use filters from both fixtures", contactFilter.shouldCollide(fixtureB, fixtureA));
	}
	
	@Test
	public void removeFixtureFilterTest()
	{
		contactFilter.addFixtureFilter(fixtureA, filterTrue);
		contactFilter.addFixtureFilter(fixtureA, filterFalse);
		assertFalse(contactFilter.shouldCollide(fixtureA, fixtureB));
		
		contactFilter.removeFixtureFilter(fixtureA, filterFalse);
		assertTrue(contactFilter.shouldCollide(fixtureA, fixtureB));
	}
	
	@Test
	public void removeFixtureFiltersTest()
	{
		contactFilter.addFixtureFilter(fixtureA, filterTrue);
		contactFilter.addFixtureFilter(fixtureA, filterFalse);
		assertFalse(contactFilter.shouldCollide(fixtureA, fixtureB));
		
		contactFilter.removeFixtureFilters(fixtureA);
		assertTrue(contactFilter.shouldCollide(fixtureA, fixtureB));
	}
	
	@Test
	public void defaultFiltersShouldCollideTest()
	{
		assertTrue(contactFilter.shouldCollide(fixtureA, fixtureB));
		assertTrue(contactFilter.shouldCollide(fixtureB, fixtureA));
	}
	
	@Test
	public void defaultBehaviourTest()
	{
		filterA.categoryBits = 0x0004;
		filterB.categoryBits = 0x0008;		
		assertTrue(contactFilter.shouldCollide(fixtureA, fixtureB));
		
		filterA.maskBits = 0x0008;
		assertTrue(contactFilter.shouldCollide(fixtureA, fixtureB));
		
		filterA.maskBits = 0x0004;
		assertFalse(contactFilter.shouldCollide(fixtureA, fixtureB));
	}
	
	@Test
	public void defaultBehaviourForGroupsTest()
	{
		filterA.maskBits = 0;
		filterB.maskBits = 0;		
		filterA.groupIndex = 1;
		filterB.groupIndex = 2;
		assertFalse(contactFilter.shouldCollide(fixtureA, fixtureB));
		
		filterA.groupIndex = 2;
		assertTrue(contactFilter.shouldCollide(fixtureA, fixtureB));
	}
	
}
