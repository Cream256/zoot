package com.zootcat.controllers.logic.triggers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.physics.box2d.Contact;
import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.fsm.events.ZootActorEventCounterListener;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.scene.ZootActor;

public class MassButtonControllerTest 
{
	private static final float REQUIRED_MASS = 1.25f;
	
	@Mock private Contact contact;
	@Mock private ZootActor otherActor;
	@Mock private PhysicsBodyController physicsBodyCtrl;
	private ZootActorEventCounterListener eventListener;
	private MassButtonController massBtnCtrl;
	private ZootActor controllerActor;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		
		eventListener = new ZootActorEventCounterListener();
		controllerActor = new ZootActor();
		controllerActor.addListener(eventListener);
		
		massBtnCtrl = new MassButtonController();
		ControllerAnnotations.setControllerParameter(massBtnCtrl, "requiredMass", REQUIRED_MASS);
		massBtnCtrl.init(controllerActor);
		massBtnCtrl.onAdd(controllerActor);
		
		when(otherActor.getSingleController(PhysicsBodyController.class)).thenReturn(physicsBodyCtrl);
	}
	
	@Test
	public void shouldPressButtonWhenActorMassIsEqualToRequired()
	{
		when(physicsBodyCtrl.getMass()).thenReturn(REQUIRED_MASS);
		
		massBtnCtrl.onEnter(otherActor, controllerActor, contact);
		
		assertEquals(1, eventListener.getCount());
		assertEquals(ZootEventType.TriggerOn, eventListener.getLastZootEvent().getType());
	}
	
	@Test
	public void shouldNotPressButtonWhenActorHasNoPhysicsBodyController()
	{
		when(otherActor.getSingleController(PhysicsBodyController.class)).thenReturn(null);
		when(physicsBodyCtrl.getMass()).thenReturn(REQUIRED_MASS);
	
		massBtnCtrl.onEnter(otherActor, controllerActor, contact);
		
		assertEquals(0, eventListener.getCount());
	}
		
	@Test
	public void shouldPressButtonWhenActorMassIsGreaterThanRequired()
	{
		when(physicsBodyCtrl.getMass()).thenReturn(REQUIRED_MASS * 2);
		
		massBtnCtrl.onEnter(otherActor, controllerActor, contact);
		
		assertEquals(1, eventListener.getCount());
		assertEquals(ZootEventType.TriggerOn, eventListener.getLastZootEvent().getType());		
	}
	
	@Test
	public void shouldNotPressButtonWhenActorMassIsLessThanRequired()
	{
		when(physicsBodyCtrl.getMass()).thenReturn(REQUIRED_MASS - 0.01f);
		
		massBtnCtrl.onEnter(otherActor, controllerActor, contact);
		assertEquals(0, eventListener.getCount());
	}
	
	@Test
	public void shouldPressButtonWhenSeveralBodiesHaveRequiredMass()
	{
		when(physicsBodyCtrl.getMass()).thenReturn(REQUIRED_MASS / 2);
		
		massBtnCtrl.onEnter(otherActor, controllerActor, contact);
		assertEquals(0, eventListener.getCount());
		
		massBtnCtrl.onEnter(otherActor, controllerActor, contact);
		assertEquals(1, eventListener.getCount());
		assertEquals(ZootEventType.TriggerOn, eventListener.getLastZootEvent().getType());	
	}
	
	@Test
	public void shouldUnpressButtonWhenBodyHasLeft()
	{
		when(physicsBodyCtrl.getMass()).thenReturn(REQUIRED_MASS);
		
		massBtnCtrl.onEnter(otherActor, controllerActor, contact);
		massBtnCtrl.onLeave(otherActor, controllerActor, contact);
		assertEquals(2, eventListener.getCount());
		assertEquals(ZootEventType.TriggerOff, eventListener.getLastZootEvent().getType());	
	}
	
	@Test
	public void shouldUnpressButtonWhenEveryBodyHadLeft()
	{
		when(physicsBodyCtrl.getMass()).thenReturn(REQUIRED_MASS);
		
		massBtnCtrl.onEnter(otherActor, controllerActor, contact);
		assertEquals("Button should be pressed", 1, eventListener.getCount());
		
		massBtnCtrl.onEnter(otherActor, controllerActor, contact);
		assertEquals("Button should be pressed only once", 1, eventListener.getCount());
		
		massBtnCtrl.onEnter(otherActor, controllerActor, contact);
		assertEquals("Button should be pressed only once", 1, eventListener.getCount());
		
		massBtnCtrl.onLeave(otherActor, controllerActor, contact);
		assertEquals("Button should not be unpressed yet", 1, eventListener.getCount());
		
		massBtnCtrl.onLeave(otherActor, controllerActor, contact);
		assertEquals("Button should not be unpressed yet", 1, eventListener.getCount());
		
		massBtnCtrl.onLeave(otherActor, controllerActor, contact);
		assertEquals("Button should be unpressed now", 2, eventListener.getCount());
		assertEquals(ZootEventType.TriggerOff, eventListener.getLastZootEvent().getType());			
	}
}
