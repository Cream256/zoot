package com.zootcat.controllers.physics;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.zootcat.scene.ZootActor;

public class KnockbackOnTouchControllerTest
{
	@Mock private ZootActor ctrlActor;
	@Mock private PhysicsBodyController otherActorPhysicsBodyCtrl;
	@Mock private PhysicsBodyController ctrlActorPhysicsBodyCtrl;
	
	private ZootActor otherActor;	
	private KnockbackOnTouchController ctrl;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		
		otherActor = new ZootActor();
		otherActor.addController(otherActorPhysicsBodyCtrl);
		
		when(ctrlActor.getController(PhysicsBodyController.class)).thenReturn(ctrlActorPhysicsBodyCtrl);
				
		ctrl = new KnockbackOnTouchController();
		ctrl.init(ctrlActor);
	}
	
	@Test
	public void shouldApplyDefaultKnockback()
	{		
		//when
		ctrl.onEnter(ctrlActor, otherActor, mock(Contact.class));
		
		//then
		verify(otherActorPhysicsBodyCtrl).setVelocity(1.0f, 1.0f, true, true);		
	}
		
	@Test
	public void shouldApplyOnlyHorizontalKnockback()
	{
		//given
		final float expectedKnockbackX = 1.28f;
		
		//when
		ctrl.setKnockback(expectedKnockbackX, 0.0f);
		ctrl.onEnter(ctrlActor, otherActor, mock(Contact.class));
		
		//then
		verify(otherActorPhysicsBodyCtrl).setVelocity(expectedKnockbackX, 0.0f, true, false);
	}
	
	@Test
	public void shouldApplyOnlyVerticalKnockback()
	{
		//given
		final float expectedKnockbackY = -1.28f;
		
		//when
		ctrl.setKnockback(0.0f, expectedKnockbackY);
		ctrl.onEnter(ctrlActor, otherActor, mock(Contact.class));
		
		//then
		verify(otherActorPhysicsBodyCtrl).setVelocity(0.0f, expectedKnockbackY, false, true);
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
		verify(otherActorPhysicsBodyCtrl).setVelocity(expectedKnockbackX, expectedKnockbackY, true, true);			
	}
	
	@Test
	public void shouldVaryHorizontalKnockback()
	{
		//given
		when(ctrlActorPhysicsBodyCtrl.getCenterPositionRef()).thenReturn(new Vector2(10.0f, 0.0f));
		when(otherActorPhysicsBodyCtrl.getCenterPositionRef()).thenReturn(new Vector2(5.0f, 0.0f));
		
		//when
		ctrl.setVaryHorizontal(true);
		ctrl.onEnter(ctrlActor, otherActor, mock(Contact.class));
		
		//then
		verify(otherActorPhysicsBodyCtrl).setVelocity(-1.0f, 1.0f, true, true);
		
		//when
		when(ctrlActorPhysicsBodyCtrl.getCenterPositionRef()).thenReturn(new Vector2(5.0f, 0.0f));
		when(otherActorPhysicsBodyCtrl.getCenterPositionRef()).thenReturn(new Vector2(10.0f, 0.0f));
		ctrl.onEnter(ctrlActor, otherActor, mock(Contact.class));
		
		//then
		verify(otherActorPhysicsBodyCtrl).setVelocity(1.0f, 1.0f, true, true);
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
