package com.zootcat.controllers.ai;

import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.controllers.ControllerPriority;
import com.zootcat.controllers.factory.CtrlDebug;
import com.zootcat.fsm.ZootState;
import com.zootcat.fsm.ZootStateMachine;
import com.zootcat.fsm.states.DeadState;
import com.zootcat.fsm.states.HurtState;
import com.zootcat.fsm.states.StunState;
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
import com.zootcat.fsm.states.ground.TurnState;
import com.zootcat.fsm.states.ground.WalkState;
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
	
	@Override
	public ControllerPriority getPriority() 
	{ 
		//high priority because other controllers can access or modify states
		return ControllerPriority.High; 
	}
	
	public ZootState getCurrentState()
	{
		return currentState;
	}
}