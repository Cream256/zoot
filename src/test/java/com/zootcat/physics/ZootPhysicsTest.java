package com.zootcat.physics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;

public class ZootPhysicsTest
{
	private ZootPhysics physics;
	
	@Before
	public void setup()
	{
		physics = new ZootPhysics();
	}
	
	@Test
	public void shouldReturnDefaultGravity()
	{
		assertEquals(ZootPhysics.DEFAULT_GRAVITY, physics.getGravity());
	}
	
	@Test
	public void shouldSetGravity()
	{
		physics.setGravity(100.0f, -200.0f);
		assertEquals(100.0f, physics.getGravity().x, 0.0f);
		assertEquals(-200.0f, physics.getGravity().y, 0.0f);
	}
	
	@Test
	public void shouldDestroyFixture()
	{
		//given
		Body body = mock(Body.class);
		Fixture fixture = mock(Fixture.class);
		
		//when
		physics.destroyFixture(body, fixture);
		
		//then
		verify(body).destroyFixture(fixture);
	}
	
	@Test
	public void shouldCreateFixturesFromDefinitions()
	{
		//given
		Body body = mock(Body.class);
		FixtureDef fixtureDef1 = mock(FixtureDef.class);
		fixtureDef1.shape = mock(Shape.class);
		FixtureDef fixtureDef2 = mock(FixtureDef.class);
		fixtureDef2.shape = mock(Shape.class);
		
		List<FixtureDef> fixtureDefs = Arrays.asList(fixtureDef1, fixtureDef2);
		
		//when
		List<Fixture> createdFixtures = physics.createFixtures(body, fixtureDefs);
		
		//then
		assertEquals(2, createdFixtures.size());
		verify(body).createFixture(fixtureDef1);
		verify(body).createFixture(fixtureDef2);
	}
	
	@Test
	public void shouldDisposeFixtureDefinitionShapeAfterFixtureCreation()
	{
		//given
		Body body = mock(Body.class);
		Shape shape = mock(Shape.class);
		FixtureDef fixtureDef = mock(FixtureDef.class);
		fixtureDef.shape = shape;		
		
		//when
		when(body.createFixture(any())).thenReturn(mock(Fixture.class));
		
		//then
		assertNotNull(physics.createFixture(body, fixtureDef));
		assertNull(fixtureDef.shape);
		verify(shape).dispose();
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
