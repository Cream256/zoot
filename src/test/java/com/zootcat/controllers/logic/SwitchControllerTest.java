package com.zootcat.controllers.logic;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.physics.box2d.Contact;
import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.scene.ZootActor;

public class SwitchControllerTest
{
	private SwitchController ctrl;
	private int triggerCount;
	private boolean wasActiveWhenTriggered;
	
	@Before
	public void setup()
	{
		triggerCount = 0;
		wasActiveWhenTriggered = false;
		ctrl = new SwitchController(){
			@Override
			public void trigger(boolean active)
			{
				++triggerCount;
				wasActiveWhenTriggered = active;
			}};
	}

	@Test
	public void shouldPerformFirstTriggerOnFalseByDefault()
	{		
		//when
		ctrl.onAdd(mock(ZootActor.class));
		
		//then
		assertEquals("Switch was not triggered", 1, triggerCount);
		assertFalse("Should be triggered as not active", wasActiveWhenTriggered);
	}
	
	@Test
	public void shouldPerformFirstTriggerBasedOnParameterValue()
	{
		//given
		ControllerAnnotations.setControllerParameter(ctrl, "active", true);
		
		//when
		ctrl.onAdd(mock(ZootActor.class));
		
		//then
		assertEquals("Switch was not triggered", 1, triggerCount);
		assertTrue("Should be triggered as active", wasActiveWhenTriggered);
	}
	
	@Test
	public void shouldDoNothingWhenEndingCollision()
	{
		//given
		Contact contact = mock(Contact.class);
		ZootActor actorA = mock(ZootActor.class);
		ZootActor actorB = mock(ZootActor.class);
		
		//when
		ctrl.onLeave(actorA, actorB, contact);
		
		//then
		verifyZeroInteractions(actorA, actorB, contact);
	}
	
	@Test
	public void shouldTriggerIfStateWasChanged()
	{
		//given
		assertFalse(ctrl.isActive());
		
		//when
		ctrl.setActive(true);
		
		//then
		assertEquals("Switch was not triggered", 1, triggerCount);
		assertTrue(wasActiveWhenTriggered);
		
		//when
		ctrl.setActive(false);
		
		//then
		assertEquals("Switch was not triggered", 2, triggerCount);
		assertFalse(wasActiveWhenTriggered);
	}
	
	@Test
	public void shouldNotTriggerIfStateWasNotChanged()
	{
		//given
		assertFalse(ctrl.isActive());
		
		//when
		ctrl.setActive(false);
		
		//then
		assertEquals("Switch should not be triggered", 0, triggerCount);
	}
	
	@Test
	public void shouldAlwaysTriggerWhenSwitchingState()
	{
		//given
		assertFalse(ctrl.isActive());
		
		//when
		boolean active = false;
		for(int i = 1; i < 10; ++i)
		{
			active = !active;			
			ctrl.switchState();
			
			//then
			assertEquals(active, ctrl.isActive());
			assertEquals(i, triggerCount);
			assertEquals(active, wasActiveWhenTriggered);
		}
	}
	
	@Test
	public void shouldSwitchStatesEachTimeWhenCollisionHappens()
	{
		//given
		assertFalse(ctrl.isActive());
		
		//when
		ctrl.onEnter(mock(ZootActor.class), mock(ZootActor.class), mock(Contact.class));
		
		//then
		assertEquals("Switch was not triggered", 1, triggerCount);
		assertTrue(wasActiveWhenTriggered);
		
		//when
		ctrl.onEnter(mock(ZootActor.class), mock(ZootActor.class), mock(Contact.class));
		
		//then
		assertEquals("Switch was not triggered proper amount of times", 2, triggerCount);
		assertFalse(wasActiveWhenTriggered);
	}
	
}
