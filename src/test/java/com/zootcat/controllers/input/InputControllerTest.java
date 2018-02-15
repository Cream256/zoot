package com.zootcat.controllers.input;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

import com.zootcat.scene.ZootActor;


public class InputControllerTest 
{	
	
	@Test
	public void initTest()
	{
		//given
		ZootActor actor = mock(ZootActor.class);
		InputController ctrl = new InputController();
		
		//when
		ctrl.init(actor);
		
		//then
		verifyNoMoreInteractions(actor);
	}
	
	@Test
	public void onUpdateTest()
	{
		//given
		ZootActor actor = mock(ZootActor.class);
		InputController ctrl = new InputController();
		
		//when
		ctrl.onUpdate(1.0f, actor);
		
		//then
		verifyNoMoreInteractions(actor);		
	}
	
	@Test
	public void onAddTest()
	{
		//given
		ZootActor actor = new ZootActor();
		InputController ctrl = new InputController();
		
		//when
		ctrl.onAdd(actor);
		
		//then
		assertTrue(actor.getListeners().contains(ctrl, true));
	}
	
	@Test
	public void onRemoveTest()
	{
		//given
		ZootActor actor = new ZootActor();
		InputController ctrl = new InputController();
		
		//when
		actor.addListener(ctrl);

		//then
		assertTrue(actor.getListeners().contains(ctrl, true));
		
		//when
		ctrl.onRemove(actor);
		
		//then
		assertFalse(actor.getListeners().contains(ctrl, true));
	}
	
}
