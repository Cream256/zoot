package com.zootcat.actions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

import com.zootcat.controllers.Controller;
import com.zootcat.scene.ZootActor;

public class ZootRemoveControllerActionTest
{
	@Test
	public void shouldInvokeOnLeaveAndRemoveController()
	{
		//given
		ZootActor actor = mock(ZootActor.class);
		Controller ctrl = mock(Controller.class);
		
		//when
		ZootRemoveControllerAction action = new ZootRemoveControllerAction();
		action.setController(ctrl);
		action.setTarget(actor);
		action.act(0.0f);
		
		//then
		verify(ctrl, times(0)).onRemove(actor);
		verify(actor).removeController(ctrl);
	}
	
	@Test
	public void shouldClearTheControllerAfterReset()
	{
		//given
		ZootAddControllerAction action = new ZootAddControllerAction();
		
		//when
		action.setController(mock(Controller.class));
		action.reset();
		
		//then
		assertNull(action.getController());
	}
}
