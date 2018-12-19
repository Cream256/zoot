package com.zootcat.controllers.physics;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.physics.box2d.Contact;
import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.controllers.recognizer.MockControllerRecognizer;
import com.zootcat.scene.ZootActor;

public class ChangeApplyImpulseDirectionOnCollideControllerTest
{	
	private static final Float IMPULSE_X = 1.23f;
	private static final Float IMPULSE_Y = -2.34f;
	private ZootActor otherActor;
	private ChangeApplyImpulseDirectionOnCollideController controller;
	@Mock private ZootActor controllerActor;
	@Mock private ApplyImpulseController applyImpulseCtrl;	
		
	@Before
	public void setup()
	{		
		MockitoAnnotations.initMocks(this);
		when(applyImpulseCtrl.getImpulseX()).thenReturn(IMPULSE_X);
		when(applyImpulseCtrl.getImpulseY()).thenReturn(IMPULSE_Y);
		
		otherActor = new ZootActor();
		otherActor.addController(applyImpulseCtrl);
		otherActor.setControllerRecognizer(MockControllerRecognizer.Instance);
		
		controller = new ChangeApplyImpulseDirectionOnCollideController();
		controller.init(controllerActor);
	}
	
	@Test
	public void shouldChangeBothImpulses()
	{
		//given
		ControllerAnnotations.setControllerParameter(controller, "changeX", true);
		ControllerAnnotations.setControllerParameter(controller, "changeY", true);
		
		//when
		controller.onEnter(controllerActor, otherActor, mock(Contact.class));
		
		//then
		verify(applyImpulseCtrl).setImpulseX(-IMPULSE_X);
		verify(applyImpulseCtrl).setImpulseY(-IMPULSE_Y);		
	}
	
	@Test
	public void shouldChangeImpulseX()
	{
		//given
		ControllerAnnotations.setControllerParameter(controller, "changeX", true);
		ControllerAnnotations.setControllerParameter(controller, "changeY", false);
		
		//when
		controller.onEnter(controllerActor, otherActor, mock(Contact.class));
		
		//then
		verify(applyImpulseCtrl).setImpulseX(-IMPULSE_X);
		verify(applyImpulseCtrl, times(0)).setImpulseY(-IMPULSE_Y);		
	}
	
	@Test
	public void shouldChangeImpulseY()
	{
		//given
		ControllerAnnotations.setControllerParameter(controller, "changeX", false);
		ControllerAnnotations.setControllerParameter(controller, "changeY", true);
		
		//when
		controller.onEnter(controllerActor, otherActor, mock(Contact.class));
		
		//then
		verify(applyImpulseCtrl, times(0)).setImpulseX(-IMPULSE_X);
		verify(applyImpulseCtrl).setImpulseY(-IMPULSE_Y);		
	}
	
	@Test
	public void shouldNotChangeAnyImpulse()
	{
		//given
		ControllerAnnotations.setControllerParameter(controller, "changeX", false);
		ControllerAnnotations.setControllerParameter(controller, "changeY", false);
		
		//when
		controller.onEnter(controllerActor, otherActor, mock(Contact.class));
		
		//then
		verify(applyImpulseCtrl, times(0)).setImpulseX(-IMPULSE_X);
		verify(applyImpulseCtrl, times(0)).setImpulseY(-IMPULSE_Y);			
	}
	
	@Test
	public void shouldDoNothingOnLeave()
	{
		Contact contact = mock(Contact.class);
		ZootActor actorA = mock(ZootActor.class);
		ZootActor actorB = mock(ZootActor.class);
		
		controller.onLeave(actorA, actorB, contact);
		verifyNoMoreInteractions(actorA, actorB, contact);
	}
}
