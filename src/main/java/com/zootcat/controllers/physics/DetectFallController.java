package com.zootcat.controllers.physics;

import com.zootcat.controllers.Controller;
import com.zootcat.controllers.factory.CtrlDebug;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.events.ZootEventType;
import com.zootcat.events.ZootEvents;
import com.zootcat.scene.ZootActor;

public class DetectFallController implements Controller
{
	@CtrlDebug private boolean falling;
	@CtrlParam(debug = true) private float threshold = -0.5f;
	
	private DetectGroundController groundCtrl;	
		
	@Override
	public void init(ZootActor actor)
	{
		falling = false;	
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
		boolean fallingNow = actor.controllerCondition(PhysicsBodyController.class,	(c) -> c.getBody().getLinearVelocity().y < threshold);		
		boolean onGround = groundCtrl.isOnGround();
		
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
