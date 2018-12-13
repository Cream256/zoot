package com.zootcat.controllers.ai;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.zootcat.fsm.ZootState;
import com.zootcat.fsm.ZootStateMachine;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.events.ZootEvents;
import com.zootcat.fsm.states.DeadState;
import com.zootcat.fsm.states.HurtState;
import com.zootcat.fsm.states.flying.FlyState;
import com.zootcat.fsm.states.ground.AttackState;
import com.zootcat.fsm.states.ground.ClimbState;
import com.zootcat.fsm.states.ground.CrouchState;
import com.zootcat.fsm.states.ground.DownState;
import com.zootcat.fsm.states.ground.FallForwardState;
import com.zootcat.fsm.states.ground.FallState;
import com.zootcat.fsm.states.ground.IdleState;
import com.zootcat.fsm.states.ground.JumpForwardState;
import com.zootcat.fsm.states.ground.JumpState;
import com.zootcat.fsm.states.ground.RunState;
import com.zootcat.fsm.states.ground.StunState;
import com.zootcat.fsm.states.ground.TurnState;
import com.zootcat.fsm.states.ground.WalkState;
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
		assertEquals(16, actor.getStateMachine().getStates().size());
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
		assertTrue("Should have Stun State", containsState(actor.getStateMachine(), StunState.class));
	}
		
	private boolean containsState(ZootStateMachine sm, Class<? extends ZootState> clazz)
	{
		ZootState foundState = sm.getStates().stream().filter(state -> state.getClass().equals(clazz)).findAny().orElse(null);		
		return foundState != null;
	}
	
	@Test
	public void shouldInitializeWithIdleState()
	{
		//when
		ctrl.init(actor);
		
		//then
		assertEquals(IdleState.ID, ctrl.getCurrentState().getId());
	}
	
	@Test
	public void shouldUpdateCurrentState()
	{
		//when
		ctrl.init(actor);
		ZootEvents.fireAndFree(actor, ZootEventType.WalkRight);
		ctrl.onUpdate(1.0f, actor);
		
		//then
		assertEquals(WalkState.ID, ctrl.getCurrentState().getId());		
	}
}
