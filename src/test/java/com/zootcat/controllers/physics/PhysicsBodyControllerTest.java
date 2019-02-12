package com.zootcat.controllers.physics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.zootcat.controllers.ControllerPriority;
import com.zootcat.controllers.factory.ControllerAnnotations;
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
		assertEquals(0.0f, physicsBodyCtrl.linearDamping, 0.0f);
		assertEquals(0.0f, physicsBodyCtrl.angularDamping, 0.0f);	
		assertEquals(0.0f, physicsBodyCtrl.gravityScale, 1.0f);	
		assertFalse(physicsBodyCtrl.bullet);
		assertTrue(physicsBodyCtrl.canRotate);
		assertTrue(physicsBodyCtrl.canSleep);
		assertEquals(BodyType.DynamicBody, physicsBodyCtrl.type);	
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
	public void shouldAssignUserDataToBodyAfterInit()
	{		
		verify(body).setUserData(ctrlActor);
	}
	
	@Test
	public void shouldSetBodyAsDeactivatedAfterInit()
	{		
		verify(body).setActive(false);
	}
	
	@Test
	public void shouldSetBodyAsActive()
	{
		physicsBodyCtrl.onAdd(ctrlActor);
		verify(body).setActive(true);
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
	public void shouldReturnEmptyFixtureArrayAfterInit()
	{
		assertEquals(0, physicsBodyCtrl.getFixtures().size());
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
		FixtureDef fixDef1 = new FixtureDef();
		FixtureDef fixDef2 = new FixtureDef();
		FixtureDef fixDef3 = new FixtureDef();
		Fixture fix1 = mock(Fixture.class);
		Fixture fix2 = mock(Fixture.class);
		Fixture fix3 = mock(Fixture.class);
		Filter filter = new Filter();
		
		//when
		when(physics.createFixture(any(), eq(fixDef1))).thenReturn(fix1);
		when(physics.createFixture(any(), eq(fixDef2))).thenReturn(fix2);
		when(physics.createFixture(any(), eq(fixDef3))).thenReturn(fix3);
		physicsBodyCtrl.addFixture(fixDef1, ctrlActor);
		physicsBodyCtrl.addFixture(fixDef2, ctrlActor);
		physicsBodyCtrl.addFixture(fixDef3, ctrlActor);
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
		when(physics.createFixture(any(), eq(fixtureDef))).thenReturn(newFixture);
		physicsBodyCtrl.addFixture(fixtureDef, ctrlActor);
		
		//then
		assertEquals(1, physicsBodyCtrl.getFixtures().size());
		assertEquals(newFixture, physicsBodyCtrl.getFixtures().get(0));
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
	
	@Test
	public void shouldSetLinearDamping()
	{
		physicsBodyCtrl.setLinearDamping(1.23f);
		verify(body).setLinearDamping(1.23f);		
	}
	
	@Test
	public void shouldGetLinearDampingFromBody()
	{
		when(body.getLinearDamping()).thenReturn(2.56f);
		assertEquals(2.56f, physicsBodyCtrl.getLinearDamping(), 0.0f);		
	}
	
	@Test
	public void shouldSetAwake()
	{
		physicsBodyCtrl.setAwake(true);
		verify(body).setAwake(true);
		
		physicsBodyCtrl.setAwake(false);
		verify(body).setAwake(false);
	}
	
	@Test
	public void shouldGetIsSleepingAllowedFromBody()
	{
		when(body.isSleepingAllowed()).thenReturn(true);
		assertTrue(physicsBodyCtrl.isSleepingAllowed());
		
		when(body.isSleepingAllowed()).thenReturn(false);
		assertFalse(physicsBodyCtrl.isSleepingAllowed());
	}
	
	@Test
	public void shouldBeSingleton()
	{
		assertTrue(physicsBodyCtrl.isSingleton());
	}
}
