package com.zootcat.physics;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.BeforeClass;
import org.junit.Test;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.zootcat.exceptions.RuntimeZootException;

public class ZootPhysicsUtilsTest 
{
	@BeforeClass
	public static void initialize()
	{
		Box2D.init();
	}
	
	@Test
	public void shouldReturnPolygonVertices()
	{
		//given
		final float halfWidth = 100.0f;
		final float halfHeight = 50.0f;
		PolygonShape polygon = new PolygonShape();
		
		//when
		polygon.setAsBox(halfWidth, halfHeight);
		Vector2[] result = ZootPhysicsUtils.getPolygonVertices(polygon);
		
		//then
		assertEquals(4, result.length);
		assertEquals(-halfWidth, result[0].x, 0.0f);
		assertEquals(-halfHeight, result[0].y, 0.0f);
		assertEquals(halfWidth, result[1].x, 0.0f);
		assertEquals(-halfHeight, result[1].y, 0.0f);
		assertEquals(halfWidth, result[2].x, 0.0f);
		assertEquals(halfHeight, result[2].y, 0.0f);
		assertEquals(-halfWidth, result[3].x, 0.0f);
		assertEquals(halfHeight, result[3].y, 0.0f);
	}	
	
	@Test
	public void shouldReturnPolygonCentroid()
	{
		//given
		final float halfWidth = 100.0f;
		final float halfHeight = 50.0f;
		final float x = 50;
		final float y = 25;
		
		//when
		PolygonShape polygon = new PolygonShape();
		polygon.setAsBox(halfWidth, halfHeight, new Vector2(x, y), 0.0f);
		Vector2 centroid = ZootPhysicsUtils.getPolygonCentroid(polygon);
		
		//then
		assertEquals(x, centroid.x, 0.0f);
		assertEquals(y, centroid.y, 0.0f);
	}
	
	@Test
	public void shouldNotMoveIfFixtureHasNoShape()
	{
		//given
		Fixture fixture = mock(Fixture.class);
		
		//when
		when(fixture.getShape()).thenReturn(null);
		ZootPhysicsUtils.moveFixture(fixture, 1.0f, 2.0f);
		
		//then
		verify(fixture).getShape();
		verifyNoMoreInteractions(fixture);
	}
	
	@Test(expected = RuntimeZootException.class)
	public void shouldThrowIfFixtureHasUnsupportedShapeType()
	{
		//given
		Shape shape = mock(Shape.class);
		Fixture fixture = mock(Fixture.class);
		
		//when
		when(fixture.getShape()).thenReturn(shape);
		when(shape.getType()).thenReturn(Shape.Type.Edge);
		ZootPhysicsUtils.moveFixture(fixture, 1.0f, 2.0f);
	}
	
	@Test
	public void shouldDoNothingIfNoShapeIsSet()
	{
		//given
		Fixture fixture = mock(Fixture.class);
		
		//when
		when(fixture.getShape()).thenReturn(null);
		ZootPhysicsUtils.moveFixture(fixture, 1.0f, 2.0f);
		
		//then
		verify(fixture).getShape();
		verifyNoMoreInteractions(fixture);
	}
	
	@Test
	public void shouldMoveFixtureWithPolygonShape()
	{
		//given
		final float mx = 5.0f;
		final float my = -3.0f;
		PolygonShape polygon = new PolygonShape();
		Fixture fixture = mock(Fixture.class);
		
		//when
		when(fixture.getBody()).thenReturn(mock(Body.class));
		when(fixture.getShape()).thenReturn(polygon);
		polygon.setAsBox(10.0f, 10.0f, new Vector2(), 0.0f);
		ZootPhysicsUtils.moveFixture(fixture, mx, my);
		
		//then
		Vector2 centroid = ZootPhysicsUtils.getPolygonCentroid(polygon);
		assertEquals(mx, centroid.x, 0.0f);
		assertEquals(my, centroid.y, 0.0f);
	}
	
	@Test
	public void shouldMoveFixtureWithCircleShape()
	{
		//given
		final float mx = 55.0f;
		final float my = -33.0f;
		CircleShape circle = new CircleShape();
		Fixture fixture = mock(Fixture.class);
		
		//when
		when(fixture.getBody()).thenReturn(mock(Body.class));
		when(fixture.getShape()).thenReturn(circle);
		circle.setRadius(1.0f);
		circle.setPosition(new Vector2());
		ZootPhysicsUtils.moveFixture(fixture, mx, my);
		
		//then
		Vector2 center = circle.getPosition();
		assertEquals(mx, center.x, 0.0f);
		assertEquals(my, center.y, 0.0f);		
	}
	
	@Test(expected = RuntimeZootException.class)
	public void shouldThrowWhenSettingPositionIfFixtureHasUnsupportedShapeType()
	{
		//given
		Shape shape = mock(Shape.class);
		Fixture fixture = mock(Fixture.class);
		
		//when
		when(fixture.getShape()).thenReturn(shape);
		when(shape.getType()).thenReturn(Shape.Type.Edge);
		ZootPhysicsUtils.setFixturePosition(fixture, 0.0f, 0.0f);
	}
	
	@Test
	public void shouldSetPolygonShapeFixturePosition()
	{
		//given
		final float posX = 5.0f;
		final float posY = -3.0f;
		PolygonShape polygon = new PolygonShape();
		Fixture fixture = mock(Fixture.class);
		
		//when
		when(fixture.getBody()).thenReturn(mock(Body.class));
		when(fixture.getShape()).thenReturn(polygon);
		polygon.setAsBox(10.0f, 10.0f, new Vector2(25.0f, 37.0f), 0.0f);
		ZootPhysicsUtils.setFixturePosition(fixture, posX, posY);
		
		//then
		Vector2 centroid = ZootPhysicsUtils.getPolygonCentroid(polygon);
		assertEquals(posX, centroid.x, 0.0f);
		assertEquals(posY, centroid.y, 0.0f);
	}
	
	@Test
	public void shouldSetCircleShapeFixturePosition()
	{
		//given
		final float posX = 55.0f;
		final float posY = -33.0f;
		CircleShape circle = new CircleShape();
		Fixture fixture = mock(Fixture.class);
		
		//when
		when(fixture.getBody()).thenReturn(mock(Body.class));
		when(fixture.getShape()).thenReturn(circle);
		circle.setRadius(1.0f);
		circle.setPosition(new Vector2(123.0f, -256.0f));
		ZootPhysicsUtils.setFixturePosition(fixture, posX, posY);
		
		//then
		Vector2 center = circle.getPosition();
		assertEquals(posX, center.x, 0.0f);
		assertEquals(posY, center.y, 0.0f);	
	}
	
	@Test(expected = RuntimeZootException.class)
	public void shouldThrowWhenGettingFixtureCenterIfFixtureShapeIsUnsupported()
	{
		//given
		Shape shape = mock(Shape.class);
		Fixture fixture = mock(Fixture.class);
		
		//when
		when(shape.getType()).thenReturn(Shape.Type.Edge);
		when(fixture.getShape()).thenReturn(shape);
		ZootPhysicsUtils.getFixtureCenter(fixture);		
	}	
	
	@Test
	public void shouldReturnNullWhenGettingFixtureCenterIfFixtureHasNoShape()
	{
		//given
		Fixture fixture = mock(Fixture.class);
		
		//when
		when(fixture.getShape()).thenReturn(null);
		assertNull(ZootPhysicsUtils.getFixtureCenter(fixture));	
	}
	
	@Test
	public void shouldReturnPolygonFixtureCenter()
	{
		//given
		final float halfWidth = 100.0f;
		final float halfHeight = 50.0f;
		final float expectedX = 50;
		final float expectedY = 25;
		
		PolygonShape polygon = new PolygonShape();
		Fixture fixture = mock(Fixture.class);
		
		//when		
		polygon.setAsBox(halfWidth, halfHeight, new Vector2(expectedX, expectedY), 0.0f);
		when(fixture.getShape()).thenReturn(polygon);		
		Vector2 center = ZootPhysicsUtils.getFixtureCenter(fixture);
		
		//then
		assertEquals(expectedX, center.x, 0.0f);
		assertEquals(expectedY, center.y, 0.0f);
	}
	
	@Test
	public void shouldReturnCircleFixtureCenter()
	{
		//given
		final float expectedX = 55.0f;
		final float expectedY = -33.0f;
		CircleShape circle = new CircleShape();
		Fixture fixture = mock(Fixture.class);
		
		//when
		when(fixture.getShape()).thenReturn(circle);
		circle.setRadius(1.0f);
		circle.setPosition(new Vector2(expectedX, expectedY));
		Vector2 center = ZootPhysicsUtils.getFixtureCenter(fixture);
		
		//then		
		assertEquals(expectedX, center.x, 123.0f);
		assertEquals(expectedY, center.y, -256.0f);			
	}
}
