package com.zootcat.controllers.physics;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.physics.box2d.Contact;
import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.controllers.recognizer.MockControllerRecognizer;
import com.zootcat.scene.ZootActor;

public class ApplyImpulseOnCollideControllerTest
{
	private static final float IMPULSE_X = 123.0f;
	private static final float IMPULSE_Y = 234.0f;
	
	private ZootActor otherActor;
	private ApplyImpulseOnCollideController controller;
	@Mock private ZootActor controllerActor;
	@Mock private PhysicsBodyController physicsBodyCtrl;	
		
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		
		otherActor = new ZootActor();
		otherActor.setControllerRecognizer(MockControllerRecognizer.Instance);
		otherActor.addController(physicsBodyCtrl);
				
		controller = new ApplyImpulseOnCollideController();
		ControllerAnnotations.setControllerParameter(controller, "impulseX", IMPULSE_X);
		ControllerAnnotations.setControllerParameter(controller, "impulseY", IMPULSE_Y);
		controller.init(controllerActor);
	}
	
	@Test
	public void shouldApplyImpulseContinously()
	{
		//given
		ControllerAnnotations.setControllerParameter(controller, "continous", true);
		controller.onEnter(controllerActor, otherActor, mock(Contact.class));
		
		//when
		controller.onUpdate(1.0f, controllerActor);
		
		//then
		verify(physicsBodyCtrl).applyImpulse(IMPULSE_X, IMPULSE_Y);
		
		//when
		controller.onUpdate(1.0f, controllerActor);
		
		//then
		verify(physicsBodyCtrl, times(2)).applyImpulse(IMPULSE_X, IMPULSE_Y);
	}
	
	@Test
	public void shouldApplyImpulseOnce()
	{
		//given
		ControllerAnnotations.setControllerParameter(controller, "continous", false);
		controller.onEnter(controllerActor, otherActor, mock(Contact.class));
		
		//when
		controller.onUpdate(1.0f, controllerActor);
		
		//then
		verify(physicsBodyCtrl).applyImpulse(IMPULSE_X, IMPULSE_Y);
		
		//when
		controller.onUpdate(1.0f, controllerActor);
		
		//then no more invocations
		verify(physicsBodyCtrl).applyImpulse(IMPULSE_X, IMPULSE_Y);		
	}	
}
