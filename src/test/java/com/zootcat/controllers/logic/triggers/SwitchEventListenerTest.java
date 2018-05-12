package com.zootcat.controllers.logic.triggers;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.zootcat.controllers.logic.triggers.TriggerEventListener;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.events.ZootEvents;
import com.zootcat.scene.ZootActor;

public class SwitchEventListenerTest
{
	private boolean isOn;
	private TriggerEventListener eventListener;
		
	@Before
	public void setup()
	{
		isOn = false;
		eventListener = new TriggerEventListener()
		{
			@Override
			public void triggerOn(ZootActor switchActor)
			{
				isOn = true;		
			}
			
			@Override
			public void triggerOff(ZootActor switchActor)
			{
				isOn = false;
			}
		};
	}
	
	@Test
	public void shouldReturnTrueOnSwitchEvents()
	{
		assertTrue(eventListener.handleZootEvent(ZootEvents.get(ZootEventType.TriggerOn)));
		assertTrue(eventListener.handleZootEvent(ZootEvents.get(ZootEventType.TriggerOn)));
	}
	
	@Test
	public void shouldReturnFalseOnNonSwitchEvents()
	{
		Arrays.stream(ZootEventType.values())
			  .filter(type -> type != ZootEventType.TriggerOn)
			  .filter(type -> type != ZootEventType.TriggerOff)
			  .forEach(eventType -> assertFalse(eventListener.handleZootEvent(ZootEvents.get(eventType))));
	}
	
	@Test
	public void shouldInvokeOnMethod()
	{
		//given
		assertFalse(isOn);
		
		//when
		eventListener.handleZootEvent(ZootEvents.get(ZootEventType.TriggerOn));
		
		//then
		assertTrue(isOn);
	}
	
	@Test
	public void shouldInvokeOffMethod()
	{
		//given
		isOn = true;
		assertTrue(isOn);
		
		//when
		eventListener.handleZootEvent(ZootEvents.get(ZootEventType.TriggerOff));
		
		//then
		assertFalse(isOn);		
	}
	
}
