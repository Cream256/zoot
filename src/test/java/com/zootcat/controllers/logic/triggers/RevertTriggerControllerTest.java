package com.zootcat.controllers.logic.triggers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.testing.ZootActorStub;

public class RevertTriggerControllerTest
{
	private static final float REVERT_AFTER = 2.0f;
		
	@Mock private TriggerController triggerCtrl;
	@Mock private TriggerOnEventController triggerOnEventCtrl;
	private ZootActorStub ctrlActor;
	private RevertTriggerController revertTriggerCtrl;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		
		ctrlActor = new ZootActorStub();
		ctrlActor.addController(triggerCtrl);
		ctrlActor.addController(triggerOnEventCtrl);
		
		revertTriggerCtrl = new RevertTriggerController();
		ControllerAnnotations.setControllerParameter(revertTriggerCtrl, "revertAfter", REVERT_AFTER);
		revertTriggerCtrl.init(ctrlActor);
		revertTriggerCtrl.onAdd(ctrlActor);
	}
	
	@Test
	public void shouldNotBeCountingDownFromStart()
	{
		assertFalse(revertTriggerCtrl.isCountingDown());
		assertEquals(REVERT_AFTER, revertTriggerCtrl.getTimeLeft(), 0.0f);
	}
	
	@Test
	public void shouldStartCountingWhenTriggeringOn()
	{
		revertTriggerCtrl.triggerOn(ctrlActor);
		assertTrue(revertTriggerCtrl.isCountingDown());
	}
	
	@Test
	public void shouldReset()
	{
		//given
		revertTriggerCtrl.triggerOn(ctrlActor);
		revertTriggerCtrl.onUpdate(REVERT_AFTER, ctrlActor);
		
		//when
		revertTriggerCtrl.reset();
		
		//then
		assertFalse(revertTriggerCtrl.isCountingDown());
		assertEquals(REVERT_AFTER, revertTriggerCtrl.getTimeLeft(), 0.0f);
	}
	
	@Test
	public void shouldResetOnTriggerOff()
	{
		//given
		revertTriggerCtrl.triggerOn(ctrlActor);
		revertTriggerCtrl.onUpdate(REVERT_AFTER, ctrlActor);
		
		//when
		revertTriggerCtrl.triggerOff(ctrlActor);
		
		//then
		assertFalse(revertTriggerCtrl.isCountingDown());
		assertEquals(REVERT_AFTER, revertTriggerCtrl.getTimeLeft(), 0.0f);		
	}
	
	@Test
	public void shouldRevertTriggerAfterTimeHasPassed()
	{
		//given
		revertTriggerCtrl.triggerOn(ctrlActor);
		
		//when
		revertTriggerCtrl.onUpdate(0.0f, ctrlActor);
		
		//then
		verify(triggerCtrl, never()).setActive(false);
		verify(triggerOnEventCtrl, never()).setActive(false);
				
		//when
		revertTriggerCtrl.onUpdate(REVERT_AFTER / 2.0f, ctrlActor);
		
		//then
		verify(triggerCtrl, never()).setActive(false);
		verify(triggerOnEventCtrl, never()).setActive(false);
		
		//when
		revertTriggerCtrl.onUpdate(REVERT_AFTER / 2.0f, ctrlActor);
		
		//then
		verify(triggerCtrl).setActive(false);
		verify(triggerOnEventCtrl).setActive(false);
	}
	
	@Test
	public void shouldNotTriggerRevertIfTriggerWasNotSetToOnFirst()
	{
		//when
		revertTriggerCtrl.onUpdate(REVERT_AFTER, ctrlActor);
		
		//then
		verify(triggerCtrl, never()).setActive(false);
		verify(triggerOnEventCtrl, never()).setActive(false);	
	}
}
