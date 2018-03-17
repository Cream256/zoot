package com.zootcat.controllers.logic.triggers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.zootcat.actions.ZootMoveActorAction;
import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;

public class MoveActorsTriggerControllerTest
{
	private static final float MX = 10.0f;
	private static final float MY = -20.0f;
	
	@Mock private ZootScene scene;
	@Mock private ZootActor actor1;
	@Mock private ZootActor actor2;
	@Mock private ZootActor actor3;
	@Mock private ZootActor switchActor;
	private MoveActorsTriggerController ctrl;
		
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		when(scene.getUnitScale()).thenReturn(1.0f);
		
		ctrl = new MoveActorsTriggerController();
		ControllerAnnotations.setControllerParameter(ctrl, "mx", MX);
		ControllerAnnotations.setControllerParameter(ctrl, "my", MY);
		ControllerAnnotations.setControllerParameter(ctrl, "scene", scene);
	}
	
	@Test
	public void shouldAddMoveActionOnTrigger()
	{
		//given
		ArgumentCaptor<ZootMoveActorAction> argument = ArgumentCaptor.forClass(ZootMoveActorAction.class);
		
		//when
		when(scene.getActors(any())).thenReturn(Arrays.asList(actor1));
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
		//when
		when(scene.getActors(any())).thenReturn(Arrays.asList(actor1, actor2, actor3));
		ctrl.triggerOn(switchActor);
		
		//then
		verify(actor1).addAction(any(ZootMoveActorAction.class));
		verify(actor2).addAction(any(ZootMoveActorAction.class));
		verify(actor3).addAction(any(ZootMoveActorAction.class));
	}
	
	@Test
	public void shouldAddMoveActionOnTriggerOnlyOnce()
	{		
		//when
		when(scene.getActors(any())).thenReturn(Arrays.asList(actor1));
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
		
		//when
		when(scene.getActors(any())).thenReturn(Arrays.asList(actor1));
		
		ctrl.triggerOn(switchActor);
		ctrl.triggerOff(switchActor);
		
		//then
		verify(actor1, times(2)).addAction(argument.capture());
		assertEquals(-MX, argument.getValue().getMovementX(), 0.0f);
		assertEquals(-MY, argument.getValue().getMovementY(), 0.0f);
		assertEquals(actor1, argument.getValue().getTargetZootActor());		
	}
}
