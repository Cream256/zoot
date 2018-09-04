package com.zootcat.controllers.physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.scene.ZootActor;

public class ChangeApplyImpulseDirectionOnCollideController extends OnCollideController
{
	@CtrlParam private boolean changeX = true;
	@CtrlParam private boolean changeY = true;
		
	@Override
	public void onEnter(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		getOtherActor(actorA, actorB).controllerAction(ApplyImpulseController.class, ctrl -> 
		{
			ctrl.setImpulseX(ctrl.getImpulseX() * (changeX ? -1 : 1));
			ctrl.setImpulseY(ctrl.getImpulseY() * (changeY ? -1 : 1));
		});
	}

	@Override
	public void onLeave(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		//noop
	}
}
