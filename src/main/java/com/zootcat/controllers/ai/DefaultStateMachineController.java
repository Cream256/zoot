package com.zootcat.controllers.ai;

import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.fsm.ZootStateMachine;
import com.zootcat.fsm.states.AttackState;
import com.zootcat.fsm.states.CrouchState;
import com.zootcat.fsm.states.DeadState;
import com.zootcat.fsm.states.DownState;
import com.zootcat.fsm.states.FallState;
import com.zootcat.fsm.states.HurtState;
import com.zootcat.fsm.states.IdleState;
import com.zootcat.fsm.states.JumpState;
import com.zootcat.fsm.states.RunState;
import com.zootcat.fsm.states.TurnState;
import com.zootcat.fsm.states.WalkState;
import com.zootcat.scene.ZootActor;

public class DefaultStateMachineController extends ControllerAdapter
{
	@Override
	public void init(ZootActor actor)
	{
		ZootStateMachine sm = actor.getStateMachine();
		sm.init(new IdleState());
		sm.addState(new WalkState());
		sm.addState(new JumpState());
		sm.addState(new FallState());
		sm.addState(new TurnState());
		sm.addState(new RunState());
		sm.addState(new AttackState());
		sm.addState(new HurtState());
		sm.addState(new DeadState());
		sm.addState(new DownState());
		sm.addState(new CrouchState());
	}
}