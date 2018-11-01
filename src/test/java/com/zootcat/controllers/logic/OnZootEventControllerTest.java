package com.zootcat.controllers.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.events.ZootEventTypeEnum;
import com.zootcat.fsm.events.ZootEvents;
import com.zootcat.scene.ZootActor;

public class OnZootEventControllerTest
{
	private OnZootEventController createDefaultController()
	{
		return new OnZootEventController()
		{			
			@Override
			public boolean onZootEvent(ZootActor actor, ZootEvent event)
			{
				return true;
			}
		};
	}
	
	@Test
	public void defaultCtorTest()
	{
		OnZootEventController ctrl = createDefaultController();
		assertFalse(ctrl.isDone());
		assertFalse(ctrl.isSingleExecution());
		assertEquals(ZootEventType.values().length, ctrl.getEventTypes().size());
	}
	
	@Test
	public void secondCtorTest()
	{
		OnZootEventController ctrl = new OnZootEventController(true) 
		{
			@Override
			public boolean onZootEvent(ZootActor actor, ZootEvent event)
			{
				//noop
				return false;
			}};
		assertFalse(ctrl.isDone());
		assertTrue(ctrl.isSingleExecution());
		assertEquals(ZootEventType.values().length, ctrl.getEventTypes().size());
	}
		
	@Test
	public void thirdCtorTest()
	{
		List<ZootEventTypeEnum> types = Arrays.asList(ZootEventType.Attack, ZootEventType.Collide);
		OnZootEventController ctrl = new OnZootEventController(types, true) 
		{
			@Override
			public boolean onZootEvent(ZootActor actor, ZootEvent event)
			{
				return true;
			}
		};
		assertFalse(ctrl.isDone());
		assertTrue(ctrl.isSingleExecution());
		assertEquals(types.size(), ctrl.getEventTypes().size());
		types.forEach(type -> assertTrue(ctrl.getEventTypes().contains(type)));
	}
	
	@Test
	public void setSingleExecutionTest()
	{
		OnZootEventController ctrl = createDefaultController();
		
		ctrl.setSingleExecution(true);
		assertTrue(ctrl.isSingleExecution());
		
		ctrl.setSingleExecution(false);
		assertFalse(ctrl.isSingleExecution());
	}
	
	@Test
	public void handleZootEventShouldProcessAllEventsTest()
	{
		OnZootEventController ctrl = createDefaultController();
		for(ZootEventType eventType : ZootEventType.values())
		{
			assertTrue(eventType + " event should be processed", ctrl.handleZootEvent(ZootEvents.get(eventType)));	
		}	
	}
	
	@Test
	public void handleZootEventShouldProcessOnlyOneEventOnSingleExecutionTest()
	{
		OnZootEventController ctrl = createDefaultController();
		ctrl.setSingleExecution(true);
		
		assertTrue("First event should be processed", ctrl.handleZootEvent(ZootEvents.get(ZootEventType.Attack)));
		assertFalse("Second event should not be processed", ctrl.handleZootEvent(ZootEvents.get(ZootEventType.Attack)));
		assertTrue(ctrl.isDone());
	}
	
	@Test
	public void handleZootEventShouldNotProcessNotIncludedEventTypesTest()
	{
		OnZootEventController ctrl = new OnZootEventController(Arrays.asList(ZootEventType.Attack), false)
		{
			@Override
			public boolean onZootEvent(ZootActor actor, ZootEvent event)
			{
				return true;
			}	
		};
		
		assertFalse(ctrl.handleZootEvent(ZootEvents.get(ZootEventType.None)));
		assertFalse(ctrl.handleZootEvent(ZootEvents.get(ZootEventType.JumpUp)));
		assertTrue(ctrl.handleZootEvent(ZootEvents.get(ZootEventType.Attack)));
		assertFalse(ctrl.isDone());
	}	
}
