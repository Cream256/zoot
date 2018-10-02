package com.zootcat.controllers.logic.triggers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.zootcat.actions.ZootMoveActorAction;
import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.scene.ZootActor;
import com.zootcat.testing.ZootSceneMock;

public class MoveActorsTriggerControllerTest
{
	private static final float MX = 10.0f;
	private static final float MY = -20.0f;
	
	private ZootSceneMock scene;
	@Mock private ZootActor actor1;
	@Mock private ZootActor actor2;
	@Mock private ZootActor actor3;
	@Mock private ZootActor actor4;
	@Mock private ZootActor switchActor;
	private MoveActorsTriggerController ctrl;
		
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		scene = new ZootSceneMock();
		
		ctrl = new MoveActorsTriggerController();
		ControllerAnnotations.setControllerParameter(ctrl, "mx", MX);
		ControllerAnnotations.setControllerParameter(ctrl, "my", MY);
		ControllerAnnotations.setControllerParameter(ctrl, "scene", scene);
		ControllerAnnotations.setControllerParameter(ctrl, "actorName", "moveable");
		
		when(actor1.getName()).thenReturn("moveable");
		when(actor2.getName()).thenReturn("moveable");
		when(actor3.getName()).thenReturn("moveable");
		when(actor4.getName()).thenReturn("not moveable");
	}
	
	@Test
	public void shouldNotAddMoveActionToWrongActor()
	{
		//given
		scene.addActor(actor4);
		
		//when
		ctrl.triggerOn(switchActor);
		
		//then
		verify(actor4, times(0)).addAction(any());;
	}
	
	@Test
	public void shouldAddMoveActionOnTrigger()
	{
		//given
		ArgumentCaptor<ZootMoveActorAction> argument = ArgumentCaptor.forClass(ZootMoveActorAction.class);
		scene.addActor(actor1);
		
		//when
		ctrl.triggerOn(switchActor);
		
		//then		
		verify(actor1).addAction(argument.capture());
		assertEquals(MX, argument.getValue().getMovementX(), 0.0f);
		assertEquals(MY, argument.getValue().getMovementY(), 0.0f);
		assertEquals(actor1, argument.getValue().getTargetZootActor());
	}
	
	@Test
	public void shouldAddMoveActionOnTriggerToAllActors()
	{		
		//given
		scene.addActor(actor1);
		scene.addActor(actor2);
		scene.addActor(actor3);
		
		//when
		ctrl.triggerOn(switchActor);
		
		//then
		verify(actor1).addAction(any(ZootMoveActorAction.class));
		verify(actor2).addAction(any(ZootMoveActorAction.class));
		verify(actor3).addAction(any(ZootMoveActorAction.class));
	}
	
	@Test
	public void shouldAddMoveActionOnTriggerOnlyOnce()
	{		
		//given
		scene.addActor(actor1);
		
		//when
		ctrl.triggerOn(switchActor);
		ctrl.triggerOn(switchActor);
		
		//then
		verify(actor1, times(1)).addAction(any(ZootMoveActorAction.class));
	}
	
	@Test
	public void shouldRevertToPreviousPosition()
	{
		//given
		ArgumentCaptor<ZootMoveActorAction> argument = ArgumentCaptor.forClass(ZootMoveActorAction.class);
		ControllerAnnotations.setControllerParameter(ctrl, "canRevert", true);
		scene.addActor(actor1);
		
		//when
		ctrl.triggerOn(switchActor);
		ctrl.triggerOff(switchActor);
		
		//then
		verify(actor1, times(2)).addAction(argument.capture());
		assertEquals(-MX, argument.getValue().getMovementX(), 0.0f);
		assertEquals(-MY, argument.getValue().getMovementY(), 0.0f);
		assertEquals(actor1, argument.getValue().getTargetZootActor());		
	}
	
	@Test
	public void shouldNotRevertToPreviousPositionIfWasNotTriggeredOn()
	{
		//given
		ControllerAnnotations.setControllerParameter(ctrl, "canRevert", true);
		scene.addActor(actor1);
		
		//when
		ctrl.triggerOff(switchActor);
		
		//then
		verify(actor1, never()).addAction(any());
	}
	
	@Test
	public void shouldNotRevertToPreviousPositionIfNotRevertable()
	{
		//given
		ArgumentCaptor<ZootMoveActorAction> argument = ArgumentCaptor.forClass(ZootMoveActorAction.class);
		ControllerAnnotations.setControllerParameter(ctrl, "canRevert", false);
		scene.addActor(actor1);
		
		//when
		ctrl.triggerOn(switchActor);
		ctrl.triggerOff(switchActor);
		
		//then
		verify(actor1, times(1)).addAction(argument.capture());
		assertEquals(MX, argument.getValue().getMovementX(), 0.0f);
		assertEquals(MY, argument.getValue().getMovementY(), 0.0f);
		assertEquals(actor1, argument.getValue().getTargetZootActor());
	}
}
