package com.zootcat.controllers.ai;

import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.fsm.ZootStateMachine;
import com.zootcat.fsm.states.ForwardingState;
import com.zootcat.fsm.states.PatrolState;
import com.zootcat.fsm.states.ground.IdleState;
import com.zootcat.scene.ZootActor;

public class PatrolStateMachineController extends DefaultStateMachineController
{
	@CtrlParam(required = true) private float patrolRange = 0.0f;
		
	@Override
	public void onAdd(ZootActor actor)
	{
		PhysicsBodyController physicsCtrl = actor.getController(PhysicsBodyController.class);
		
		PatrolState patrolState = new PatrolState();
		patrolState.setPatrolRange(patrolRange);
		patrolState.setStartX(physicsCtrl.getCenterPositionRef().x);
		
		ZootStateMachine sm = actor.getStateMachine();	
		sm.addState(patrolState);
		sm.init(new ForwardingState(IdleState.ID, PatrolState.ID));
	}
}
