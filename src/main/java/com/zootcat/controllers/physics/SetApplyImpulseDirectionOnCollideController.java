package com.zootcat.controllers.physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.scene.ZootActor;
import com.zootcat.utils.ZootDirection;

public class SetApplyImpulseDirectionOnCollideController extends OnCollideController
{
	@CtrlParam private boolean setX = true;
	@CtrlParam private boolean setY = true;
	@CtrlParam private ZootDirection directionX = ZootDirection.None;
	@CtrlParam private ZootDirection directionY = ZootDirection.None;
	
	@Override
	public void onEnter(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		getOtherActor(actorA, actorB).controllersAction(ApplyImpulseController.class, ctrl -> 
		{
			if(setX && Math.signum(ctrl.getImpulseX()) != Math.signum(directionX.getHorizontalValue())) 
			{
				ctrl.setImpulseX(ctrl.getImpulseX() * -1);
			}
			
			if(setY && Math.signum(ctrl.getImpulseY()) != Math.signum(directionY.getVerticalValue())) 
			{
				ctrl.setImpulseY(ctrl.getImpulseY() * -1);
			}
		});
	}

	@Override
	public void onLeave(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		//noop
	}
}
