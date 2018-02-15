package com.zootcat.controllers.logic;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.scene.ZootActor;

public class HoverInAirTest
{
	private static final float VY = 10.0f;
	private static final float TIME_SCALE = 2.0f;
	
	private ZootActor actor;
	private HoverInAirController ctrl;
	private PhysicsBodyController physicsCtrl;
	
	@Before
	public void setup()
	{		
		physicsCtrl = mock(PhysicsBodyController.class);		
		actor = new ZootActor();
		actor.addController(physicsCtrl);
		
		ctrl = new HoverInAirController();
		ControllerAnnotations.setControllerParameter(ctrl, "vy", VY);
		ctrl.init(actor);
	}
	
	@Test
	public void onUpdateWithNoPhysicsCtrlShouldNotThrowTest()
	{
		actor.removeController(physicsCtrl);
		assertTrue(actor.getControllers().isEmpty());		
		ctrl.onUpdate(0.0f, actor);
		//ok
	}
	
	@Test
	public void shouldSetZeroVelocityWhenAtStartTest()
	{		
		ctrl.onUpdate(0.0f, actor);	
		verify(physicsCtrl, times(1)).setVelocity(0.0f, 0.0f, false, true);
	}
	
	@Test
	public void shouldHoverUpTest()
	{
		//0.25f = PI / 2 = 90 degrees
		ctrl.onUpdate(0.25f, actor);
		verify(physicsCtrl).setVelocity(0.0f, VY, false, true);
		
		//0.50f = PI = 180 degrees
		ctrl.onUpdate(0.25f, actor);
		verify(physicsCtrl).setVelocity(0.0f, VY, false, true);
	}
	
	@Test
	public void shouldHoverDownTest()
	{
		//0.75f = 3*PI / 2 = 270 degrees
		ctrl.onUpdate(0.75f, actor);
		verify(physicsCtrl).setVelocity(0.0f, -VY, false, true);
		
		//1.00f = 2*PI = 360 degrees
		ctrl.onUpdate(0.25f, actor);
		verify(physicsCtrl).setVelocity(0.0f, -VY, false, true);
	}
	
	@Test
	public void shouldUseVariableTimeScaleTest()
	{
		//given
		ControllerAnnotations.setControllerParameter(ctrl, "timeScale", TIME_SCALE);
		
		//when 0.245f = ~0.5f = ~170 degrees
		ctrl.onUpdate(0.245f, actor);
		verify(physicsCtrl).setVelocity(0.0f, VY, false, true);
		
		//when 0.255 = ~0.9f = ~350 degrees
		ctrl.onUpdate(0.245f, actor);
		verify(physicsCtrl).setVelocity(0.0f, -VY, false, true);
	}
}
