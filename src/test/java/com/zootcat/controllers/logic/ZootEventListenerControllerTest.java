package com.zootcat.controllers.logic;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.zootcat.events.ZootEvent;
import com.zootcat.events.ZootEventType;
import com.zootcat.events.ZootEvents;
import com.zootcat.physics.ZootCollisionEvent;
import com.zootcat.scene.ZootActor;

public class ZootEventListenerControllerTest
{
	private ZootActor actor;
	private ZootEventListenerController ctrl;
	
	@Before
	public void setup()
	{
		actor = new ZootActor();
		ctrl = new ZootEventListenerController() 
		{
			@Override
			public boolean handleZootEvent(ZootEvent event)
			{
				return true;
			}
		};
	}
	
	@Test
	public void onAddShouldRegisterListenerTest()
	{
		//given
		assertFalse(actor.getListeners().contains(ctrl, true));		
		
		//when
		ctrl.onAdd(actor);
		
		//then
		assertTrue(actor.getListeners().contains(ctrl, true));
	}
	
	@Test
	public void onRemoveShouldDeregisterListenerTest()
	{
		//given
		actor.addListener(ctrl);
		assertTrue(actor.getListeners().contains(ctrl, true));
		
		//when
		ctrl.onRemove(actor);
		
		//then
		assertFalse(actor.getListeners().contains(ctrl, true));
	}
	
	@Test
	public void handleTest()
	{
		assertTrue("Should handle zoot event", ctrl.handle(ZootEvents.get(ZootEventType.Attack)));
		assertTrue("Should handle zoot event", ctrl.handle(ZootEvents.get(ZootEventType.Fall)));
		assertTrue("Should handle zoot event", ctrl.handle(ZootEvents.get(ZootEventType.None)));
		assertFalse("Should handle only zoot events", ctrl.handle(null));
		assertFalse("Should handle only zoot events", ctrl.handle(new Event()));
		assertFalse("Should handle only zoot events", ctrl.handle(new ZootCollisionEvent()));		
	}
	
}
