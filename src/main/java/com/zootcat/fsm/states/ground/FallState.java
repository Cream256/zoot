package com.zootcat.fsm.states.ground;

import java.util.Arrays;
import java.util.List;

import com.zootcat.controllers.logic.ClimbController;
import com.zootcat.controllers.logic.DirectionController;
import com.zootcat.controllers.physics.WalkableController;
import com.zootcat.fsm.ZootState;
import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.states.BasicState;
import com.zootcat.fsm.states.HurtState;
import com.zootcat.fsm.states.ZootStateUtils;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;

public class FallState extends BasicState
{
	public static final int ID = FallState.class.hashCode();

	private static final List<Integer> NOT_ALLOWED_TO_DELAY_JUMP_IDS = Arrays.asList(0, JumpState.ID, JumpForwardState.ID);
	private float allowedJumpDelay;
	private float currentJumpDelay;
	
	public FallState(String name)
	{
		super(name);
	}
	
	public FallState()
	{
		super("Fall");
	}

	@Override
	public void onEnter(ZootActor actor, ZootEvent event)
	{
		ZootState previousState = actor.getStateMachine().getPreviousState();
		int previousStateId = previousState != null ? previousState.getId() : 0;
		currentJumpDelay = NOT_ALLOWED_TO_DELAY_JUMP_IDS.contains(previousStateId) ? 0.0f : allowedJumpDelay; 
				
		setAnimationBasedOnStateName(actor);
	}
	
	@Override
	public void onUpdate(ZootActor actor, float delta)
	{
		currentJumpDelay = Math.max(0.0f, currentJumpDelay - delta);
	}
	
	@Override
	public boolean handle(ZootEvent event)
	{
		if(event.getType() == ZootEventType.Ground)
		{
			changeState(event, IdleState.ID);
		}
		else if(ZootStateUtils.isMoveEvent(event))
		{
			ZootDirection dir = ZootStateUtils.getDirectionFromEvent(event);
			event.getTargetZootActor().controllersAction(WalkableController.class, ctrl -> ctrl.moveInAir(dir));
			event.getTargetZootActor().controllersAction(DirectionController.class, ctrl -> ctrl.setDirection(dir));
			event.getTargetZootActor().controllersAction(ClimbController.class, ctrl -> ctrl.setSensorPosition(dir));
		}
		else if(event.getType() == ZootEventType.Hurt)
		{
			changeState(event, HurtState.ID);
		}
		else if(event.getType() == ZootEventType.Grab || event.getType() == ZootEventType.GrabSide)
		{
			changeState(event, ClimbState.ID);
		}
		else if(event.getType() == ZootEventType.JumpUp && currentJumpDelay > 0.0f)
		{
			changeState(event, JumpState.ID);
		}
		else if(event.getType() == ZootEventType.JumpForward && currentJumpDelay > 0.0f)
		{
			changeState(event, JumpForwardState.ID);
		}		
		return true;
	}
	
	@Override
	public int getId()
	{
		return ID;
	}
	
	public void setAllowedJumpDelay(float delay)
	{
		allowedJumpDelay = delay;
	}
}
