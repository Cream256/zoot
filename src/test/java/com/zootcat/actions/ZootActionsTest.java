package com.zootcat.actions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.zootcat.controllers.Controller;
import com.zootcat.scene.ZootActor;

public class ZootActionsTest
{
	@Mock private ZootActor actor;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void shouldCreateKillActorAction()
	{
		ZootKillActorAction action = ZootActions.killActorAction(actor);
		assertEquals(actor, action.getTargetZootActor());
		assertNotNull(action.getPool());
	}
	
	@Test
	public void shouldCreateMoveActorAction()
	{
		final float mx = 16.0f;
		final float my = 32.0f;
		ZootMoveActorAction action = ZootActions.moveActorAction(actor, mx, my);
		assertEquals(actor, action.getTargetZootActor());
		assertEquals(mx, action.getMovementX(), 0.0f);
		assertEquals(my, action.getMovementY(), 0.0f);
		assertNotNull(action.getPool());
	}
	
	@Test
	public void shouldCreateAddControllerAction()
	{
		Controller ctrl = mock(Controller.class);
		ZootAddControllerAction action = ZootActions.addControllerAction(actor, ctrl);		
		assertEquals(actor, action.getTargetZootActor());
		assertEquals(ctrl, action.getController());
		assertNotNull(action.getPool());
	}
	
	@Test
	public void shouldCreateRemoveControllerAction()
	{
		Controller ctrl = mock(Controller.class);
		ZootRemoveControllerAction action = ZootActions.removeControllerAction(actor, ctrl);		
		assertEquals(actor, action.getTargetZootActor());
		assertEquals(ctrl, action.getController());
		assertNotNull(action.getPool());		
	}
}
