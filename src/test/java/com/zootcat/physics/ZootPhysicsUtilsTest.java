package com.zootcat.physics;

import static org.junit.Assert.assertEquals;
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
	
	@Test
	public void shouldSetPolygonShapeFixturePosition()
	{
		//TODO
	}
	
	@Test
	public void shouldSetCircleShapeFixturePosition()
	{
		//TODO
	}
}
