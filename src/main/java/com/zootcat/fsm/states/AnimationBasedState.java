package com.zootcat.fsm.states;

import com.zootcat.controllers.gfx.AnimatedSpriteController;
import com.zootcat.events.ZootEvent;
import com.zootcat.gfx.ZootAnimation;
import com.zootcat.scene.ZootActor;

public class AnimationBasedState extends BasicState
{
	protected ZootAnimation animation;
	
	public AnimationBasedState(String name)
	{
		super(name);
	}
	
	@Override
	public void onEnter(ZootActor actor, ZootEvent event)
	{		
		//get animation
		animation = null;
		actor.controllerAction(AnimatedSpriteController.class, ctrl -> 
		{
			animation = ctrl.getAnimation(getName());
			if(animation != null) ctrl.setAnimation(getName());
		});
	}
	
	@Override
	public void onUpdate(ZootActor actor, float delta)
	{
		if(animation == null || animation.isFinished())
		{
			changeState(actor, IdleState.ID);
		}
	}

}
