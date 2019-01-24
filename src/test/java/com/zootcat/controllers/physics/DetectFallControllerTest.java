package com.zootcat.controllers.physics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.zootcat.fsm.events.ZootActorEventCounterListener;
import com.zootcat.scene.ZootActor;
import com.zootcat.testing.ZootActorStub;

public class DetectFallControllerTest
{
	private static final Vector2 UP_VELOCITY = new Vector2(0.0f, 10.0f);
	private static final Vector2 DOWN_VELOCITY = new Vector2(0.0f, -10.0f);
	
	private ZootActor actor;
	private ZootActorEventCounterListener eventCounter;
	private DetectFallController ctrl;
	@Mock private Body bodyMock;
	@Mock private DetectGroundSensorController groundCtrlMock;
	@Mock private PhysicsBodyController physicsCtrlMock;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);				
		when(physicsCtrlMock.getBody()).thenReturn(bodyMock);
		
		actor = new ZootActorStub();
		actor.addController(groundCtrlMock);
		actor.addController(physicsCtrlMock);
		
		eventCounter = new ZootActorEventCounterListener();
		actor.addListener(eventCounter);
		
		ctrl = new DetectFallController();		
	}
	
	@Test
	public void shouldSetFallingFlagToFalse()
	{
		ctrl.init(actor);
		assertFalse(ctrl.isFalling());
	}
	
	@Test
	public void shouldNotThrowIfDetectGroundControllerIsNotAssignedToActor()
	{
		actor.removeController(groundCtrlMock);
		ctrl.onAdd(actor);
		//ok
	}
	
	@Test
	public void shouldBeFallingWhenNotOnGroundAndHavingDownVelocity()
	{
		when(bodyMock.getLinearVelocity()).thenReturn(DOWN_VELOCITY);
		when(groundCtrlMock.isOnGround()).thenReturn(false);
		
		ctrl.init(actor);
		ctrl.onAdd(actor);
		ctrl.onUpdate(0.0f, actor);
		
		assertTrue(ctrl.isFalling());
		assertEquals(1, eventCounter.getCount());
	}
	
	@Test
	public void shouldBeFallingWhenNoGroundDetectorAndHavingDownVelocity()
	{
		when(bodyMock.getLinearVelocity()).thenReturn(DOWN_VELOCITY);
		actor.removeController(groundCtrlMock);
		
		ctrl.init(actor);
		ctrl.onAdd(actor);
		ctrl.onUpdate(0.0f, actor);
		
		assertTrue(ctrl.isFalling());
		assertEquals(1, eventCounter.getCount());
	}
	
	@Test
	public void shouldNotBeFallingWhenOnGroundAndHavingDownVelocity()
	{
		when(bodyMock.getLinearVelocity()).thenReturn(DOWN_VELOCITY);
		when(groundCtrlMock.isOnGround()).thenReturn(true);
		
		ctrl.init(actor);
		ctrl.onAdd(actor);
		ctrl.onUpdate(0.0f, actor);
		
		assertFalse(ctrl.isFalling());
		assertEquals(0, eventCounter.getCount());		
	}
	
	@Test
	public void shouldNotBeFallingWhenInAirAndHavingUpVelocity()
	{
		when(bodyMock.getLinearVelocity()).thenReturn(UP_VELOCITY);
		when(groundCtrlMock.isOnGround()).thenReturn(false);
		
		ctrl.init(actor);
		ctrl.onAdd(actor);
		ctrl.onUpdate(0.0f, actor);
		
		assertFalse(ctrl.isFalling());
		assertEquals(0, eventCounter.getCount());			
	}
	
	@Test
	public void shouldNotBeFallingWhenOnGroundAndHavingUpVelocity()
	{
		when(bodyMock.getLinearVelocity()).thenReturn(UP_VELOCITY);
		when(groundCtrlMock.isOnGround()).thenReturn(true);
		
		ctrl.init(actor);
		ctrl.onAdd(actor);
		ctrl.onUpdate(0.0f, actor);
		
		assertFalse(ctrl.isFalling());
		assertEquals(0, eventCounter.getCount());			
	}
	
	@Test
	public void shouldFireEventContinouslyWhenFallingTest()
	{
		when(bodyMock.getLinearVelocity()).thenReturn(DOWN_VELOCITY);
		when(groundCtrlMock.isOnGround()).thenReturn(false);
		
		ctrl.init(actor);
		ctrl.onAdd(actor);
		ctrl.onUpdate(0.0f, actor);		
		assertEquals("First event should be fired", 1, eventCounter.getCount());
		
		ctrl.onUpdate(0.0f, actor);
		assertEquals("Second event should be fired", 2, eventCounter.getCount());
		
		when(bodyMock.getLinearVelocity()).thenReturn(DOWN_VELOCITY);
		when(groundCtrlMock.isOnGround()).thenReturn(true);
		ctrl.onUpdate(0.0f, actor);
		assertEquals("Has landed, no event should be fired", 2, eventCounter.getCount());
		
		when(bodyMock.getLinearVelocity()).thenReturn(DOWN_VELOCITY);
		when(groundCtrlMock.isOnGround()).thenReturn(false);
		ctrl.onUpdate(0.0f, actor);
		assertEquals("Falling again, third event should be fired", 3, eventCounter.getCount());
	}
	
	@Test
	public void shouldDoNothingOnRemove()
	{
		ZootActor actor = mock(ZootActor.class);
		ctrl.onRemove(actor);
		
		verifyZeroInteractions(actor);
	}
}
