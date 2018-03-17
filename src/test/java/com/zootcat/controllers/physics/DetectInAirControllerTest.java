package com.zootcat.controllers.physics;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.zootcat.events.ZootEvent;
import com.zootcat.events.ZootEventType;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.scene.ZootActor;
import com.zootcat.testing.ZootActorEventCounterListener;

public class DetectInAirControllerTest
{
	@Mock private ZootActor actor;
	@Mock private DetectGroundController groundCtrl;	
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
		when(actor.getController(DetectGroundController.class)).thenReturn(groundCtrl);
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
		ZootActor actor = new ZootActor();
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
		ZootActor actor = new ZootActor();
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
}
