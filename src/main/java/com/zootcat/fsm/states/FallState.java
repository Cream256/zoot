package com.zootcat.fsm.states;

import java.util.Arrays;
import java.util.List;

import com.zootcat.controllers.logic.DirectionController;
import com.zootcat.controllers.physics.MoveableController;
import com.zootcat.events.ZootEvent;
import com.zootcat.events.ZootEventType;
import com.zootcat.fsm.ZootState;
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
			event.getTargetZootActor().controllerAction(MoveableController.class, (ctrl) -> ctrl.moveInAir(dir));
			event.getTargetZootActor().controllerAction(DirectionController.class, (ctrl) -> ctrl.setDirection(dir));
		}
		else if(event.getType() == ZootEventType.Hurt)
		{
			changeState(event, HurtState.ID);
		}
		else if(event.getType() == ZootEventType.Grab)
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
