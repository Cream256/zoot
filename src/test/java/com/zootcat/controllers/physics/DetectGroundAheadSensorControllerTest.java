package com.zootcat.controllers.physics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.controllers.logic.DirectionController;
import com.zootcat.fsm.events.ZootActorEventCounterListener;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.physics.ZootFixtureDefBuilder.FixtureDimensions;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;
import com.zootcat.scene.ZootScene;
import com.zootcat.testing.ZootActorStub;

public class DetectGroundAheadSensorControllerTest
{
	private static final float SENSOR_WIDTH = 10.0f;
	private static final float SENSOR_HEIGHT = 5.0f;
	
	private PolygonShape shape;
	private ZootActor ctrlActor;	
	private DetectGroundAheadSensorController ctrl;
	private ZootActorEventCounterListener eventCounter;
			
	@Mock private ZootScene scene;
	@Mock private PhysicsBodyController physicsCtrl;
	@Mock private DirectionController directionCtrl;
	@Mock private Fixture fixture;
	
	@BeforeClass
	public static void initialize()
	{
		Box2D.init();
	}
	
	@Before
	public void setup()
	{
		shape = new PolygonShape();
		shape.setAsBox(SENSOR_WIDTH / 2.0f, SENSOR_HEIGHT / 2.0f);
		
		MockitoAnnotations.initMocks(this);
		when(scene.getUnitScale()).thenReturn(1.0f);
		when(physicsCtrl.addFixture(any(), any())).thenReturn(fixture);
		when(fixture.getShape()).thenReturn(shape);
		when(fixture.getBody()).thenReturn(mock(Body.class));
		when(directionCtrl.getDirection()).thenReturn(ZootDirection.None);
		
		eventCounter = new ZootActorEventCounterListener();
		
		ctrlActor = new ZootActorStub();		
		ctrlActor.addController(physicsCtrl);
		ctrlActor.addController(directionCtrl);
		ctrlActor.addListener(eventCounter);
		
		ctrl = new DetectGroundAheadSensorController();
		ControllerAnnotations.setControllerParameter(ctrl, "scene", scene);
	}
	
	@Test
	public void shouldSetSensorPositionWhenAddingControllerToActor()
	{
		//given
		final float actorWidth = 10.0f;
		
		//when
		ctrlActor.setSize(actorWidth, 1.0f);
		when(directionCtrl.getDirection()).thenReturn(ZootDirection.Right);
				
		ctrl.init(ctrlActor);
		ctrl.onAdd(ctrlActor);
		
		//then
		Vector2 position = ctrl.getSensorPosition();
		assertEquals(actorWidth / 2.0f + SENSOR_WIDTH / 2.0f, position.x, 0.0f);		
	}
	
	@Test
	public void shouldSetSensorPositionToFaceActorDirectionOnPreUpdate()
	{
		//given
		final float actorWidth = 20.0f;
		final float actorHeight = 10.0f;
		
		//when
		ctrlActor.setSize(actorWidth, actorHeight);
		when(directionCtrl.getDirection()).thenReturn(ZootDirection.None);
		
		ctrl.init(ctrlActor);
		ctrl.onAdd(ctrlActor);
		
		when(directionCtrl.getDirection()).thenReturn(ZootDirection.Left);
		ctrl.preUpdate(1.0f, ctrlActor);
		
		//then
		Vector2 position = ctrl.getSensorPosition();
		assertEquals(-actorWidth / 2.0f - SENSOR_WIDTH / 2.0f, position.x, 0.0f);
		assertEquals(-actorHeight / 2.0f, position.y, 0.0f);		
	}
	
	@Test
	public void shouldFireNoGroundAheadEventWhenThereIsNoGroundAhead()
	{
		//when
		when(directionCtrl.getDirection()).thenReturn(ZootDirection.None);
		ctrl.init(ctrlActor);
		ctrl.onAdd(ctrlActor);
		ctrl.preUpdate(1.0f, ctrlActor);
		ctrl.postUpdate(1.0f, ctrlActor);
		
		//then
		assertEquals(1, eventCounter.getCount());
		assertEquals(ZootEventType.NoGroundAhead, eventCounter.getLastZootEvent().getType());
	}
		
	@Test
	public void shouldUseProvidedSizeWhenCreatingSensor()
	{
		//given
		ControllerAnnotations.setControllerParameter(ctrl, "dimensions", FixtureDimensions.Provided);
		ControllerAnnotations.setControllerParameter(ctrl, "sensorWidth", SENSOR_WIDTH);
		ControllerAnnotations.setControllerParameter(ctrl, "sensorHeight", SENSOR_HEIGHT);
				
		//when		
		ctrl.init(ctrlActor);
		ctrl.onAdd(ctrlActor);
		
		//then
		assertEquals("Should use provided width", SENSOR_WIDTH, ctrl.sensorWidth, 0.0f);
		assertEquals("Should use provided height", SENSOR_HEIGHT, ctrl.sensorHeight, 0.0f);		
	}
	
	@Test
	public void shouldNotDetectSensorAsGround()
	{
		//given
		Contact contact = mock(Contact.class);
		ZootActor otherActor = mock(ZootActor.class);
		Fixture otherActorFixture = mock(Fixture.class);
				
		//when
		when(otherActor.getWidth()).thenReturn(1.0f);
		when(otherActor.getHeight()).thenReturn(1.0f);
		when(contact.getFixtureA()).thenReturn(fixture);
		when(contact.getFixtureB()).thenReturn(otherActorFixture);
		when(otherActorFixture.isSensor()).thenReturn(true);
		
		ctrl.init(ctrlActor);
		ctrl.onAdd(ctrlActor);
		ctrl.onEnter(ctrlActor, otherActor, contact);		
		ctrl.onUpdate(1.0f, ctrlActor);
		
		//then
		assertFalse(ctrl.isGroundAhead());
	}
	
	@Test
	public void shouldDetectFixtureAsGround()
	{
		//given
		Contact contact = mock(Contact.class);
		ZootActor otherActor = mock(ZootActor.class);
		Fixture otherActorFixture = mock(Fixture.class);
				
		//when
		when(contact.getFixtureA()).thenReturn(fixture);
		when(contact.getFixtureB()).thenReturn(otherActorFixture);
		when(otherActorFixture.isSensor()).thenReturn(false);
		
		ctrl.init(ctrlActor);
		ctrl.onAdd(ctrlActor);
		ctrl.onEnter(ctrlActor, otherActor, contact);		
		ctrl.onUpdate(1.0f, ctrlActor);
		
		//then
		assertTrue(ctrl.isGroundAhead());
	}
}
