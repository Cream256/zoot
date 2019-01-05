package com.zootcat.actions;

import static org.junit.Assert.assertEquals;
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
import com.zootcat.testing.ZootActorStub;

public class ZootPositionActorActionTest
{
	private static final float POS_X = 123.45f;
	private static final float POS_Y = 234.56f;
	
	private ZootActor actor;
	private ZootPositionActorAction action;	
	@Mock private PhysicsBodyController physCtrl;

	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		when(physCtrl.getCenterPositionRef()).thenReturn(new Vector2());
		
		actor = new ZootActorStub();
		actor.addController(physCtrl);
				
		action = new ZootPositionActorAction();
		action.setTarget(actor);
	}
	
	@Test
	public void shouldSetPosition()
	{		
		action.setPosition(POS_X, POS_Y);
		assertEquals(POS_X, action.getX(), 0.0f);
		assertEquals(POS_Y, action.getY(), 0.0f);
	}
	
	@Test
	public void shouldPositionActor()
	{
		//given
		action.setPosition(POS_X, POS_Y);
		
		//when		
		action.act(1.0f);
		
		//then
		verify(physCtrl).setPosition(POS_X, POS_Y);		
	}
	
	@Test
	public void shouldReturnTrueOnAct()
	{
		assertTrue(action.act(1.0f));
	}
}
