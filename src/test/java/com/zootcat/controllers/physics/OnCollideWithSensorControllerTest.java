package com.zootcat.controllers.physics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape.Type;
import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.math.ZootBoundingBoxFactory;
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
	private boolean preUpdateCalled;
	private boolean postUpdateCalled;
		
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
		
		//other fixture
		when(otherFixture.getFilterData()).thenReturn(new Filter());
		
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
		preUpdateCalled = false;
		postUpdateCalled = false;
		ctrl = new OnCollideWithSensorController() 
		{
			@Override
			protected SensorCollisionResult onCollideWithSensor(Fixture fixture)
			{
				fixture.testPoint(0.0f, 0.0f);	//required to check for interactions with fixture				
				return --positiveResultsCount > 0 ? SensorCollisionResult.ProcessNext : SensorCollisionResult.StopProcessing;
			}

			@Override
			public void preUpdate(float delta, ZootActor actor)
			{
				preUpdateCalled = true;
			}

			@Override
			public void postUpdate(float delta, ZootActor actor)
			{
				postUpdateCalled = true;
			}
		};
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
	public void shouldRemoveFixture()
	{
		//when
		ctrl.init(ctrlActor);
		ctrl.onAdd(ctrlActor);
		Fixture sensor = ctrl.getSensor();
		
		//then
		assertNotNull("Should create sensor", sensor);
		
		//when
		ctrl.onRemove(ctrlActor);
		
		//then
		assertNull("Should remove sensor", ctrl.getSensor());
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
	
	@Test
	public void shouldProperlyDetectCollisionWhenContactIsDisabledAndReenabled()
	{
		//given
		Manifold manifold = mock(Manifold.class);
		ContactImpulse contactImpulse = mock(ContactImpulse.class);
		
		//when
		ctrl.init(ctrlActor);
		ctrl.onAdd(ctrlActor);
		when(contact.getFixtureA()).thenReturn(ctrl.getSensor());
		when(contact.getFixtureB()).thenReturn(otherFixture);
		when(contact.isEnabled()).thenReturn(false);
		
		ctrl.beginContact(ctrlActor, otherActor, contact);
		ctrl.preSolve(ctrlActor, otherActor, contact, manifold);
		ctrl.postSolve(ctrlActor, otherActor, contactImpulse);
		ctrl.onUpdate(0.0f, ctrlActor);
		
		//then
		verify(otherFixture, never()).testPoint(anyFloat(), anyFloat());
				
		//when
		when(contact.isEnabled()).thenReturn(true);
		ctrl.preSolve(ctrlActor, otherActor, contact, manifold);
		ctrl.postSolve(ctrlActor, otherActor, contactImpulse);
		ctrl.onUpdate(0.0f, ctrlActor);
		
		//then
		verify(otherFixture).testPoint(anyFloat(), anyFloat());
	}
	
	@Test
	public void shouldProperlyHandleMultiplyContacts()
	{
		//given
		positiveResultsCount = 100;	//to process all fixtures when needed
		
		//when
		ctrl.init(ctrlActor);
		ctrl.onAdd(ctrlActor);
		Fixture sensorFixture = ctrl.getSensor();
		
		Fixture fixture1 = mock(Fixture.class);
		when(fixture1.getFilterData()).thenReturn(new Filter());
		
		Fixture fixture2 = mock(Fixture.class);
		when(fixture2.getFilterData()).thenReturn(new Filter());
		
		Fixture fixture3 = mock(Fixture.class);
		when(fixture3.getFilterData()).thenReturn(new Filter());
			
		Contact contact1 = mock(Contact.class);
		when(contact1.getFixtureA()).thenReturn(sensorFixture);
		when(contact1.getFixtureB()).thenReturn(fixture1);
		when(contact1.isEnabled()).thenReturn(true);
		
		Contact contact2 = mock(Contact.class);
		when(contact2.getFixtureA()).thenReturn(sensorFixture);
		when(contact2.getFixtureB()).thenReturn(fixture2);
		when(contact2.isEnabled()).thenReturn(true);
		
		Contact contact3 = mock(Contact.class);
		when(contact3.getFixtureA()).thenReturn(sensorFixture);
		when(contact3.getFixtureB()).thenReturn(fixture3);
		when(contact3.isEnabled()).thenReturn(true);
				
		//when all contacts are enabled
		ctrl.beginContact(ctrlActor, otherActor, contact1);
		ctrl.beginContact(ctrlActor, otherActor, contact2);
		ctrl.beginContact(ctrlActor, otherActor, contact3);
		ctrl.preSolve(ctrlActor, otherActor, contact1, mock(Manifold.class));
		ctrl.preSolve(ctrlActor, otherActor, contact2, mock(Manifold.class));
		ctrl.preSolve(ctrlActor, otherActor, contact3, mock(Manifold.class));
		ctrl.onUpdate(1.0f, ctrlActor);
		
		//then detect collision for all contacts
		verify(fixture1).testPoint(anyFloat(), anyFloat());
		verify(fixture2).testPoint(anyFloat(), anyFloat());
		verify(fixture3).testPoint(anyFloat(), anyFloat());
				
		//when only one contact is left enabled
		when(contact2.isEnabled()).thenReturn(false);
		when(contact3.isEnabled()).thenReturn(false);
		ctrl.preSolve(ctrlActor, otherActor, contact1, mock(Manifold.class));
		ctrl.preSolve(ctrlActor, otherActor, contact2, mock(Manifold.class));
		ctrl.preSolve(ctrlActor, otherActor, contact3, mock(Manifold.class));			

		//this is required to simulate box2d behaviour, all contacts are reenabled after postsolve
		when(contact2.isEnabled()).thenReturn(true);
		when(contact3.isEnabled()).thenReturn(true);

		ctrl.onUpdate(1.0f, ctrlActor);
		
		//then detect only contact for fixture 1
		verify(fixture1, times(2)).testPoint(anyFloat(), anyFloat());
		verify(fixture2, times(1)).testPoint(anyFloat(), anyFloat());
		verify(fixture3, times(1)).testPoint(anyFloat(), anyFloat());
		
		//when all contacts are disabled
		when(contact1.isEnabled()).thenReturn(false);
		when(contact2.isEnabled()).thenReturn(false);
		when(contact3.isEnabled()).thenReturn(false);
		ctrl.preSolve(ctrlActor, otherActor, contact1, mock(Manifold.class));
		ctrl.preSolve(ctrlActor, otherActor, contact2, mock(Manifold.class));
		ctrl.preSolve(ctrlActor, otherActor, contact3, mock(Manifold.class));			
		
		//this is required to simulate box2d behaviour, all contacts are reenabled after postsolve
		when(contact1.isEnabled()).thenReturn(true);
		when(contact2.isEnabled()).thenReturn(true);
		when(contact3.isEnabled()).thenReturn(true);
		
		ctrl.onUpdate(1.0f, ctrlActor);
		
		//then no more contacts should be detected
		verify(fixture1, times(2)).testPoint(anyFloat(), anyFloat());
		verify(fixture2, times(1)).testPoint(anyFloat(), anyFloat());
		verify(fixture3, times(1)).testPoint(anyFloat(), anyFloat());
	}
	
	@Test
	public void shouldSetScene()
	{
		//given
		ZootScene scene = mock(ZootScene.class);
		
		//when		
		ctrl.setScene(scene);
		
		//then
		assertEquals(scene, ctrl.getScene());		
	}
	
	@Test
	public void shouldSetSensorSizeAndPositionParameters()
	{
		//given
		final float width = 123.0f;
		final float height = 234.0f;
		final float x = 345.0f;
		final float y = 456.0f;
		
		//when		
		ctrl = new OnCollideWithSensorController(width, height, x, y) {
			@Override
			protected SensorCollisionResult onCollideWithSensor(Fixture fixture)
			{
				return null;
			}

			@Override
			public void preUpdate(float delta, ZootActor actor)
			{
				//noop
				
			}

			@Override
			public void postUpdate(float delta, ZootActor actor)
			{
				//noop
				
			}};
		
		//then		
		assertEquals(width, ctrl.sensorWidth, 0.0f);
		assertEquals(height, ctrl.sensorHeight, 0.0f);
		assertEquals(x, ctrl.sensorX, 0.0f);
		assertEquals(y, ctrl.sensorY, 0.0f);
	}
	
	@Test
	public void shouldSetFilterForSensor()
	{
		//given
		final int expectedGroupIndex = 10;
		final int expectedCategory = 20;
		final int expectedMask = 30;
		
		Filter filter = new Filter();
		filter.groupIndex = expectedGroupIndex;
		filter.categoryBits = expectedCategory;		
		filter.maskBits = expectedMask;
				
		//when
		ctrl.init(ctrlActor);
		ctrl.onAdd(ctrlActor);
		ctrl.setFilter(filter);
		
		//then
		assertEquals(expectedGroupIndex, ctrl.getFilter().groupIndex);
		assertEquals(expectedCategory, ctrl.getFilter().categoryBits);
		assertEquals(expectedMask, ctrl.getFilter().maskBits);
		assertEquals(expectedGroupIndex, ctrl.getSensor().getFilterData().groupIndex);
		assertEquals(expectedCategory, ctrl.getSensor().getFilterData().categoryBits);
		assertEquals(expectedMask, ctrl.getSensor().getFilterData().maskBits);
	}
	
	@Test
	public void shouldSetSensorPosition()
	{
		//given
		final float expectedX = 256.0f;
		final float expectedY = 512.0f;
		
		//when
		ctrl.init(ctrlActor);
		ctrl.onAdd(ctrlActor);		
		ctrl.setSensorPosition(expectedX, expectedY);		
		Vector2 sensorPosition = ctrl.getSensorPosition();
		
		//then
		assertEquals(expectedX, sensorPosition.x, 0.0f);
		assertEquals(expectedY, sensorPosition.y, 0.0f);
	}
	
	@Test
	public void shouldScaleSensor()
	{
		//given
		final float expectedScale = 0.5f;
		ControllerAnnotations.setControllerParameter(ctrl, "sensorWidth", SENSOR_WIDTH);
		ControllerAnnotations.setControllerParameter(ctrl, "sensorHeight", SENSOR_HEIGHT);
		
		//when		
		ctrl.init(ctrlActor);
		ctrl.onAdd(ctrlActor);		
		ctrl.scaleSensor(expectedScale);		
		
		//then
		BoundingBox box = ZootBoundingBoxFactory.create(ctrl.getSensor());
		assertEquals(SENSOR_WIDTH * expectedScale, box.getWidth(), 0.0f);
		assertEquals(SENSOR_HEIGHT * expectedScale, box.getHeight(), 0.0f);		
	}
			
	@Test
	public void shouldCallPreUpdateAndPostUpdate()
	{
		ctrl.onUpdate(1.0f, ctrlActor);
		
		assertTrue(preUpdateCalled);
		assertTrue(postUpdateCalled);		
	}
}
