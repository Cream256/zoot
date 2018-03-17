package com.zootcat.controllers.logic.triggers;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.zootcat.controllers.factory.CtrlDebug;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.scene.ZootActor;

public class MassButtonController extends ButtonController
{
	@CtrlParam(required = true, debug = true) private float requiredMass;
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
		Fixture otherFixture = super.getOtherFixture(actorA, actorB, contact);
		return otherFixture.getBody().getMass();
	}	
}
