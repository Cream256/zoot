package com.zootcat.fsm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.zootcat.events.ZootEvent;
import com.zootcat.events.ZootEventType;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.fsm.states.NullState;
import com.zootcat.scene.ZootActor;

public class ZootStateMachine implements EventListener
{
	private ZootActor owner = null;
	private ZootState previousState = null;
	private ZootState currentState = NullState.INSTANCE;	
	private Map<Integer, ZootState> states = new HashMap<Integer, ZootState>();
	
	public void init(ZootState state)
	{
		previousState = null;
		currentState = state;
		addState(state);		
		currentState.onEnter(owner, new ZootEvent(ZootEventType.InitEvent));		
	}
	
	public void addState(ZootState state)
	{
		states.put(state.getId(), state);		
	}
	
	public Set<ZootState> getStates()
	{
		return new HashSet<ZootState>(states.values());
	}
	
	public ZootState getStateById(int id)
	{
		ZootState state = states.get(id);
		if(state == null)
		{
			throw new RuntimeZootException("No state for id: " + id);
		}
		return state;
	}
	
	public void setOwner(ZootActor actor)
	{
		owner = actor;
	}
	
	public ZootActor getOwner()
	{
		return owner;
	}
	
	public ZootState getCurrentState()
	{
		return currentState;
	}
	
	public ZootState getPreviousState()
	{
		return previousState;
	}
	
	public void update(float delta)
	{
		currentState.onUpdate(owner, delta);
	}

	public void changeState(ZootState newState, ZootEvent event)
	{
        if(newState == null)
        {
            throw new RuntimeZootException("Cannot change to null state");
        }
        
        if(newState != currentState)
        {
            currentState.onLeave(owner, event);
            previousState = currentState;
            currentState = newState;
            newState.onEnter(owner, event);            
        }
	}
	
	@Override
	public boolean handle(Event event)
	{
        if(!ClassReflection.isInstance(ZootEvent.class, event))
        {
        	return false;
        }
		return currentState.handle((ZootEvent)event);
	}	
}
