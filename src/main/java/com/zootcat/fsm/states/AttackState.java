package com.zootcat.fsm.states;

import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.events.ZootEvent;
import com.zootcat.events.ZootEventType;
import com.zootcat.scene.ZootActor;

public class AttackState extends AnimationBasedState
{
	public static final int ID = AttackState.class.hashCode();
			
	public AttackState()
	{
		super("Attack");
	}
	
	@Override
	public void onEnter(ZootActor actor, ZootEvent event)
	{		
		super.onEnter(actor, event);
		
		//stop actor
		if(animation != null)
		{
			actor.controllerAction(PhysicsBodyController.class, (ctrl) -> ctrl.setVelocity(0.0f, 0.0f, true, false));
		}
	}
			
	@Override
	public boolean handle(ZootEvent event)
	{	
		if(event.getType() == ZootEventType.Hurt)
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
