package com.zootcat.controllers.logic.triggers;

import java.util.Collection;

import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.controllers.logic.OnZootEventController;
import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.events.ZootEventTypeEnum;
import com.zootcat.fsm.events.ZootEvents;
import com.zootcat.logic.Trigger;
import com.zootcat.scene.ZootActor;

public class TriggerOnEventController extends OnZootEventController
{
	@CtrlParam private boolean active = false;
	
	private Trigger trigger;
	private boolean triggerInitialized;
		
	public TriggerOnEventController(Collection<ZootEventTypeEnum> types, boolean singleExecution)
	{		
		super(types, singleExecution);
	}
	
	@Override
	public void onAdd(ZootActor actor)
	{
		super.onAdd(actor);	
		triggerInitialized = false;
		trigger = new Trigger((on) -> sendTriggerEvent(on, actor), active);
	}
		
	@Override
	public void onUpdate(float delta, ZootActor actor)
	{
		if(triggerInitialized) return;
		triggerInitialized = true;
		trigger.initialize();	
	}
	
	@Override
	public boolean onZootEvent(ZootActor actor, ZootEvent event)
	{
		trigger.switchState();
		return true;
	}
	
	private void sendTriggerEvent(boolean active, ZootActor actor)
	{
		ZootEvents.fireAndFree(actor, active ? ZootEventType.TriggerOn : ZootEventType.TriggerOff, actor);
	}
}
