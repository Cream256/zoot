package com.zootcat.controllers.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction;
import com.zootcat.fsm.events.ZootActorEventCounterListener;
import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.events.ZootEvents;
import com.zootcat.scene.ZootActor;

public class DieOnZootEventControllerTest
{
	private ZootActor actor;
	private DieOnZootEventController ctrl;
	private ZootEvent eventToDieOn;
	private ZootActorEventCounterListener eventCounter;
	
	@Before
	public void setup()
	{
		actor = new ZootActor();
		eventCounter = new ZootActorEventCounterListener();
		actor.addListener(eventCounter);
		
		ctrl = new DieOnZootEventController(Arrays.asList(ZootEventType.JumpUp), true);
		eventToDieOn = ZootEvents.get(ZootEventType.JumpUp);
		eventToDieOn.setTarget(actor);
	}
	
	@Test
	public void shouldSendDeadEventToActor()
	{		
		assertTrue(ctrl.handle(eventToDieOn));
		assertEquals(1, eventCounter.getCount());
		assertEquals(ZootEventType.Dead, eventCounter.getLastZootEvent().getType());
	}
	
	@Test
	public void shouldRemoveActor()
	{
		assertTrue(ctrl.handle(eventToDieOn));
		assertTrue(actor.hasActions());
		assertEquals(RemoveActorAction.class, actor.getActions().get(0).getClass());
	}	
}
