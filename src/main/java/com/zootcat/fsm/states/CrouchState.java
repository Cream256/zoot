package com.zootcat.fsm.states;

import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.controllers.physics.PhysicsBodyScale;
import com.zootcat.events.ZootEvent;
import com.zootcat.events.ZootEventType;
import com.zootcat.scene.ZootActor;

public class CrouchState extends WalkState
{	
	public static final int ID = CrouchState.class.hashCode();
		
	private PhysicsBodyScale bodyScaling;
	
	public CrouchState()
	{
		super("Crouch");
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
		super.onEnter(actor, event);
		
		if(bodyScaling != null)	actor.controllerAction(PhysicsBodyController.class, c -> c.scale(bodyScaling));
	}
	
	@Override
	public void onLeave(ZootActor actor, ZootEvent event)
	{
		if(bodyScaling != null) actor.controllerAction(PhysicsBodyController.class, c -> c.scale(bodyScaling.invert()));
	}
	
	@Override
	public boolean handle(ZootEvent event)
	{	
		if(event.getType() == ZootEventType.Up)
		{
			changeState(event, IdleState.ID);
		}	
		else if(event.getType() == ZootEventType.Stop)
		{
			changeState(event, DownState.ID);
		}		
		else if(event.getType() == ZootEventType.Fall)
		{
			changeState(event, FallState.ID);
		}
		else if(event.getType() == ZootEventType.Hurt)
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
