package com.zootcat.physics;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;

public class ZootPhysicsTest
{
	private ZootPhysics physics;
	
	@Before
	public void setup()
	{
		physics = new ZootPhysics();
	}
	
	@Test
	public void shouldReturnFixturesInArea()
	{
		//given
		Body body = physics.createBody(new BodyDef());		
		
		//when
		Fixture fix1 = body.createFixture(ZootShapeFactory.createBox(10, 10, 5.0f, 5.0f), 1.0f);		
		
		//then should not detect fixture
		assertFalse(physics.getFixturesInArea(10.5f, 0.0f, 11.0f, 0.5f).contains(fix1));
		assertFalse(physics.getFixturesInArea(-1.0f, 0.0f, -0.5f, 0.5f).contains(fix1));
		
		//then fixture corners should be included in area
		assertTrue(physics.getFixturesInArea(0.0f, 0.0f, 0.5f, 0.5f).contains(fix1));
		assertTrue(physics.getFixturesInArea(0.0f, 10.0f, 0.5f, 10.5f).contains(fix1));
		assertTrue(physics.getFixturesInArea(10.0f, 10.0f, 10.5f, 0.5f).contains(fix1));
		assertTrue(physics.getFixturesInArea(10.0f, 10.0f, 10.5f, 10.5f).contains(fix1));
	}
}
