package com.zootcat.controllers.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.events.ZootActorEventCounterListener;
import com.zootcat.fsm.states.ClimbState;
import com.zootcat.fsm.states.IdleState;
import com.zootcat.physics.ZootPhysics;
import com.zootcat.physics.ZootShapeFactory;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;
import com.zootcat.scene.ZootScene;
import com.zootcat.utils.BitMaskConverter;

public class ClimbControllerTest 
{
	private static final float CTRL_ACTOR_WIDTH = 100.0f;
	private static final float CTRL_ACTOR_HEIGHT = 50.0f;
	private static final float MAX_CLIMB_VELOCITY = 12.0f;
	private static final float CLIMB_TIMEOUT = 2.0f;
	private static final float TRESHOLD = 5.0f;
	
	@Mock private ZootScene scene;
	@Mock private Contact contact;
	@Mock private Manifold manifold;
	@Mock private Fixture otherFixture;
	@Mock private ZootActor otherActor;
	@Mock private ContactImpulse contactImpulse;
		
	private ClimbController ctrl;
	private ZootActor ctrlActor;
	private ZootPhysics physics;
	private PhysicsBodyController physicsCtrl;
	private ZootActorEventCounterListener eventCounter;
		
	@Before
	public void setup()
	{		
		MockitoAnnotations.initMocks(this);
		
		//physics
		physics = new ZootPhysics();
		when(scene.getPhysics()).thenReturn(physics);
		when(scene.getUnitScale()).thenReturn(1.0f);
		
		//ctrl actor
		ctrlActor = new ZootActor();
		ctrlActor.setSize(CTRL_ACTOR_WIDTH, CTRL_ACTOR_HEIGHT);
		
		//event counter
		eventCounter = new ZootActorEventCounterListener();
		ctrlActor.addListener(eventCounter);
		
		//physics body ctrl
		physicsCtrl = new PhysicsBodyController();
		ControllerAnnotations.setControllerParameter(physicsCtrl, "scene", scene);
		physicsCtrl.init(ctrlActor);
		ctrlActor.addController(physicsCtrl);
				
		//tested controller
		ctrl = new ClimbController();
		ControllerAnnotations.setControllerParameter(ctrl, "scene", scene);
		ControllerAnnotations.setControllerParameter(ctrl, "timeout", CLIMB_TIMEOUT);
		ControllerAnnotations.setControllerParameter(ctrl, "treshold", TRESHOLD);
		ControllerAnnotations.setControllerParameter(ctrl, "maxVelocity", MAX_CLIMB_VELOCITY);
		
		//bitmask converter cleanup
		BitMaskConverter.Instance.clear();
	}
	

	@Test
	public void shouldReturnTrueIfActorIsInClimbingState()
	{
		//when
		ctrlActor.getStateMachine().changeState(new ClimbState(), null);
		
		//then
		assertTrue(ctrl.isActorClimbing(ctrlActor));
	}
	
	@Test
	public void shouldReturnFalseWhenActorIsNotInClimbingState()
	{
		//when
		ctrlActor.getStateMachine().changeState(new IdleState(), null);
		
		//then
		assertFalse(ctrl.isActorClimbing(ctrlActor));
	}
	
	@Test
	public void shouldReturnTrueWhenActorCanClimb()
	{
		//when		
		ctrlActor.getStateMachine().changeState(new IdleState(), null);
		physicsCtrl.setVelocity(0.0f, MAX_CLIMB_VELOCITY);
				
		//then
		assertTrue(ctrl.canActorGrab(ctrlActor));
	}
	
	@Test
	public void shouldReturnFalseWhenActorCanNotClimbBecauseOfVelocity()
	{
		//when
		physicsCtrl.setVelocity(0.0f, MAX_CLIMB_VELOCITY + 0.1f);
		
		//then
		assertFalse(ctrl.canActorGrab(ctrlActor));
	}
	
	@Test
	public void shouldReturnFalseWhenActorCanNotClimbBecauseIsClimbingNow()
	{
		//when
		ctrlActor.getStateMachine().changeState(new ClimbState(), null);
		
		//then
		assertFalse(ctrl.canActorGrab(ctrlActor));
	}
	
	@Test
	public void shouldNotBeAbleToClimbWhenTimeoutIsExceeded()
	{
		//when timeout is set to max
		ctrlActor.getStateMachine().changeState(new ClimbState(), null);
		ctrl.onUpdate(0.0f, ctrlActor);
		
		//then
		assertFalse(ctrl.canActorGrab(ctrlActor));
		
		//when timeout is half way through and actor no longer climbing
		ctrlActor.getStateMachine().changeState(new IdleState(), null);
		ctrl.onUpdate(CLIMB_TIMEOUT / 2.0f, ctrlActor);
		
		//then
		assertFalse(ctrl.canActorGrab(ctrlActor));
		
		//when timeout is reached
		ctrl.onUpdate(CLIMB_TIMEOUT / 2.0f, ctrlActor);
		
		//then
		assertTrue(ctrl.canActorGrab(ctrlActor));
	}
	
	private Fixture createFixture(boolean isSensor, Vector2 position, float width, float height)
	{
		Fixture fixture = mock(Fixture.class);
		Body body = mock(Body.class);
		
		when(fixture.isSensor()).thenReturn(isSensor);
		when(fixture.getBody()).thenReturn(body);
		when(fixture.getShape()).thenReturn(ZootShapeFactory.createBox(width, height));		
		when(body.getPosition()).thenReturn(position);
		
		return fixture;
	}
	
	@Test
	public void shouldNotBeAbleToGrabFixtureWhenFixtureIsSensor()
	{
		//when
		Fixture sensor = createFixture(true, new Vector2(), CTRL_ACTOR_WIDTH, CTRL_ACTOR_HEIGHT);
		Fixture fixtureToGrab = createFixture(true, new Vector2(), CTRL_ACTOR_WIDTH, CTRL_ACTOR_HEIGHT);
		
		//then
		assertFalse(ctrl.isFixtureGrabbable(ctrlActor, sensor, fixtureToGrab));
	}
		
	@Test
	public void shouldNotBeAbleToGrabFixtureIfNotCollidingWithFixtureTop()
	{
		//when
		Fixture sensor = createFixture(true, new Vector2(), CTRL_ACTOR_WIDTH, CTRL_ACTOR_HEIGHT);
		Fixture fixtureToGrab = createFixture(false, new Vector2(0.0f, TRESHOLD + 0.1f), CTRL_ACTOR_WIDTH, CTRL_ACTOR_HEIGHT);
				
		//then
		assertFalse(ctrl.isFixtureGrabbable(ctrlActor, sensor, fixtureToGrab));
	}
	
	@Test
	public void shouldBeAbleToGrabFixture()
	{
		//when
		Fixture sensor = createFixture(true, new Vector2(), CTRL_ACTOR_WIDTH, CTRL_ACTOR_HEIGHT);
		Fixture fixtureToGrab = createFixture(false, new Vector2(), CTRL_ACTOR_WIDTH, CTRL_ACTOR_HEIGHT);
				
		//then
		assertTrue(ctrl.isFixtureGrabbable(ctrlActor, sensor, fixtureToGrab));
	}
	
	@Test
	public void shouldReturnNoneDirectionByDefault()
	{
		assertEquals(ZootDirection.None, ctrl.getSensorPosition());
	}
	
	@Test
	public void shouldSetUpSensorPositionAfterAddingToActor()
	{
		ctrl.init(ctrlActor);
		ctrl.onAdd(ctrlActor);
		assertEquals(ZootDirection.Up, ctrl.getSensorPosition());
	}
	
	@Test
	public void shouldSetClimbSensorFixtureToDifferentPlaces()
	{
		ctrl.init(ctrlActor);
		ctrl.onAdd(ctrlActor);		
		
		ctrl.setSensorPosition(ZootDirection.Right);
		assertEquals(ZootDirection.Right, ctrl.getSensorPosition());
		
		ctrl.setSensorPosition(ZootDirection.Left);
		assertEquals(ZootDirection.Left, ctrl.getSensorPosition());
		
		ctrl.setSensorPosition(ZootDirection.Up);
		assertEquals(ZootDirection.Up, ctrl.getSensorPosition());
	}
}