package com.zootcat.controllers.ai;

import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.controllers.factory.CtrlDebug;
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
import com.zootcat.fsm.states.StunState;
import com.zootcat.fsm.states.TurnState;
import com.zootcat.fsm.states.WalkState;
import com.zootcat.scene.ZootActor;

public class DefaultStateMachineController extends ControllerAdapter
{
	@CtrlDebug private ZootState currentState = null;
	
	@Override
	public void init(ZootActor actor)
	{
		ZootStateMachine sm = actor.getStateMachine();
		sm.init(new IdleState());
		sm.addState(new WalkState());
		sm.addState(new JumpState());
		sm.addState(new JumpForwardState());
		sm.addState(new FallState());
		sm.addState(new FallForwardState());
		sm.addState(new TurnState());
		sm.addState(new RunState());
		sm.addState(new FlyState());
		sm.addState(new AttackState());
		sm.addState(new HurtState());
		sm.addState(new DeadState());
		sm.addState(new DownState());
		sm.addState(new CrouchState());
		sm.addState(new ClimbState());
		sm.addState(new StunState());
		
		currentState = actor.getStateMachine().getCurrentState();
	}
	
	@Override
	public void onUpdate(float delta, ZootActor actor)
	{
		currentState = actor.getStateMachine().getCurrentState();
	}
	
	public ZootState getCurrentState()
	{
		return currentState;
	}
}