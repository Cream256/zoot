package com.zootcat.controllers.physics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape.Type;
import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.physics.ZootPhysics;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;
import com.zootcat.utils.BitMaskConverter;

public class OnCollideWithSensorControllerTest
{
	private static final int DEFAULT_GROUP_INDEX = 0;
	private static final float SENSOR_WIDTH = 10.0f;
	private static final float SENSOR_HEIGHT = 20.0f;
	private static final float SENSOR_X = 100.0f;
	private static final float SENSOR_Y = 200.0f;
	
	@Mock private ZootScene scene;
	@Mock private Contact contact;
	@Mock private Fixture otherFixture;
	@Mock private ZootActor otherActor;
	
	private ZootActor ctrlActor;
	private ZootPhysics physics;
	private OnCollideWithSensorController ctrl;
	private PhysicsBodyController physicsCtrl;
	private int positiveResultsCount = 0;
		
	@BeforeClass
	public static void setupClass()
	{
		Box2D.init();
	}

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
		ctrlActor.setSize(1.0f, 1.0f);
				
		//physics body ctrl
		physicsCtrl = new PhysicsBodyController();
		ControllerAnnotations.setControllerParameter(physicsCtrl, "scene", scene);		
		physicsCtrl.init(ctrlActor);
		ctrlActor.addController(physicsCtrl);
				
		//tested controller
		positiveResultsCount = 0;
		ctrl = new OnCollideWithSensorController() {
			@Override
			protected SensorCollisionResult onCollideWithSensor(Fixture fixture)
			{
				fixture.testPoint(0.0f, 0.0f);	//required to check for interactions with fixture				
				return --positiveResultsCount > 0 ? SensorCollisionResult.ProcessNext : SensorCollisionResult.StopProcessing;
			}};
		ControllerAnnotations.setControllerParameter(ctrl, "scene", scene);
						
		//bitmask converter cleanup
		BitMaskConverter.Instance.clear();
	}
	
	@Test
	public void shouldCreateSensor()
	{
		//when
		ctrl.init(ctrlActor);
		ctrl.onAdd(ctrlActor);		
		Fixture sensor = ctrl.getSensor();
		
		//then
		assertNotNull("Sensor not created", sensor);
		assertTrue("Should be a sensor", sensor.isSensor());
		assertEquals("Should point to controller actor", ctrlActor, sensor.getUserData());
		assertEquals("Should point to controller actor", ctrlActor, sensor.getBody().getUserData());
	}
	
	@Test
	public void shouldCreateSensorWithProperShapeAndOffset()
	{
		//when
		ControllerAnnotations.setControllerParameter(ctrl, "sensorX", SENSOR_X);
		ControllerAnnotations.setControllerParameter(ctrl, "sensorY", SENSOR_Y);
		ControllerAnnotations.setControllerParameter(ctrl, "sensorWidth", SENSOR_WIDTH);
		ControllerAnnotations.setControllerParameter(ctrl, "sensorHeight", SENSOR_HEIGHT);
		ctrl.init(ctrlActor);
		ctrl.onAdd(ctrlActor);		
		Fixture sensor = ctrl.getSensor();
		
		//then
		assertNotNull("Sensor should be created", sensor);
		assertEquals("Should be polygon fixture", Type.Polygon, sensor.getType());
		
		//when
		PolygonShape fixtureShape = (PolygonShape) sensor.getShape();
		
		//then
		assertEquals("Should have 4 vertices", 4, fixtureShape.getVertexCount());
				
		//when
		Vector2 vertex1 = new Vector2();
		Vector2 vertex2 = new Vector2();
		Vector2 vertex3 = new Vector2();
		fixtureShape.getVertex(0, vertex1);
		fixtureShape.getVertex(1, vertex2);
		fixtureShape.getVertex(2, vertex3);
		
		//then
		assertEquals("Should create sensor at proper X position", SENSOR_X - SENSOR_WIDTH / 2.0f, vertex1.x, 0.0f);
		assertEquals("Should create sensor at proper Y position", SENSOR_Y - SENSOR_HEIGHT / 2.0f, vertex1.y, 0.0f);
		assertEquals("Should create sensor with proper width", SENSOR_WIDTH, vertex2.x - vertex1.x , 0.0f);
		assertEquals("Should create sensor with proper height", SENSOR_HEIGHT, vertex3.y - vertex1.y, 0.0f);
	}
		
	@Test
	public void shouldCreateSensorWithDefaultFilterIfControllerActorHasNone()
	{
		//when
		ctrl.init(ctrlActor);
		ctrl.onAdd(ctrlActor);
		Fixture climbSensorFixture = ctrl.getSensor();
		
		//then
		assertNotNull("Should create sensor", climbSensorFixture);
		assertEquals("Should have default category", 1, climbSensorFixture.getFilterData().categoryBits);
		assertEquals("Should have default group", 0, climbSensorFixture.getFilterData().groupIndex);
		assertEquals("Should have default mask", BitMaskConverter.MASK_COLLIDE_WITH_ALL, climbSensorFixture.getFilterData().maskBits);
	}
	
	@Test
	public void shouldCreateSensorWithDefaultFilterAndSuppliedMask()
	{
		//given
		String suppliedMask = "STATIC";
		int suppliedMaskBits = BitMaskConverter.Instance.fromString(suppliedMask); 
		
		//when
		ControllerAnnotations.setControllerParameter(ctrl, "mask", suppliedMask);
		
		ctrl.init(ctrlActor);
		ctrl.onAdd(ctrlActor);
		Fixture sensor = ctrl.getSensor();
		
		//then
		assertNotNull("Sensor should be created", sensor);
		assertEquals("Should have default category", 1, sensor.getFilterData().categoryBits);
		assertEquals("Should have default group", 0, sensor.getFilterData().groupIndex);
		assertEquals("Should have suplied mask", suppliedMaskBits, sensor.getFilterData().maskBits);
	}
	
	@Test
	public void shouldCreateSensorWithControllerActorFilterAndSuppliedMask()
	{		
		//given supplied mask
		String suppliedCategory = "CAT";
		int suppliedCategoryBits = BitMaskConverter.Instance.fromString(suppliedCategory);
		
		String suppliedMask = "STATIC | SOLID";		
		int suppliedMaskBits = BitMaskConverter.Instance.fromString(suppliedMask); 
	
		//when
		ControllerAnnotations.setControllerParameter(ctrl, "mask", suppliedMask);
		ControllerAnnotations.setControllerParameter(ctrl, "category", suppliedCategory);
		
		ctrl.init(ctrlActor);
		ctrl.onAdd(ctrlActor);
		Fixture sensor = ctrl.getSensor();
		
		//then
		assertNotNull("Should create sensor", sensor);
		assertEquals("Should have filter category", suppliedCategoryBits, sensor.getFilterData().categoryBits);
		assertEquals("Should have filter group", DEFAULT_GROUP_INDEX, sensor.getFilterData().groupIndex);
		assertEquals("Should have suplied mask", suppliedMaskBits, sensor.getFilterData().maskBits);
	}
	
	@Test
	public void shouldRegisterControllerAsListener()
	{
		//when
		ctrl.init(ctrlActor);
		ctrl.onAdd(ctrlActor);
		
		//then
		assertTrue("Listener not registered", ctrlActor.getListeners().contains(ctrl, true));
	}
	
	@Test
	public void shouldProcessAllCollidedFixtures()
	{
		//given
		Fixture fixture1 = mock(Fixture.class);
		Fixture fixture2 = mock(Fixture.class);
		Fixture fixture3 = mock(Fixture.class);
		
		//when first contact
		ctrl.init(ctrlActor);
		ctrl.onAdd(ctrlActor);		
		when(contact.getFixtureA()).thenReturn(ctrl.getSensor());
		when(contact.getFixtureB()).thenReturn(fixture1);		
		ctrl.onEnter(ctrlActor, otherActor, contact);
		
		//when second contact
		when(contact.getFixtureA()).thenReturn(ctrl.getSensor());
		when(contact.getFixtureB()).thenReturn(fixture2);		
		ctrl.onEnter(ctrlActor, otherActor, contact);
		
		//when third contact
		when(contact.getFixtureA()).thenReturn(ctrl.getSensor());
		when(contact.getFixtureB()).thenReturn(fixture3);		
		ctrl.onEnter(ctrlActor, otherActor, contact);
		
		//when process
		positiveResultsCount = 3;
		ctrl.onUpdate(1.0f, ctrlActor);
		
		//then
		verify(fixture1).testPoint(anyFloat(), anyFloat());
		verify(fixture2).testPoint(anyFloat(), anyFloat());
		verify(fixture3).testPoint(anyFloat(), anyFloat());
	}
	
	@Test
	public void shouldProcessCollidedFixturesUpToFirstNegativeResult()
	{
		//given
		Fixture fixture1 = mock(Fixture.class);
		Fixture fixture2 = mock(Fixture.class);
		Fixture fixture3 = mock(Fixture.class);
		
		//when first contact
		ctrl.init(ctrlActor);
		ctrl.onAdd(ctrlActor);		
		when(contact.getFixtureA()).thenReturn(ctrl.getSensor());
		when(contact.getFixtureB()).thenReturn(fixture1);		
		ctrl.onEnter(ctrlActor, otherActor, contact);
		
		//when second contact
		when(contact.getFixtureA()).thenReturn(ctrl.getSensor());
		when(contact.getFixtureB()).thenReturn(fixture2);		
		ctrl.onEnter(ctrlActor, otherActor, contact);
		
		//when third contact
		when(contact.getFixtureA()).thenReturn(ctrl.getSensor());
		when(contact.getFixtureB()).thenReturn(fixture3);		
		ctrl.onEnter(ctrlActor, otherActor, contact);
		
		//when process
		positiveResultsCount = 2;
		ctrl.onUpdate(1.0f, ctrlActor);
		
		//then
		verify(fixture1).testPoint(anyFloat(), anyFloat());
		verify(fixture2).testPoint(anyFloat(), anyFloat());
		verify(fixture3, never()).testPoint(anyFloat(), anyFloat());
	}
	
	@Test
	public void shouldProcessCollidedFixturesThatAreInContact()
	{
		//given
		Fixture fixture1 = mock(Fixture.class);
		Fixture fixture2 = mock(Fixture.class);
		Fixture fixture3 = mock(Fixture.class);
		
		//when first contact
		ctrl.init(ctrlActor);
		ctrl.onAdd(ctrlActor);		
		when(contact.getFixtureA()).thenReturn(ctrl.getSensor());
		when(contact.getFixtureB()).thenReturn(fixture1);		
		ctrl.onEnter(ctrlActor, otherActor, contact);
		
		//when second contact
		when(contact.getFixtureA()).thenReturn(ctrl.getSensor());
		when(contact.getFixtureB()).thenReturn(fixture2);		
		ctrl.onEnter(ctrlActor, otherActor, contact);
		
		//when third contact
		when(contact.getFixtureA()).thenReturn(ctrl.getSensor());
		when(contact.getFixtureB()).thenReturn(fixture3);		
		ctrl.onEnter(ctrlActor, otherActor, contact);
		
		//when first and third loses contact
		when(contact.getFixtureB()).thenReturn(fixture1);
		ctrl.onLeave(ctrlActor, otherActor, contact);
		when(contact.getFixtureB()).thenReturn(fixture3);
		ctrl.onLeave(ctrlActor, otherActor, contact);
				
		//when process
		positiveResultsCount = 3;
		ctrl.onUpdate(1.0f, ctrlActor);
		
		//then
		verify(fixture1, never()).testPoint(anyFloat(), anyFloat());
		verify(fixture2).testPoint(anyFloat(), anyFloat());
		verify(fixture3, never()).testPoint(anyFloat(), anyFloat());		
	}
}
