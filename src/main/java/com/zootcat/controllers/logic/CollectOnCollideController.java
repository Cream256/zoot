package com.zootcat.controllers.logic;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.zootcat.controllers.physics.OnCollideController;
import com.zootcat.events.ZootEventType;
import com.zootcat.events.ZootEvents;
import com.zootcat.scene.ZootActor;

public abstract class CollectOnCollideController extends OnCollideController
{
	@Override
	public void onEnter(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		onCollect(getControllerActor(), getOtherActor(actorA, actorB));
		ZootEvents.fireAndFree(getControllerActor(), ZootEventType.Dead);
		getControllerActor().addAction(Actions.removeActor());
	}

	@Override
	public void onLeave(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		//noop		
	}
	
	public abstract void onCollect(ZootActor collectible, ZootActor collector);
}