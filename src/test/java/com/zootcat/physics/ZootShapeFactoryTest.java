package com.zootcat.physics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape.Type;

public class ZootShapeFactoryTest
{	
	private static final float BOX_WIDTH = 10.0f;
	private static final float BOX_HEIGHT = 20.0f;
	private static final float CIRCLE_RADIUS = 128.0f;
	private static final float SLOPE_WIDTH = 5.0f;
	private static final float SLOPE_HEIGHT = 7.5f;
	private static final float ACTOR_X = 10.0f;
	private static final float ACTOR_Y = 20.0f;
	private static final float SCENE_UNIT_SCALE = 0.5f;
	private static final float POLY_WIDTH = 5.0f;
	private static final float POLY_HEIGHT = 10.0f;
	private static final float[] POLY_VERTICES = {0.0f, 0.0f, POLY_WIDTH, POLY_HEIGHT, POLY_WIDTH, 0.0f};
	private static final float CENTER_X = 33.0f;
	private static final float CENTER_Y = 44.0f;
	
	@BeforeClass
	public static void setupClass()
	{
		Box2D.init();
	}
	
	@Test
	public void shouldReturnBoxShape()
	{		
		//when
		Vector2 vertex = new Vector2();
		PolygonShape box = ZootShapeFactory.createBox(BOX_WIDTH, BOX_HEIGHT); 
				
		//then
		assertNotNull(box);
		assertEquals(4, box.getVertexCount());
		assertEquals(Type.Polygon, box.getType());
						
		box.getVertex(0, vertex);
		assertEquals(BOX_WIDTH / -2.0f, vertex.x, 0.0f);
		assertEquals(BOX_HEIGHT / -2.0f, vertex.y, 0.0f);
		
		box.getVertex(1, vertex);
		assertEquals(BOX_WIDTH / 2.0f, vertex.x, 0.0f);
		assertEquals(BOX_HEIGHT / -2.0f, vertex.y, 0.0f);
		
		box.getVertex(2, vertex);
		assertEquals(BOX_WIDTH / 2.0f, vertex.x, 0.0f);
		assertEquals(BOX_HEIGHT / 2.0f, vertex.y, 0.0f);
		
		box.getVertex(3, vertex);
		assertEquals(BOX_WIDTH / -2.0f, vertex.x, 0.0f);
		assertEquals(BOX_HEIGHT / 2.0f, vertex.y, 0.0f);
	}
	
	@Test
	public void shuoldReturnBoxShapeWithOffsetAtCenter()
	{
		//when
		Vector2 vertex = new Vector2();
		PolygonShape box = ZootShapeFactory.createBox(BOX_WIDTH, BOX_HEIGHT, CENTER_X, CENTER_Y); 
				
		//then
		assertNotNull(box);
		assertEquals(4, box.getVertexCount());
		assertEquals(Type.Polygon, box.getType());
						
		box.getVertex(0, vertex);
		assertEquals(BOX_WIDTH / -2.0f + CENTER_X, vertex.x, 0.0f);
		assertEquals(BOX_HEIGHT / -2.0f + CENTER_Y, vertex.y, 0.0f);
		
		box.getVertex(1, vertex);
		assertEquals(BOX_WIDTH / 2.0f + CENTER_X, vertex.x, 0.0f);
		assertEquals(BOX_HEIGHT / -2.0f + CENTER_Y, vertex.y, 0.0f);
		
		box.getVertex(2, vertex);
		assertEquals(BOX_WIDTH / 2.0f + CENTER_X, vertex.x, 0.0f);
		assertEquals(BOX_HEIGHT / 2.0f + CENTER_Y, vertex.y, 0.0f);
		
		box.getVertex(3, vertex);
		assertEquals(BOX_WIDTH / -2.0f + CENTER_X, vertex.x, 0.0f);
		assertEquals(BOX_HEIGHT / 2.0f + CENTER_Y, vertex.y, 0.0f);
	}
	
	@Test
	public void shouldReturnCircleShape()
	{
		//when
		CircleShape circle = ZootShapeFactory.createCircle(CIRCLE_RADIUS);
		
		//then
		assertNotNull(circle);
		assertEquals(Type.Circle, circle.getType());
		assertEquals(CIRCLE_RADIUS, circle.getRadius(), 0.0f);
		assertEquals(0.0f, circle.getPosition().x, 0.0f);
		assertEquals(0.0f, circle.getPosition().y, 0.0f);
	}
	
	@Test
	public void shouldReturnLeftSlope()
	{
		//when
		Vector2 vertex = new Vector2();
		PolygonShape slope = ZootShapeFactory.createSlope(SLOPE_WIDTH, SLOPE_HEIGHT, true);
		
		//then
		assertNotNull(slope);
		assertEquals(Type.Polygon, slope.getType());
		assertEquals(3, slope.getVertexCount());
		
		slope.getVertex(0, vertex);
		assertEquals(SLOPE_WIDTH / 2.0f, vertex.x, 0.0f);
		assertEquals(SLOPE_HEIGHT / -2.0f, vertex.y, 0.0f);
		
		slope.getVertex(1, vertex);
		assertEquals(SLOPE_WIDTH / 2.0f, vertex.x, 0.0f);
		assertEquals(SLOPE_HEIGHT / 2.0f, vertex.y, 0.0f);
		
		slope.getVertex(2, vertex);
		assertEquals(SLOPE_WIDTH / -2.0f, vertex.x, 0.0f);
		assertEquals(SLOPE_HEIGHT / -2.0f, vertex.y, 0.0f);
	}
	
	@Test
	public void shouldReturnRightSlope()
	{
		//when
		Vector2 vertex = new Vector2();
		PolygonShape slope = ZootShapeFactory.createSlope(SLOPE_WIDTH, SLOPE_HEIGHT, false);
		
		//then
		assertNotNull(slope);
		assertEquals(Type.Polygon, slope.getType());
		assertEquals(3, slope.getVertexCount());
		
		slope.getVertex(0, vertex);
		assertEquals(SLOPE_WIDTH / 2.0f, vertex.x, 0.0f);
		assertEquals(SLOPE_HEIGHT / -2.0f, vertex.y, 0.0f);
		
		slope.getVertex(1, vertex);
		assertEquals(SLOPE_WIDTH / -2.0f, vertex.x, 0.0f);
		assertEquals(SLOPE_HEIGHT / 2.0f, vertex.y, 0.0f);
		
		slope.getVertex(2, vertex);
		assertEquals(SLOPE_WIDTH / -2.0f, vertex.x, 0.0f);
		assertEquals(SLOPE_HEIGHT / -2.0f, vertex.y, 0.0f);		
	}
	
	@Test
	public void shouldReturnPolygonTranslatedByHalfPolygonSize()
	{
		//given
		Polygon poly = new Polygon(POLY_VERTICES);
		
		//when
		Vector2 vertex = new Vector2();
		PolygonShape polygon = ZootShapeFactory.createPolygon(poly, 0.0f, 0.0f, 1.0f);
		
		//then
		assertNotNull(polygon);
		assertEquals(Type.Polygon, polygon.getType());
		assertEquals(POLY_VERTICES.length / 2, polygon.getVertexCount());
		
		polygon.getVertex(0, vertex);
		assertEquals(POLY_WIDTH / 2.0f, vertex.x, 0.0f);
		assertEquals(POLY_HEIGHT / -2.0f, vertex.y, 0.0f);
		
		polygon.getVertex(1, vertex);
		assertEquals(POLY_WIDTH / 2.0f, vertex.x, 0.0f);
		assertEquals(POLY_HEIGHT / 2.0f, vertex.y, 0.0f);
		
		polygon.getVertex(2, vertex);
		assertEquals(POLY_WIDTH / -2.0f, vertex.x, 0.0f);
		assertEquals(POLY_HEIGHT / -2.0f, vertex.y, 0.0f);
	}
	
	@Test
	public void shouldReturnPolygonScaledByUnitScale()
	{
		//given
		Polygon poly = new Polygon(POLY_VERTICES);
		
		//when
		Vector2 vertex = new Vector2();
		PolygonShape polygon = ZootShapeFactory.createPolygon(poly, 0.0f, 0.0f, SCENE_UNIT_SCALE);
		
		//then
		assertNotNull(polygon);
		assertEquals(Type.Polygon, polygon.getType());
		assertEquals(POLY_VERTICES.length / 2, polygon.getVertexCount());
		
		polygon.getVertex(0, vertex);
		assertEquals((POLY_WIDTH / 2.0f) * SCENE_UNIT_SCALE, vertex.x, 0.0f);
		assertEquals((POLY_HEIGHT / -2.0f) * SCENE_UNIT_SCALE, vertex.y, 0.0f);
		
		polygon.getVertex(1, vertex);
		assertEquals((POLY_WIDTH / 2.0f) * SCENE_UNIT_SCALE, vertex.x, 0.0f);
		assertEquals((POLY_HEIGHT / 2.0f) * SCENE_UNIT_SCALE, vertex.y, 0.0f);
		
		polygon.getVertex(2, vertex);
		assertEquals((POLY_WIDTH / -2.0f) * SCENE_UNIT_SCALE, vertex.x, 0.0f);
		assertEquals((POLY_HEIGHT / -2.0f) * SCENE_UNIT_SCALE, vertex.y, 0.0f);
	}
	
	@Test
	public void shouldReturnPolygonTranslatedByActorPosition()
	{
		//given
		Polygon poly = new Polygon(POLY_VERTICES);
		
		//when
		Vector2 vertex = new Vector2();
		PolygonShape polygon = ZootShapeFactory.createPolygon(poly, ACTOR_X, ACTOR_Y, 1.0f);
		
		//then
		assertNotNull(polygon);
		assertEquals(Type.Polygon, polygon.getType());
		assertEquals(POLY_VERTICES.length / 2, polygon.getVertexCount());
		
		polygon.getVertex(0, vertex);
		assertEquals(POLY_WIDTH / 2.0f - ACTOR_X, vertex.x, 0.0f);
		assertEquals(POLY_HEIGHT / -2.0f - ACTOR_Y, vertex.y, 0.0f);
		
		polygon.getVertex(1, vertex);
		assertEquals(POLY_WIDTH / 2.0f - ACTOR_X, vertex.x, 0.0f);
		assertEquals(POLY_HEIGHT / 2.0f - ACTOR_Y, vertex.y, 0.0f);
		
		polygon.getVertex(2, vertex);
		assertEquals(POLY_WIDTH / -2.0f - ACTOR_X, vertex.x, 0.0f);
		assertEquals(POLY_HEIGHT / -2.0f - ACTOR_Y, vertex.y, 0.0f);	
	}
}
