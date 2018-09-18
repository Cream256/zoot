package com.zootcat.controllers.logic;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.zootcat.controllers.factory.CtrlDebug;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.events.ZootEvents;
import com.zootcat.scene.ZootActor;

public class LifeController extends IntValueController
{
	public static final int DEFAULT_LIFE = 3;
	
	@CtrlParam boolean removeWhenDead = true;
	@CtrlDebug boolean sendDeadEvent = false;
	@CtrlDebug boolean deadEventSend = false;
			
	@Override
	public void init(ZootActor actor) 
	{		
		super.init(actor);
		
		setMinValue(0);
		if(getMaxValue() == 0) setMaxValue(DEFAULT_LIFE);
		if(getValue() == 0) setValue(DEFAULT_LIFE);
	}
	
	@Override
	public void setValue(int value)
	{
		super.setValue(value);
		sendDeadEvent = getValue() == 0;
		deadEventSend = deadEventSend && getValue() == 0;
	}
	
	@Override
	public void setMaxValue(int newValue)
	{
		super.setMaxValue(Math.max(newValue, 1));
	}
	
	@Override
	public void onUpdate(float delta, ZootActor actor)
	{
		if(sendDeadEvent && !deadEventSend)
		{
			ZootEvents.fireAndFree(actor, ZootEventType.Dead);		
			deadEventSend = true;
			
			if(removeWhenDead)
			{
				actor.addAction(Actions.removeActor());
			}			
		}		
	}
	
	public boolean isAlive()
	{
		return getValue() > 0;
	}

	public boolean getRemoveWhenDead()
	{
		return removeWhenDead;
	}
	
	public void setRemoveWhenDead(boolean value)
	{
		removeWhenDead = value;
	}
}