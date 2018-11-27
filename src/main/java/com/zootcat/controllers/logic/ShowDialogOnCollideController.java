package com.zootcat.controllers.logic;

import com.badlogic.gdx.physics.box2d.Contact;
import com.zootcat.actions.ZootActions;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.controllers.physics.OnCollideController;
import com.zootcat.game.ZootGame;
import com.zootcat.scene.ZootActor;

public class ShowDialogOnCollideController extends OnCollideController
{
	@CtrlParam private String token = "";
	@CtrlParam private String path = "";	
	@CtrlParam(global = true) private ZootGame game;
	
	private boolean shown = false;
		
	@Override
	public void onEnter(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		if(shown) return;
		
		ZootActor triggeringActor = getOtherActor(actorA, actorB);		
		triggeringActor.addAction(ZootActions.showDialog(path, token, game, triggeringActor));
		shown = true;
	}

	@Override
	public void onLeave(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		//noop
	}
}
