package com.zootcat.controllers.logic;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.zootcat.actions.ZootKillActorAction;
import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.fsm.events.ZootActorEventCounterListener;
import com.zootcat.scene.ZootActor;

public class DieOnTimerControllerTest
{
	@Test
	public void shouldKillActorAfterTimerIsOutTest()
	{
		//given
		ZootActorEventCounterListener eventCounter = new ZootActorEventCounterListener();
		ZootActor actor = new ZootActor();
		actor.addListener(eventCounter);
		DieOnTimerController ctrl = new DieOnTimerController();
		ControllerAnnotations.setControllerParameter(ctrl, "interval", 1.0f);
		
		//when
		ctrl.onUpdate(1.0f, actor);
		
		//then	
		assertEquals("Remove actor action should be present", 1, actor.getActions().size);
		assertEquals(ZootKillActorAction.class, actor.getActions().get(0).getClass());
		
		ZootKillActorAction killActorAction = (ZootKillActorAction) actor.getActions().get(0);
		assertEquals(actor, killActorAction.getTargetZootActor());
	}
}
