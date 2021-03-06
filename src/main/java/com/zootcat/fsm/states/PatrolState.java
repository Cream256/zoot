package com.zootcat.fsm.states;

import com.zootcat.controllers.gfx.AnimatedSpriteController;
import com.zootcat.controllers.logic.DirectionController;
import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.controllers.physics.WalkableController;
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
	private int patrolRange = 0;
	private float turnCooldown = 0.0f;
			
	public PatrolState()
	{
		super("Patrol");
	}
	
	@Override
	public void onEnter(ZootActor actor, ZootEvent event)
	{		
		actor.controllersAction(DirectionController.class, ctrl ->
		{
			moveDirection = ctrl.getDirection();
		});
		setActorAnimation(actor, "Walk");
	}
		
	@Override
	public void onUpdate(ZootActor actor, float delta)
	{		
		updateCooldown(delta);
		
		if(outOfPatrolRange(actor) && canTurnAround())
		{			
			turnAround(actor);
		}
		else
		{
			move(actor, delta);	
		}
	}
	
	protected void updateCooldown(float delta)
	{
		turnCooldown = Math.max(0.0f, turnCooldown - delta);
	}
	
	protected void move(ZootActor actor, float delta)
	{
		actor.controllersAction(WalkableController.class, (mvCtrl) -> mvCtrl.walk(moveDirection));
	}
	
	protected boolean outOfPatrolRange(ZootActor actor)
	{
		PhysicsBodyController physicsCtrl = actor.getSingleController(PhysicsBodyController.class);		
		float distance = Math.abs(physicsCtrl.getCenterPositionRef().x - startX);
		float realRange = patrolRange * actor.getScene().getUnitScale(); 
		return distance > realRange;
	}
	
	protected boolean canTurnAround()
	{
		return turnCooldown == 0.0f;
	}
	
	protected void turnAround(ZootActor actor)
	{
		setTurnCooldown(actor);		
		changeState(actor, TurnState.ID);
	}
	
	private void setTurnCooldown(ZootActor actor)
	{
		actor.controllersAction(AnimatedSpriteController.class, ctrl -> 
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
	
	public void setPatrolRange(int range)
	{
		patrolRange = Math.max(0, range);
	}
	
	public int getPatrolRange()
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
