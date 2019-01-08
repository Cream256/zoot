package com.zootcat.fsm.states.ground;

import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.controllers.physics.PhysicsBodyScale;
import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.states.BasicState;
import com.zootcat.fsm.states.HurtState;
import com.zootcat.fsm.states.ZootStateUtils;
import com.zootcat.scene.ZootActor;

public class DownState extends BasicState
{	
	public static final int ID = DownState.class.hashCode();
		
	private PhysicsBodyScale bodyScaling = null;
	
	public DownState()
	{
		super("Down");
	}
	
	public void setBodyScaling(PhysicsBodyScale scale)
	{
		bodyScaling = scale;
	}
	
	public PhysicsBodyScale getBodyScaling()
	{
		return bodyScaling;
	}
	
	@Override
	public void onEnter(ZootActor actor, ZootEvent event)
	{
		setAnimationBasedOnStateName(actor);
		actor.controllersAction(PhysicsBodyController.class, (ctrl) -> ctrl.setVelocity(0.0f, 0.0f, true, false));
		if(bodyScaling != null) actor.controllersAction(PhysicsBodyController.class, c -> c.scale(bodyScaling));
	}
	
	@Override
	public void onLeave(ZootActor actor, ZootEvent event)
	{
		if(bodyScaling != null)
		{
			actor.controllersAction(PhysicsBodyController.class, c -> c.scale(bodyScaling.invert()));
		}
	}
	
	@Override
	public boolean handle(ZootEvent event)
	{		
		if(ZootStateUtils.isMoveEvent(event))
		{			
			changeState(event, CrouchState.ID);
		}
		else if(event.getType() == ZootEventType.Up)
		{
			changeState(event, IdleState.ID);
		}		
		else if(event.getType() == ZootEventType.Fall)
		{
			changeState(event, FallState.ID);
		}
		else if(event.getType() == ZootEventType.Hurt && ZootStateUtils.canHurtActor(event))
		{
			changeState(event, HurtState.ID);
		}
		return true;
	}
	
	@Override
	public int getId()
	{
		return ID;
	}
}
