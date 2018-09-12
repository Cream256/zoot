package com.zootcat.actions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.zootcat.game.ZootGame;
import com.zootcat.input.ZootInputManager;

public class ZootEnableInputActionTest
{
	@Mock private ZootGame game;
	@Mock private ZootInputManager inputManager;
	private ZootEnableInputAction action;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		when(game.getInputManager()).thenReturn(inputManager);
		
		action = new ZootEnableInputAction();
	}
	
	@Test
	public void shouldSetGame()
	{
		action.setGame(game);
		assertEquals(game, action.getGame());		
	}
	
	@Test
	public void shouldSetInputEnabled()
	{
		action.setInputEnabled(true);
		assertTrue(action.isInputEnabled());
		
		action.setInputEnabled(false);
		assertFalse(action.isInputEnabled());
	}
	
	@Test
	public void shouldReturnTrueOnAct()
	{
		action.setGame(game);
		assertTrue(action.act(1.0f));
	}
	
	@Test
	public void shouldEnableInputManager()
	{
		action.setGame(game);
		action.setInputEnabled(true);		
		action.act(1.0f);
		
		verify(inputManager).enable(true);
	}
	
	@Test
	public void shouldDisableInputManager()
	{
		action.setGame(game);
		action.setInputEnabled(false);		
		action.act(1.0f);
		
		verify(inputManager).enable(false);
	}
}
