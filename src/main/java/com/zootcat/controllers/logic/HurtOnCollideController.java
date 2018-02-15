package com.zootcat.controllers.logic;

import com.badlogic.gdx.physics.box2d.Contact;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.controllers.physics.OnCollideController;
import com.zootcat.events.ZootEventType;
import com.zootcat.events.ZootEvents;
import com.zootcat.scene.ZootActor;

/**
 * HurtOnCollide controller - lowers health of collided actor 
 * and sends Hurt {@link ZootEvent}. 
 * 
 * @ctrlParam damage - amout of damage dealt to the collided actor, default 1
 * 
 * @author Cream
 * @see OnCollideController
 */
public class HurtOnCollideController extends OnCollideController
{
	@CtrlParam(debug = true) private int damage = 1;
	
	@Override
	public void onEnter(ZootActor actorA, ZootActor actorB, Contact contact)
	{		
		ZootActor otherActor = getOtherActor(actorA, actorB);
		ZootEvents.fireAndFree(otherActor, ZootEventType.Hurt);
		otherActor.controllerAction(LifeController.class, ctrl -> ctrl.addToValue(-damage));
	}

	@Override
	public void onLeave(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		//noop
	}
}