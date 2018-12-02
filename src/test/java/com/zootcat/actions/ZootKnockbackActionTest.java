package com.zootcat.actions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.math.Vector2;
import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.scene.ZootActor;

public class ZootKnockbackActionTest
{
	@Mock private ZootActor actionActor;
	@Mock private PhysicsBodyController targetActorPhysicsCtrl;
	@Mock private PhysicsBodyController actionActorPhysicsCtrl;
	private ZootActor targetActor;
	private ZootKnockbackAction action;
		
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		when(actionActor.getController(PhysicsBodyController.class)).thenReturn(actionActorPhysicsCtrl);
		
		targetActor = new ZootActor();
		targetActor.addController(targetActorPhysicsCtrl);
		
		action = new ZootKnockbackAction();
		action.setAttackActor(actionActor);
		action.setKnockbackActor(targetActor);
	}
	
	@Test
	public void shouldReturnTrueOnAct()
	{
		assertTrue(action.act(0.0f));
	}
	
	@Test
	public void shouldSetDefaultValues()
	{		
		assertEquals(0.0f, action.getKnockbackX(), 0.0f);
		assertEquals(0.0f, action.getKnockbackY(), 0.0f);
		assertFalse(action.getVaryHorizontal());
	}
	
	@Test
	public void resetShouldSetDefaultValues()
	{
		//given
		action.setKnockback(1.0f, 2.0f);
		action.setVaryHorizontal(true);
		
		//when
		action.reset();
		
		//then
		assertEquals(0.0f, action.getKnockbackX(), 0.0f);
		assertEquals(0.0f, action.getKnockbackY(), 0.0f);
		assertFalse(action.getVaryHorizontal());		
		assertNull(action.getKnockbackActor());
		assertNull(action.getAttackActor());
	}
		
	@Test
	public void shouldApplyOnlyHorizontalKnockback()
	{
		//given
		final float expectedKnockbackX = 1.28f;
		
		//when
		action.setKnockback(expectedKnockbackX, 0.0f);		
		action.act(0.0f);
		
		//then
		verify(targetActorPhysicsCtrl).setVelocity(expectedKnockbackX, 0.0f, true, false);
	}
	
	@Test
	public void shouldApplyOnlyVerticalKnockback()
	{
		//given
		final float expectedKnockbackY = -1.28f;
		
		//when
		action.setKnockback(0.0f, expectedKnockbackY);
		action.act(0.0f);
		
		//then
		verify(targetActorPhysicsCtrl).setVelocity(0.0f, expectedKnockbackY, false, true);
	}
	
	@Test
	public void shouldApplyCustomKnockback()
	{
		//given
		final float expectedKnockbackX = 1.28f;
		final float expectedKnockbackY = -2.56f;
		
		//when
		action.setKnockback(expectedKnockbackX, expectedKnockbackY);	
		action.act(0.0f);
		
		//then
		verify(targetActorPhysicsCtrl).setVelocity(expectedKnockbackX, expectedKnockbackY, true, true);			
	}
	
	@Test
	public void shouldVaryHorizontalKnockback()
	{
		//given
		when(actionActorPhysicsCtrl.getCenterPositionRef()).thenReturn(new Vector2(10.0f, 0.0f));
		when(targetActorPhysicsCtrl.getCenterPositionRef()).thenReturn(new Vector2(5.0f, 0.0f));
		
		//when
		action.setVaryHorizontal(true);
		action.setKnockback(1.0f, 1.0f);
		action.act(0.0f);
		
		//then
		verify(targetActorPhysicsCtrl).setVelocity(-1.0f, 1.0f, true, true);
		
		//when
		when(actionActorPhysicsCtrl.getCenterPositionRef()).thenReturn(new Vector2(5.0f, 0.0f));
		when(targetActorPhysicsCtrl.getCenterPositionRef()).thenReturn(new Vector2(10.0f, 0.0f));
		action.act(0.0f);
		
		//then
		verify(targetActorPhysicsCtrl).setVelocity(1.0f, 1.0f, true, true);
	}
		
	@Test
	public void shouldSetKnockback()
	{
		//given
		final float expectedKnockbackX = 1.28f;
		final float expectedKnockbackY = -2.56f;
	
		//when
		action.setKnockback(expectedKnockbackX, expectedKnockbackY);
		
		//then
		assertEquals(expectedKnockbackX, action.getKnockbackX(), 0.0f);
		assertEquals(expectedKnockbackY, action.getKnockbackY(), 0.0f);
	}
	
	@Test
	public void shouldSetKnockbackActor()
	{
		action.setKnockbackActor(targetActor);
		assertEquals(targetActor, action.getKnockbackActor());
		
		action.setKnockbackActor(null);
		assertNull(action.getKnockbackActor());
	}
	
	@Test
	public void shouldSetAttackActor()
	{
		action.setAttackActor(targetActor);
		assertEquals(targetActor, action.getAttackActor());
		
		action.setAttackActor(null);
		assertNull(action.getAttackActor());
	}
}
