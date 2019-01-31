package com.zootcat.controllers.logic.triggers;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.physics.box2d.Contact;
import com.zootcat.scene.ZootActor;
import com.zootcat.testing.ZootTriggerEventCounterListener;

public class ButtonControllerTest
{
	@Mock private Contact contact;
	@Mock private ZootActor otherActor;
	
	private ZootActor ctrlActor;	
	private ButtonController ctrl;	
	private ZootTriggerEventCounterListener triggerCounter;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		triggerCounter = new ZootTriggerEventCounterListener();		
		ctrlActor = new ZootActor();
		ctrlActor.addListener(triggerCounter);
			
		ctrl = new ButtonController();
		ctrl.init(ctrlActor);
	}
	
	@Test
	public void shouldActivateOnlyOnFirstContact()
	{
		ctrl.onAdd(ctrlActor);
		ctrl.onEnter(ctrlActor, otherActor, contact);
		assertEquals(1, triggerCounter.onCount);
		
		ctrl.onEnter(ctrlActor, otherActor, contact);
		assertEquals(1, triggerCounter.onCount);
		
		ctrl.onEnter(ctrlActor, otherActor, contact);
		assertEquals(1, triggerCounter.onCount);
	}
	
	@Test
	public void shouldDeactiveWhenAllContactsAreGone()
	{
		ctrl.onAdd(ctrlActor);
		ctrl.onEnter(ctrlActor, otherActor, contact);
		ctrl.onEnter(ctrlActor, otherActor, contact);
		ctrl.onEnter(ctrlActor, otherActor, contact);
		assertEquals(0, triggerCounter.offCount);
		
		ctrl.onLeave(ctrlActor, otherActor, contact);
		assertEquals(0, triggerCounter.offCount);
		
		ctrl.onLeave(ctrlActor, otherActor, contact);
		assertEquals(0, triggerCounter.offCount);
		
		ctrl.onLeave(ctrlActor, otherActor, contact);
		assertEquals(1, triggerCounter.offCount);
	}
}
