package com.zootcat.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.zootcat.assets.ZootAssetManager;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.scene.ZootScene;
import com.zootcat.screen.ZootLoadingScreen;
import com.zootcat.screen.ZootSceneScreen;
import com.zootcat.screen.ZootScreen;

public class ZootGameTest
{
	private ZootGame game;
	private boolean onCreateCalled;
	private boolean onDisposeCalled;
	
	@Before
	public void setup()
	{
		Gdx.input = mock(Input.class);
		Gdx.graphics = mock(Graphics.class);
				
		onCreateCalled = false;
		onDisposeCalled = false;
		game = new ZootGame()
		{
			@Override
			public void onCreate()
			{
				onCreateCalled = true;
			}

			@Override
			public void onDispose()
			{
				onDisposeCalled = true;
			}};
	}
	
	@After
	public void tearDown()
	{
		Gdx.input = null;
		Gdx.graphics = null;
	}
	
	@Test
	public void shouldCallOnCreateDuringCreation()
	{
		game.create();
		assertTrue(onCreateCalled);
	}
	
	@Test
	public void shouldCallOnDisposeDuringDisposing()
	{
		game.dispose();
		assertTrue(onDisposeCalled);
	}
	
	@Test
	public void shouldDisposeScreens()
	{
		//given
		ZootScreen screen1 = mock(ZootScreen.class);
		ZootScreen screen2 = mock(ZootScreen.class);
		
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
		ZootScreen screen1 = mock(ZootScreen.class);
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
	public void shouldSetGraphicsFactoryGlobalParameterForControllerFactory()
	{
		assertNotNull(game.getControllerFactory().getGlobalParameters().get("graphicsFactory"));
	}
	
	@Test
	public void shouldSetScreen()
	{
		ZootScreen screen = mock(ZootScreen.class);
		game.setScreen(screen);
		
		assertEquals(screen, game.getScreen());
	}
	
	@Test(expected = RuntimeZootException.class)
	public void shouldThrowIfScreenIsNotZootScreen()
	{
		Screen screen = mock(Screen.class);
		game.setScreen(screen);		
	}
	
	@Test
	public void shouldReturnEmptyInputManagerAfterCreation()
	{
		assertNotNull(game.getInputManager());
		assertEquals(0, game.getInputManager().getProcessorsCount());
	}
	
	@Test
	public void shouldReturnPreviousScreen()
	{
		//given
		ZootScreen screen1 = mock(ZootScreen.class);
		ZootScreen screen2 = mock(ZootScreen.class);
		
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
	
	@Test
	public void shouldCreateLoadingScreen()
	{
		//given
		ZootLoadingScreen screen = mock(ZootLoadingScreen.class);
		
		//when
		game.setLoadingScreenSupplier(game -> screen);
		
		//then
		assertEquals(screen, game.createLoadingScreen());
	}
	
	@Test
	public void shouldCreateSceenScreen()
	{
		//given
		ZootScene scene = mock(ZootScene.class);
		ZootSceneScreen screen = mock(ZootSceneScreen.class);
		
		//when
		game.setSceneScreenSupplier((g, s) -> screen);
		
		//then
		assertEquals(screen, game.createSceneScreen(scene));
	}
	
	@Test
	public void shouldReturnGraphicsFactory()
	{
		assertNotNull(game.getGraphicsFactory());
	}
}
