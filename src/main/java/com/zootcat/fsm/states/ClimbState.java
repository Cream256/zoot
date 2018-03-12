package com.zootcat.fsm.states;

import com.zootcat.controllers.gfx.AnimatedSpriteController;
import com.zootcat.controllers.logic.ClimbController;
import com.zootcat.events.ZootEvent;
import com.zootcat.events.ZootEventType;
import com.zootcat.scene.ZootActor;

public class ClimbState extends BasicState
{
	public static final String CLIMB_SIDE_ANIMATION = "ClimbSide";
	public static final String CLIMB_ANIMATION = "Climb";
	
	public static final int ID = ClimbState.class.hashCode();
		
	public ClimbState()
	{
		super("Climb");
	}
	
	@Override
	public void onEnter(ZootActor actor, ZootEvent event)
	{				
		actor.controllerAction(AnimatedSpriteController.class, ctrl -> 
		{
			String usedAnimation = event.getType() == ZootEventType.GrabSide ? CLIMB_SIDE_ANIMATION : CLIMB_ANIMATION; 			
			ctrl.setAnimation(usedAnimation);
		});
		actor.controllerAction(ClimbController.class, ctrl -> ctrl.grab());
	}
		
	@Override
	public boolean handle(ZootEvent event)
	{
		ZootActor actor = event.getTargetZootActor();
		switch(event.getType())
		{
		case Hurt:
			changeState(event, HurtState.ID);
			return true;
			
		case Up:
			actor.controllerAction(ClimbController.class, ctrl -> 
			{
				if(ctrl.climb()) 
					changeState(event, IdleState.ID);
			});
			return true;
		
		case Down:			
			actor.controllerAction(ClimbController.class, ctrl -> ctrl.letGo());			
			changeState(event, FallState.ID);
			return true;
		
		default:
			return true;		
		}
	}
	
	@Override
	public int getId()
	{
		return ID;
	}	
}
