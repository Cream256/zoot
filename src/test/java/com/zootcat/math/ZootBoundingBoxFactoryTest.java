package com.zootcat.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.BeforeClass;
import org.junit.Test;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.math.ZootBoundingBoxFactory;

public class ZootBoundingBoxFactoryTest
{
	@BeforeClass
	public static void setupClass()
	{
		Box2D.init();
	}
	
	@Test
	public void createForCircleShapeTest()
	{
		//given
		final float circleX = 5.0f;
		final float circleY = 10.0f;
		final float circleR = 25.0f;
		Fixture fixture = mock(Fixture.class);		
		CircleShape circle = new CircleShape();
		
		//when
		circle.setPosition(new Vector2(circleX, circleY));
		circle.setRadius(circleR);
		when(fixture.getShape()).thenReturn(circle);
		
		//then
		BoundingBox bbox = ZootBoundingBoxFactory.create(fixture);
		assertNotNull(bbox);
		assertTrue(bbox.isValid());
		
		assertEquals(circleX, bbox.getCenterX(), 0.0f);
		assertEquals(circleY, bbox.getCenterY(), 0.0f);
		assertEquals(0.0f, bbox.getCenterZ(), 0.0f);				
		assertEquals(circleR * 2, bbox.getWidth(), 0.0f);
		assertEquals(circleR * 2, bbox.getHeight(), 0.0f);
		assertEquals(0.0f, bbox.getDepth(), 0.0f);
				
		//finally
		circle.dispose();		
	}
	
	@Test
	public void createForRectanglePolygonShapeTest()
	{
		//given
		final float centerX = 5.0f;
		final float centerY = 10.0f;
		final float width = 25.0f;
		final float height = 33.0f;
		Fixture fixture = mock(Fixture.class);		
		PolygonShape box = new PolygonShape();
		
		//when
		box.setAsBox(width / 2.0f, height / 2.0f, new Vector2(centerX, centerY), 0.0f);
		when(fixture.getShape()).thenReturn(box);
		
		//then
		BoundingBox bbox = ZootBoundingBoxFactory.create(fixture);
		assertNotNull(bbox);
		assertTrue(bbox.isValid());
		
		assertEquals(centerX, bbox.getCenterX(), 0.0f);
		assertEquals(centerY, bbox.getCenterY(), 0.0f);
		assertEquals(0.0f, bbox.getCenterZ(), 0.0f);
		assertEquals(width, bbox.getWidth(), 0.0f);
		assertEquals(height, bbox.getHeight(), 0.0f);
		assertEquals(0.0f, bbox.getDepth(), 0.0f);
						
		//finally
		box.dispose();		
	}
	
	@Test
	public void createShouldThrowOnNotImplementedShapeTest()
	{
		boolean throwed = false;
		ChainShape chain = new ChainShape();
		Fixture fixture = mock(Fixture.class);
		when(fixture.getShape()).thenReturn(chain);
		
		try		
		{
			ZootBoundingBoxFactory.create(fixture);
		}
		catch(RuntimeZootException e)
		{
			throwed = true;
		}		
		finally
		{
			chain.dispose();
		}
		
		assertTrue("Should throw exception on unimplemented shape type", throwed);
	}
	
}
