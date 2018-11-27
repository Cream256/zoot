package com.zootcat.actions;

import com.zootcat.dialogs.ZootDialog;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.exceptions.ZootException;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.events.ZootEvents;
import com.zootcat.game.ZootGame;
import com.zootcat.scene.ZootActor;
import com.zootcat.screen.ZootDialogScreen;

public class ZootShowDialogScreenAction extends ZootAction
{
	private String token = "";
	private String path = "";	
	private ZootGame game;
	
	public void setDialogToken(String token)
	{
		this.token = token;
	}
	
	public String getDialogToken()
	{
		return token;
	}
	
	public void setDialogPath(String path)
	{
		this.path = path;
	}
	
	public String getDialogPath()
	{
		return path;
	}
	
	public void setZootGame(ZootGame game)
	{
		this.game = game;
	}
	
	public ZootGame getZootGame()
	{
		return game;
	}
	
	@Override
	public void reset()
	{
		path = null;
		token = null;
		game = null;		
		super.reset();
	}
	
	@Override
	public boolean act(float delta)
	{
		if(path == null || path.isEmpty()) throw new RuntimeZootException("No dialog path was given for ZootShowDialogScreenAction");
		if(token == null || token.isEmpty()) throw new RuntimeZootException("No dialog token was given for ZootShowDialogScreenAction");
		if(game == null) throw new RuntimeZootException("No zoot game was given for ZootShowDialogScreenAction");
		
		try
		{			
			String startToken = String.format(":%s", token);
			String endToken = String.format(":~%s", token);
			ZootDialog dialog = new ZootDialog(path, startToken, endToken, game.getAssetManager());
			
			ZootActor triggeringActor = super.getTargetZootActor();
			triggeringActor.fire(ZootEvents.get(ZootEventType.Stop));
			
			ZootDialogScreen dialogScreen = new ZootDialogScreen(game);
			dialogScreen.setDialog(dialog);
			dialogScreen.setTriggeringActor(triggeringActor);
			game.setScreen(dialogScreen);
		}
		catch (ZootException e)
		{
			throw new RuntimeZootException(e);
		}
		
		return true;
	}
}
