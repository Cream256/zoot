package com.zootcat.controllers;

import static org.junit.Assert.assertEquals;
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
	public void shouldDoNothingOnInit()
	{
		ctrl.init(actor);
		verifyZeroInteractions(actor);
	}
	
	@Test
	public void shouldDoNothingOnTest()
	{
		ctrl.onAdd(actor);
		verifyZeroInteractions(actor);
	}
	
	@Test
	public void shouldDoNothingOnRemove()
	{
		ctrl.onRemove(actor);
		verifyZeroInteractions(actor);
	}
	
	@Test
	public void shouldDoNothingOnUpdate()
	{
		ctrl.onUpdate(1.0f, actor);
		verifyZeroInteractions(actor);
	}
	
	@Test
	public void shouldReturnNormalPriority()
	{
		assertEquals(ControllerPriority.Normal, ctrl.getPriority());
	}
}
