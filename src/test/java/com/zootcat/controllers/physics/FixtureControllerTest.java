package com.zootcat.controllers.physics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.physics.ZootBodyShape;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;
import com.zootcat.utils.BitMaskConverter;

public class FixtureControllerTest
{
	private static final float ACTOR_WIDTH = 10.0f;
	private static final float ACTOR_HEIGHT = 20.0f;
	private static final float SCENE_UNIT_SCALE = 0.5f;
	
	@Mock private PhysicsBodyController physicsBodyCtrl;
	@Mock private ZootScene scene;
	@Captor private ArgumentCaptor<FixtureDef> fixtureDefCaptor;	
	private ZootActor actor;
	private FixtureController ctrl;
			
	@BeforeClass
	public static void init()
	{
		Box2D.init();
	}
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);		
		when(scene.getUnitScale()).thenReturn(SCENE_UNIT_SCALE);
		
		actor = new ZootActor();
		actor.addController(physicsBodyCtrl);
				
		ctrl = new FixtureController();
		ControllerAnnotations.setControllerParameter(ctrl, "scene", scene);
	}
	
	@Test
	public void shouldHaveProperDefaultValues()
	{
		assertEquals(0.0f, ctrl.density, 1.0f);
		assertEquals(0.0f, ctrl.friction, 0.2f);
		assertEquals(0.0f, ctrl.restitution, 0.0f);
		assertEquals(0.0f, ctrl.offsetX, 0.0f);
		assertEquals(0.0f, ctrl.offsetY, 0.0f);
		assertEquals(0.0f, ctrl.width, 0.0f);
		assertEquals(0.0f, ctrl.height, 0.0f);
		assertFalse(ctrl.sensor);
		assertEquals("", ctrl.category);
		assertEquals("", ctrl.mask);
		assertEquals(ZootBodyShape.BOX, ctrl.shape);	
	}
	
	@Test(expected = RuntimeZootException.class)
	public void shouldThrowWhenBodyShapeIsNone()
	{
		//given
		ControllerAnnotations.setControllerParameter(ctrl, "shape", ZootBodyShape.NONE);
		
		//when
		ctrl.init(actor);
	}
	
	@Test
	public void shouldAddFixtureToBody()
	{
		//when
		ctrl.init(actor);
		ctrl.onAdd(actor);
		
		//then
		verify(physicsBodyCtrl).addFixture(any(), eq(actor));		
	}
	
	@Test
	public void shouldRemoveFixtureFromBody()
	{
		//when
		ctrl.init(actor);
		ctrl.onAdd(actor);
		ctrl.onRemove(actor);
		
		//then
		verify(physicsBodyCtrl).removeFixture(any());		
	}
	
	@Test
	public void shouldCreateValidFixtureDefinition()
	{
		//when
		ctrl.init(actor);
		ctrl.onAdd(actor);
		
		//then
		verify(physicsBodyCtrl).addFixture(fixtureDefCaptor.capture(), eq(actor));
		FixtureDef def = fixtureDefCaptor.getValue();
		
		assertFalse(def.isSensor);
		assertEquals(1.0f, def.density, 0.0f);
		assertEquals(0.2f, def.friction, 0.0f);
		assertEquals(0.0f, def.restitution, 0.0f);
		assertEquals("Filter should have default category", 1, def.filter.categoryBits);
		assertEquals("Filter should have default group", 0, def.filter.groupIndex);
		assertEquals("Filter should have default mask", -1, def.filter.maskBits);
	}
	
	@Test
	public void shouldCreateFixtureDefinitionWithCustomFilter()
	{
		//given
		ControllerAnnotations.setControllerParameter(ctrl, "category", "CAT");
		ControllerAnnotations.setControllerParameter(ctrl, "mask", "MASK");
		
		//when
		ctrl.init(actor);
		ctrl.onAdd(actor);
		
		//then
		verify(physicsBodyCtrl).addFixture(fixtureDefCaptor.capture(), eq(actor));
		FixtureDef def = fixtureDefCaptor.getValue();
		
		assertEquals("Filter should have custom category", BitMaskConverter.Instance.fromString("CAT"), def.filter.categoryBits);
		assertEquals("Filter should have default group", 0, def.filter.groupIndex);
		assertEquals("Filter should have custom mask", BitMaskConverter.Instance.fromString("MASK"), def.filter.maskBits);
	}
	
	@Test
	public void shouldCreateBoxShapeUsingActorSize()
	{
		//given
		Vector2 vertex1 = new Vector2();
		Vector2 vertex2 = new Vector2();
		Vector2 vertex3 = new Vector2();
		Vector2 vertex4 = new Vector2();
		ControllerAnnotations.setControllerParameter(ctrl, "shape", ZootBodyShape.BOX);
		
		//when
		actor.setSize(ACTOR_WIDTH, ACTOR_HEIGHT);
		ctrl.init(actor);
		ctrl.onAdd(actor);
		
		//then
		verify(physicsBodyCtrl).addFixture(fixtureDefCaptor.capture(), eq(actor));		
		FixtureDef def = fixtureDefCaptor.getValue();		
		assertEquals("Should create box shape", Shape.Type.Polygon, def.shape.getType());
		
		//and
		PolygonShape boxShape = (PolygonShape)def.shape;
		assertEquals(4, boxShape.getVertexCount());
		boxShape.getVertex(0, vertex1);
		boxShape.getVertex(1, vertex2);
		boxShape.getVertex(2, vertex3);
		boxShape.getVertex(3, vertex4);
		assertEquals(ACTOR_WIDTH, vertex2.x - vertex1.x, 0.0f);
		assertEquals(ACTOR_HEIGHT, vertex4.y - vertex2.y, 0.0f);
	}
	
	@Test
	public void shouldCreateBoxShapeFixtureUsingWidthAndHeightPropertiesScaled()
	{
		//given
		final float expectedWidth = 256.0f;
		final float expectedHeight = 128.0f;
		Vector2 vertex1 = new Vector2();
		Vector2 vertex2 = new Vector2();
		Vector2 vertex3 = new Vector2();
		Vector2 vertex4 = new Vector2();
		ControllerAnnotations.setControllerParameter(ctrl, "shape", ZootBodyShape.BOX);
		ControllerAnnotations.setControllerParameter(ctrl, "width", expectedWidth);
		ControllerAnnotations.setControllerParameter(ctrl, "height", expectedHeight);
		
		//when
		actor.setSize(ACTOR_WIDTH, ACTOR_HEIGHT);
		ctrl.init(actor);
		ctrl.onAdd(actor);
		
		//then
		verify(physicsBodyCtrl).addFixture(fixtureDefCaptor.capture(), eq(actor));		
		FixtureDef def = fixtureDefCaptor.getValue();		
		assertEquals("Should create box shape", Shape.Type.Polygon, def.shape.getType());
				
		PolygonShape boxShape = (PolygonShape)def.shape;
		assertEquals(4, boxShape.getVertexCount());
		boxShape.getVertex(0, vertex1);
		boxShape.getVertex(1, vertex2);
		boxShape.getVertex(2, vertex3);
		boxShape.getVertex(3, vertex4);
		assertEquals(expectedWidth * SCENE_UNIT_SCALE, vertex2.x - vertex1.x, 0.0f);
		assertEquals(expectedHeight * SCENE_UNIT_SCALE, vertex4.y - vertex2.y, 0.0f);
	}
	
	@Test
	public void shouldCreateCircleShapedFixtureUsingActorWidth()
	{
		//given
		ControllerAnnotations.setControllerParameter(ctrl, "shape", ZootBodyShape.CIRCLE);
		
		//when
		actor.setSize(ACTOR_WIDTH, ACTOR_HEIGHT);
		ctrl.init(actor);
		ctrl.onAdd(actor);
		
		//then
		verify(physicsBodyCtrl).addFixture(fixtureDefCaptor.capture(), eq(actor));		
		FixtureDef def = fixtureDefCaptor.getValue();
		assertEquals("Should create circle shape", Shape.Type.Circle, def.shape.getType());
		assertEquals("Radius should be equal to actor width", 
					ACTOR_WIDTH, 
					def.shape.getRadius(), 0.0f);
	}
	
	@Test
	public void shouldCreateCircleShapedFixtureUsingWidthParameter()
	{
		//given
		final float widthParam = 256.0f;
		ControllerAnnotations.setControllerParameter(ctrl, "shape", ZootBodyShape.CIRCLE);
		ControllerAnnotations.setControllerParameter(ctrl, "width", widthParam);
		
		//when
		actor.setSize(ACTOR_WIDTH, ACTOR_HEIGHT);
		ctrl.init(actor);
		ctrl.onAdd(actor);
		
		//then
		verify(physicsBodyCtrl).addFixture(fixtureDefCaptor.capture(), eq(actor));		
		FixtureDef def = fixtureDefCaptor.getValue();
		
		assertTrue("Should create circle shape", def.shape instanceof CircleShape);
		assertEquals("Radius should be equal to width parameter scaled by scene unit scale", 
					widthParam * SCENE_UNIT_SCALE, 
					def.shape.getRadius(), 0.0f);
	}
}
