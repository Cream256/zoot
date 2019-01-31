package com.zootcat.controllers.physics;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.controllers.physics.OnCollideWithSensorController.SensorCollisionResult;
import com.zootcat.exceptions.ZootControllerNotFoundException;
import com.zootcat.scene.ZootScene;
import com.zootcat.testing.ZootActorStub;

public class StickyPlatformSensorControllerTest
{
	private static final Vector2 EXPECTED_PLATFORM_VELOCITY = new Vector2(50.0f, 25.0f);
	
	private Shape otherShape;
	private Shape platformShape;
	private Shape sensorShape;
	private ZootActorStub otherActor;
	private ZootActorStub platformActor;	
	private StickyPlatformSensorController stickyPlatformSensorCtrl;
	@Mock private PhysicsBodyController otherPhysicsBodyCtrl;
	@Mock private PhysicsBodyController platformPhysicsBodyCtrl;
	@Mock private Fixture platformFixture;
	@Mock private Fixture otherFixture;
	@Mock private Fixture sensorFixture;	
	@Mock private ZootScene scene;
	
	@BeforeClass()
	public static void init()
	{
		Box2D.init();
	}
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		when(scene.getUnitScale()).thenReturn(1.0f);
		when(otherPhysicsBodyCtrl.getVelocity()).thenReturn(new Vector2(0.0f, 0.0f));
		when(otherPhysicsBodyCtrl.getCenterPositionRef()).thenReturn(new Vector2(0.0f, 0.0f));
				
		otherActor = new ZootActorStub();
		otherActor.addController(otherPhysicsBodyCtrl);
		when(otherFixture.getUserData()).thenReturn(otherActor);
		
		otherShape = new PolygonShape();
		when(otherFixture.getShape()).thenReturn(otherShape);
		
		platformActor = new ZootActorStub();		
		platformActor.addController(platformPhysicsBodyCtrl);
		when(platformFixture.getUserData()).thenReturn(platformActor);
		
		platformShape = new PolygonShape();
		when(platformFixture.getShape()).thenReturn(platformShape);
		
		stickyPlatformSensorCtrl = new StickyPlatformSensorController();
		stickyPlatformSensorCtrl.init(platformActor);
		ControllerAnnotations.setControllerParameter(stickyPlatformSensorCtrl, "scene", scene);
		
		when(platformPhysicsBodyCtrl.addFixture(any(), eq(platformActor))).thenReturn(sensorFixture);
		when(platformPhysicsBodyCtrl.getCenterPositionRef()).thenReturn(new Vector2());
		
		sensorShape = new PolygonShape();
		when(sensorFixture.getShape()).thenReturn(sensorShape);
		
		platformActor.addController(stickyPlatformSensorCtrl);		
	}
	
	@Test(expected = ZootControllerNotFoundException.class)
	public void shouldThrowIfPlatformHasNoPhysicsBodyController()
	{
		//when
		platformActor.removeController(platformPhysicsBodyCtrl);
		
		//then throw
		stickyPlatformSensorCtrl.onCollision(otherFixture);		
	}
	
	@Test
	public void shouldNotThrowIfOtherActorHasNoPhysicsController()
	{
		//given
		otherActor.removeController(otherPhysicsBodyCtrl);
		
		//when		
		stickyPlatformSensorCtrl.onCollision(otherFixture);
		
		//then ok		
	}
	
	@Test
	public void shouldUpdateOtherActorHorziontalVelocityIfActorIsNotMoving()
	{
		//given
		when(platformPhysicsBodyCtrl.getVelocity()).thenReturn(EXPECTED_PLATFORM_VELOCITY);
		
		//when		
		assertEquals(SensorCollisionResult.ProcessNext, stickyPlatformSensorCtrl.onCollision(otherFixture));
		
		//then
		verify(otherPhysicsBodyCtrl).setVelocity(EXPECTED_PLATFORM_VELOCITY.x, EXPECTED_PLATFORM_VELOCITY.y, true, false);
	}
	
	@Test
	public void shouldMoveActorRightIfActorIsMovingSlowerThanThePlatform()
	{
		//given
		when(platformPhysicsBodyCtrl.getVelocity()).thenReturn(EXPECTED_PLATFORM_VELOCITY);
		when(otherPhysicsBodyCtrl.getVelocity()).thenReturn(new Vector2(EXPECTED_PLATFORM_VELOCITY.x / 2.0f, 0.0f));
		
		//when
		assertEquals(SensorCollisionResult.ProcessNext, stickyPlatformSensorCtrl.onCollision(otherFixture));
		
		//then
		verify(otherPhysicsBodyCtrl).setVelocity(EXPECTED_PLATFORM_VELOCITY.x, EXPECTED_PLATFORM_VELOCITY.y, true, false);		
	}
	
	@Test
	public void shouldNotMoveActorRightIfActorIsMovingFasterThanThePlatform()
	{
		//given
		when(platformPhysicsBodyCtrl.getVelocity()).thenReturn(EXPECTED_PLATFORM_VELOCITY);
		when(otherPhysicsBodyCtrl.getVelocity()).thenReturn(new Vector2(EXPECTED_PLATFORM_VELOCITY.x * 2.0f, 0.0f));
		
		//when
		assertEquals(SensorCollisionResult.ProcessNext, stickyPlatformSensorCtrl.onCollision(otherFixture));
		
		//then
		verify(otherPhysicsBodyCtrl).setVelocity(EXPECTED_PLATFORM_VELOCITY.x, EXPECTED_PLATFORM_VELOCITY.y, false, false);		
	}
	
	@Test
	public void shouldNotMoveActorRightIfActorIsMovingAndPlatformIsNot()
	{
		//given
		when(platformPhysicsBodyCtrl.getVelocity()).thenReturn(new Vector2(0.0f, 0.0f));
		when(otherPhysicsBodyCtrl.getVelocity()).thenReturn(new Vector2(EXPECTED_PLATFORM_VELOCITY.x * 2.0f, 0.0f));
		
		//when
		assertEquals(SensorCollisionResult.ProcessNext, stickyPlatformSensorCtrl.onCollision(otherFixture));
		
		//then
		verify(otherPhysicsBodyCtrl).setVelocity(0.0f, 0.0f, false, false);		
	}
	
	@Test
	public void shouldNotMoveActorRightIfActorIsNotAndPlatformIsNotMoving()
	{
		//given
		when(platformPhysicsBodyCtrl.getVelocity()).thenReturn(new Vector2(0.0f, 0.0f));
		when(otherPhysicsBodyCtrl.getVelocity()).thenReturn(new Vector2(0.0f, 0.0f));
		
		//when
		assertEquals(SensorCollisionResult.ProcessNext, stickyPlatformSensorCtrl.onCollision(otherFixture));
		
		//then
		verify(otherPhysicsBodyCtrl).setVelocity(0.0f, 0.0f, false, false);		
	}
		
	@Test
	public void shouldNotMoveActorLeftIfActorIsMovingAndPlatformIsNot()
	{
		//given
		when(platformPhysicsBodyCtrl.getVelocity()).thenReturn(new Vector2(0.0f, 0.0f));
		when(otherPhysicsBodyCtrl.getVelocity()).thenReturn(new Vector2(-EXPECTED_PLATFORM_VELOCITY.x * 2.0f, 0.0f));
		
		//when
		assertEquals(SensorCollisionResult.ProcessNext, stickyPlatformSensorCtrl.onCollision(otherFixture));
		
		//then
		verify(otherPhysicsBodyCtrl).setVelocity(0.0f, 0.0f, false, false);		
	}
			
	@Test
	public void shouldNotUpdateOtherActorHorizontalVelocityIfActorIsJumping()
	{
		//given
		when(platformPhysicsBodyCtrl.getVelocity()).thenReturn(EXPECTED_PLATFORM_VELOCITY);
		when(otherPhysicsBodyCtrl.getVelocity()).thenReturn(new Vector2(0.0f, StickyPlatformSensorController.VELOCITY_Y_JUMP_THRESHOLD));
		
		//when
		assertEquals(SensorCollisionResult.ProcessNext, stickyPlatformSensorCtrl.onCollision(otherFixture));
		
		//then
		verify(otherPhysicsBodyCtrl).setVelocity(anyFloat(), anyFloat(), eq(false), eq(false));	
	}
	
	@Test
	public void shouldNotUpdateOtherActorHorizontalVelicityIfActorIsNotStandingOnTopOfSensor()
	{
		//given
		when(platformPhysicsBodyCtrl.getVelocity()).thenReturn(EXPECTED_PLATFORM_VELOCITY);
		when(otherPhysicsBodyCtrl.getCenterPositionRef()).thenReturn(new Vector2(0.0f, -0.5f));
		
		//when		
		assertEquals(SensorCollisionResult.ProcessNext, stickyPlatformSensorCtrl.onCollision(otherFixture));
		
		//then
		verify(otherPhysicsBodyCtrl, never()).setVelocity(anyFloat(), anyFloat(), anyBoolean(), anyBoolean());	
	}
	
}
