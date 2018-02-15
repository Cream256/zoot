package com.zootcat.game;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

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
	public void disposeTest()
	{
		game.dispose();
		assertNull(game.getAssetManager());
	}
	
	@Test
	public void getAssetManagerTest()
	{
		assertNotNull(game.getAssetManager());
	}
	
	@Test
	public void setViewportWidthTest()
	{
		game.setViewportWidth(10.0f);
		assertEquals(10.0f, game.getViewportWidth(), 0.0f);
	}
	
	@Test
	public void setViewportHeightTest()
	{
		game.setViewportHeight(20.0f);
		assertEquals(20.0f, game.getViewportHeight(), 0.0f);
	}
	
	@Test
	public void setUnitPerTileTest()
	{
		game.setUnitPerTile(2.15f);
		assertEquals(2.15f, game.getUnitPerTile(), 0.0f);
	}
}
