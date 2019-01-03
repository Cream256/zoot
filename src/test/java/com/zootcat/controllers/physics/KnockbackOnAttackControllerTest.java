package com.zootcat.controllers.physics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.zootcat.actions.ZootKnockbackAction;
import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.events.ZootEvents;
import com.zootcat.scene.ZootActor;

public class KnockbackOnAttackControllerTest
{
	@Mock private ZootActor ctrlActor;
	@Mock private PhysicsBodyController otherActorPhysicsBodyCtrl;
	@Mock private PhysicsBodyController ctrlActorPhysicsBodyCtrl;
	
	private ZootActor otherActor;	
	private KnockbackOnAttackController ctrl;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		
		otherActor = new ZootActor();
		otherActor.addController(otherActorPhysicsBodyCtrl);
		
		when(ctrlActor.getSingleController(PhysicsBodyController.class)).thenReturn(ctrlActorPhysicsBodyCtrl);
				
		ctrl = new KnockbackOnAttackController();
		ctrl.init(ctrlActor);
	}
	
	@Test
	public void shouldSetDefaultParameters()
	{		
		assertFalse(ctrl.getVaryHorizontal());
		assertEquals(1.0f, ctrl.getKnockbackX(), 0.0f);
		assertEquals(1.0f, ctrl.getKnockbackY(), 0.0f);
	}
		
	@Test
	public void shouldAddKnockbackAction()
	{
		//given
		ZootEvent attackEvent = ZootEvents.get(ZootEventType.Attack);
		attackEvent.setUserObject(otherActor);
		
		//when		
		ctrl.onZootEvent(ctrlActor, attackEvent);
		
		//then
		assertEquals(1, otherActor.getActions().size);
		assertEquals(ZootKnockbackAction.class, otherActor.getActions().get(0).getClass());
	}
	
	@Test
	public void shouldSetKnockback()
	{
		//given
		final float expectedKnockbackX = 1.28f;
		final float expectedKnockbackY = -2.56f;
	
		//when
		ctrl.setKnockback(expectedKnockbackX, expectedKnockbackY);
		
		//then
		assertEquals(expectedKnockbackX, ctrl.getKnockbackX(), 0.0f);
		assertEquals(expectedKnockbackY, ctrl.getKnockbackY(), 0.0f);
	}
	
	@Test
	public void shouldSetVaryHorizontal()
	{
		ctrl.setVaryHorizontal(true);
		assertTrue(ctrl.getVaryHorizontal());
		
		ctrl.setVaryHorizontal(false);
		assertFalse(ctrl.getVaryHorizontal());
	}
}
