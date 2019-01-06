package com.zootcat.controllers.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.zootcat.actions.ZootKillActorAction;
import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.events.ZootEvents;
import com.zootcat.scene.ZootActor;

public class DieOnZootEventControllerTest
{
	private ZootActor actor;	
	private ZootEvent eventToDieOn;
	private DieOnZootEventController ctrl;
	
	@Before
	public void setup()
	{
		actor = new ZootActor();
		
		ctrl = new DieOnZootEventController(Arrays.asList(ZootEventType.JumpUp), true);
		eventToDieOn = ZootEvents.get(ZootEventType.JumpUp);
		eventToDieOn.setTarget(actor);
	}
		
	@Test
	public void shouldReturnTrueOnHandle()
	{
		assertTrue(ctrl.handle(eventToDieOn));
	}
	
	@Test
	public void shouldAddKillActorAction()
	{		
		ctrl.handle(eventToDieOn);
		
		assertTrue(actor.hasActions());
		assertEquals(ZootKillActorAction.class, actor.getActions().get(0).getClass());
	}
}
