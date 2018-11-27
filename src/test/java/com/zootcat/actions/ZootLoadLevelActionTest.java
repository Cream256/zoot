package com.zootcat.actions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.zootcat.game.ZootGame;

public class ZootLoadLevelActionTest
{
	private static final String NEXT_LEVEL_PATH = "/data/next.tmx";
	private ZootLoadLevelAction action;
	@Mock private ZootGame game;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		action = new ZootLoadLevelAction();
		action.setZootGame(game);
		action.setLevelPath(NEXT_LEVEL_PATH);
	}
	
	@Test
	public void shouldLoadLevel()
	{
		//when
		action.act(0.0f);
		
		//then
		verify(game).loadLevel(NEXT_LEVEL_PATH);		
	}
	
	@Test
	public void shouldReturnTrue()
	{
		assertTrue(action.act(0.0f));		
	}
	
	@Test
	public void shouldSetZootGame()
	{
		assertEquals(game, action.getZootGame());
	}
	
	@Test
	public void shouldSetLevelPath()
	{
		assertEquals(NEXT_LEVEL_PATH, action.getLevelPath());
	}
	
}
