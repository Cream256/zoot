package com.zootcat.controllers.physics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.fsm.events.ZootActorEventCounterListener;
import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.scene.ZootActor;
import com.zootcat.testing.ZootActorStub;

public class DetectInAirControllerTest
{
	@Mock private ZootActor actor;
	@Mock private DetectGroundSensorController groundCtrl;	
	private DetectInAirController ctrl;
		
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		ctrl = new DetectInAirController();
	}
	
	@Test
	public void shouldSetInAirToFalseAfterInit()
	{
		ctrl.init(actor);
		assertFalse(ctrl.isInAir());
	}
	
	@Test(expected = RuntimeZootException.class)
	public void shouldThrowIfNoGroundControllerWasAssignedToActor()
	{
		ctrl.onAdd(new ZootActor());
	}
	
	@Test
	public void shouldNotThrowIfGroundControllerWasAssignedToActor()
	{
		when(actor.getSingleController(DetectGroundSensorController.class)).thenReturn(groundCtrl);
		ctrl.onAdd(actor);
		//ok
	}
	
	@Test
	public void shouldNotInteractWithActorOnRemovingController()
	{
		ctrl.onRemove(actor);
		verifyZeroInteractions(actor);
	}
	
	@Test
	public void shouldNotBeInAirAndEventShouldNotBeSend()
	{
		//given		
		ZootActor actor = new ZootActorStub();
		actor.addController(groundCtrl);
		
		ZootActorEventCounterListener eventCounter = new ZootActorEventCounterListener();
		actor.addListener(eventCounter);
		
		//when
		ctrl.init(actor);
		ctrl.onAdd(actor);
		when(groundCtrl.isOnGround()).thenReturn(true);
		ctrl.onUpdate(1.0f, actor);
		
		//then
		assertFalse(ctrl.isInAir());
		assertEquals(0, eventCounter.getCount());
	}
	
	@Test
	public void shouldBeInAirAndEventShouldBeSend()
	{
		//given		
		ZootActor actor = new ZootActorStub();
		actor.addController(groundCtrl);
		
		ZootActorEventCounterListener eventCounter = new ZootActorEventCounterListener();
		actor.addListener(eventCounter);
		
		//when
		ctrl.init(actor);
		ctrl.onAdd(actor);
		when(groundCtrl.isOnGround()).thenReturn(false);
		ctrl.onUpdate(1.0f, actor);
		
		//then
		assertTrue(ctrl.isInAir());
		assertEquals(1, eventCounter.getCount());
		assertTrue(ClassReflection.isInstance(ZootEvent.class, eventCounter.getLastEvent()));
		assertEquals(ZootEventType.InAir, ((ZootEvent)eventCounter.getLastEvent()).getType());
	}	
	
	@Test
	public void shouldBeSingleton()
	{
		assertTrue(ctrl.isSingleton());
	}
}
