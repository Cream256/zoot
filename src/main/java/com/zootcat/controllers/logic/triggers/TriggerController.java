package com.zootcat.controllers.logic.triggers;

import com.badlogic.gdx.physics.box2d.Contact;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.controllers.physics.OnCollideController;
import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.events.ZootEvents;
import com.zootcat.logic.Trigger;
import com.zootcat.scene.ZootActor;

/**
 * Trigger Controller - used for actors that are triggers. When collision
 * happens, the trigger changes it's state and fires the SwitchOn/SwitchOff
 * {@link ZootEvent}.
 * <br/><br/>
 * If you want to react when the trigger changes it's state, you should
 * listen to SwitchOn/SwitchOff events. This can be done by implementing
 * the {@link TriggerEventListener} class.
 * <br/><br/>
 * WARNING - when using trigger controller, the trigger will automatically
 * fire TriggerOn/TriggerOff event on initialization. It might override
 * the default behaviour set for the object connected with the trigger.
 * <br/><br/>
 * @author Cream
 * @see OnCollideController
 */
public class TriggerController extends OnCollideController
{
	@CtrlParam private boolean active = false;
	@CtrlParam private boolean canRevert = true;
	
	private Trigger trigger;		
	private boolean triggerInitialized;
		
	@Override
	public void onAdd(ZootActor actor) 
	{
		super.onAdd(actor);
		triggerInitialized = false;
		trigger = new Trigger((isOn) -> sendTriggerEvent(isOn), active, canRevert);
	}
	
	@Override
	public void onUpdate(float delta, ZootActor actor)
	{
		super.onUpdate(delta, actor);
		if(!triggerInitialized)
		{
			trigger.initialize();
			triggerInitialized = true;
		}
	}
	
	@Override
	public void onEnter(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		switchState();
	}

	@Override
	public void onLeave(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		//noop
	}
	
	public boolean isActive()
	{
		return trigger.isActive();
	}
	
	public void setActive(boolean isActive)
	{
		trigger.setActive(isActive);		
	}
	
	public void switchState()
	{
		trigger.switchState();
	}
	
	private void sendTriggerEvent(boolean active)
	{
		ZootEvents.fireAndFree(getControllerActor(), active ? ZootEventType.TriggerOn : ZootEventType.TriggerOff, getControllerActor());
	}
}
