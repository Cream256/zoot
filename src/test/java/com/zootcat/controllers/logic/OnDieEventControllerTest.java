package com.zootcat.controllers.logic;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.zootcat.events.ZootEvent;
import com.zootcat.events.ZootEventType;
import com.zootcat.events.ZootEvents;
import com.zootcat.scene.ZootActor;

public class OnDieEventControllerTest
{
	private OnDieEventController ctrl;
	
	@Before
	public void setup()
	{
		ctrl = new OnDieEventController() 
		{
			@Override
			protected boolean onDie(ZootActor actor, ZootEvent event)
			{
				return true;
			}
		};	
	}
	
	@Test
	public void handleZootEventShouldReturnTrueOnlyForFirstDeadEventTest()
	{
		assertTrue(ctrl.handleZootEvent(ZootEvents.get(ZootEventType.Dead)));
		for(ZootEventType eventType : ZootEventType.values())
		{		
			assertFalse("Should return false for event " + eventType, ctrl.handleZootEvent(ZootEvents.get(eventType)));	
		}
	}
	
	@Test
	public void handleZootEventShouldReturnFalseForEventsOtherThanDead()
	{
		Arrays.stream(ZootEventType.values()).filter(e -> e != ZootEventType.Dead).forEach(e ->
		{
			assertFalse("Should return false for event " + e, ctrl.handleZootEvent(ZootEvents.get(e)));
		});		
	}
}
