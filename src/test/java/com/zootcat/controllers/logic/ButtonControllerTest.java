package com.zootcat.controllers.logic;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.physics.box2d.Contact;
import com.zootcat.scene.ZootActor;

public class ButtonControllerTest
{
	@Mock private Contact contact;
	@Mock private ZootActor otherActor;
	
	private int onCount;
	private int offCount;
	private ZootActor ctrlActor;	
	private ButtonController ctrl;	
	private SwitchEventListener listener;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		
		onCount = 0;
		offCount = 0;		
		listener = new SwitchEventListener() {
			@Override
			public void turnOn(ZootActor switchActor)
			{
				++onCount;
			}

			@Override
			public void turnOff(ZootActor switchActor)
			{
				++offCount;
			}}; 
		
		ctrlActor = new ZootActor();
		ctrlActor.addListener(listener);
			
		ctrl = new ButtonController();
		ctrl.init(ctrlActor);
	}
	
	@Test
	public void shouldActivateOnlyOnFirstContact()
	{
		ctrl.onEnter(ctrlActor, otherActor, contact);
		assertEquals(1, onCount);
		
		ctrl.onEnter(ctrlActor, otherActor, contact);
		assertEquals(1, onCount);
		
		ctrl.onEnter(ctrlActor, otherActor, contact);
		assertEquals(1, onCount);
	}
	
	@Test
	public void shouldDeactiveWhenAllContactsAreGone()
	{
		ctrl.onEnter(ctrlActor, otherActor, contact);
		ctrl.onEnter(ctrlActor, otherActor, contact);
		ctrl.onEnter(ctrlActor, otherActor, contact);
		assertEquals(0, offCount);
		
		ctrl.onLeave(ctrlActor, otherActor, contact);
		assertEquals(0, offCount);
		
		ctrl.onLeave(ctrlActor, otherActor, contact);
		assertEquals(0, offCount);
		
		ctrl.onLeave(ctrlActor, otherActor, contact);
		assertEquals(1, offCount);
	}
}
