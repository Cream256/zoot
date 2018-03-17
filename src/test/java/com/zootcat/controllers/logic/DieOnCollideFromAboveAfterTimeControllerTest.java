package com.zootcat.controllers.logic;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction;
import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.controllers.logic.DieOnCollideFromAboveAfterTimeController;
import com.zootcat.events.ZootActorEventCounterListener;
import com.zootcat.scene.ZootActor;

public class DieOnCollideFromAboveAfterTimeControllerTest
{
	private static final float TIME = 5.0f;
		
	@Mock private ZootActor otherActor;
	private ZootActor ctrlActor;
	private DieOnCollideFromAboveAfterTimeController ctrl;
		
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		ctrlActor = new ZootActor();
		
		ctrl = new DieOnCollideFromAboveAfterTimeController();
		ControllerAnnotations.setControllerParameter(ctrl, "time", TIME);
	}
	
	@Test
	public void shouldProperlyReturnControllerActor()
	{
		ctrl.init(ctrlActor);
		assertEquals(ctrlActor, ctrl.getControllerActor());
	}
	
	@Test
	public void shouldKillActorAfterTimeHasPassed()
	{
		//given
		ZootActorEventCounterListener eventCounter = new ZootActorEventCounterListener();		
		ctrlActor.addListener(eventCounter);
		ctrl.init(ctrlActor);
		
		//when
		ctrl.onCollidedFromAbove(ctrlActor, otherActor, mock(Contact.class));
		ctrl.onUpdate(TIME, ctrlActor);
		
		//then
		assertEquals("Dead event should be send", 1, eventCounter.getCount());
		assertEquals("Remove actor action should be present", 1, ctrlActor.getActions().size);
		assertEquals(RemoveActorAction.class, ctrlActor.getActions().get(0).getClass());
		verifyZeroInteractions(otherActor);
		
		//when
		ctrl.onUpdate(1.0f, ctrlActor);
		
		//then
		assertEquals("Dead event should be send only once", 1, eventCounter.getCount());
		assertEquals("Remove actor action should be present only once", 1, ctrlActor.getActions().size);
		assertEquals(RemoveActorAction.class, ctrlActor.getActions().get(0).getClass());
		verifyZeroInteractions(otherActor);
	}
	
	@Test
	public void shouldNotKillActorBeforeTimeHasPassed()
	{
		//given
		ZootActorEventCounterListener eventCounter = new ZootActorEventCounterListener();		
		ctrlActor.addListener(eventCounter);
		ctrl.init(ctrlActor);
		
		//when
		ctrl.onCollidedFromAbove(ctrlActor, otherActor, mock(Contact.class));
		ctrl.onUpdate(TIME - 0.1f, ctrlActor);
		
		//then
		assertEquals("Dead event should not be send", 0, eventCounter.getCount());
		assertEquals("Remove actor action should not be present", 0, ctrlActor.getActions().size);
		verifyZeroInteractions(otherActor);
	}
}
