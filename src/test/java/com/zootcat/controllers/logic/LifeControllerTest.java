package com.zootcat.controllers.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction;
import com.zootcat.fsm.events.ZootActorEventCounterListener;
import com.zootcat.scene.ZootActor;

public class LifeControllerTest
{
	private LifeController ctrl;	
	
	@Before
	public void setup()
	{
		ctrl = new LifeController();
		ctrl.init(mock(ZootActor.class));
	}
	
	@Test
	public void shouldProperlyInitialize()
	{
		assertTrue(ctrl.getRemoveWhenDead());
		assertEquals(LifeController.DEFAULT_LIFE, ctrl.getValue());
		assertEquals(LifeController.DEFAULT_LIFE, ctrl.getMaxValue());		
	}
	
	@Test
	public void shouldReturnIsAlive()
	{
		ctrl.setValue(3);
		assertTrue(ctrl.isAlive());
		
		ctrl.setValue(1);
		assertTrue(ctrl.isAlive());
		
		ctrl.setValue(0);
		assertFalse(ctrl.isAlive());
		
		ctrl.setValue(-1);
		assertFalse(ctrl.isAlive());
	}
	
	@Test
	public void shouldAddLife()
	{
		ctrl.addToValue(-1);
		assertEquals(2, ctrl.getValue());
		
		ctrl.addToValue(1);
		assertEquals(3, ctrl.getValue());
		
		ctrl.addToValue(ctrl.getMaxValue());
		assertEquals(ctrl.getMaxValue(), ctrl.getValue());
		
		ctrl.addToValue(-Integer.MAX_VALUE);
		assertEquals(0, ctrl.getValue());
	}
	
	@Test
	public void shouldGetMaxLife()
	{
		ctrl.addToMaxValue(1);
		assertEquals(4, ctrl.getMaxValue());
		
		ctrl.addToMaxValue(-1);
		assertEquals(3, ctrl.getMaxValue());
		
		ctrl.addToMaxValue(0);
		assertEquals(3, ctrl.getMaxValue());
	}
	
	@Test
	public void shouldSetLife()
	{		
		ctrl.setValue(0);
		assertEquals(0, ctrl.getValue());
		
		ctrl.setValue(1);
		assertEquals(1, ctrl.getValue());
		
		ctrl.setValue(ctrl.getMaxValue());
		assertEquals(ctrl.getMaxValue(), ctrl.getValue());
		
		ctrl.setValue(ctrl.getMaxValue() + 1);
		assertEquals("Should not be able to set more than max life", ctrl.getMaxValue(), ctrl.getValue());
		
		ctrl.setValue(-1);
		assertEquals("Should not be able to set negative value", 0, ctrl.getValue());
	}
	
	@Test
	public void shouldSetMaxLife()
	{		
		ctrl.setMaxValue(10);
		assertEquals(10, ctrl.getMaxValue());
		
		ctrl.setMaxValue(0);
		assertEquals("Should not be able to set max life below 1", 1, ctrl.getMaxValue());
		
		ctrl.setMaxValue(-1);
		assertEquals("Should not be able to set max life below 1", 1, ctrl.getMaxValue());
				
		ctrl.setMaxValue(10);
		ctrl.setValue(10);
		ctrl.setMaxValue(5);
		assertEquals(5, ctrl.getMaxValue());
		assertEquals("Life should be clamped to max life vaule", 5, ctrl.getValue());
		
		ctrl.setValue(5);
		ctrl.setMaxValue(0);
		assertEquals(1, ctrl.getMaxValue());
		assertEquals("Life should be clamped to max life vaule", 1, ctrl.getValue());
	}
	
	@Test
	public void shouldSetRemoveWhenDead()
	{
		ctrl.setRemoveWhenDead(false);
		assertFalse(ctrl.getRemoveWhenDead());
		
		ctrl.setRemoveWhenDead(true);
		assertTrue(ctrl.getRemoveWhenDead());
	}
	
	@Test
	public void shouldRemoveFromSceneWhenDead()
	{
		//given
		ZootActor actor = new ZootActor();
		ctrl.init(actor);
		ctrl.onAdd(actor);
		ctrl.setRemoveWhenDead(true);
		
		//when		
		ctrl.setValue(0);
		ctrl.onUpdate(1.0f, actor);
		
		//then
		assertEquals(1, actor.getActions().size);
		assertEquals(RemoveActorAction.class, actor.getActions().get(0).getClass());
	}
	
	@Test
	public void shouldNotRemoveFromSceneWhenDead()
	{
		//given
		ZootActor actor = new ZootActor();
		ctrl.init(actor);
		ctrl.onAdd(actor);
		ctrl.setRemoveWhenDead(false);
		
		//when		
		ctrl.setValue(0);
		ctrl.onUpdate(1.0f, actor);
		
		//then
		assertEquals(0, actor.getActions().size);		
	}
	
	@Test
	public void shouldSendDeadEvent()
	{	
		//given
		ZootActor actor = new ZootActor();		
		ZootActorEventCounterListener counter = new ZootActorEventCounterListener();
		actor.addListener(counter);
		ctrl.init(actor);
		ctrl.onAdd(actor);
		
		//when
		ctrl.onUpdate(1.0f, actor);
		
		//then
		assertEquals(0, counter.getCount());
		
		//when
		ctrl.setValue(0);
		ctrl.onUpdate(1.0f, actor);
		
		//then
		assertEquals("Should send dead event", 1, counter.getCount());
		
		//when
		ctrl.onUpdate(1.0f, actor);
		
		//then
		assertEquals("Should not send any event", 1, counter.getCount());
		
		//when
		ctrl.setValue(1);
		ctrl.setValue(0);
		ctrl.onUpdate(1.0f, actor);
		
		//then
		assertEquals("Should send dead event again", 2, counter.getCount());
	}
	
	@Test
	public void shouldBeSingleton()
	{
		assertTrue(ctrl.isSingleton());
	}
	
	@Test
	public void shouldSetFrozenFlag()
	{
		ctrl.setFrozen(true);
		assertTrue(ctrl.isFrozen());
		ctrl.setFrozen(false);
		assertFalse(ctrl.isFrozen());
	}
	
	@Test
	public void shouldNotSetValueIfFrozen()
	{
		//given
		int initialValue = ctrl.getValue();
		
		//when
		ctrl.setFrozen(true);		
		ctrl.setValue(0);
		
		//then		
		assertEquals(initialValue, ctrl.getValue());
		
		//when
		ctrl.setFrozen(false);		
		ctrl.setValue(0);
		
		//then
		assertEquals(0, ctrl.getValue());
	}
	
	@Test
	public void shouldNotSetMaxValueIfFrozen()
	{
		//given
		int initialValue = ctrl.getMaxValue();
		
		//when
		ctrl.setFrozen(true);		
		ctrl.setMaxValue(100);
		
		//then		
		assertEquals(initialValue, ctrl.getMaxValue());
		
		//when
		ctrl.setFrozen(false);		
		ctrl.setMaxValue(100);
		
		//then
		assertEquals(100, ctrl.getMaxValue());
	}
	
	@Test
	public void shouldNotSetMinValueIfFrozen()
	{
		//given
		int initialValue = ctrl.getMinValue();
		
		//when
		ctrl.setFrozen(true);		
		ctrl.setMinValue(2);
		
		//then		
		assertEquals(initialValue, ctrl.getMinValue());
		
		//when
		ctrl.setFrozen(false);		
		ctrl.setMinValue(2);
		
		//then
		assertEquals(2, ctrl.getMinValue());		
	}
}
