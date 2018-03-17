package com.zootcat.controllers.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Test;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.zootcat.actions.ZootKillActorAction;
import com.zootcat.scene.ZootActor;
import com.zootcat.testing.ZootActorEventCounterListener;

public class DieOnCollideControllerTest
{
	@Test
	public void shouldKillActorAfterTimerIsOutTest()
	{
		//given	
		ZootActor actorThatShouldDie = new ZootActor();
		ZootActor actorThatShouldLive = mock(ZootActor.class);
		ZootActorEventCounterListener eventCounter = new ZootActorEventCounterListener();		
		actorThatShouldDie.addListener(eventCounter);
				
		DieOnCollideController ctrl = new DieOnCollideController();
		ctrl.init(actorThatShouldDie);
				
		//when
		ctrl.onEnter(actorThatShouldDie, actorThatShouldLive, mock(Contact.class));
		
		//then
		assertEquals(1, actorThatShouldDie.getActions().size);
		assertTrue(ClassReflection.isInstance(ZootKillActorAction.class, actorThatShouldDie.getActions().get(0)));		
		verifyZeroInteractions(actorThatShouldLive);
	}
	
	@Test
	public void onLeaveTest()
	{
		//given
		ZootActor actorThatShouldDie = mock(ZootActor.class);
		ZootActor actorThatShouldLive = mock(ZootActor.class);
		Contact contact = mock(Contact.class);
				
		DieOnCollideController ctrl = new DieOnCollideController();
		ctrl.init(actorThatShouldDie);
		
		//when
		ctrl.onLeave(actorThatShouldDie, actorThatShouldLive, contact);
		
		//then
		verifyZeroInteractions(actorThatShouldDie, actorThatShouldLive, contact);
	}
}
