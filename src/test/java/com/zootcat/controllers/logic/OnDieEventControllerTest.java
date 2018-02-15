package com.zootcat.controllers.logic;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.zootcat.events.ZootEventType;
import com.zootcat.events.ZootEvents;

public class OnDieEventControllerTest
{
	@Test
	public void handleZootEventShouldReturnTrueOnlyForFirstDeadEventTest()
	{
		OnDieEventController ctrl = new OnDieEventController();		
		assertTrue(ctrl.handleZootEvent(ZootEvents.get(ZootEventType.Dead)));		
				
		for(ZootEventType eventType : ZootEventType.values())
		{		
			assertFalse("Should return false for event " + eventType, ctrl.handleZootEvent(ZootEvents.get(eventType)));	
		}
	}
	
	@Test
	public void onZootEventShouldReturnTrueByDefaultTest()
	{
		OnDieEventController ctrl = new OnDieEventController();
		for(ZootEventType eventType : ZootEventType.values())
		{		
			assertTrue("Should return true for event " + eventType, ctrl.onZootEvent(null, ZootEvents.get(eventType)));
		}
	}
	
}
