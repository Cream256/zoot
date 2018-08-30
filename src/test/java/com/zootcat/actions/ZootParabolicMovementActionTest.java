package com.zootcat.actions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.math.Vector2;
import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.math.ParaboleMovementPattern;
import com.zootcat.scene.ZootActor;

public class ZootParabolicMovementActionTest
{
	public ZootParabolicMovementAction action;
	
	@Before
	public void setup()
	{
		action = new ZootParabolicMovementAction();		
	}
	
	@Test
	public void shouldReturnDefaultParaboleIfParamsWereNotSet()
	{
		assertNotNull(action.getParabole());
	}
	
	@Test
	public void shouldSetParaboleParameters()
	{
		action.setParaboleParams(new Vector2(3.0f, -6.0f), new Vector2(0.0f, 3.0f));
		
		ParaboleMovementPattern parabole = action.getParabole();
		assertNotNull(parabole);
		assertEquals(-6.0f, parabole.at(3.0f).y, 0.0f);
		assertEquals(3.0f, parabole.at(0.0f).y, 0.0f);
	}
	
	@Test
	public void shouldResetAction()
	{
		action.reset();
		assertNull(action.getParabole());
		assertNull(action.getActionZootActor());
		assertEquals(0.0f, action.getTime(), 0.0f);
	}
	
	@Test
	public void shouldMoveActorInParabolicPattern()
	{
		//given
		PhysicsBodyController bodyCtrl = mock(PhysicsBodyController.class);
		ZootActor actor = new ZootActor();
		actor.addController(bodyCtrl);
		action.setTarget(actor);
		
		//when				
		when(bodyCtrl.getCenterPositionRef()).thenReturn(new Vector2());
		action.setParaboleParams(new Vector2(3.0f, -6.0f), new Vector2(0.0f, 3.0f));		
		action.act(1.0f);
		
		//then
		verify(bodyCtrl).setPosition(1.0f, -2.0f);
		
		//when
		action.act(1.0f);
		
		//then
		verify(bodyCtrl).setPosition(2.0f, -5.0f);
	}
}
