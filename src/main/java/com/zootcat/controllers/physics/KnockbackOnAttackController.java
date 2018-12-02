package com.zootcat.controllers.physics;

import com.zootcat.actions.ZootActions;
import com.zootcat.actions.ZootKnockbackAction;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.controllers.logic.OnZootEventController;
import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.scene.ZootActor;

public class KnockbackOnAttackController extends OnZootEventController
{
	@CtrlParam private float knockbackX = 1.0f;
	@CtrlParam private float knockbackY = 1.0f;
	@CtrlParam private boolean varyHorizontal = false;
	
	@Override
	public boolean onZootEvent(ZootActor actor, ZootEvent event)
	{
		if(event.getType() == ZootEventType.Attack)
		{
			ZootActor target = event.getUserObject(ZootActor.class);			
			ZootKnockbackAction knockback = ZootActions.knockback(knockbackX, knockbackY, varyHorizontal, target, actor);
			target.addAction(knockback);			
			return true;
		}		
		return false;
	}
		
	public void setKnockback(float knockbackX, float knockbackY)
	{
		this.knockbackX = knockbackX;
		this.knockbackY = knockbackY;
	}
	
	public float getKnockbackX()
	{
		return knockbackX;
	}
	
	public float getKnockbackY()
	{
		return knockbackY;
	}
	
	public void setVaryHorizontal(boolean value)
	{
		varyHorizontal = value;
	}
	
	public boolean getVaryHorizontal()
	{
		return varyHorizontal;
	}
}
