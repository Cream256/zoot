package com.zootcat.controllers.ai;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.zootcat.fsm.ZootState;
import com.zootcat.fsm.ZootStateMachine;
import com.zootcat.fsm.states.AttackState;
import com.zootcat.fsm.states.ClimbState;
import com.zootcat.fsm.states.CrouchState;
import com.zootcat.fsm.states.DeadState;
import com.zootcat.fsm.states.DownState;
import com.zootcat.fsm.states.FallForwardState;
import com.zootcat.fsm.states.FallState;
import com.zootcat.fsm.states.FlyState;
import com.zootcat.fsm.states.HurtState;
import com.zootcat.fsm.states.IdleState;
import com.zootcat.fsm.states.JumpForwardState;
import com.zootcat.fsm.states.JumpState;
import com.zootcat.fsm.states.RunState;
import com.zootcat.fsm.states.TurnState;
import com.zootcat.fsm.states.WalkState;
import com.zootcat.scene.ZootActor;

public class DefaultStateMachineControllerTest
{
	private ZootActor actor;
	private DefaultStateMachineController ctrl;
	
	@Before
	public void setup()
	{
		actor = new ZootActor();
		ctrl = new DefaultStateMachineController();
	}
	
	@Test
	public void shouldHaveValidNumberOfStates()
	{
		ctrl.init(actor);
		assertEquals(15, actor.getStateMachine().getStates().size());
	}
		
	@Test
	public void shouldHaveValidStates()
	{
		ctrl.init(actor);
		assertTrue("Should have Idle State", containsState(actor.getStateMachine(), IdleState.class));
		assertTrue("Should have Walk State", containsState(actor.getStateMachine(), WalkState.class));
		assertTrue("Should have Jump State", containsState(actor.getStateMachine(), JumpState.class));
		assertTrue("Should have Jump Forward State", containsState(actor.getStateMachine(), JumpForwardState.class));
		assertTrue("Should have Fall State", containsState(actor.getStateMachine(), FallState.class));
		assertTrue("Should have Fall Forward State", containsState(actor.getStateMachine(), FallForwardState.class));
		assertTrue("Should have Turn State", containsState(actor.getStateMachine(), TurnState.class));
		assertTrue("Should have Run State", containsState(actor.getStateMachine(), RunState.class));
		assertTrue("Should have Attack State", containsState(actor.getStateMachine(), AttackState.class));
		assertTrue("Should have Hurt State", containsState(actor.getStateMachine(), HurtState.class));
		assertTrue("Should have Dead State", containsState(actor.getStateMachine(), DeadState.class));
		assertTrue("Should have Down State", containsState(actor.getStateMachine(), DownState.class));
		assertTrue("Should have Crouch State", containsState(actor.getStateMachine(), CrouchState.class));
		assertTrue("Should have Climb State", containsState(actor.getStateMachine(), ClimbState.class));
		assertTrue("Should have Fly State", containsState(actor.getStateMachine(), FlyState.class));
	}
		
	private boolean containsState(ZootStateMachine sm, Class<? extends ZootState> clazz)
	{
		ZootState foundState = sm.getStates().stream().filter(state -> state.getClass().equals(clazz)).findAny().orElse(null);		
		return foundState != null;
	}
}
