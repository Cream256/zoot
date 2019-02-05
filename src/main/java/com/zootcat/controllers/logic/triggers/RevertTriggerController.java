package com.zootcat.controllers.logic.triggers;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.zootcat.controllers.factory.CtrlDebug;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.scene.ZootActor;

public class RevertTriggerController extends TriggerEventListener
{
	@CtrlParam(required = true) private float revertAfter;
	@CtrlDebug private float time = 0.0f;
	@CtrlDebug private boolean countdown = false;
	
	@Override
	public void onAdd(ZootActor actor)
	{
		super.onAdd(actor);
		reset();
	}
	
	@Override
	public void onUpdate(float delta, ZootActor actor)
	{
		super.onUpdate(delta, actor);
		if(!countdown) return;
		
		time = Math.max(0.0f, time - delta);
		if(time != 0.0f) return;
		
		actor.controllersAction(ctrl -> ClassReflection.isInstance(TriggerController.class, ctrl), ctrl -> ((TriggerController)ctrl).setActive(false));
		actor.controllersAction(ctrl -> ClassReflection.isInstance(TriggerOnEventController.class, ctrl), ctrl -> ((TriggerOnEventController)ctrl).setActive(false));
		reset();
	}
	
	@Override
	public void triggerOn(ZootActor switchActor)
	{
		countdown = true;		
	}

	@Override
	public void triggerOff(ZootActor switchActor)
	{
		reset();
	}
	
	public void reset()
	{
		countdown = false;
		time = revertAfter;
	}
	
	public boolean isCountingDown()
	{
		return countdown;
	}
	
	public float getTimeLeft()
	{
		return time;
	}
}
