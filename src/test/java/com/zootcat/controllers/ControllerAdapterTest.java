package com.zootcat.controllers;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.zootcat.scene.ZootActor;

public class ControllerAdapterTest
{
	private ZootActor actor;
	private ControllerAdapter ctrl;
	
	@Before
	public void setup()
	{
		actor = mock(ZootActor.class);
		ctrl = new ControllerAdapter();
	}
	
	@Test
	public void initTest()
	{
		ctrl.init(actor);
		verifyZeroInteractions(actor);
	}
	
	@Test
	public void onAddTest()
	{
		ctrl.onAdd(actor);
		verifyZeroInteractions(actor);
	}
	
	@Test
	public void onRemoveTest()
	{
		ctrl.onRemove(actor);
		verifyZeroInteractions(actor);
	}
	
	@Test
	public void onUpdateTest()
	{
		ctrl.onUpdate(1.0f, actor);
		verifyZeroInteractions(actor);
	}
}
