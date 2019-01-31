package com.zootcat.controllers.logic.triggers;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.fsm.events.ZootActorEventCounterListener;
import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.events.ZootEvents;
import com.zootcat.scene.ZootActor;

public class TriggerOnEventControllerTest
{
	private ZootActor ctrlActor;
	private ZootActorEventCounterListener eventCounter;
	private TriggerOnEventController triggerCtrl;
		
	@Before
	public void setup()
	{
		eventCounter = new ZootActorEventCounterListener();
		ctrlActor = new ZootActor();
		ctrlActor.addListener(eventCounter);
		triggerCtrl = new TriggerOnEventController(Arrays.asList(ZootEventType.Attack), false);
	}
	
	@Test
	public void shouldPerformFirstTriggerOnFalseByDefault()
	{		
		//when
		triggerCtrl.onAdd(ctrlActor);
		triggerCtrl.onUpdate(0.0f, ctrlActor);
		
		//then
		assertEquals("Should send event", 1, eventCounter.getCount());
		assertEquals("Should be triggered as not active", ZootEventType.TriggerOff, ((ZootEvent)eventCounter.getLastEvent()).getType());
	}
	
	@Test
	public void shouldPerformFirstTriggerBasedOnParameterValue()
	{
		//given
		ControllerAnnotations.setControllerParameter(triggerCtrl, "active", true);
		
		//when
		triggerCtrl.onAdd(ctrlActor);
		triggerCtrl.onUpdate(0.0f, ctrlActor);
		
		//then
		assertEquals("Should send event", 1, eventCounter.getCount());
		assertEquals("Should be triggered as active", ZootEventType.TriggerOn, ((ZootEvent)eventCounter.getLastEvent()).getType());
	}
	
	@Test
	public void shouldInitializeOnlyOnce()
	{		
		//when
		triggerCtrl.onAdd(ctrlActor);
		triggerCtrl.onUpdate(0.0f, ctrlActor);
		triggerCtrl.onUpdate(0.0f, ctrlActor);
		triggerCtrl.onUpdate(0.0f, ctrlActor);
		
		//then
		assertEquals("Should send event", 1, eventCounter.getCount());
		assertEquals("Should be triggered as not active", ZootEventType.TriggerOff, ((ZootEvent)eventCounter.getLastEvent()).getType());
	}
	
	@Test
	public void shouldTriggerOnEvent()
	{
		//given
		triggerCtrl.onAdd(ctrlActor);
		
		//when
		triggerCtrl.handle(ZootEvents.get(ZootEventType.Attack));
		
		//then
		assertEquals("Should send event", 1, eventCounter.getCount());
		assertEquals("Should be triggered as active", ZootEventType.TriggerOn, ((ZootEvent)eventCounter.getLastEvent()).getType());
		
		//when
		triggerCtrl.handle(ZootEvents.get(ZootEventType.Attack));
		
		//then
		assertEquals("Should send event", 2, eventCounter.getCount());
		assertEquals("Should be triggered as not active", ZootEventType.TriggerOff, ((ZootEvent)eventCounter.getLastEvent()).getType());
	}
	
}
