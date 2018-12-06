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
import com.zootcat.actions.ZootFireEventAction;
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
		
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);		
		controllerActor = new ZootActor();							
		
		ctrl = new HurtOnCollideController();
		ctrl.init(controllerActor);
	}
			
	@Test
	public void shouldAddSendHurtEventAction()
	{
		//given
		ZootActor hurtActor = new ZootActor();
		when(hurtActorFixture.getUserData()).thenReturn(hurtActor);
				
		//when
		ctrl.setDamage(DAMAGE);
		ctrl.onCollideWithSensor(hurtActorFixture);
		
		//then
		assertEquals(1, hurtActor.getActions().size);
		assertEquals(ZootFireEventAction.class, hurtActor.getActions().get(0).getClass());
		ZootFireEventAction fireEventAction = (ZootFireEventAction) hurtActor.getActions().get(0);
		assertEquals(hurtActor, fireEventAction.getTarget());
		assertEquals(ZootEventType.Hurt, fireEventAction.getEvent().getType());
		assertEquals((int)DAMAGE, (int)fireEventAction.getEvent().getUserObject(Integer.class));
	}
			
	@Test
	public void shouldSendHurtEventToOwner()
	{
		//given
		when(hurtActorFixture.getUserData()).thenReturn(mock(ZootActor.class));
		
		ZootActorEventCounterListener eventCounter = new ZootActorEventCounterListener();
		controllerActor.addListener(eventCounter);
				
		//when
		ctrl.setHurtOwner(true);
		ctrl.setDamage(DAMAGE);
		ctrl.onCollideWithSensor(hurtActorFixture);
				
		//then
		assertEquals(1, controllerActor.getActions().size);
		assertEquals(ZootFireEventAction.class, controllerActor.getActions().get(0).getClass());
		ZootFireEventAction fireEventAction = (ZootFireEventAction) controllerActor.getActions().get(0);
		assertEquals(controllerActor, fireEventAction.getTarget());
		assertEquals(ZootEventType.Hurt, fireEventAction.getEvent().getType());
		assertEquals((int)DAMAGE, (int)fireEventAction.getEvent().getUserObject(Integer.class));
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
}
