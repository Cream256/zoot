package com.zootcat.actions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.math.Vector2;
import com.zootcat.camera.ZootCamera;
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
		ZootKillActorAction action = ZootActions.killActor(actor);
		assertEquals(actor, action.getTargetZootActor());
		assertNotNull(action.getPool());
	}
	
	@Test
	public void shouldCreateMoveActorAction()
	{
		final float mx = 16.0f;
		final float my = 32.0f;
		ZootMoveActorAction action = ZootActions.moveActor(actor, mx, my);
		assertEquals(actor, action.getTargetZootActor());
		assertEquals(mx, action.getMovementX(), 0.0f);
		assertEquals(my, action.getMovementY(), 0.0f);
		assertNotNull(action.getPool());
	}
	
	@Test
	public void shouldCreatePositionActorAction()
	{
		final float posX = 16.0f;
		final float posY = 32.0f;
		ZootPositionActorAction action = ZootActions.positionActor(actor, posX, posY);
		assertEquals(actor, action.getTargetZootActor());
		assertEquals(posX, action.getX(), 0.0f);
		assertEquals(posY, action.getY(), 0.0f);
		assertNotNull(action.getPool());
	}
	
	@Test
	public void shouldCreateAddControllerAction()
	{
		Controller ctrl = mock(Controller.class);
		ZootAddControllerAction action = ZootActions.addController(actor, ctrl);		
		assertEquals(actor, action.getTargetZootActor());
		assertEquals(ctrl, action.getController());
		assertNotNull(action.getPool());
	}
	
	@Test
	public void shouldCreateRemoveControllerAction()
	{
		Controller ctrl = mock(Controller.class);
		ZootRemoveControllerAction action = ZootActions.removeController(actor, ctrl);		
		assertEquals(actor, action.getTargetZootActor());
		assertEquals(ctrl, action.getController());
		assertNotNull(action.getPool());		
	}
	
	@Test
	public void shouldCreateParabolicMovementAction()
	{
		Vector2 peak = new Vector2(10.0f, 10.0f);
		Vector2 point = new Vector2(5.0f, 5.0f);
		
		ZootParabolicMovementAction action = ZootActions.parabolicMovement(actor, peak, point);
		assertEquals(actor, action.getTargetZootActor());
		assertNotNull(action.getParabole());
		assertNotNull(action.getPool());
	}
	
	@Test
	public void shouldCreateEnableInputProcessorControllerAction()
	{
		ZootEnableInputProcessorControllerAction action = ZootActions.enableInputProcessorController(actor, false);
		assertEquals(actor, action.getTargetZootActor());
		assertFalse(action.getControllerEnabled());
	}
	
	@Test
	public void shouldCreateCameraFocusAction()
	{
		ZootCamera camera = new ZootCamera(1.0f, 1.0f);		
		ZootCameraFocusAction action = ZootActions.cameraFocus(camera, actor);
		assertEquals(actor, action.getTargetZootActor());
		assertEquals(camera, action.getCamera());
	}
}
