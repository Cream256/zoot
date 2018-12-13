package com.zootcat.fsm.states;

import com.zootcat.controllers.gfx.AnimatedSpriteController;
import com.zootcat.controllers.logic.DirectionController;
import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.states.ground.TurnState;
import com.zootcat.fsm.states.ground.WalkState;
import com.zootcat.gfx.ZootAnimation;
import com.zootcat.scene.ZootActor;

public class PatrolState extends WalkState
{	
	public static final float TURN_TIME_PADDING = 0.05f;
	private static final float DEFAULT_TURN_COOLDOWN = 1.0f;
	
	private float startX = 0.0f;
	private float patrolRange = 0.0f;
	private float turnCooldown = 0.0f;
			
	public PatrolState()
	{
		super("Patrol");
	}
	
	@Override
	public void onEnter(ZootActor actor, ZootEvent event)
	{		
		actor.controllerAction(DirectionController.class, ctrl ->
		{
			moveDirection = ctrl.getDirection();
		});
		setActorAnimation(actor, "Walk");
	}
		
	@Override
	public void onUpdate(ZootActor actor, float delta)
	{		
		turnCooldown = Math.max(0.0f, turnCooldown - delta);
		
		PhysicsBodyController physicsCtrl = actor.getController(PhysicsBodyController.class);		
		float distance = Math.abs(physicsCtrl.getCenterPositionRef().x - startX);
		float realRange = patrolRange * actor.getScene().getUnitScale(); 
				
		if(distance > realRange && canTurnAround())
		{			
			turnAround(actor);
		}
		else
		{
			super.onUpdate(actor, delta);	//walk	
		}
	}
	
	private boolean canTurnAround()
	{
		return turnCooldown == 0.0f;
	}
	
	private void turnAround(ZootActor actor)
	{
		setTurnCooldown(actor);		
		changeState(actor, TurnState.ID);
	}
	
	private void setTurnCooldown(ZootActor actor)
	{
		actor.controllerAction(AnimatedSpriteController.class, ctrl -> 
		{
			ZootAnimation turnAnimation = ctrl.getAnimation("Turn"); 
			turnCooldown = turnAnimation == null 
					? turnCooldown = DEFAULT_TURN_COOLDOWN	
					: turnAnimation.getFrameCount() * turnAnimation.getFrameDuration() + TURN_TIME_PADDING;
		});
	}
	
	@Override
	public boolean handle(ZootEvent event)
	{
		boolean shouldTurn = event.getType() == ZootEventType.Obstacle || event.getType() == ZootEventType.NoGroundAhead;		
		if(shouldTurn && canTurnAround())
		{
			turnAround(event.getTargetZootActor());
			return true;
		}
		
		return super.handle(event);
	}
	
	public void setPatrolRange(float range)
	{
		patrolRange = Math.max(0.0f, range);
	}
	
	public float getPatrolRange()
	{
		return patrolRange;
	}
	
	public void setStartX(float value)
	{
		startX = value;
	}
	
	public float getStartX()
	{
		return startX;
	}
	
	@Override
	public int getId()
	{
		return WalkState.ID;	//this state replaces WalkState
	}
}
