package com.zootcat.controllers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.zootcat.controllers.logic.LifeController;
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
	
	@Test
	public void shouldBeEnabledByDefault()
	{
		assertTrue(ctrl.isEnabled());
	}
	
	@Test
	public void shouldSetEnabled()
	{
		ctrl.setEnabled(false);
		assertFalse(ctrl.isEnabled());
		
		ctrl.setEnabled(true);
		assertTrue(ctrl.isEnabled());
	}
	
	@Test
	public void shouldReturnValidHashCode()
	{
		assertEquals(ControllerAdapter.class.hashCode(), ctrl.hashCode());
	}
	
	@Test
	public void shouldNotBeEqualToOtherTypes()
	{
		assertFalse(ctrl.equals(null));
		assertFalse(ctrl.equals("string"));
		assertFalse(ctrl.equals(42));
		assertFalse(ctrl.equals(1.25f));
	}
	
	@Test
	public void shouldNotBeEqualToOtherControllerType()
	{
		LifeController lifeCtrl = new LifeController();
		assertFalse(ctrl.equals(lifeCtrl));
		assertFalse(lifeCtrl.equals(ctrl));
	}
	
	@Test
	public void shouldBeEqualToItself()
	{
		assertTrue(ctrl.equals(ctrl));
	}
	
	@Test
	public void shouldBeEqualToOtherControllerAdapterInstance()
	{
		assertTrue(ctrl.equals(new ControllerAdapter()));
		assertTrue(new ControllerAdapter().equals(ctrl));
	}
}
