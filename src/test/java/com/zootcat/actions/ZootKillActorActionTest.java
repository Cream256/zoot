package com.zootcat.actions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.zootcat.fsm.events.ZootActorEventCounterListener;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.scene.ZootActor;

public class ZootKillActorActionTest
{
	private ZootActor actor;
	private ZootKillActorAction action;
	private ZootActorEventCounterListener eventCounter;
		
	@Before
	public void setup()
	{
		eventCounter = new ZootActorEventCounterListener();
		
		actor = new ZootActor();
		actor.addListener(eventCounter);
		
		action = new ZootKillActorAction();		
		action.setTarget(actor);
	}
	
	@Test
	public void shouldAlwaysReturnTrueOnAct()
	{
		assertTrue(action.act(1.0f));
	}
	
	@Test
	public void shouldSendDeadEvent()
	{	
		action.act(1.0f);
		
		assertEquals(1, eventCounter.getCount());
		assertEquals(ZootEventType.Dead, eventCounter.getLastZootEvent().getType());
	}
	
	@Test
	public void shouldSendDeadEventOnlyOnce()
	{		
		action.act(1.0f);
		action.act(1.0f);
		action.act(1.0f);
		
		assertEquals(1, eventCounter.getCount());
		assertEquals(ZootEventType.Dead, eventCounter.getLastZootEvent().getType());
	}
	
	@Test
	public void shouldAddRemoveActorAction()
	{
		action.act(1.0f);
		
		assertEquals(1, actor.getActions().size);
		assertTrue(ClassReflection.isInstance(RemoveActorAction.class, actor.getActions().get(0)));
	}
	
	@Test
	public void shouldBeAbleToKillAgainAfterRestart()
	{
		action.act(1.0f);
		action.restart();
		action.act(1.0f);
		
		assertEquals(2, eventCounter.getCount());
		assertEquals(ZootEventType.Dead, eventCounter.getLastZootEvent().getType());
	}
}
