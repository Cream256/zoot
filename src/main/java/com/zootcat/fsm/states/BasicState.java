package com.zootcat.fsm.states;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.zootcat.controllers.gfx.AnimatedSpriteController;
import com.zootcat.events.ZootEvent;
import com.zootcat.fsm.ZootState;
import com.zootcat.fsm.ZootStateMachine;
import com.zootcat.scene.ZootActor;

public class BasicState implements ZootState
{
	private final String name;
	
	public BasicState(String name)
	{
		this.name = name;
	}
		
	@Override
	public void onEnter(ZootActor actor, ZootEvent event)
	{
		//noop
	}

	@Override
	public void onLeave(ZootActor actor, ZootEvent event)
	{
		//noop
	}

	@Override
	public void onUpdate(ZootActor actor, float delta)
	{
		//noop
	}

	@Override
	public boolean handle(ZootEvent event)
	{
		return false;
	}
	
	@Override
	public int getId()
	{
		return name.hashCode();
	}
	
	@Override
	public int hashCode()
	{
		return getId();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj) return true;
		if(!ClassReflection.isInstance(ZootState.class, obj)) return false;
		return getId() == ((ZootState)obj).getId();
	}
		
	@Override
	public String toString()
	{
		return name;
	}
		
	@Override
	public String getName()
	{
		return name;
	}
	
	protected void changeState(ZootActor actor, int stateId)
	{
		ZootStateMachine sm = actor.getStateMachine();
		sm.changeState(sm.getStateById(stateId), null);
	}
	
	protected void changeState(ZootEvent event, int stateId)
	{
		ZootStateMachine sm = event.getTargetZootActor().getStateMachine();
		sm.changeState(sm.getStateById(stateId), event);
	}
			
	protected void setAnimationBasedOnStateName(ZootActor actor)
	{
		actor.controllerAction(AnimatedSpriteController.class, (ctrl) -> ctrl.setAnimation(name));
	}
}
