package com.zootcat.actions;

import com.zootcat.game.ZootGame;

public class ZootLoadLevelAction extends ZootAction
{
	private String levelPath;
	private ZootGame game;
	
	public void setZootGame(ZootGame game)
	{
		this.game = game;
	}
	
	public ZootGame getZootGame()
	{
		return game;
	}
	
	public void setLevelPath(String path)
	{
		levelPath = path;
	}
	
	public String getLevelPath()
	{
		return levelPath;
	}
		
	@Override
	public boolean act(float delta)
	{
		game.loadLevel(levelPath);
		return true;
	}
}
