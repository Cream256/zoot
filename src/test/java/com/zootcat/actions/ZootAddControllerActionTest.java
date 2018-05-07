package com.zootcat.actions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

import com.zootcat.controllers.Controller;
import com.zootcat.scene.ZootActor;

public class ZootAddControllerActionTest
{
	@Test
	public void shouldInitAndAddController()
	{
		//given
		ZootActor actor = mock(ZootActor.class);
		Controller ctrl = mock(Controller.class);
		
		//when
		ZootAddControllerAction action = new ZootAddControllerAction();
		action.setController(ctrl);
		action.setTarget(actor);
		action.act(0.0f);
		
		//then
		verify(ctrl).init(actor);
		verify(actor).addController(ctrl);
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
