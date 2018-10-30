package com.zootcat.controllers.ai;

import com.badlogic.gdx.math.Vector2;
import com.zootcat.controllers.factory.CtrlDebug;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.controllers.gfx.AnimatedSpriteController;
import com.zootcat.controllers.logic.ZootEventListenerController;
import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.events.ZootEventTypeEnum;
import com.zootcat.fsm.events.ZootEvents;
import com.zootcat.gfx.ZootAnimation;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;
import com.zootcat.scene.ZootScene;

public class PatrolAIController extends ZootEventListenerController
{	
	public static final float DEFAULT_TURN_COOLDOWN = 0.5f;
	public static final float TURN_TIME_PADDING = 0.05f;
	
	@CtrlParam private int distance = 0;
	@CtrlParam private String startDirection = "Left";
	@CtrlParam private boolean isFlying = false;
	@CtrlParam(global = true) private ZootScene scene;
	@CtrlDebug private float currentTurnCooldown = 0.0f;
	@CtrlDebug private float turnCooldown = DEFAULT_TURN_COOLDOWN;
	
	private Vector2 start;
	private ZootDirection direction = ZootDirection.Left;
	private PhysicsBodyController physicsCtrl;
			
	@Override
	public void onAdd(ZootActor actor) 
	{
		super.onAdd(actor);
		physicsCtrl = actor.getController(PhysicsBodyController.class);
		start = physicsCtrl.getCenterPositionRef().cpy();
		direction = ZootDirection.fromString(startDirection);
		setTurnCooldownFromAnimation(actor);
	}

	private void setTurnCooldownFromAnimation(ZootActor actor)
	{
		actor.controllerAction(AnimatedSpriteController.class, ctrl -> 
		{
			ZootAnimation turnAnimation = ctrl.getAnimation("Turn"); 
			if(turnAnimation == null) return;			
			turnCooldown = turnAnimation.getFrameCount() * turnAnimation.getFrameDuration() + TURN_TIME_PADDING; 
		});
	}

	@Override
	public void onRemove(ZootActor actor)
	{
		super.onRemove(actor);
		start = null;
		scene = null;
		physicsCtrl = null;		
	}
	
	public ZootDirection getCurrentDirection()
	{
		return direction;
	}

	@Override
	public void onUpdate(float delta, ZootActor actor)
	{		
		float currentX = physicsCtrl.getCenterPositionRef().x;
		float realDist = distance * scene.getUnitScale();
		if(direction == ZootDirection.Left)
		{
			if(currentX < start.x - realDist) direction = ZootDirection.Right;
		}
		else
		{
			if(currentX > start.x + realDist) direction = ZootDirection.Left;			
		}		
		ZootEvents.fireAndFree(actor, getEvent(direction));		
		currentTurnCooldown = Math.max(0.0f, currentTurnCooldown - delta);
	}
	
	private ZootEventTypeEnum getEvent(ZootDirection direction)
	{
		switch(direction)
		{
		case Right:
			return isFlying ? ZootEventType.FlyRight : ZootEventType.WalkRight;
			
		default:
			return isFlying ? ZootEventType.FlyLeft : ZootEventType.WalkLeft;
		}
	}

	@Override
	public boolean handleZootEvent(ZootEvent event)
	{
		boolean shouldTurn = event.getType() == ZootEventType.Obstacle || event.getType() == ZootEventType.NoGroundAhead;		
		if(shouldTurn && currentTurnCooldown == 0.0f)
		{
			currentTurnCooldown = turnCooldown;
			direction = direction.invert();
			return true;
		}		
		return false;
	}	
}
