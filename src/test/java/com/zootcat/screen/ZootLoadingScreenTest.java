package com.zootcat.screen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.function.Consumer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.assets.AssetManager;
import com.zootcat.game.ZootGame;

public class ZootLoadingScreenTest
{
	@Mock private ZootGame game;
	@Mock private AssetManager assetManager;	
	@Mock private Consumer<Void> task;
	
	private boolean success;
	private ZootLoadingScreen screen;
		
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		when(assetManager.update()).thenReturn(true);
		when(game.getAssetManager()).thenReturn(assetManager);
		
		success = false;
		screen = new ZootLoadingScreen(game);
	}
	
	@Test
	public void getAssetManagerTest()
	{
		assertEquals(game.getAssetManager(), screen.getAssetManager());
	}
	
	@Test
	public void coverEmptyFunctionsTest()
	{
		screen.resize(0, 0);
		screen.pause();
		screen.resume();
		screen.hide();
		screen.dispose();
	}
	
	@Test
	public void onRenderWhileLoadingTest()
	{
		//given
		screen.onRenderWhileLoading = (dt) -> { success = true; };
		screen.addTask(task);
		screen.addTask(task);
		
		//when
		screen.show();
		screen.render(0.0f);
		
		//then
		assertTrue("onRenderWhileLoading should execute while loading", success);
		
		//when
		success = false;
		screen.render(0.0f);
		
		//then
		assertTrue("onRenderWhileLoading should execute while loading", success);
		
		//when
		success = false;
		screen.render(0.0f);
		
		//then
		assertFalse("onRenderWhileLoading should not execute after load is complete", success);
	}
	
	@Test
	public void onRenderAfterLoadingTest()
	{
		//given
		screen.onRenderAfterLoading = (dt) -> { success = true; };
		screen.addTask(task);
		screen.addTask(task);
		
		//when
		screen.show();
		screen.render(0.0f);
		
		//then
		assertFalse("onRenderAfterLoading should not execute while loading", success);
		
		//when
		success = false;
		screen.render(0.0f);
		
		//then
		assertFalse("onRenderAfterLoading should not execute while loading", success);
		
		//when
		success = false;
		screen.render(0.0f);
		
		//then
		assertTrue("onRenderAfterLoading should execute after load is complete", success);
	}
	
	@Test
	public void onFinishLoadingTest()
	{
		//given
		screen.onFinishLoading = (game) -> { success = true; };		
		screen.addTask(task);
		screen.addTask(task);
		
		//when
		screen.show();
		
		//then
		assertFalse("onFinishLoading should not execute yet", success);
		
		//when
		screen.render(0.0f);
		
		//then
		assertFalse("onFinishLoading should not execute yet", success);

		//when
		screen.render(0.0f);
		
		//then		
		assertEquals(1.0f, screen.getProgress(), 0.0f);
		assertTrue("onFinishLoading should execute after load is complete", success);
	}
	
	@Test
	public void getProgressTest()
	{		
		assertEquals(0.0f, screen.getProgress(), 0.0f);
		
		screen.addTask(task);
		screen.addTask(task);
		screen.addTask(task);
		screen.addTask(task);
		assertEquals(0.0f, screen.getProgress(), 0.0f);
		
		screen.show();
		assertEquals(0.00f, screen.getProgress(), 0.0f);
		
		screen.render(0.0f);
		assertEquals(0.25f, screen.getProgress(), 0.0f);
		
		screen.render(0.0f);
		assertEquals(0.50f, screen.getProgress(), 0.0f);
		
		screen.render(0.0f);
		assertEquals(0.75f, screen.getProgress(), 0.0f);
		
		screen.render(0.0f);
		assertEquals(1.0f, screen.getProgress(), 0.0f);
		
		screen.render(0.0f);
		assertEquals("Should not go over 100%", 1.0f, screen.getProgress(), 0.0f);
	}
}
