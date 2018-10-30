package com.zootcat.controllers.physics;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.physics.box2d.Contact;
import com.zootcat.scene.ZootActor;

public class KnockbackOnTouchControllerTest
{
	@Mock private ZootActor ctrlActor;
	@Mock private PhysicsBodyController physicsBodyCtrl;
	
	private ZootActor otherActor;	
	private KnockbackOnTouchController ctrl;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		
		otherActor = new ZootActor();
		otherActor.addController(physicsBodyCtrl);
				
		ctrl = new KnockbackOnTouchController();
		ctrl.init(ctrlActor);
	}
	
	@Test
	public void shouldApplyDefaultKnockback()
	{		
		//when
		ctrl.onEnter(ctrlActor, otherActor, mock(Contact.class));
		
		//then
		verify(physicsBodyCtrl).applyImpulse(1.0f, 1.0f);		
	}
		
	@Test
	public void shouldApplyCustomKnockback()
	{
		//given
		final float expectedKnockbackX = 1.28f;
		final float expectedKnockbackY = -2.56f;
		
		//when
		ctrl.setKnockback(expectedKnockbackX, expectedKnockbackY);
		ctrl.onEnter(ctrlActor, otherActor, mock(Contact.class));
		
		//then
		verify(physicsBodyCtrl).applyImpulse(expectedKnockbackX, expectedKnockbackY);			
	}
	
	@Test
	public void shouldDoNothinOnLeave()
	{
		//given
		Contact contact = mock(Contact.class);
		ZootActor otherActor = mock(ZootActor.class);
				
		//when
		ctrl.onLeave(ctrlActor, otherActor, contact);
		
		//then
		verifyZeroInteractions(ctrlActor, otherActor, contact);
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
}
