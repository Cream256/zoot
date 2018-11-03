package com.zootcat.controllers.logic.triggers;

import com.badlogic.gdx.physics.box2d.Contact;
import com.zootcat.controllers.factory.CtrlDebug;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.scene.ZootActor;

public class MassButtonController extends ButtonController
{
	@CtrlParam(required = true) private float requiredMass;
	@CtrlDebug private float currentMass;
	
	@Override
	public void onEnter(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		currentMass += getOtherBodyMass(actorA, actorB, contact);		
		if(currentMass >= requiredMass)
		{
			press();
		}
	}

	@Override
	public void onLeave(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		currentMass -= getOtherBodyMass(actorA, actorB, contact);
		if(currentMass < requiredMass)
		{
			unpress();
		}
	}
	
	private float getOtherBodyMass(ZootActor actorA, ZootActor actorB, Contact contact) 
	{
		ZootActor otherActor = getOtherActor(actorA, actorB);
		PhysicsBodyController physicsBodyCtrl = otherActor.tryGetController(PhysicsBodyController.class);
		return physicsBodyCtrl != null ? physicsBodyCtrl.getMass() : 0.0f;
	}	
}
