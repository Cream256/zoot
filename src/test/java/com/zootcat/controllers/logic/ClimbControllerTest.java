package com.zootcat.controllers.logic;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape.Type;
import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.controllers.physics.CollisionFilterController;
import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.physics.ZootPhysics;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;
import com.zootcat.utils.BitMaskConverter;

public class ClimbControllerTest 
{
	private static final float CTRL_ACTOR_WIDTH = 100.0f;
	private static final float CTRL_ACTOR_HEIGHT = 50.0f;
	
	@Mock private ZootScene scene;
	@Mock private Contact contact;
	@Mock private Manifold manifold;
	@Mock private Fixture otherFixture;
	@Mock private ContactImpulse contactImpulse;
		
	private ClimbController ctrl;
	private ZootActor ctrlActor;
	private ZootPhysics physics;
	private PhysicsBodyController physicsCtrl;
		
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
		
		//physics body ctrl
		physicsCtrl = new PhysicsBodyController();
		ControllerAnnotations.setControllerParameter(physicsCtrl, "scene", scene);
		physicsCtrl.init(ctrlActor);
		ctrlActor.addController(physicsCtrl);
				
		//tested controller
		ctrl = new ClimbController();
		
		//bitmask converter cleanup
		BitMaskConverter.Instance.clear();
	}
	
	@Test
	public void shouldCreateClimbSensorFixture()
	{
		//when
		ctrl.init(ctrlActor);
		ctrl.onAdd(ctrlActor);		
		Fixture climbSensorFixture = ctrl.getClimbSensor();
		
		//then
		assertNotNull("Climb sensor not created", climbSensorFixture);
		assertTrue("Should be sensor", climbSensorFixture.isSensor());
		assertEquals("Should point to controller actor", ctrlActor, climbSensorFixture.getUserData());
		assertEquals("Should point to controller actor", ctrlActor, climbSensorFixture.getBody().getUserData());
	}
	
	@Test
	public void shouldCreateClimbSensorFixtureWithProperShape()
	{
		//when
		ctrl.init(ctrlActor);
		ctrl.onAdd(ctrlActor);		
		Fixture climbSensorFixture = ctrl.getClimbSensor();
		
		//then
		assertNotNull("Climb sensor not created", climbSensorFixture);
		assertEquals("Should be polygon fixture", Type.Polygon, climbSensorFixture.getType());
		
		//when
		PolygonShape fixtureShape = (PolygonShape) climbSensorFixture.getShape();
		
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
		assertEquals("Should have actor width", CTRL_ACTOR_WIDTH, vertex2.x - vertex1.x , 0.0f);
		assertEquals("Should have 20% of actor height", CTRL_ACTOR_HEIGHT * 0.2f, vertex3.y - vertex1.y, 0.0f);
	}
	
	@Test
	public void shouldCreateClimbSensorFixtureWithDefaultFilterIfControllerActorHasNone()
	{
		//when
		ctrl.init(ctrlActor);
		ctrl.onAdd(ctrlActor);
		Fixture climbSensorFixture = ctrl.getClimbSensor();
		
		//then
		assertNotNull("Climb sensor not created", climbSensorFixture);
		assertEquals("Should have default category", 1, climbSensorFixture.getFilterData().categoryBits);
		assertEquals("Should have default group", 0, climbSensorFixture.getFilterData().groupIndex);
		assertEquals("Should have default mask", BitMaskConverter.MASK_COLLIDE_WITH_ALL, climbSensorFixture.getFilterData().maskBits);
	}
	
	@Test
	public void shouldCreateClimbSensorFixtureWithDefaultFilterAndSuppliedMask()
	{
		//given
		String suppliedMask = "STATIC";
		int suppliedMaskBits = BitMaskConverter.Instance.fromString(suppliedMask); 
		
		//when
		ControllerAnnotations.setControllerParameter(ctrl, "mask", suppliedMask);
		
		ctrl.init(ctrlActor);
		ctrl.onAdd(ctrlActor);
		Fixture climbSensorFixture = ctrl.getClimbSensor();
		
		//then
		assertNotNull("Climb sensor not created", climbSensorFixture);
		assertEquals("Should have default category", 1, climbSensorFixture.getFilterData().categoryBits);
		assertEquals("Should have default group", 0, climbSensorFixture.getFilterData().groupIndex);
		assertEquals("Should have suplied mask", suppliedMaskBits, climbSensorFixture.getFilterData().maskBits);
	}
	
	@Test
	public void shouldCreateClimbSensorFixtureWithControllerActorFilterAndSuppliedMask()
	{		
		//given supplied mask
		String suppliedMask = "STATIC | SOLID";
		int suppliedMaskBits = BitMaskConverter.Instance.fromString(suppliedMask); 
	
		//given filter
		Filter filter = new Filter();
		filter.categoryBits = 2;
		filter.groupIndex = 3;
		filter.maskBits = 0;
		
		CollisionFilterController collisionFilterCtrl = mock(CollisionFilterController.class);
		when(collisionFilterCtrl.getCollisionFilter()).thenReturn(filter);		
		ctrlActor.addController(collisionFilterCtrl);
		
		//when
		ControllerAnnotations.setControllerParameter(ctrl, "mask", suppliedMask);
		
		ctrl.init(ctrlActor);
		ctrl.onAdd(ctrlActor);
		Fixture climbSensorFixture = ctrl.getClimbSensor();
		
		//then
		assertNotNull("Climb sensor not created", climbSensorFixture);
		assertEquals("Should have filter category", filter.categoryBits, climbSensorFixture.getFilterData().categoryBits);
		assertEquals("Should have filter group", filter.groupIndex, climbSensorFixture.getFilterData().groupIndex);
		assertEquals("Should have suplied mask", suppliedMaskBits, climbSensorFixture.getFilterData().maskBits);
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
	

	
	
}
