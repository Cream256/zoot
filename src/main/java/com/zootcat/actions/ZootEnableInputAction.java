package com.zootcat.actions;

import com.zootcat.game.ZootGame;

public class ZootEnableInputAction extends ZootAction
{
	private ZootGame game;
	private boolean inputEnabled;
	
	@Override
	public boolean act(float delta)
	{
		game.getInputManager().enable(inputEnabled);
		return true;
	}
	
	public void setGame(ZootGame game)
	{
		this.game = game;
	}
	
	public void setInputEnabled(boolean value)
	{
		inputEnabled = value;
	}
	
	public boolean isInputEnabled()
	{
		return inputEnabled;
	}
	
	public ZootGame getGame()
	{
		return game;
	}
}
