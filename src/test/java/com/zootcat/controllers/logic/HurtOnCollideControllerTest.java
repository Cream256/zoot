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
import com.zootcat.exceptions.ZootControllerNotFoundException;
import com.zootcat.fsm.events.ZootActorEventCounterListener;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.scene.ZootActor;
import com.zootcat.testing.ZootActorStub;

public class HurtOnCollideControllerTest
{
	private static final int DAMAGE = 122;
	private static final int ATTACKER_DAMAGE = 211;
	
	@Mock private Contact contact;
	@Mock private Fixture hurtActorFixture;
	@Mock private DamageController damageCtrl;
	
	private ZootActor controllerActor;		
	private HurtOnCollideController ctrl = new HurtOnCollideController();
	private ZootActorEventCounterListener eventCounter = new ZootActorEventCounterListener();
		
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);		
		controllerActor = new ZootActorStub();
		
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
	
	@Test
	public void shouldNotUseAttackerDamageByDefault()
	{
		assertFalse(ctrl.useAttackerDamage());
	}
	
	@Test
	public void shouldSetUseAttackerDamage()
	{
		ctrl.setUseAttackerDamage(true);
		assertTrue(ctrl.useAttackerDamage());
		
		ctrl.setUseAttackerDamage(false);
		assertFalse(ctrl.useAttackerDamage());
	}
	
	@Test
	public void shouldUseAttackerDamage()
	{
		//given
		ZootActor attackerActor = new ZootActorStub();
		attackerActor.addController(damageCtrl);		
		controllerActor.addListener(eventCounter);
		
		when(damageCtrl.getValue()).thenReturn(ATTACKER_DAMAGE);		
		when(hurtActorFixture.getUserData()).thenReturn(attackerActor);
		
		//when
		ctrl.setDamage(DAMAGE);
		ctrl.setHurtOwner(true);		
		ctrl.setUseAttackerDamage(true);		
		ctrl.onEnterCollision(hurtActorFixture);
		
		//then
		assertEquals("Event should be send", 1, eventCounter.getCount());
		assertEquals("Hurt event should be send", ZootEventType.Hurt, eventCounter.getLastZootEvent().getType());
		assertEquals("Damage should be taken from attacker", (int)ATTACKER_DAMAGE, (int)eventCounter.getLastZootEvent().getUserObject(Integer.class));
	}
	
	@Test(expected = ZootControllerNotFoundException.class)
	public void shouldThrowIfUsingAttackerDamageWithNoDamageControllerOnAttacker()
	{
		//given
		ZootActor attackerActor = new ZootActorStub();				
		when(hurtActorFixture.getUserData()).thenReturn(attackerActor);
		
		//when
		ctrl.setDamage(DAMAGE);
		ctrl.setHurtOwner(true);		
		ctrl.setUseAttackerDamage(true);		
		ctrl.onEnterCollision(hurtActorFixture);
		
		//then throw		
	}
}
