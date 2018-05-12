package com.zootcat.scene.tiled;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.MapObject;
import com.zootcat.assets.ZootAssetManager;
import com.zootcat.controllers.factory.ControllerFactory;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.map.tiled.ZootTiledMap;
import com.zootcat.scene.ZootActor;

//TODO finish 
public class ZootTiledSceneActorSpawnerTest
{	
	@Mock private ZootTiledScene zootTiledScene;
	@Mock private ZootTiledMap zootTiledMap;
	@Mock private ZootAssetManager zootAssetManager;
	@Mock private ControllerFactory ctrlFactory;
		
	private ZootTiledSceneActorSpawner spawner;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);		
		when(zootTiledScene.getAssetManager()).thenReturn(zootAssetManager);
		when(zootTiledScene.getControllerFactory()).thenReturn(ctrlFactory);
		when(zootTiledScene.getUnitScale()).thenReturn(1.0f);
		when(zootTiledScene.getMap()).thenReturn(zootTiledMap);
		
		spawner = new ZootTiledSceneActorSpawner(zootTiledScene);
	}
	
	@Test(expected = RuntimeZootException.class)
	public void shouldThrowIfIdIsNotFound()
	{
		spawner.spawn("", 0);
	}
	
	@Test	//TODO
	public void spawnByNameShouldReturnValidActor()
	{
		//given		
		String expectedActorName = "Skeleton";		
		final int expectedId = 1;
		final float expectedX = 128.0f;
		final float expectedY = 256.0f;
		final float expectedWidth = 123.0f;
		final float expectedHeight = 321.0f;
		final Color expectedColor = Color.WHITE;
		
		//when
		MapObject mapObj = new MapObject();
		mapObj.setName(expectedActorName);
		mapObj.setColor(expectedColor);
		mapObj.getProperties().put("id", expectedId);
		mapObj.getProperties().put("x", expectedX);
		mapObj.getProperties().put("y", expectedY);
		mapObj.getProperties().put("width", expectedWidth);
		mapObj.getProperties().put("height", expectedHeight);
		when(zootTiledMap.getObjectById(expectedId)).thenReturn(mapObj);
		
		//then
		ZootActor actor = spawner.spawn("", expectedId);
		assertNotNull(actor);
		assertEquals(expectedActorName, actor.getName());
		assertEquals(expectedColor, actor.getColor());
		assertEquals(expectedId, actor.getId());
		assertEquals(expectedX, actor.getX(), 0.0f);
		assertEquals(expectedY, actor.getY(), 0.0f);
		assertEquals(expectedWidth, actor.getWidth(), 0.0f);
		assertEquals(expectedHeight, actor.getHeight(), 0.0f);
	}
}
