package com.zootcat.actions;

import com.zootcat.dialogs.ZootDialog;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.exceptions.ZootException;
import com.zootcat.game.ZootGame;
import com.zootcat.screen.ZootDialogScreen;

public class ZootShowDialogScreenAction extends ZootAction
{
	private String token = "";
	private String path = "";	
	private ZootGame game;
	private boolean dialogRunning = false;
	private ZootDialogScreen dialogScreen;
	
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
		dialogRunning = false;
		dialogScreen = null;
		super.reset();
	}
	
	@Override
	public boolean act(float delta)
	{
		if(!dialogRunning)
		{
			dialogScreen = createDialogScreen();		
			game.setScreen(dialogScreen);
			dialogRunning = true;
			return false;
		}
			
		boolean inDialogScreen = game.getScreen() == dialogScreen; 
		return !inDialogScreen;
	}
	
	private ZootDialogScreen createDialogScreen()
	{
		if(path == null || path.isEmpty()) throw new RuntimeZootException("No dialog path was given for ZootShowDialogScreenAction");
		if(token == null || token.isEmpty()) throw new RuntimeZootException("No dialog token was given for ZootShowDialogScreenAction");
		if(game == null) throw new RuntimeZootException("No zoot game was given for ZootShowDialogScreenAction");		
		try
		{			
			String startToken = String.format(":%s", token);
			String endToken = String.format(":~%s", token);
			ZootDialog dialog = new ZootDialog(path, startToken, endToken, game.getAssetManager());
			
			ZootDialogScreen dialogScreen = new ZootDialogScreen(game);
			dialogScreen.setDialog(dialog);
			dialogScreen.setTriggeringActor(getTargetZootActor());
			return dialogScreen;
		}
		catch (ZootException e)
		{
			throw new RuntimeZootException(e);
		}	
	}
}
