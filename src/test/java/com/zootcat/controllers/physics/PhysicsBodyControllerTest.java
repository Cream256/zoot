package com.zootcat.controllers.physics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.zootcat.controllers.ControllerPriority;
import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.physics.ZootBodyShape;
import com.zootcat.physics.ZootPhysics;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;

public class PhysicsBodyControllerTest
{
	private static final float ACTOR_X = 50;
	private static final float ACTOR_Y = 75;
	private static final float ACTOR_WIDTH = 100.0f;
	private static final float ACTOR_HEIGHT = 200.0f;
	private static final float SCENE_UNIT_SCALE = 0.5f;

	private static final Vector2 BODY_LINEAR_VELOCITY = new Vector2(50.0f, 75.0f);

	@Mock private Body body;
	@Mock private Fixture fixture;
	@Mock private ZootScene scene;
	@Mock private ZootActor ctrlActor;
	@Mock private ZootPhysics physics;
	@Captor private ArgumentCaptor<List<FixtureDef>> fixtureDefCaptor;
	
	private PhysicsBodyController physicsBodyCtrl;
	
	@BeforeClass
	public static void initialize()
	{
		Box2D.init();
	}
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		when(scene.getPhysics()).thenReturn(physics);
		when(scene.getUnitScale()).thenReturn(SCENE_UNIT_SCALE);
		when(body.getPosition()).thenReturn(new Vector2());
		when(body.getLinearVelocity()).thenReturn(BODY_LINEAR_VELOCITY);
		when(physics.createBody(any())).thenReturn(body);
		when(physics.createFixtures(any(), any())).thenReturn(new ArrayList<Fixture>(Arrays.asList(fixture)));
		when(ctrlActor.getX()).thenReturn(ACTOR_X);
		when(ctrlActor.getY()).thenReturn(ACTOR_Y);
		when(ctrlActor.getWidth()).thenReturn(ACTOR_WIDTH);
		when(ctrlActor.getHeight()).thenReturn(ACTOR_HEIGHT);
		
		physicsBodyCtrl = new PhysicsBodyController();
		ControllerAnnotations.setControllerParameter(physicsBodyCtrl, "scene", scene);
		physicsBodyCtrl.init(ctrlActor);
	}
	
	@Test
	public void shouldHaveProperDefaultValues()
	{
		assertEquals(0.0f, physicsBodyCtrl.density, 1.0f);
		assertEquals(0.0f, physicsBodyCtrl.friction, 0.2f);
		assertEquals(0.0f, physicsBodyCtrl.restitution, 0.0f);
		assertEquals(0.0f, physicsBodyCtrl.linearDamping, 0.0f);
		assertEquals(0.0f, physicsBodyCtrl.angularDamping, 0.0f);	
		assertEquals(0.0f, physicsBodyCtrl.gravityScale, 1.0f);
		assertEquals(0.0f, physicsBodyCtrl.shapeOffsetX, 0.0f);
		assertEquals(0.0f, physicsBodyCtrl.shapeOffsetY, 0.0f);
		assertEquals(0.0f, physicsBodyCtrl.width, 0.0f);
		assertEquals(0.0f, physicsBodyCtrl.height, 0.0f);
		assertFalse(physicsBodyCtrl.sensor);	
		assertFalse(physicsBodyCtrl.bullet);
		assertTrue(physicsBodyCtrl.canRotate);
		assertTrue(physicsBodyCtrl.canSleep);
		assertEquals(BodyType.DynamicBody, physicsBodyCtrl.type);
		assertEquals(ZootBodyShape.BOX, physicsBodyCtrl.shape);	
	}
	
	@Test
	public void shouldCreateProperPhysicsBodyDefinition()
	{
		//given
		ArgumentCaptor<BodyDef> captor = ArgumentCaptor.forClass(BodyDef.class);
		
		//then
		verify(physics).createBody(captor.capture());
		assertTrue(captor.getValue().awake);
		assertTrue(captor.getValue().active);
		assertTrue(captor.getValue().allowSleep);
		assertFalse(captor.getValue().bullet);
		assertFalse(captor.getValue().fixedRotation);
		assertEquals(0.0f, captor.getValue().angularDamping, 0.0f);
		assertEquals(0.0f, captor.getValue().angularVelocity, 0.0f);
		assertEquals(0.0f, captor.getValue().angle, 0.0f);
		assertEquals(1.0f, captor.getValue().gravityScale, 0.0f);
		assertEquals(0.0f, captor.getValue().linearDamping, 0.0f);
		assertEquals(BodyType.DynamicBody, captor.getValue().type);
		assertEquals("Should offset position by half width", ACTOR_X + ACTOR_WIDTH * 0.5f, captor.getValue().position.x, 0.0f);
		assertEquals("Should offset position by half height", ACTOR_Y + ACTOR_HEIGHT * 0.5f, captor.getValue().position.y, 0.0f);
	}
	
	@Test
	public void shouldCreateProperFixtureDefinition()
	{
		verify(physics).createFixtures(any(), fixtureDefCaptor.capture());
		assertEquals(1, fixtureDefCaptor.getValue().size());
		
		FixtureDef def = fixtureDefCaptor.getValue().get(0);
		assertFalse(def.isSensor);
		assertEquals(1.0f, def.density, 0.0f);
		assertEquals(0.2f, def.friction, 0.0f);
		assertEquals(0.0f, def.restitution, 0.0f);
		assertEquals("Filter should have default values", 1, def.filter.categoryBits);
		assertEquals("Filter should have default values", 0, def.filter.groupIndex);
		assertEquals("Filter should have default values", -1, def.filter.maskBits);
	}
	
	@Test
	public void shouldCreateBoxShapeFixtureUsingActorSize()
	{
		//given
		Vector2 vertex1 = new Vector2();
		Vector2 vertex2 = new Vector2();
		Vector2 vertex3 = new Vector2();
		Vector2 vertex4 = new Vector2();
		ControllerAnnotations.setControllerParameter(physicsBodyCtrl, "shape", ZootBodyShape.BOX);
		
		//when
		physicsBodyCtrl.init(ctrlActor);
		verify(physics, times(2)).createFixtures(any(), fixtureDefCaptor.capture());	//first time is in @Before method
		
		//then
		assertEquals(1, fixtureDefCaptor.getValue().size());
		
		//and
		FixtureDef fixtureDef = fixtureDefCaptor.getValue().get(0);
		assertEquals("Should create box shape", Shape.Type.Polygon, fixtureDef.shape.getType());
		
		//and
		PolygonShape boxShape = (PolygonShape)fixtureDef.shape;
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
		ControllerAnnotations.setControllerParameter(physicsBodyCtrl, "shape", ZootBodyShape.BOX);
		ControllerAnnotations.setControllerParameter(physicsBodyCtrl, "width", expectedWidth);
		ControllerAnnotations.setControllerParameter(physicsBodyCtrl, "height", expectedHeight);
		
		//when
		physicsBodyCtrl.init(ctrlActor);
		verify(physics, times(2)).createFixtures(any(), fixtureDefCaptor.capture());	//first time is in @Before method
		
		//then
		assertEquals(1, fixtureDefCaptor.getValue().size());
		
		//and
		FixtureDef fixtureDef = fixtureDefCaptor.getValue().get(0);
		assertEquals("Should create box shape", Shape.Type.Polygon, fixtureDef.shape.getType());
		
		//and
		PolygonShape boxShape = (PolygonShape)fixtureDef.shape;
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
		ControllerAnnotations.setControllerParameter(physicsBodyCtrl, "shape", ZootBodyShape.CIRCLE);
		
		//when
		physicsBodyCtrl.init(ctrlActor);
		
		//then
		verify(physics, times(2)).createFixtures(any(), fixtureDefCaptor.capture());	//first time is in @Before method
		assertEquals(1, fixtureDefCaptor.getValue().size());
		
		FixtureDef fixtureDef = fixtureDefCaptor.getValue().get(0);
		assertEquals("Should create circle shape", Shape.Type.Circle, fixtureDef.shape.getType());
		assertEquals("Radius should be equal to actor width", 
					ACTOR_WIDTH, 
					fixtureDef.shape.getRadius(), 0.0f);
	}
	
	@Test
	public void shouldCreateCircleShapedFixtureUsingWidthParameter()
	{
		//given
		final float widthParam = 256.0f;
		ControllerAnnotations.setControllerParameter(physicsBodyCtrl, "shape", ZootBodyShape.CIRCLE);
		ControllerAnnotations.setControllerParameter(physicsBodyCtrl, "width", widthParam);
		
		//when
		physicsBodyCtrl.init(ctrlActor);
		
		//then
		verify(physics, times(2)).createFixtures(any(), fixtureDefCaptor.capture());	//first time is in @Before method
		assertEquals(1, fixtureDefCaptor.getValue().size());
		
		FixtureDef fixtureDef = fixtureDefCaptor.getValue().get(0);
		assertTrue("Should create circle shape", fixtureDef.shape instanceof CircleShape);
		assertEquals("Radius should be equal to width parameter scaled by scene unit scale", 
					widthParam * SCENE_UNIT_SCALE, 
					fixtureDef.shape.getRadius(), 0.0f);
	}
	
	@Test(expected = RuntimeZootException.class)
	public void shouldThrowWhenUnknownBodyShapeIsProvided()
	{
		ControllerAnnotations.setControllerParameter(physicsBodyCtrl, "shape", ZootBodyShape.NONE);
		physicsBodyCtrl.init(ctrlActor);
	}
	
	@Test
	public void shouldAssignUserData()
	{
		verify(body).setUserData(eq(ctrlActor));
		verify(fixture).setUserData(eq(ctrlActor));
	}
	
	@Test
	public void shouldSetBodyAsActive()
	{
		physicsBodyCtrl.onAdd(ctrlActor);
		verify(body).setActive(eq(true));
	}
	
	@Test
	public void shouldRemoveBodyFromPhysics()
	{
		physicsBodyCtrl.onRemove(ctrlActor);
		verify(physics).removeBody(body);
	}
	
	@Test
	public void shouldReturnHighPriority()
	{
		assertEquals(ControllerPriority.High, physicsBodyCtrl.getPriority());
	}
	
	@Test
	public void shouldReturnBody()
	{
		assertEquals(body, physicsBodyCtrl.getBody());
	}
	
	@Test
	public void shouldReturnFixtures()
	{
		assertEquals(1, physicsBodyCtrl.getFixtures().size());
		assertEquals(fixture, physicsBodyCtrl.getFixtures().get(0));
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void shouldReturnUnmodifiableFixturesList()
	{
		physicsBodyCtrl.getFixtures().remove(null);
	}
	
	@Test
	public void shouldReturnBodyVelocity()
	{
		when(body.getLinearVelocity()).thenReturn(new Vector2(128.0f, 256.0f));
		assertEquals(128.0f, physicsBodyCtrl.getVelocity().x, 0.0f);
		assertEquals(256.0f, physicsBodyCtrl.getVelocity().y, 0.0f);
	}
	
	@Test
	public void shouldSetVelocityForBothParameters()
	{
		physicsBodyCtrl.setVelocity(1.23f, 2.34f);
		verify(body).setLinearVelocity(1.23f, 2.34f);
	}
	
	@Test
	public void shouldSetVelocityForXParameterOnly()
	{
		when(body.getLinearVelocity()).thenReturn(new Vector2(100.0f, 200.0f));
		
		physicsBodyCtrl.setVelocity(1.0f, -1.0f, true, false);
		verify(body).setLinearVelocity(1.00f, 200.0f);
	}
	
	@Test
	public void shouldSetVelocityForYParameterOnly()
	{
		when(body.getLinearVelocity()).thenReturn(new Vector2(100.0f, 200.0f));
		
		physicsBodyCtrl.setVelocity(1.0f, -1.0f, false, true);
		verify(body).setLinearVelocity(100.00f, -1.0f);		
	}
	
	@Test
	public void shouldSetVelocityForXAndYParameters()
	{
		when(body.getLinearVelocity()).thenReturn(new Vector2(100.0f, 200.0f));
		
		physicsBodyCtrl.setVelocity(1.0f, -1.0f, true, true);
		verify(body).setLinearVelocity(1.0f, -1.0f);
	}
	
	@Test
	public void shouldSetAngularVelocity()
	{
		physicsBodyCtrl.setAngularVelocity(2.56f);
		verify(body).setAngularVelocity(2.56f);
	}
	
	@Test
	public void shouldReturnAngularVelocity()
	{
		when(body.getAngularVelocity()).thenReturn(0.123f);
		assertEquals(0.123f, physicsBodyCtrl.getAngularVelocity(), 0.0f);
	}
	
	@Test
	public void shouldSetGravityScale()
	{
		physicsBodyCtrl.setGravityScale(1.23f);
		verify(body).setGravityScale(1.23f);
	}
	
	@Test
	public void shouldAwakeBodyWhenSettingGravityScale()
	{
		physicsBodyCtrl.setGravityScale(1.23f);
		verify(body).setAwake(eq(true));
	}
	
	@Test
	public void shouldReturnGravityScale()
	{
		when(body.getGravityScale()).thenReturn(0.5f);
		assertEquals(0.5f, physicsBodyCtrl.getGravityScale(), 0.0f);
	}
	
	@Test
	public void shouldReturnMass()
	{
		when(body.getMass()).thenReturn(1.56f);
		assertEquals(1.56f, physicsBodyCtrl.getMass(), 0.0f);
	}
	
	@Test
	public void shouldApplyLinearImpulseToBodyCenter()
	{
		when(body.getPosition()).thenReturn(new Vector2(1.0f, 2.0f));
		
		physicsBodyCtrl.applyImpulse(100.0f, -200.0f);
		verify(body).applyLinearImpulse(100.0f, -200.0f, 1.0f, 2.0f, true);
	}
	
	@Test
	public void shouldApplyAngularImpulse()
	{
		physicsBodyCtrl.applyAngularImpulse(-2.34f);
		verify(body).applyAngularImpulse(-2.34f, true);
	}
	
	@Test
	public void shouldSetBodyPosition()
	{
		when(body.getAngle()).thenReturn(1.23f);
		
		physicsBodyCtrl.setPosition(2.34f, 3.45f);
		verify(body).setTransform(2.34f, 3.45f, 1.23f);
	}
	
	@Test
	public void shouldReturnBodyCenterPositionReference()
	{
		//given
		Vector2 posRef = new Vector2(1.0f, 2.0f);
		
		//when
		when(body.getPosition()).thenReturn(posRef);
		Vector2 result = physicsBodyCtrl.getCenterPositionRef();
		
		//then
		assertEquals(posRef, result);
		
		//when
		posRef.x = 100.0f;
		
		//then
		assertEquals(posRef, result);
	}
	
	@Test
	public void shouldSetFixedRotation()
	{
		physicsBodyCtrl.setCanRotate(false);
		verify(body).setFixedRotation(true);
		
		physicsBodyCtrl.setCanRotate(true);
		verify(body).setFixedRotation(false);
	}
	
	@Test
	public void shouldSetCollisionFilterOnAllFixtures()
	{
		//given
		Fixture fix1 = mock(Fixture.class);
		Fixture fix2 = mock(Fixture.class);
		Fixture fix3 = mock(Fixture.class);
		Filter filter = new Filter();
		
		//when
		when(physics.createFixtures(any(), any())).thenReturn(Arrays.asList(fix1, fix2, fix3));
		physicsBodyCtrl.init(ctrlActor);
		physicsBodyCtrl.setCollisionFilter(filter);
		
		//then
		verify(fix1).setFilterData(filter);
		verify(fix2).setFilterData(filter);
		verify(fix3).setFilterData(filter);
	}
	
	@Test
	public void shouldAddFixture()
	{
		//given
		FixtureDef fixtureDef = new FixtureDef();
		Fixture newFixture = mock(Fixture.class);
		
		//when
		when(body.createFixture(eq(fixtureDef))).thenReturn(newFixture);
		physicsBodyCtrl.addFixture(fixtureDef, ctrlActor);
		
		//then
		assertEquals(2, physicsBodyCtrl.getFixtures().size());
		assertEquals(newFixture, physicsBodyCtrl.getFixtures().get(1));
		verify(newFixture).setUserData(ctrlActor);
	}
	
	@Test
	public void shouldRemoveFixture()
	{
		physicsBodyCtrl.removeFixture(fixture);
		
		assertEquals(0, physicsBodyCtrl.getFixtures().size());
		verify(body).destroyFixture(fixture);
	}
	
	@Test
	public void shouldSetActorPositionWithBottomLeftOffset()
	{
		//given
		final float bodyCenterX = 200.0f;
		final float bodyCenterY = 50.0f;
		final float expectedX = bodyCenterX - ACTOR_WIDTH * 0.5f;
		final float expectedY = bodyCenterY - ACTOR_HEIGHT * 0.5f;
		
		//when
		when(body.getPosition()).thenReturn(new Vector2(bodyCenterX, bodyCenterY));
		physicsBodyCtrl.onUpdate(1.0f, ctrlActor);
		
		//then
		verify(ctrlActor).setPosition(expectedX, expectedY);
	}
	
	@Test
	public void shouldSetActorRotation()
	{
		//given
		final float expectedAngle = 1.23f;
		final float expectedDegrees = expectedAngle * MathUtils.radiansToDegrees;
		
		//when
		when(body.getAngle()).thenReturn(expectedAngle);
		
		physicsBodyCtrl.onUpdate(1.0f, ctrlActor);
		
		//then
		verify(ctrlActor).setRotation(expectedDegrees);
	}
	
	@Test
	public void shouldTakeVelocityFromBody()
	{
		physicsBodyCtrl.onUpdate(1.0f, ctrlActor);
		assertEquals(BODY_LINEAR_VELOCITY, physicsBodyCtrl.getVelocity());
	}
}
