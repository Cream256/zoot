package com.zootcat.controllers.logic;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction;
import com.zootcat.controllers.physics.OnCollideController;
import com.zootcat.events.ZootEventType;
import com.zootcat.events.ZootEvents;
import com.zootcat.scene.ZootActor;

public class DieOnCollideController extends OnCollideController
{
	@Override
	public void onEnter(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		ZootEvents.fireAndFree(getControllerActor(), ZootEventType.Dead);				
		getControllerActor().addAction(new RemoveActorAction());		
	}

	@Override
	public void onLeave(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		//noop
	}
}
