package com.zootcat.controllers.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction;
import com.zootcat.events.ZootActorEventCounterListener;
import com.zootcat.events.ZootEvent;
import com.zootcat.events.ZootEventType;
import com.zootcat.events.ZootEvents;
import com.zootcat.scene.ZootActor;

public class DieOnHurtControllerTest
{
	private DieOnHurtController ctrl;
	
	@Before
	public void setup()
	{
		ctrl = new DieOnHurtController();
	}
	
	@Test
	public void shouldKillActorOnHurtEvent()
	{
		//given
		ZootActorEventCounterListener eventCounter = new ZootActorEventCounterListener();
		ZootActor actor = new ZootActor();
		actor.addListener(eventCounter);
		
		//when
		ZootEvent hurtEvent = ZootEvents.get(ZootEventType.Hurt);
		hurtEvent.setTarget(actor);
		ctrl.handleZootEvent(hurtEvent);
		
		//then	
		assertEquals("Event should be send", 1, eventCounter.getCount());
		assertEquals("Event should be dead event", ZootEventType.Dead, eventCounter.getLastZootEvent().getType());
		assertEquals("Remove actor action should be present", 1, actor.getActions().size);
		assertEquals(RemoveActorAction.class, actor.getActions().get(0).getClass());
	}
	
	@Test
	public void shouldReturnTrueOnHurtEvent()
	{
		ZootEvent hurtEvent = ZootEvents.get(ZootEventType.Hurt);
		hurtEvent.setTarget(new ZootActor());
		
		assertTrue(ctrl.handleZootEvent(hurtEvent));
	}
	
	@Test
	public void shouldReturnFalseOnNotHurtEvent()
	{
		Arrays.stream(ZootEventType.values())
		.filter(type -> type != ZootEventType.Hurt)
		.forEach(type -> 
		{
			assertFalse(ctrl.handleZootEvent(ZootEvents.get(type)));			
		});
	}
	
}
