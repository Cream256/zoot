package com.zootcat.controllers.physics;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape.Type;
import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.events.ActorEventCounterListener;
import com.zootcat.physics.ZootPhysics;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;

public class DetectGroundControllerTest
{		
	private static final float ACTOR_WIDTH = 20.0f;
	private static final float ACTOR_HEIGHT = 10.0f;
	private static final float SCENE_UNIT_SCALE = 1.0f;
	
	private ZootActor actor;
	private ZootActor otherActor;
	private PhysicsBodyController otherActorPhysicsCtrl;
	
	private ZootScene sceneMock;
	private ZootPhysics physics;
	private PhysicsBodyController physicsCtrl;
	private DetectGroundController groundCtrl;
	private ActorEventCounterListener eventCounter;
	
	@Before
	public void setup()
	{
		//create physics
		physics = new ZootPhysics();
		
		//create scene
		sceneMock = mock(ZootScene.class);
		when(sceneMock.getPhysics()).thenReturn(physics);
		when(sceneMock.getUnitScale()).thenReturn(SCENE_UNIT_SCALE);		
		
		//create main actor
		actor = new ZootActor();
		actor.setSize(ACTOR_WIDTH, ACTOR_HEIGHT);
		
		eventCounter = new ActorEventCounterListener();
		actor.addListener(eventCounter);
		
		physicsCtrl = new PhysicsBodyController();
		ControllerAnnotations.setControllerParameter(physicsCtrl, "scene", sceneMock);
		
		physicsCtrl.init(actor);		
		actor.addController(physicsCtrl);
		
		//create other actor
		otherActor = new ZootActor();
		otherActor.setSize(ACTOR_WIDTH, ACTOR_HEIGHT);
		
		otherActorPhysicsCtrl = new PhysicsBodyController();
		ControllerAnnotations.setControllerParameter(otherActorPhysicsCtrl, "scene", sceneMock);
		
		otherActorPhysicsCtrl.init(actor);
		otherActor.addController(otherActorPhysicsCtrl);
		
		//create ground detector controller
		groundCtrl = new DetectGroundController();
		ControllerAnnotations.setControllerParameter(groundCtrl, "scene", sceneMock);	
	}
	
	@After
	public void tearDown()
	{
		physics.dispose();
		physics = null;
	}
		
	@Test
	public void shouldAddFeetFixture()
	{
		//given
		assertEquals("Should have only body fixture", 1, physicsCtrl.getFixtures().size());
		
		//when
		groundCtrl.init(actor);
		groundCtrl.onAdd(actor);

		//then
		assertEquals("Sensor fixture should be present", 2, physicsCtrl.getFixtures().size());
		
		Fixture sensorFixture = physicsCtrl.getFixtures().get(1);
		assertTrue("Fixture should be a sensor", sensorFixture.isSensor());		
		assertEquals("Fixture should be assigned to proper body", physicsCtrl.getBody(), sensorFixture.getBody());
		assertEquals("Fixture shape should be polygon", Type.Polygon, sensorFixture.getShape().getType());
		
		assertTrue("Actor should have registered ground detector listener", actor.getListeners().contains(groundCtrl, true));
	}
	
	@Test
	public void shouldCreateFixtureUsingCustomWidth()
	{
		//when
		final int customWidth = 128;
		ControllerAnnotations.setControllerParameter(groundCtrl, "sensorWidth", customWidth);
		
		groundCtrl.init(actor);
		groundCtrl.onAdd(actor);
		PolygonShape fixtureShape = (PolygonShape)physicsCtrl.getFixtures().get(1).getShape();
		
		//then
		Vector2 vertex1 = new Vector2();
		Vector2 vertex2 = new Vector2();
		Vector2 vertex3 = new Vector2();
		fixtureShape.getVertex(0, vertex1);
		fixtureShape.getVertex(1, vertex2);
		fixtureShape.getVertex(2, vertex3);
		assertEquals("Sensor should have actor width", customWidth, Math.abs(vertex1.x - vertex2.x), 0.0f);
		assertEquals("Sensor should have 10% of actor height", ACTOR_HEIGHT * DetectGroundController.SENSOR_HEIGHT_PERCENT, 
					Math.abs(vertex1.y - vertex3.y), 0.0f);
	}
	
	@Test
	public void shouldCreateFixtureUsingActorWidthWhenWidthIsSetToZero()
	{
		//when
		groundCtrl.init(actor);
		groundCtrl.onAdd(actor);
		PolygonShape fixtureShape = (PolygonShape)physicsCtrl.getFixtures().get(1).getShape();
		
		//then
		Vector2 vertex1 = new Vector2();
		Vector2 vertex2 = new Vector2();
		Vector2 vertex3 = new Vector2();
		fixtureShape.getVertex(0, vertex1);
		fixtureShape.getVertex(1, vertex2);
		fixtureShape.getVertex(2, vertex3);
		assertEquals("Sensor should have actor width", ACTOR_WIDTH, Math.abs(vertex1.x - vertex2.x), 0.0f);
		assertEquals("Sensor should have 10% of actor height", ACTOR_HEIGHT * DetectGroundController.SENSOR_HEIGHT_PERCENT, 
					Math.abs(vertex1.y - vertex3.y), 0.0f);
	}
	
	@Test
	public void shouldRemoveFeetFixture()
	{
		//when
		groundCtrl.init(actor);
		groundCtrl.onAdd(actor);
		groundCtrl.onRemove(actor);
		
		//then
		assertEquals("Sensor fixture should be removed", 1, physicsCtrl.getFixtures().size());
		assertFalse("Actor should have deregistered ground detector listener", actor.getListeners().contains(groundCtrl, true));
	}
	
	@Test
	public void shouldProperlyDetectContacts()
	{
		//given
		Fixture fixtureB = mock(Fixture.class);
		Contact contactMock = mock(Contact.class);
		when(contactMock.getFixtureB()).thenReturn(fixtureB);
		when(contactMock.isEnabled()).thenReturn(true);
		
		//when
		groundCtrl.init(actor);
		groundCtrl.onAdd(actor);
		
		Fixture groundSensorFixture = physicsCtrl.getFixtures().get(1);		
		when(contactMock.getFixtureA()).thenReturn(groundSensorFixture);
		
		//then
		groundCtrl.beginContact(actor, otherActor, contactMock);
		groundCtrl.onUpdate(1.0f, actor);
		assertTrue("Should detect contact", groundCtrl.isOnGround());
		groundCtrl.endContact(actor, otherActor, contactMock);
		
		groundCtrl.beginContact(otherActor, otherActor, contactMock);
		groundCtrl.onUpdate(1.0f, actor);
		assertFalse("Should not detect contact between invalid actors", groundCtrl.isOnGround()); 
				
		//when
		when(fixtureB.isSensor()).thenReturn(true);
		
		//then
		groundCtrl.beginContact(actor, otherActor, contactMock);
		groundCtrl.onUpdate(1.0f, actor);
		assertFalse("Should not detect contact between two sensors", groundCtrl.isOnGround()); 
	}
		
	@Test
	public void shouldProperlyReturnOnGroundProperty()
	{
		//given
		groundCtrl.init(actor);
		groundCtrl.onAdd(actor);
		
		//then
		assertFalse("Should not be on ground by default", groundCtrl.isOnGround());
		
		//given
		Fixture fixtureB = mock(Fixture.class);
		Contact contactMock = mock(Contact.class);
		when(contactMock.getFixtureB()).thenReturn(fixtureB);
		Fixture groundSensorFixture = physicsCtrl.getFixtures().get(1);		
		when(contactMock.getFixtureA()).thenReturn(groundSensorFixture);
		when(contactMock.isEnabled()).thenReturn(true);
		
		//when
		groundCtrl.beginContact(actor, otherActor, contactMock);
		groundCtrl.onUpdate(1.0f, actor);
		
		//then
		assertTrue("Ground should be detected", groundCtrl.isOnGround());
		assertEquals("Event should be sent to actor", 1, eventCounter.getCount());
		
		//when
		groundCtrl.endContact(actor, otherActor, contactMock);
	    groundCtrl.beginContact(actor, otherActor, contactMock);
		groundCtrl.onUpdate(1.0f, actor);
		
		//then
		assertTrue("Ground still should be detected", groundCtrl.isOnGround());
		assertEquals("Event should be sent", 2, eventCounter.getCount());
		
		//when
		groundCtrl.endContact(actor, otherActor, contactMock);
		groundCtrl.onUpdate(1.0f, actor);
		
		//then
		assertFalse("Ground should not be detected", groundCtrl.isOnGround());
		assertEquals("No event should be sent", 2, eventCounter.getCount());
		
		//when
		groundCtrl.beginContact(actor, otherActor, contactMock);
		groundCtrl.onUpdate(1.0f, actor);
		
		//then
		assertTrue("Ground should be detected", groundCtrl.isOnGround());
		assertEquals("Event should be sent", 3, eventCounter.getCount());
		
		//when contact is disabled
		when(contactMock.isEnabled()).thenReturn(false);
		groundCtrl.beginContact(actor, otherActor, contactMock);
		groundCtrl.preSolve(actor, otherActor, contactMock, mock(Manifold.class));
		groundCtrl.onUpdate(1.0f, actor);
		
		//then
		assertFalse("Ground should not be detected", groundCtrl.isOnGround());
		assertEquals("No event should be sent", 3, eventCounter.getCount());
		
		//when contact is enabled
		when(contactMock.isEnabled()).thenReturn(true);
		groundCtrl.beginContact(actor, otherActor, contactMock);
		groundCtrl.preSolve(actor, otherActor, contactMock, mock(Manifold.class));
		groundCtrl.onUpdate(1.0f, actor);
		
		//then
		assertTrue("Ground should be detected", groundCtrl.isOnGround());
		assertEquals("Event should be sent", 4, eventCounter.getCount());
	}
		
	@Test
	public void shouldDoNothingOnPostSolve()
	{
		ZootActor actorA = mock(ZootActor.class);
		ZootActor actorB = mock(ZootActor.class);
		ContactImpulse contactImpulse = mock(ContactImpulse.class);
				
		groundCtrl.postSolve(actorA, actorB, contactImpulse);
		verifyZeroInteractions(actorA, actorB, contactImpulse);
	}
	
	@Test
	public void multipleContactsShouldBeProperlyHandled()
	{
		//given
		groundCtrl.init(actor);
		groundCtrl.onAdd(actor);
		
		Fixture fixtureB = mock(Fixture.class);
		Fixture fixtureC = mock(Fixture.class);
		Fixture fixtureD = mock(Fixture.class);
		Fixture groundSensorFixture = physicsCtrl.getFixtures().get(1);
			
		Contact contact1 = mock(Contact.class);
		when(contact1.getFixtureA()).thenReturn(groundSensorFixture);
		when(contact1.getFixtureB()).thenReturn(fixtureB);
		when(contact1.isEnabled()).thenReturn(true);
		
		Contact contact2 = mock(Contact.class);
		when(contact2.getFixtureA()).thenReturn(groundSensorFixture);
		when(contact2.getFixtureB()).thenReturn(fixtureC);
		when(contact2.isEnabled()).thenReturn(true);
		
		Contact contact3 = mock(Contact.class);
		when(contact3.getFixtureA()).thenReturn(groundSensorFixture);
		when(contact3.getFixtureB()).thenReturn(fixtureD);
		when(contact3.isEnabled()).thenReturn(true);
		
		//when
		groundCtrl.beginContact(actor, otherActor, contact1);
		groundCtrl.beginContact(actor, otherActor, contact2);
		groundCtrl.beginContact(actor, otherActor, contact3);
		groundCtrl.preSolve(actor, otherActor, contact1, mock(Manifold.class));
		groundCtrl.preSolve(actor, otherActor, contact2, mock(Manifold.class));
		groundCtrl.preSolve(actor, otherActor, contact3, mock(Manifold.class));			
		groundCtrl.onUpdate(1.0f, actor);
		
		//then
		assertTrue("Ground should be detected", groundCtrl.isOnGround());
		assertEquals("Event should be sent", 1, eventCounter.getCount());
		
		//when only one contact is left enabled
		when(contact2.isEnabled()).thenReturn(false);
		when(contact3.isEnabled()).thenReturn(false);
		groundCtrl.preSolve(actor, otherActor, contact1, mock(Manifold.class));
		groundCtrl.preSolve(actor, otherActor, contact2, mock(Manifold.class));
		groundCtrl.preSolve(actor, otherActor, contact3, mock(Manifold.class));			

		//this is required to simulate box2d behaviour, all contacts are reenabled after postsolve
		when(contact2.isEnabled()).thenReturn(true);
		when(contact3.isEnabled()).thenReturn(true);

		groundCtrl.onUpdate(1.0f, actor);
		
		//then
		assertTrue("Ground should be detected", groundCtrl.isOnGround());
		assertEquals("Event should be sent", 2, eventCounter.getCount());
		
		//when all contacts are disabled
		when(contact1.isEnabled()).thenReturn(false);
		when(contact2.isEnabled()).thenReturn(false);
		when(contact3.isEnabled()).thenReturn(false);
		groundCtrl.preSolve(actor, otherActor, contact1, mock(Manifold.class));
		groundCtrl.preSolve(actor, otherActor, contact2, mock(Manifold.class));
		groundCtrl.preSolve(actor, otherActor, contact3, mock(Manifold.class));			
		
		//this is required to simulate box2d behaviour, all contacts are reenabled after postsolve
		when(contact1.isEnabled()).thenReturn(true);
		when(contact2.isEnabled()).thenReturn(true);
		when(contact3.isEnabled()).thenReturn(true);
		
		groundCtrl.onUpdate(1.0f, actor);
		
		//then
		assertFalse("Ground should not be detected", groundCtrl.isOnGround());
		assertEquals("Event should not be sent", 2, eventCounter.getCount());
	}
	
	@Test
	public void feetFixtureShouldHaveTheSameCollisionFilterAsActor()
	{
		//given
		Filter expectedFilter = mock(Filter.class);
		expectedFilter.categoryBits = 1;
		expectedFilter.maskBits = 2;
		expectedFilter.groupIndex = 3;
		
		CollisionFilterController filterCtrl = mock(CollisionFilterController.class);
		when(filterCtrl.getCollisionFilter()).thenReturn(expectedFilter);
		
		//when
		actor.addController(filterCtrl);
		groundCtrl.init(actor);
		groundCtrl.onAdd(actor);
		
		//then		
		Filter feetFilter = physicsCtrl.getFixtures().get(1).getFilterData();
		assertNotNull(feetFilter);
		assertEquals(expectedFilter.categoryBits, feetFilter.categoryBits);
		assertEquals(expectedFilter.maskBits, feetFilter.maskBits);
		assertEquals(expectedFilter.groupIndex, feetFilter.groupIndex);
	}
}
