package com.zootcat.controllers.physics;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.controllers.logic.DirectionController;
import com.zootcat.fsm.events.ZootActorEventCounterListener;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.physics.ZootPhysics;
import com.zootcat.physics.ZootPhysicsUtils;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;
import com.zootcat.scene.ZootScene;
import com.zootcat.testing.ZootActorStub;

public class DetectObstacleControllerTest
{
	private static final float ACTOR_WIDTH = 50.0f;
	private static final float ACTOR_HEIGHT = 50.0f;
	
	@Mock private Contact contact;
	@Mock private Fixture fixture;
	@Mock private ZootActor otherActor;
	@Mock private ZootScene scene;
	@Mock private DirectionController dirCtrl;
		
	private ZootPhysics physics;
	private ZootActor controllerActor;
	private PhysicsBodyController physicsBodyCtrl;
	private ZootActorEventCounterListener eventListener;
	private DetectObstacleController detectObstacleCtrl;
			
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		physics = new ZootPhysics();
		when(scene.getPhysics()).thenReturn(physics);
		when(scene.getUnitScale()).thenReturn(1.0f);
		when(dirCtrl.getDirection()).thenReturn(ZootDirection.None);
				
		//create controller actor
		controllerActor = new ZootActorStub();
		controllerActor.setWidth(ACTOR_WIDTH);
		controllerActor.setHeight(ACTOR_HEIGHT);
		
		physicsBodyCtrl = new PhysicsBodyController();
		ControllerAnnotations.setControllerParameter(physicsBodyCtrl, "scene", scene);
		physicsBodyCtrl.init(controllerActor);
		controllerActor.addController(physicsBodyCtrl);
		
		eventListener = new ZootActorEventCounterListener();				
		controllerActor.addListener(eventListener);
		
		controllerActor.addController(dirCtrl);		
		
		//create controller
		detectObstacleCtrl = new DetectObstacleController();
		ControllerAnnotations.setControllerParameter(detectObstacleCtrl, "scene", scene);
		detectObstacleCtrl.init(controllerActor);
		detectObstacleCtrl.onAdd(controllerActor);
	}
	
	@Test
	public void shouldFireObstacleEventOnCollision()
	{
		//when
		when(contact.getFixtureA()).thenReturn(detectObstacleCtrl.getSensor());
		detectObstacleCtrl.onEnter(controllerActor, otherActor, contact);
		detectObstacleCtrl.onUpdate(0.0f, controllerActor);
		
		//then		
		assertEquals(1, eventListener.getCount());
		assertEquals(ZootEventType.Obstacle, eventListener.getLastZootEvent().getType());
	}
	
	@Test
	public void shouldStartWithSensorPositionToTheRight()
	{
		//when
		when(dirCtrl.getDirection()).thenReturn(ZootDirection.Right);
		detectObstacleCtrl.onAdd(controllerActor);
		
		//then
		Vector2 sensorCenter = ZootPhysicsUtils.getPolygonCentroid((PolygonShape) detectObstacleCtrl.getSensor().getShape());
		assertEquals(ACTOR_WIDTH / 2.0f, sensorCenter.x, 0.0f);
	}
	
	@Test
	public void shouldStartWithSensorPositionToTheLeft()
	{
		//when
		when(dirCtrl.getDirection()).thenReturn(ZootDirection.Left);
		detectObstacleCtrl.onAdd(controllerActor);
		
		//then
		Vector2 sensorCenter = ZootPhysicsUtils.getPolygonCentroid((PolygonShape) detectObstacleCtrl.getSensor().getShape());
		assertEquals(-ACTOR_WIDTH / 2.0f, sensorCenter.x, 0.0f);		
	}
		
	@Test
	public void shouldSetLeftSensorPositionOnPostUpdate()
	{
		//when
		when(dirCtrl.getDirection()).thenReturn(ZootDirection.Left);
		detectObstacleCtrl.postUpdate(1.0f, controllerActor);
				
		//then
		Vector2 sensorCenter = ZootPhysicsUtils.getPolygonCentroid((PolygonShape) detectObstacleCtrl.getSensor().getShape());
		assertEquals(-ACTOR_WIDTH / 2.0f, sensorCenter.x, 0.0f);
	}
	
	@Test
	public void shouldSetRightSensorPositionOnPostUpdate()
	{
		//when
		when(dirCtrl.getDirection()).thenReturn(ZootDirection.Right);
		detectObstacleCtrl.postUpdate(1.0f, controllerActor);
				
		//then
		Vector2 sensorCenter = ZootPhysicsUtils.getPolygonCentroid((PolygonShape) detectObstacleCtrl.getSensor().getShape());
		assertEquals(ACTOR_WIDTH / 2.0f, sensorCenter.x, 0.0f);
	}
}
