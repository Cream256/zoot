package com.zootcat.controllers.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.zootcat.fsm.events.ZootActorEventCounterListener;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.scene.ZootActor;

public class HurtOnCollideControllerTest
{
	private static final int DAMAGE = 122;
	
	@Mock private Contact contact;
	@Mock private Fixture hurtActorFixture;
		
	private ZootActor controllerActor;		
	private HurtOnCollideController ctrl = new HurtOnCollideController();
	private ZootActorEventCounterListener eventCounter = new ZootActorEventCounterListener();
		
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);		
		controllerActor = new ZootActor();							
		
		ctrl = new HurtOnCollideController();
		ctrl.init(controllerActor);
	}
			
	@Test
	public void shouldSendHurtEventAction()
	{
		//given
		ZootActor hurtActor = new ZootActor();
		when(hurtActorFixture.getUserData()).thenReturn(hurtActor);
		hurtActor.addListener(eventCounter);
				
		//when
		ctrl.setDamage(DAMAGE);
		ctrl.onEnterCollision(hurtActorFixture);
		
		//then
		assertEquals("Event should be send", 1, eventCounter.getCount());
		assertEquals(ZootEventType.Hurt, eventCounter.getLastZootEvent().getType());
		assertEquals((int)DAMAGE, (int)eventCounter.getLastZootEvent().getUserObject(Integer.class));
	}
			
	@Test
	public void shouldSendHurtEventToOwner()
	{
		//given
		when(hurtActorFixture.getUserData()).thenReturn(mock(ZootActor.class));
		controllerActor.addListener(eventCounter);
				
		//when
		ctrl.setHurtOwner(true);
		ctrl.setDamage(DAMAGE);
		ctrl.onEnterCollision(hurtActorFixture);
				
		//then
		assertEquals("Event should be send", 1, eventCounter.getCount());
		assertEquals(ZootEventType.Hurt, eventCounter.getLastZootEvent().getType());
		assertEquals((int)DAMAGE, (int)eventCounter.getLastZootEvent().getUserObject(Integer.class));
	}
	
	@Test
	public void shouldSetDamage()
	{
		ctrl.setDamage(DAMAGE);
		assertEquals(DAMAGE, ctrl.getDamage());
	}
	
	@Test
	public void shouldSetHurtOwner()
	{
		ctrl.setHurtOwner(true);
		assertTrue(ctrl.getHurtOwner());
		
		ctrl.setHurtOwner(false);
		assertFalse(ctrl.getHurtOwner());
	}
	
	@Test
	public void shouldReturnTrueForCanHurt()
	{
		assertTrue(ctrl.canHurt(mock(Fixture.class)));
	}
}
