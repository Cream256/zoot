package com.zootcat.controllers.physics;

import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.controllers.factory.CtrlDebug;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.events.ZootEvents;
import com.zootcat.scene.ZootActor;

public class DetectFallController extends ControllerAdapter
{
	@CtrlDebug private boolean falling;
	@CtrlParam private float threshold = -0.5f;
	
	private DetectGroundSensorController groundCtrl;	
		
	@Override
	public void init(ZootActor actor)
	{
		falling = false;	
	}

	@Override
	public void onAdd(ZootActor actor)
	{
		groundCtrl = actor.tryGetSingleController(DetectGroundSensorController.class);
	}

	@Override
	public void onRemove(ZootActor actor)
	{
		groundCtrl = null;
	}

	@Override
	public void onUpdate(float delta, ZootActor actor)
	{		
		boolean fallingNow = actor.controllersAllMatch(PhysicsBodyController.class,	ctrl -> ctrl.getBody().getLinearVelocity().y < threshold);		
		boolean onGround = groundCtrl == null ? false : groundCtrl.isOnGround();
		
		if(fallingNow && !onGround)
		{
			ZootEvents.fireAndFree(actor, ZootEventType.Fall);
		}
		falling = fallingNow && !onGround;
	}
	
	public boolean isFalling()
	{
		return falling;
	}
}
