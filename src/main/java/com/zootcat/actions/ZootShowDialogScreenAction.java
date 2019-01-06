package com.zootcat.actions;

import java.util.function.Consumer;

import com.badlogic.gdx.Game;
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
	private Consumer<Game> onShowAction = null;
	private Consumer<Game> onHideAction = null;
	
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
	
	public void setOnShowAction(Consumer<Game> action)
	{
		onShowAction = action;
	}
	
	public Consumer<Game> getOnShowAction()
	{
		return onShowAction;
	}
	
	public void setOnHideAction(Consumer<Game> action)
	{
		onHideAction = action;
	}
	
	public Consumer<Game> getOnHideAction()
	{
		return onHideAction;
	}
	
	@Override
	public void reset()
	{
		path = null;
		token = null;
		game = null;
		dialogRunning = false;
		dialogScreen = null;
		onShowAction = null;
		onHideAction = null;
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
			dialogScreen.setOnShowAction(onShowAction);
			dialogScreen.setOnHideAction(onHideAction);
			return dialogScreen;
		}
		catch (ZootException e)
		{
			throw new RuntimeZootException(e);
		}	
	}
}
