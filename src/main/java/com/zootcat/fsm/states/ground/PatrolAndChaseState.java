package com.zootcat.fsm.states.ground;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Array;
import com.zootcat.actions.ZootActions;
import com.zootcat.controllers.physics.OnCollideWithSensorController;
import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.controllers.physics.WalkableController;
import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.fsm.states.PatrolState;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;

public class PatrolAndChaseState extends PatrolState
{
	public static final int ID = PatrolState.ID;
	
	private int lookRange = 0;	
	private String chasedActorType = "";
	private ZootActor chasedActor;
	private OnCollideWithSensorController chaseSensorCtrl;
	private Array<ZootActor> actorsInChaseRange = new Array<ZootActor>();
		
	@Override
	public void onEnter(ZootActor actor, ZootEvent event)
	{		
		super.onEnter(actor, event);
		if(chaseSensorCtrl == null && actor.getScene() != null)
		{
			chaseSensorCtrl = createChaseSensorCtrl(actor);
			actor.addAction(ZootActions.addController(actor, chaseSensorCtrl));
		}		
	}
	
	private OnCollideWithSensorController createChaseSensorCtrl(ZootActor actor)
	{
		float sensorWidth = lookRange * 2;
		float sensorHeight = actor.getHeight() / actor.getScene().getUnitScale();
		
		OnCollideWithSensorController ctrl = new OnCollideWithSensorController(sensorWidth, sensorHeight, 0.0f, 0.0f)
		{
			@Override
			public void onEnterCollision(Fixture fixture)
			{
				actorsInChaseRange.add((ZootActor)fixture.getUserData());
			}
			
			@Override
			public void onLeaveCollision(Fixture fixture)
			{
				actorsInChaseRange.removeValue((ZootActor)fixture.getUserData(), true);
			}
		};
		ctrl.setScene(actor.getScene());
		ctrl.setCategoryParameter("SENSOR");
		ctrl.setMaskParameter(chasedActorType.toUpperCase());		
		return ctrl;
	}
	
	@Override
	public void onUpdate(ZootActor actor, float delta)
	{		
		updateCooldown(delta);
		
		chasedActor = getChasedActor(actor);
		if(chasedActor != null)
		{
			if(canTurnAround() && shouldTurnAroundToChasedActor(actor, chasedActor))
			{
				turnAround(actor);
			}
			else
			{
				actor.controllersAction(WalkableController.class, ctrl -> ctrl.run(moveDirection));			
				setActorAnimationIfNotSet(actor, "Run");	
			}
			return;
		}
		
		setActorAnimationIfNotSet(actor, "Walk");
		if(outOfPatrolRange(actor) && canTurnAround())
		{			
			turnAround(actor);
		}
		else
		{
			move(actor, delta);	
		}
	}
	
	private boolean shouldTurnAroundToChasedActor(ZootActor chaser, ZootActor chased)
	{
		float chaserX = chaser.getSingleController(PhysicsBodyController.class).getCenterPositionRef().x;
		float chasedX = chased.getSingleController(PhysicsBodyController.class).getCenterPositionRef().x;
		
		if(moveDirection == ZootDirection.Left && chaserX < chasedX) return true;
		if(moveDirection == ZootDirection.Right && chaserX > chasedX) return true;		
		return false;		
	}
	
	private ZootActor getChasedActor(ZootActor chaser)
	{
		float minDist = Float.MAX_VALUE;
		float chaserX = chaser.getSingleController(PhysicsBodyController.class).getCenterPositionRef().x;
			
		ZootActor closestActor = null;
		for(ZootActor act : actorsInChaseRange)
		{
			float posX = act.getSingleController(PhysicsBodyController.class).getCenterPositionRef().x;
			float diff = Math.abs(posX - chaserX);
			if(diff < minDist)
			{
				minDist = diff;
				closestActor = act;
			}			
		}
		return closestActor;
	}
			
	public void setChasedActorType(String type)
	{
		chasedActorType = type;
	}
	
	public String getChasedActorType()
	{
		return chasedActorType;
	}
	
	public void setLookRange(int range)
	{
		lookRange = range;
	}
	
	public int getLookRange()
	{
		return lookRange;
	}
	
	@Override
	public String toString()
	{
		return "Patrol And Chase";
	}
	
	@Override
	public int getId()
	{
		return ID;
	}
}
