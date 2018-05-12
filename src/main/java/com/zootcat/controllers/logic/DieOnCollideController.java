package com.zootcat.controllers.logic;

import com.badlogic.gdx.physics.box2d.Contact;
import com.zootcat.actions.ZootActions;
import com.zootcat.controllers.physics.OnCollideController;
import com.zootcat.scene.ZootActor;

public class DieOnCollideController extends OnCollideController
{
	@Override
	public void onEnter(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		die();
	}

	@Override
	public void onLeave(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		//noop
	}
	
	protected void die()
	{
		getControllerActor().addAction(ZootActions.killActorAction(getControllerActor()));
	}
}
