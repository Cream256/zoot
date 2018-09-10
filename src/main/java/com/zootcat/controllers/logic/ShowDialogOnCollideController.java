package com.zootcat.controllers.logic;

import com.badlogic.gdx.physics.box2d.Contact;
import com.zootcat.assets.ZootAssetManager;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.controllers.physics.OnCollideController;
import com.zootcat.dialogs.ZootDialog;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.exceptions.ZootException;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.events.ZootEvents;
import com.zootcat.game.ZootGame;
import com.zootcat.gfx.ZootGraphicsFactory;
import com.zootcat.scene.ZootActor;
import com.zootcat.screen.ZootDialogScreen;

public class ShowDialogOnCollideController extends OnCollideController
{
	@CtrlParam private String token = "";
	@CtrlParam private String path = "";	
	@CtrlParam(global = true) private ZootGame game;
	@CtrlParam(global = true) private ZootAssetManager assetManager;
	@CtrlParam(global = true) private ZootGraphicsFactory graphicsFactory;
		
	@Override
	public void onEnter(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		if(path.isEmpty()) throw new RuntimeZootException("No dialog path was given for ShowDialogOnCollideController");
		if(token.isEmpty()) throw new RuntimeZootException("No dialog token was given for ShowDialogOnCollideController");
		
		try
		{
			String startToken = String.format(":%s", token);
			String endToken = String.format(":~%s", token);
			ZootDialog dialog = new ZootDialog(path, startToken, endToken, assetManager);
			
			ZootActor triggeringActor = getOtherActor(actorA, actorB);
			triggeringActor.fire(ZootEvents.get(ZootEventType.Stop));
			
			ZootDialogScreen dialogScreen = new ZootDialogScreen(game, graphicsFactory);
			dialogScreen.setDialog(dialog);
			dialogScreen.setTriggeringActor(triggeringActor);
			game.setScreen(dialogScreen);
		}
		catch (ZootException e)
		{
			throw new RuntimeZootException(e);
		}
	}

	@Override
	public void onLeave(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		//noop
	}
}
