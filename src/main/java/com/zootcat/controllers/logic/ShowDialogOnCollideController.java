package com.zootcat.controllers.logic;

import java.util.function.Consumer;

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
	private Consumer<ZootGame> onShowAction = null;
	private Consumer<ZootGame> onHideAction = null;
		
	@Override
	public void onEnter(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		if(shown) return;
		
		ZootActor triggeringActor = getOtherActor(actorA, actorB);		
		triggeringActor.addAction(ZootActions.showDialog(path, token, game, triggeringActor, onShowAction, onHideAction));
		shown = true;
	}

	@Override
	public void onLeave(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		//noop
	}
	
	public void setOnShowAction(Consumer<ZootGame> action)
	{
		onShowAction = action;
	}
	
	public Consumer<ZootGame> getOnShowAction()
	{
		return onShowAction;
	}
	
	public void setOnHideAction(Consumer<ZootGame> action)
	{
		onHideAction = action;
	}
	
	public Consumer<ZootGame> getOnHideAction()
	{
		return onHideAction;
	}
}
