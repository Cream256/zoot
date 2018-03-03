package com.zootcat.fsm.states;

import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.events.ZootEvent;
import com.zootcat.scene.ZootActor;

//TODO add test
public class ClimbState extends AnimationBasedState
{
	public static final int ID = ClimbState.class.hashCode();
	
	private float oldGravity;
	
	public ClimbState()
	{
		super("Climb");
	}
	
	@Override
	public void onEnter(ZootActor actor, ZootEvent event)
	{		
		super.onEnter(actor, event);
		
		actor.controllerAction(PhysicsBodyController.class, ctrl -> 
		{
			oldGravity = ctrl.getGravityScale();
			ctrl.setGravityScale(0.0f);
			ctrl.setVelocity(0.0f, 0.0f);
		});
	}
	
	@Override
	public void onLeave(ZootActor actor, ZootEvent event)
	{
		actor.controllerAction(PhysicsBodyController.class, ctrl ->
		{
			ctrl.setGravityScale(oldGravity);
		});
	}
	
	@Override
	public boolean handle(ZootEvent event)
	{
		switch(event.getType())
		{
		case Hurt:
			changeState(event, HurtState.ID);
			return true;
			
		case Down:
			changeState(event, FallState.ID);
			return true;
		
		default:
			return true;		
		}
	}
	
	@Override
	public void onUpdate(ZootActor actor, float delta)
	{
		//noop
	}
	
	@Override
	public int getId()
	{
		return ID;
	}	
}
