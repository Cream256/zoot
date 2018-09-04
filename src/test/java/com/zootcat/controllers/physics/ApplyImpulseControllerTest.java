package com.zootcat.controllers.physics;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.scene.ZootActor;

public class ApplyImpulseControllerTest
{
	private static final float IMPULSE_X = 1.0f;
	private static final float IMPULSE_Y = 2.0f;
		
	private ZootActor controllerActor;
	private ApplyImpulseController controller;
	@Mock private PhysicsBodyController physicsBodyCtrl;	
		
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		
		controllerActor = new ZootActor();
		controllerActor.addController(physicsBodyCtrl);
		
		controller = new ApplyImpulseController();
		ControllerAnnotations.setControllerParameter(controller, "impulseX", IMPULSE_X);
		ControllerAnnotations.setControllerParameter(controller, "impulseY", IMPULSE_Y);
	}
	
	@Test
	public void shouldApplyImpulse()
	{
		controller.onUpdate(1.0f, controllerActor);
		verify(physicsBodyCtrl).applyImpulse(IMPULSE_X, IMPULSE_Y);
		
		controller.onUpdate(1.0f, controllerActor);
		verify(physicsBodyCtrl, times(2)).applyImpulse(IMPULSE_X, IMPULSE_Y);
		
		controller.onUpdate(0.5f, controllerActor);
		verify(physicsBodyCtrl).applyImpulse(IMPULSE_X * 0.5f, IMPULSE_Y * 0.5f);
	}
	
	@Test
	public void shouldDoNothingOnInit()
	{
		//given
		ZootActor actor = mock(ZootActor.class);
		
		//when
		controller.init(actor);
		
		//then
		verifyNoMoreInteractions(actor);
	}
	
	@Test
	public void shouldDoNothingOnAdd()
	{
		//given
		ZootActor actor = mock(ZootActor.class);
		
		//when
		controller.onAdd(actor);
		
		//then
		verifyNoMoreInteractions(actor);		
	}
	
	@Test
	public void shouldDoNothingOnLeave()
	{
		//given
		ZootActor actor = mock(ZootActor.class);
		
		//when
		controller.onRemove(actor);
		
		//then
		verifyNoMoreInteractions(actor);		
	}	
}
