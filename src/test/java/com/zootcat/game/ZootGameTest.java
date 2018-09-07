package com.zootcat.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.zootcat.assets.ZootAssetManager;

public class ZootGameTest
{
	private ZootGame game;
	
	@Before
	public void setup()
	{
		Gdx.graphics = mock(Graphics.class);
		
		game = new ZootGame(){
			@Override
			public void create()
			{
				//noop
			}};
	}
	
	@After
	public void tearDown()
	{
		Gdx.graphics = null;
	}
	
	@Test
	public void shouldDisposeScreens()
	{
		//given
		Screen screen1 = mock(Screen.class);
		Screen screen2 = mock(Screen.class);
		
		//when
		game.setScreen(screen1);
		game.setScreen(screen2);
		
		//then
		assertEquals(screen2, game.getScreen());
		assertEquals(screen1, game.getPreviousScreen());
		
		//when
		game.dispose();
		
		//then
		assertNull(game.getAssetManager());
		assertNull(game.getScreen());
		assertNull(game.getPreviousScreen());
		
		InOrder inOrder = inOrder(screen1, screen2);
		inOrder.verify(screen1).dispose();
		inOrder.verify(screen2).dispose();
	}
	
	@Test
	public void shouldHideCurrentSceneOnDispose()
	{
		//given
		Screen screen1 = mock(Screen.class);
		game.setScreen(screen1);
		
		//when
		game.dispose();
		
		//then
		verify(screen1).hide();
	}
	
	@Test
	public void shouldReturnZootAssetManager()
	{
		assertNotNull(game.getAssetManager());
		assertTrue(ClassReflection.isInstance(ZootAssetManager.class, game.getAssetManager()));
	}
	
	@Test
	public void shouldSetViewportWidth()
	{
		game.setViewportWidth(10.0f);
		assertEquals(10.0f, game.getViewportWidth(), 0.0f);
	}
	
	@Test
	public void shouldSetViewportHeight()
	{
		game.setViewportHeight(20.0f);
		assertEquals(20.0f, game.getViewportHeight(), 0.0f);
	}
	
	@Test
	public void shouldSetUnitPerTile()
	{
		game.setUnitPerTile(2.15f);
		assertEquals(2.15f, game.getUnitPerTile(), 0.0f);
	}
	
	@Test
	public void shouldReturnControllerFactory()
	{
		assertNotNull(game.getControllerFactory());
	}
	
	@Test
	public void shouldSetGameAsGlobalParameterForControllerFactory()
	{
		assertEquals(game, game.getControllerFactory().getGlobalParameters().get("game"));
	}
	
	@Test
	public void shouldReturnPreviousScreen()
	{
		//given
		Screen screen1 = mock(Screen.class);
		Screen screen2 = mock(Screen.class);
		
		//then
		assertNull(game.getPreviousScreen());
		
		//when
		game.setScreen(screen1);
		
		//then
		assertNull(game.getPreviousScreen());
		
		//when
		game.setScreen(screen2);
		
		//then
		assertEquals(screen1, game.getPreviousScreen());
		
		//when
		game.setScreen(screen1);
		
		//then
		assertEquals(screen2, game.getPreviousScreen());		
	}
}
