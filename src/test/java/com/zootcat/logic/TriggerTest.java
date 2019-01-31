package com.zootcat.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.function.Consumer;

import org.junit.Before;
import org.junit.Test;

public class TriggerTest
{
	private int triggerOnCount;
	private int triggerOffCount;
	private Consumer<Boolean> triggerAction;
	
	@Before
	public void setup()
	{
		triggerOnCount = 0;
		triggerOffCount = 0;
		triggerAction = (on) -> 
		{
			if(on) ++triggerOnCount;
			else ++triggerOffCount;
		};
	}
	
	@Test
	public void shouldNotBeActiveAfterCreate()
	{
		Trigger trigger = new Trigger(triggerAction);
		assertFalse(trigger.isActive());				
	}
	
	@Test
	public void shouldBeActiveAfterCreate()
	{
		Trigger trigger = new Trigger(triggerAction, true);
		assertTrue(trigger.isActive());
	}
	
	@Test
	public void shouldPerformFirstTriggerOnFalseByDefault()
	{		
		//given
		Trigger trigger = new Trigger(triggerAction);
		
		//when
		trigger.initialize();
		
		//then
		assertEquals("Should not be triggered as active", 0, triggerOnCount);
		assertEquals("Should be triggered as not active", 1, triggerOffCount);
	}
	
	@Test
	public void shouldPerformFirstTriggerBasedOnParameterValue()
	{
		//given
		Trigger trigger = new Trigger(triggerAction, true);
		
		//when
		trigger.initialize();
		
		//then
		assertEquals("Should be triggered as active", 1, triggerOnCount);
		assertEquals("Should not be triggered as not active", 0, triggerOffCount);
	}
	
	@Test
	public void shouldSwitchState()
	{
		//given
		Trigger trigger = new Trigger(triggerAction, true);
		
		//when
		trigger.switchState();
		
		//then
		assertFalse(trigger.isActive());
		
		//when
		trigger.switchState();
		
		//then
		assertTrue(trigger.isActive());		
	}
	
	@Test
	public void shouldNotBeTriggeredIfStateWasNotChanged()
	{
		//given
		Trigger trigger = new Trigger(triggerAction, true);
		
		//when
		trigger.setActive(true);
		
		//then
		assertEquals("Should not be triggered as active", 0, triggerOnCount);
		assertEquals("Should not be triggered as not active", 0, triggerOffCount);
	}
	
	@Test
	public void shouldTriggerWhenSettingAsActive()
	{
		//given
		Trigger trigger = new Trigger(triggerAction, true);
		
		//when
		trigger.setActive(false);
		
		//then
		assertEquals("Should not be triggered as active", 0, triggerOnCount);
		assertEquals("Should be triggered as not active", 1, triggerOffCount);
		
		//when
		trigger.setActive(true);
		
		//then
		assertEquals("Should not be triggered as active", 1, triggerOnCount);
		assertEquals("Should be triggered as not active", 1, triggerOffCount);
	}
}
