package com.zootcat.controllers.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.zootcat.events.ZootEventType;
import com.zootcat.events.ZootEvents;

public class OnZootEventControllerTest
{
	
	@Test
	public void defaultCtorTest()
	{
		OnZootEventController ctrl = new OnZootEventController();
		assertFalse(ctrl.isDone());
		assertFalse(ctrl.isSingleExecution());
		assertEquals(ZootEventType.values().length, ctrl.getEventTypes().size());
	}
	
	@Test
	public void secondCtorTest()
	{
		OnZootEventController ctrl = new OnZootEventController(true);
		assertFalse(ctrl.isDone());
		assertTrue(ctrl.isSingleExecution());
		assertEquals(ZootEventType.values().length, ctrl.getEventTypes().size());
	}
		
	@Test
	public void thirdCtorTest()
	{
		List<ZootEventType> types = Arrays.asList(ZootEventType.Attack, ZootEventType.Collide);
		OnZootEventController ctrl = new OnZootEventController(types, true);
		assertFalse(ctrl.isDone());
		assertTrue(ctrl.isSingleExecution());
		assertEquals(types.size(), ctrl.getEventTypes().size());
		types.forEach(type -> assertTrue(ctrl.getEventTypes().contains(type)));
	}
	
	@Test
	public void setSingleExecutionTest()
	{
		OnZootEventController ctrl = new OnZootEventController();
		
		ctrl.setSingleExecution(true);
		assertTrue(ctrl.isSingleExecution());
		
		ctrl.setSingleExecution(false);
		assertFalse(ctrl.isSingleExecution());
	}
	
	@Test
	public void handleZootEventShouldProcessAllEventsTest()
	{
		OnZootEventController ctrl = new OnZootEventController();
		for(ZootEventType eventType : ZootEventType.values())
		{
			assertTrue(eventType + " event should be processed", ctrl.handleZootEvent(ZootEvents.get(eventType)));	
		}	
	}
	
	@Test
	public void handleZootEventShouldProcessOnlyOneEventOnSingleExecutionTest()
	{
		OnZootEventController ctrl = new OnZootEventController();
		ctrl.setSingleExecution(true);
		
		assertTrue("First event should be processed", ctrl.handleZootEvent(ZootEvents.get(ZootEventType.Attack)));
		assertFalse("Second event should not be processed", ctrl.handleZootEvent(ZootEvents.get(ZootEventType.Attack)));
		assertTrue(ctrl.isDone());
	}
	
	@Test
	public void handleZootEventShouldNotProcessNotIncludedEventTypesTest()
	{
		OnZootEventController ctrl = new OnZootEventController(Arrays.asList(ZootEventType.Attack), false);
		
		assertFalse(ctrl.handleZootEvent(ZootEvents.get(ZootEventType.None)));
		assertFalse(ctrl.handleZootEvent(ZootEvents.get(ZootEventType.Jump)));
		assertTrue(ctrl.handleZootEvent(ZootEvents.get(ZootEventType.Attack)));
		assertFalse(ctrl.isDone());
	}
	
	@Test
	public void onZootEventShouldReturnTrueByDefaultTest()
	{
		OnZootEventController ctrl = new OnZootEventController();
		assertTrue(ctrl.onZootEvent(null, null));
	}
	
}
