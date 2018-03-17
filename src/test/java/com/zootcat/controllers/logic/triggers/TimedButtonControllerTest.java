package com.zootcat.controllers.logic.triggers;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.physics.box2d.Contact;
import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.scene.ZootActor;
import com.zootcat.testing.ZootTriggerEventCounterListener;

public class TimedButtonControllerTest
{
	private static final float REVERT_AFTER = 2.0f;
	
	private ZootActor ctrlActor;
	private TimedButtonController ctrl;
	private ZootTriggerEventCounterListener triggerCounter;
	@Mock private ZootActor otherActor;
	@Mock private Contact contact;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		
		triggerCounter = new ZootTriggerEventCounterListener();		
		ctrlActor = new ZootActor();
		ctrlActor.addListener(triggerCounter);
		
		ctrl = new TimedButtonController();		
		ControllerAnnotations.setControllerParameter(ctrl, "revertAfter", REVERT_AFTER);
		ctrl.init(ctrlActor);
	}
	
	@Test
	public void shouldEmitTriggerOnEventOnPress()
	{
		ctrl.onEnter(ctrlActor, otherActor, contact);
		assertEquals(1, triggerCounter.onCount);
	}
	
	@Test
	public void shouldNotEmitTriggerOffEventOnUnpress()
	{
		ctrl.onEnter(ctrlActor, otherActor, contact);
		ctrl.onLeave(ctrlActor, otherActor, contact);
		
		assertEquals(0, triggerCounter.offCount);
	}
	
	@Test
	public void shouldEmitTriggerOffEventAfterRevertTimeHasPassed()
	{
		//initial state
		ctrl.onUpdate(0.0f, ctrlActor);
		triggerCounter.offCount = 0;
		
		//when
		ctrl.onEnter(ctrlActor, otherActor, contact);
		ctrl.onLeave(ctrlActor, otherActor, contact);		
		ctrl.onUpdate(0.0f, ctrlActor);		
		
		//then
		assertEquals(0, triggerCounter.offCount); 
		
		//when
		ctrl.onUpdate(REVERT_AFTER / 2.0f, ctrlActor);
		
		//then
		assertEquals(0, triggerCounter.offCount);
		
		//when
		ctrl.onUpdate(REVERT_AFTER / 2.0f, ctrlActor);
		
		//then
		assertEquals(1, triggerCounter.offCount);		
	}
}
