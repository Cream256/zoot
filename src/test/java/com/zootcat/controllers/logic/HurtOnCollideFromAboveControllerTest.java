package com.zootcat.controllers.logic;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.physics.box2d.Contact;
import com.zootcat.fsm.events.ZootActorEventCounterListener;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.scene.ZootActor;

public class HurtOnCollideFromAboveControllerTest
{
	private static final int DAMAGE = 512;
	
	private ZootActor otherActor;
	private ZootActor controllerActor;	
	private HurtOnCollideFromAboveController controller;
	private ZootActorEventCounterListener eventCounter;
	
	@Before
	public void setup()
	{
		otherActor = new ZootActor();
		otherActor.setName("Other actor");
		controllerActor = new ZootActor();
		controllerActor.setName("Ctrl actor");
		eventCounter = new ZootActorEventCounterListener();
		
		controller = new HurtOnCollideFromAboveController();
		controller.init(controllerActor);
	}
	
	@Test
	public void shouldHurtControllerActor()
	{
		//given
		controller.setDamage(DAMAGE);
		controller.setHurtOwner(true);
		controller.onAdd(controllerActor);
		controllerActor.addListener(eventCounter);
		
		//when
		controller.onCollidedFromAbove(otherActor, controllerActor, mock(Contact.class));
		
		//then
		assertEquals(1, eventCounter.getCount());
		assertEquals(ZootEventType.Hurt, eventCounter.getLastZootEvent().getType());
		assertEquals(DAMAGE, (int)eventCounter.getLastZootEvent().getUserObject(Integer.class));
	}
	
	@Test
	public void shouldHurtOtherActor()
	{
		//given
		controller.setDamage(DAMAGE);
		controller.setHurtOwner(false);
		controller.onAdd(controllerActor);
		otherActor.addListener(eventCounter);
		
		//when
		controller.onCollidedFromAbove(otherActor, controllerActor, mock(Contact.class));
		
		//then
		assertEquals(1, eventCounter.getCount());
		assertEquals(ZootEventType.Hurt, eventCounter.getLastZootEvent().getType());
		assertEquals(DAMAGE, (int)eventCounter.getLastZootEvent().getUserObject(Integer.class));
	}
	
	@Test
	public void shouldSetDamage()
	{
		controller.setDamage(0);
		assertEquals(0, controller.getDamage());
		
		controller.setDamage(DAMAGE);
		assertEquals(DAMAGE, controller.getDamage());
	}
	
	@Test
	public void shouldSetHurtOwner()
	{
		controller.setHurtOwner(true);
		assertTrue(controller.getHurtOwner());
		
		controller.setHurtOwner(false);
		assertFalse(controller.getHurtOwner());
	}
	
}
