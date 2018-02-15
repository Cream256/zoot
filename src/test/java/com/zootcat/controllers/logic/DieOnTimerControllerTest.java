package com.zootcat.controllers.logic;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction;
import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.events.ActorEventCounterListener;
import com.zootcat.scene.ZootActor;

public class DieOnTimerControllerTest
{
	@Test
	public void shouldKillActorAfterTimerIsOutTest()
	{
		//given
		ActorEventCounterListener eventCounter = new ActorEventCounterListener();
		ZootActor actor = new ZootActor();
		actor.addListener(eventCounter);
		DieOnTimerController ctrl = new DieOnTimerController();
		ControllerAnnotations.setControllerParameter(ctrl, "interval", 1.0f);
		
		//when
		ctrl.onUpdate(1.0f, actor);
		
		//then	
		assertEquals("Dead event should be send", 1, eventCounter.getCount());
		assertEquals("Remove actor action should be present", 1, actor.getActions().size);
		assertEquals(RemoveActorAction.class, actor.getActions().get(0).getClass());
	}
}
