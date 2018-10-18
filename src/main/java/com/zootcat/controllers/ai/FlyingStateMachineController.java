package com.zootcat.controllers.ai;

import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.fsm.ZootStateMachine;
import com.zootcat.fsm.states.AttackState;
import com.zootcat.fsm.states.DeadState;
import com.zootcat.fsm.states.FlyState;
import com.zootcat.fsm.states.HurtState;
import com.zootcat.fsm.states.IdleState;
import com.zootcat.fsm.states.TurnState;
import com.zootcat.scene.ZootActor;

public class FlyingStateMachineController extends ControllerAdapter
{
	@Override
	public void init(ZootActor actor)
	{
		ZootStateMachine sm = actor.getStateMachine();
		sm.init(new IdleState());
		sm.addState(new FlyState());
		sm.addState(new TurnState());
		sm.addState(new AttackState());
		sm.addState(new HurtState());
		sm.addState(new DeadState());
	}
}