package com.zootcat.controllers.physics;

import static org.junit.Assert.assertEquals;
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
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.controllers.logic.DirectionController;
import com.zootcat.fsm.events.ZootActorEventCounterListener;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;
import com.zootcat.scene.ZootScene;

public class DetectGroundAheadControllerTest
{
	private static final float SENSOR_WIDTH = 10.0f;
	private static final float SENSOR_HEIGHT = 5.0f;
	
	private PolygonShape shape;
	private ZootActor ctrlActor;	
	private DetectGroundAheadController ctrl;
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
		
		eventCounter = new ZootActorEventCounterListener();
		
		ctrlActor = new ZootActor();		
		ctrlActor.addController(physicsCtrl);
		ctrlActor.addController(directionCtrl);
		ctrlActor.addListener(eventCounter);
		
		ctrl = new DetectGroundAheadController();
		ControllerAnnotations.setControllerParameter(ctrl, "scene", scene);
	}
	
	@Test
	public void shouldSetSensorPositionWhenAddingControllerToActor()
	{
		//given
		final float actorWidth = 10.0f;
		
		//when
		ctrlActor.setWidth(actorWidth);
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
		
		//when
		ctrlActor.setWidth(actorWidth);
		when(directionCtrl.getDirection()).thenReturn(ZootDirection.None);
		
		ctrl.init(ctrlActor);
		ctrl.onAdd(ctrlActor);
		
		when(directionCtrl.getDirection()).thenReturn(ZootDirection.Left);
		ctrl.preUpdate(1.0f, ctrlActor);
		
		//then
		Vector2 position = ctrl.getSensorPosition();
		assertEquals(-actorWidth / 2.0f - SENSOR_WIDTH / 2.0f, position.x, 0.0f);
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
}
