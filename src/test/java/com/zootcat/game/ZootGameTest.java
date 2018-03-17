package com.zootcat.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.zootcat.assets.ZootAssetManager;

public class ZootGameTest
{
	private ZootGame game;
	
	@Before
	public void setup()
	{
		game = new ZootGame(){
			@Override
			public void create()
			{
				//noop
			}};
	}
	
	@Test
	public void shouldDispose()
	{
		game.dispose();
		assertNull(game.getAssetManager());
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
}
