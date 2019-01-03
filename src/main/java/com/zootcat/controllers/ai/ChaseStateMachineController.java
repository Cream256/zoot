package com.zootcat.controllers.ai;

import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.fsm.ZootStateMachine;
import com.zootcat.fsm.states.ForwardingState;
import com.zootcat.fsm.states.ground.IdleState;
import com.zootcat.fsm.states.ground.PatrolAndChaseState;
import com.zootcat.scene.ZootActor;

public class ChaseStateMachineController extends DefaultStateMachineController
{
	@CtrlParam(required = true) private int patrolRange;
	@CtrlParam(required = true) private int lookRange;
	@CtrlParam(required = true) private String chasedType;	
	
	@Override
	public void onAdd(ZootActor actor)
	{
		PhysicsBodyController physicsCtrl = actor.getSingleController(PhysicsBodyController.class);
		
		PatrolAndChaseState patrolAndChaseState = new PatrolAndChaseState();
		patrolAndChaseState.setPatrolRange(patrolRange);
		patrolAndChaseState.setLookRange(lookRange);
		patrolAndChaseState.setChasedActorType(chasedType);
		patrolAndChaseState.setStartX(physicsCtrl.getCenterPositionRef().x);
				
		ZootStateMachine sm = actor.getStateMachine();				
		sm.addState(patrolAndChaseState);
		sm.init(new ForwardingState(IdleState.ID, patrolAndChaseState.getId()));		
	}
}
