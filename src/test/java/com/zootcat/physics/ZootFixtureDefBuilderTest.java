package com.zootcat.physics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape.Type;
import com.zootcat.physics.ZootFixtureDefBuilder.FixtureDimensions;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;
import com.zootcat.utils.BitMaskConverter;

public class ZootFixtureDefBuilderTest
{
	@Mock private ZootScene scene;
	private ZootActor actor;
	private ZootFixtureDefBuilder builder;
	
	@BeforeClass
	public static void initialize()
	{
		Box2D.init();
	}
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		BitMaskConverter.Instance.clear();
		
		when(scene.getUnitScale()).thenReturn(1.0f);
		
		actor = new ZootActor();
		builder = new ZootFixtureDefBuilder(scene);		
	}
	
	@Test
	public void shouldBeCreatedWithDefaultValues()
	{
		assertEquals(1.0f, builder.getDensity(), 0.0f);
		assertEquals(0.2f, builder.getFriction(), 0.0f);
		assertEquals(0.0f, builder.getRestitution(), 0.0f);
		assertEquals(0.0f, builder.getWidth(), 0.0f);
		assertEquals(0.0f, builder.getHeight(), 0.0f);
		assertEquals(0.0f, builder.getOffsetX(), 0.0f);
		assertEquals(0.0f, builder.getOffsetY(), 0.0f);
		assertEquals(false, builder.getSensor());
		assertEquals(ZootBodyShape.BOX, builder.getShape());		
		assertEquals("", builder.getCategory());
		assertEquals("", builder.getMask());
		assertEquals(FixtureDimensions.Provided, builder.getFixtureDimensions());
	}
	
	@Test
	public void shouldSetDensity()
	{
		builder.setDensity(2.0f);
		assertEquals(2.0f, builder.getDensity(), 0.0f);
	}
	
	@Test
	public void shouldSetFriction()
	{
		builder.setFriction(5.0f);
		assertEquals(5.0f, builder.getFriction(), 0.0f);
	}
	
	@Test
	public void shouldSetRestitution()
	{
		builder.setRestitution(3.0f);
		assertEquals(3.0f, builder.getRestitution(), 0.0f);
	}
	
	@Test
	public void shouldSetWidth()
	{
		builder.setWidth(10.0f);
		assertEquals(10.0f, builder.getWidth(), 0.0f);
	}
	
	@Test
	public void shouldSetHeight()
	{
		builder.setHeight(20.0f);
		assertEquals(20.0f, builder.getHeight(), 0.0f);
	}
	
	@Test
	public void shouldSetOffsetX()
	{
		builder.setOffsetX(1.0f);
		assertEquals(1.0f, builder.getOffsetX(), 0.0f);
	}

	@Test
	public void shouldSetOffsetY()
	{
		builder.setOffsetY(-1.0f);
		assertEquals(-1.0f, builder.getOffsetY(), 0.0f);
	}
	
	@Test
	public void shouldSetSensor()
	{
		builder.setSensor(true);
		assertEquals(true, builder.getSensor());
		
		builder.setSensor(false);
		assertEquals(false, builder.getSensor());
	}
	
	@Test
	public void shouldSetCategory()
	{
		builder.setCategory("ABC");
		assertEquals("ABC", builder.getCategory());
	}
	
	@Test
	public void shouldSetMask()
	{
		builder.setMask("QWE");
		assertEquals("QWE", builder.getMask());
	}
	
	@Test
	public void shouldSetShape()
	{
		builder.setShape(ZootBodyShape.CIRCLE);
		assertEquals(ZootBodyShape.CIRCLE, builder.getShape());
	}
	
	@Test
	public void shouldSetFixtureDimensions()
	{
		builder.setDimensions(FixtureDimensions.Actor);
		assertEquals(FixtureDimensions.Actor, builder.getFixtureDimensions());
		
		builder.setDimensions(FixtureDimensions.ActorScaled);
		assertEquals(FixtureDimensions.ActorScaled, builder.getFixtureDimensions());
		
		builder.setDimensions(FixtureDimensions.Provided);
		assertEquals(FixtureDimensions.Provided, builder.getFixtureDimensions());
	}
		
	@Test
	public void shouldBuildValidFixtureDef()
	{
		FixtureDef def = builder.setDensity(0.5f).setFriction(2.0f).setRestitution(3.0f).setWidth(4.0f).setHeight(5.0f)
			   .setOffsetX(6.0f).setOffsetY(7.0f).setSensor(true).setCategory("ABC").setMask("QWE").setShape(ZootBodyShape.CIRCLE)
			   .build(actor);
		
		assertNotNull(def);
		assertEquals(0.5f, def.density, 0.0f);
		assertEquals(2.0f, def.friction, 0.0f);
		assertEquals(3.0f, def.restitution, 0.0f);
		assertEquals(true, def.isSensor);
		assertEquals(Type.Circle, def.shape.getType());
		assertEquals(4.0f, def.shape.getRadius(), 0.0f);
		assertEquals(BitMaskConverter.Instance.fromString("ABC"), def.filter.categoryBits);
		assertEquals(BitMaskConverter.Instance.fromString("QWE"), def.filter.maskBits);
		assertEquals(0, def.filter.groupIndex);
	}
	
	@Test
	public void shouldResetAfterBuild()
	{
		builder.setDensity(0.5f).setFriction(2.0f).setRestitution(3.0f).setWidth(4.0f).setHeight(5.0f)
			   .setOffsetX(6.0f).setOffsetY(7.0f).setSensor(true).setCategory("ABC").setMask("QWE").setShape(ZootBodyShape.CIRCLE)
			   .setDimensions(FixtureDimensions.ActorScaled).build(actor);
		
		assertEquals(1.0f, builder.getDensity(), 0.0f);
		assertEquals(0.2f, builder.getFriction(), 0.0f);
		assertEquals(0.0f, builder.getRestitution(), 0.0f);
		assertEquals(0.0f, builder.getWidth(), 0.0f);
		assertEquals(0.0f, builder.getHeight(), 0.0f);
		assertEquals(0.0f, builder.getOffsetX(), 0.0f);
		assertEquals(0.0f, builder.getOffsetY(), 0.0f);
		assertEquals(false, builder.getSensor());
		assertEquals(ZootBodyShape.BOX, builder.getShape());		
		assertEquals("", builder.getCategory());
		assertEquals("", builder.getMask());
		assertEquals(FixtureDimensions.Provided, builder.getFixtureDimensions());
	}
	
	@Test
	public void shouldBuildBoxShapeFromProvidedSize()
	{
		//given
		final float width = 1.0f;
		final float height = 2.0f;
		Vector2 vertex = new Vector2();
		
		//when
		FixtureDef def = builder.setWidth(width).setHeight(height).setShape(ZootBodyShape.BOX).build(actor);
		
		//then
		assertEquals(Type.Polygon, def.shape.getType());		
		PolygonShape polygonShape = (PolygonShape) def.shape;		
		
		polygonShape.getVertex(0, vertex);
		assertEquals(width / -2.0f, vertex.x, 0.0f);
		assertEquals(height/ -2.0f, vertex.y, 0.0f);
		
		polygonShape.getVertex(1, vertex);
		assertEquals(width / 2.0f, vertex.x, 0.0f);
		assertEquals(height/ -2.0f, vertex.y, 0.0f);
		
		polygonShape.getVertex(2, vertex);
		assertEquals(width / 2.0f, vertex.x, 0.0f);
		assertEquals(height / 2.0f, vertex.y, 0.0f);
		
		polygonShape.getVertex(3, vertex);
		assertEquals(width / -2.0f, vertex.x, 0.0f);
		assertEquals(height / 2.0f, vertex.y, 0.0f);
	}
	
	@Test
	public void shouldBuildBoxShapeFromActorSize()
	{
		//given
		final float actorWidth = 10.0f;
		final float actorHeight = 20.0f;
		Vector2 vertex = new Vector2();
				
		//when
		actor.setWidth(actorWidth);
		actor.setHeight(actorHeight);		
		FixtureDef def = builder
				.setWidth(0.0f)
				.setHeight(0.0f)
				.setShape(ZootBodyShape.BOX)
				.setDimensions(FixtureDimensions.Actor)
				.build(actor);
		
		//then
		assertEquals(Type.Polygon, def.shape.getType());		
		PolygonShape polygonShape = (PolygonShape) def.shape;		
		
		polygonShape.getVertex(0, vertex);
		assertEquals(actorWidth / -2.0f, vertex.x, 0.0f);
		assertEquals(actorHeight/ -2.0f, vertex.y, 0.0f);
		
		polygonShape.getVertex(1, vertex);
		assertEquals(actorWidth / 2.0f, vertex.x, 0.0f);
		assertEquals(actorHeight/ -2.0f, vertex.y, 0.0f);
		
		polygonShape.getVertex(2, vertex);
		assertEquals(actorWidth / 2.0f, vertex.x, 0.0f);
		assertEquals(actorHeight / 2.0f, vertex.y, 0.0f);
		
		polygonShape.getVertex(3, vertex);
		assertEquals(actorWidth / -2.0f, vertex.x, 0.0f);
		assertEquals(actorHeight / 2.0f, vertex.y, 0.0f);
	}
	
	@Test
	public void shouldBuildBoxShapeFromScaledActorSize()
	{
		//given
		final float actorWidth = 10.0f;
		final float actorHeight = 20.0f;
		final float scaleX = 0.5f;
		final float scaleY = 2.0f;		
		final float scaledActorWidth = actorWidth * scaleX;
		final float scaledActorHeight = actorHeight * scaleY;
		Vector2 vertex = new Vector2();
				
		//when
		actor.setWidth(actorWidth);
		actor.setHeight(actorHeight);		
		FixtureDef def = builder
				.setWidth(scaleX)
				.setHeight(scaleY)
				.setShape(ZootBodyShape.BOX)
				.setDimensions(FixtureDimensions.ActorScaled)
				.build(actor);
		
		//then
		assertEquals(Type.Polygon, def.shape.getType());		
		PolygonShape polygonShape = (PolygonShape) def.shape;		
		
		polygonShape.getVertex(0, vertex);
		assertEquals(scaledActorWidth / -2.0f, vertex.x, 0.0f);
		assertEquals(scaledActorHeight/ -2.0f, vertex.y, 0.0f);
		
		polygonShape.getVertex(1, vertex);
		assertEquals(scaledActorWidth / 2.0f, vertex.x, 0.0f);
		assertEquals(scaledActorHeight/ -2.0f, vertex.y, 0.0f);
		
		polygonShape.getVertex(2, vertex);
		assertEquals(scaledActorWidth / 2.0f, vertex.x, 0.0f);
		assertEquals(scaledActorHeight / 2.0f, vertex.y, 0.0f);
		
		polygonShape.getVertex(3, vertex);
		assertEquals(scaledActorWidth / -2.0f, vertex.x, 0.0f);
		assertEquals(scaledActorHeight / 2.0f, vertex.y, 0.0f);
	}
	
	@Test
	public void shouldBuildNoShape()
	{
		FixtureDef def = builder.setShape(ZootBodyShape.NONE).build(actor);
		
		//then
		assertEquals(null, def.shape);
	}
}
