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
import com.zootcat.controllers.recognizer.MockControllerRecognizer;
import com.zootcat.scene.ZootActor;

public class ZootMoveActorActionTest
{
	private static final float MX = 123.45f;
	private static final float MY = 234.56f;
	
	private ZootMoveActorAction action;
	private ZootActor actor;
	@Mock private PhysicsBodyController physCtrl;

	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		when(physCtrl.getCenterPositionRef()).thenReturn(new Vector2());
		
		action = new ZootMoveActorAction();
		actor = new ZootActor();
		actor.setControllerRecognizer(MockControllerRecognizer.Instance);
		actor.addController(physCtrl);
	}
	
	@Test
	public void shouldSetMovementX()
	{		
		action.setMovementX(MX);
		assertEquals(MX, action.getMovementX(), 0.0f);
	}
	
	@Test
	public void shouldSetMovementY()
	{		
		action.setMovementY(MY);
		assertEquals(MY, action.getMovementY(), 0.0f);
	}
	
	@Test
	public void shouldMoveActor()
	{
		//given
		action.setMovementX(MX);
		action.setMovementY(MY);
		action.setTarget(actor);
		
		//when		
		action.act(1.0f);
		
		//then
		verify(physCtrl).setPosition(MX, MY);		
	}
	
	@Test
	public void shouldReturnTrueOnAct()
	{
		action.setTarget(actor);
		assertTrue(action.act(1.0f));
	}
}
