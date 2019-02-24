package com.zootcat.controllers.physics;

import static org.mockito.Matchers.anyFloat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.physics.box2d.Contact;
import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.scene.ZootActor;
import com.zootcat.testing.ZootActorStub;
import com.zootcat.utils.ZootDirection;

public class SetApplyImpulseDirectionOnCollideControllerTest
{
	@Mock private ZootActor ctrlActor;
	@Mock private ApplyImpulseController applyImpulseCtrl;

	private ZootActor otherActor;
	private SetApplyImpulseDirectionOnCollideController ctrl;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		
		otherActor = new ZootActorStub();
		otherActor.addController(applyImpulseCtrl);
		
		ctrl = new SetApplyImpulseDirectionOnCollideController();
		ctrl.init(ctrlActor);
	}
	
	@Test
	public void shouldSetNewHorizontalDirection()
	{
		//given
		final float impulseX = -2.0f;
		when(applyImpulseCtrl.getImpulseX()).thenReturn(impulseX);
		
		ControllerAnnotations.setControllerParameter(ctrl, "setX", true);
		ControllerAnnotations.setControllerParameter(ctrl, "directionX", ZootDirection.Right);		
		
		//when		
		ctrl.onEnter(ctrlActor, otherActor, mock(Contact.class));
		
		//then
		verify(applyImpulseCtrl).setImpulseX(-impulseX);
	}
	
	@Test
	public void shouldNotSetNewHorizontalDirectionIfDirectionIsTheSame()
	{
		//given
		final float impulseX = 2.0f;
		when(applyImpulseCtrl.getImpulseX()).thenReturn(impulseX);
		
		ControllerAnnotations.setControllerParameter(ctrl, "setX", true);
		ControllerAnnotations.setControllerParameter(ctrl, "directionX", ZootDirection.Right);		
		
		//when		
		ctrl.onEnter(ctrlActor, otherActor, mock(Contact.class));
		
		//then
		verify(applyImpulseCtrl, never()).setImpulseX(anyFloat());				
	}
	
	@Test
	public void shouldNotSetNewHorizontalDirectionIfNotAllowed()
	{
		//given
		final float impulseX = -2.0f;
		when(applyImpulseCtrl.getImpulseX()).thenReturn(impulseX);
		
		ControllerAnnotations.setControllerParameter(ctrl, "setX", false);
		ControllerAnnotations.setControllerParameter(ctrl, "directionX", ZootDirection.Right);		
		
		//when		
		ctrl.onEnter(ctrlActor, otherActor, mock(Contact.class));
		
		//then
		verify(applyImpulseCtrl, never()).setImpulseX(anyFloat());
	}
	
	@Test
	public void shouldSetNewVerticalDirection()
	{
		//given
		final float impulseY = -2.0f;
		when(applyImpulseCtrl.getImpulseY()).thenReturn(impulseY);
		
		ControllerAnnotations.setControllerParameter(ctrl, "setY", true);
		ControllerAnnotations.setControllerParameter(ctrl, "directionY", ZootDirection.Up);		
		
		//when		
		ctrl.onEnter(ctrlActor, otherActor, mock(Contact.class));
		
		//then
		verify(applyImpulseCtrl).setImpulseY(-impulseY);
	}
	
	@Test
	public void shouldNotSetNewVerticalDirectionIfDirectionIsTheSame()
	{
		//given
		final float impulseY = 2.0f;
		when(applyImpulseCtrl.getImpulseY()).thenReturn(impulseY);
		
		ControllerAnnotations.setControllerParameter(ctrl, "setY", true);
		ControllerAnnotations.setControllerParameter(ctrl, "directionY", ZootDirection.Up);		
		
		//when		
		ctrl.onEnter(ctrlActor, otherActor, mock(Contact.class));
		
		//then
		verify(applyImpulseCtrl, never()).setImpulseY(anyFloat());			
	}
	
	@Test
	public void shouldNotSetNewVerticalDirectionIfNotAllowed()
	{
		//given
		final float impulseY = -2.0f;
		when(applyImpulseCtrl.getImpulseY()).thenReturn(impulseY);
		
		ControllerAnnotations.setControllerParameter(ctrl, "setY", false);
		ControllerAnnotations.setControllerParameter(ctrl, "directionY", ZootDirection.Up);		
		
		//when		
		ctrl.onEnter(ctrlActor, otherActor, mock(Contact.class));
		
		//then
		verify(applyImpulseCtrl, never()).setImpulseY(anyFloat());	
	}
	
	@Test
	public void shouldDoNothingOnLeave()
	{
		Contact contact = mock(Contact.class);
		ZootActor actorA = mock(ZootActor.class);
		ZootActor actorB = mock(ZootActor.class);
		
		ctrl.onLeave(actorA, actorB, contact);
		verifyNoMoreInteractions(actorA, actorB, contact);
	}
	
}
