package com.zootcat.controllers.ai;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.zootcat.fsm.ZootState;
import com.zootcat.fsm.ZootStateMachine;
import com.zootcat.fsm.states.AttackState;
import com.zootcat.fsm.states.DeadState;
import com.zootcat.fsm.states.FlyState;
import com.zootcat.fsm.states.HurtState;
import com.zootcat.fsm.states.IdleState;
import com.zootcat.fsm.states.TurnState;
import com.zootcat.scene.ZootActor;

public class FlyingStateMachineControllerTest
{
	private ZootActor actor;
	private FlyingStateMachineController ctrl;
	
	@Before
	public void setup()
	{
		actor = new ZootActor();
		ctrl = new FlyingStateMachineController();
	}
	
	@Test
	public void shouldHaveValidNumberOfStates()
	{
		ctrl.init(actor);
		assertEquals(6, actor.getStateMachine().getStates().size());
	}
		
	@Test
	public void shouldHaveValidStates()
	{
		ctrl.init(actor);
		assertTrue("Should have Fly State", containsState(actor.getStateMachine(), FlyState.class));
		assertTrue("Should have Idle State", containsState(actor.getStateMachine(), IdleState.class));
		assertTrue("Should have Turn State", containsState(actor.getStateMachine(), TurnState.class));
		assertTrue("Should have Attack State", containsState(actor.getStateMachine(), AttackState.class));
		assertTrue("Should have Hurt State", containsState(actor.getStateMachine(), HurtState.class));
		assertTrue("Should have Dead State", containsState(actor.getStateMachine(), DeadState.class));		
	}
	
	private boolean containsState(ZootStateMachine sm, Class<? extends ZootState> clazz)
	{
		ZootState foundState = sm.getStates().stream().filter(state -> state.getClass().equals(clazz)).findAny().orElse(null);		
		return foundState != null;
	}
}
