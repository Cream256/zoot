package com.zootcat.controllers.logic;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.zootcat.events.ZootEventType;
import com.zootcat.events.ZootEvents;
import com.zootcat.scene.ZootActor;

public class SwitchEventListenerTest
{
	private boolean isOn;
	private SwitchEventListener eventListener;
		
	@Before
	public void setup()
	{
		isOn = false;
		eventListener = new SwitchEventListener()
		{
			@Override
			public void turnOn(ZootActor switchActor)
			{
				isOn = true;		
			}
			
			@Override
			public void turnOff(ZootActor switchActor)
			{
				isOn = false;
			}
		};
	}
	
	@Test
	public void shouldReturnTrueOnSwitchEvents()
	{
		assertTrue(eventListener.handleZootEvent(ZootEvents.get(ZootEventType.SwitchOn)));
		assertTrue(eventListener.handleZootEvent(ZootEvents.get(ZootEventType.SwitchOn)));
	}
	
	@Test
	public void shouldReturnFalseOnNonSwitchEvents()
	{
		Arrays.stream(ZootEventType.values())
			  .filter(type -> type != ZootEventType.SwitchOn)
			  .filter(type -> type != ZootEventType.SwitchOff)
			  .forEach(eventType -> assertFalse(eventListener.handleZootEvent(ZootEvents.get(eventType))));
	}
	
	@Test
	public void shouldInvokeOnMethod()
	{
		//given
		assertFalse(isOn);
		
		//when
		eventListener.handleZootEvent(ZootEvents.get(ZootEventType.SwitchOn));
		
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
		eventListener.handleZootEvent(ZootEvents.get(ZootEventType.SwitchOff));
		
		//then
		assertFalse(isOn);		
	}
	
}
