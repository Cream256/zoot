package com.zootcat.fsm.states.ground;

import com.zootcat.controllers.logic.ClimbSensorController;
import com.zootcat.controllers.logic.DirectionController;
import com.zootcat.controllers.physics.WalkableController;
import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.states.BasicState;
import com.zootcat.fsm.states.HurtState;
import com.zootcat.fsm.states.StunState;
import com.zootcat.fsm.states.ZootStateUtils;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;

public class JumpState extends BasicState
{
	public static final int ID = JumpState.class.hashCode();
	public static final String NAME = "Jump";
			
	public JumpState(String name)
	{
		super(name);
	}
	
	public JumpState()
	{
		super(NAME);
	}
	
	@Override
	public void onEnter(ZootActor actor, ZootEvent event)
	{		
		super.setAnimationBasedOnStateName(actor);
		actor.controllersAction(WalkableController.class, ctrl -> ctrl.jumpUp());
		actor.controllersAction(ClimbSensorController.class, ctrl -> ctrl.setSensorPosition(ZootDirection.Up));
	}
	
	@Override
	public void onUpdate(ZootActor actor, float delta)
	{
		//noop
	}

	@Override
	public boolean handle(ZootEvent event)
	{
		if(event.getType() == ZootEventType.Fall)
		{
			changeState(event, FallState.ID);
		}		
		else if(event.getType() == ZootEventType.Ground)
		{
			boolean falling = event.getTargetZootActor().controllersAllMatch(PhysicsBodyController.class, ctrl -> ctrl.getVelocity().y <= 0.0f);
			if(falling)	changeState(event, IdleState.ID);
		}
		else if(ZootStateUtils.isMoveEvent(event))
		{
			ZootDirection dir = ZootStateUtils.getDirectionFromEvent(event);
			event.getTargetZootActor().controllersAction(WalkableController.class, ctrl -> ctrl.moveInAir(dir));
			event.getTargetZootActor().controllersAction(DirectionController.class, ctrl -> ctrl.setDirection(dir));
			event.getTargetZootActor().controllersAction(ClimbSensorController.class, ctrl -> ctrl.setSensorPosition(dir));
		}
		else if(event.getType() == ZootEventType.Hurt && ZootStateUtils.canHurtActor(event))
		{
			changeState(event, HurtState.ID);
		}
		else if(event.getType() == ZootEventType.Grab || event.getType() == ZootEventType.GrabSide)
		{
			changeState(event, ClimbState.ID);
		}
		else if(event.getType() == ZootEventType.Stun)
		{
			changeState(event, StunState.ID);
		}
		
		return true;
	}
	
	@Override
	public int getId()
	{
		return ID;
	}
}
