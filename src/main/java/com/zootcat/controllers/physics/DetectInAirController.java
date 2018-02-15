package com.zootcat.controllers.physics;

import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.controllers.factory.CtrlDebug;
import com.zootcat.events.ZootEventType;
import com.zootcat.events.ZootEvents;
import com.zootcat.scene.ZootActor;

public class DetectInAirController extends ControllerAdapter
{
	@CtrlDebug private boolean inAir;	
	private DetectGroundController groundCtrl;	
		
	@Override
	public void init(ZootActor actor)
	{
		inAir = false;
	}

	@Override
	public void onAdd(ZootActor actor)
	{
		groundCtrl = actor.getController(DetectGroundController.class);
	}

	@Override
	public void onRemove(ZootActor actor)
	{
		groundCtrl = null;
	}

	@Override
	public void onUpdate(float delta, ZootActor actor)
	{				
		inAir = !groundCtrl.isOnGround(); 
		if(inAir)
		{
			ZootEvents.fireAndFree(actor, ZootEventType.InAir);
		}
	}
	
	public boolean isInAir()
	{
		return inAir;
	}
}
