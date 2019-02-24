package com.zootcat.map.tiled;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.zootcat.controllers.factory.ControllerFactory;
import com.zootcat.controllers.factory.mocks.Mock1Controller;
import com.zootcat.controllers.factory.mocks.Mock2Controller;
import com.zootcat.controllers.factory.mocks.SimpleController;
import com.zootcat.controllers.factory.mocks.inner.Mock3Controller;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.map.tiled.optimizer.ZootLayerRegion;
import com.zootcat.physics.ZootPhysics;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.tiled.ZootTiledScene;

public class ZootTiledSceneActorFactoryTest
{	
	private static final String CTRL_PACKAGE = "com.zootcat.controllers.factory.mocks";
	private static final String ACTOR_NAME = "abc";
	private static final Color ACTOR_COLOR = Color.RED;
	private static final float ACTOR_X = 1.0f;
	private static final float ACTOR_Y = 2.0f;
	private static final float ACTOR_WIDTH = 100.0f;
	private static final float ACTOR_HEIGHT = 200.0f;
	private static final float ACTOR_OPACITY = 0.0f;
	private static final float ACTOR_ROTATION = 45.0f;
	private static final boolean ACTOR_VISIBLE = false;
	private static final int CELL_X = 5;
	private static final int CELL_Y = 7;
	private static final float CELL_WIDTH = 32;
	private static final float CELL_HEIGHT = 48;
	private static final int ACTOR_ID = 1;
			
	@Mock private ZootTiledMap mapMock;
	@Mock private ZootTiledScene sceneMock;	
	@Mock private ZootPhysics physicsMock;
	@Mock private TiledMapTile tile;
	@Mock private Cell innerCell;
	private MapProperties tileProperties;
	private ZootTiledSceneActorFactory factory;
	private ControllerFactory ctrlFactory;
		
	@Before
	public void setup()
	{				
		MockitoAnnotations.initMocks(this);
		
		//mock scene
		when(physicsMock.createBody(any(BodyDef.class))).thenReturn(mock(Body.class));		
		when(sceneMock.getPhysics()).thenReturn(physicsMock);
		when(sceneMock.getUnitScale()).thenReturn(1.0f);
		when(sceneMock.getAssetManager()).thenReturn(mock(AssetManager.class));
		when(sceneMock.getTiledMap()).thenReturn(mapMock);
		ctrlFactory = new ControllerFactory();
		when(sceneMock.getControllerFactory()).thenReturn(ctrlFactory);
		
		//mock tile and inner cell
		tileProperties = new MapProperties();
		tileProperties.put("id", ACTOR_ID);
		
		tile = mock(TiledMapTile.class);
		when(tile.getProperties()).thenReturn(tileProperties);
		
		innerCell = mock(Cell.class);
		when(innerCell.getTile()).thenReturn(tile);
		when(innerCell.getTile().getProperties()).thenReturn(tileProperties);
		
		//create factory
		factory = new ZootTiledSceneActorFactory(sceneMock);
	}
			
	@Test(expected = RuntimeZootException.class)
	public void createFromMapObjectShuoldThrowIfBasicPropertiesAreNotInPlaceTest()
	{
		//given
		MapObject mapObject = new MapObject();
				
		//then
		factory.createFromMapObject(mapObject);
	}
	
	@Test(expected = NumberFormatException.class)
	public void createFromMapObjectShouldThrowNumberFormatExceptionIfIntegerValuesAreWrong()
	{
		//given
		MapObject mapObject = new MapObject();
		mapObject.getProperties().put("id", "wrongValue");
				
		//then
		factory.createFromMapObject(mapObject);
	}
	
	@Test
	public void createFromMapObjectShouldAddControllerWithNotParamsTest()
	{
		//given
		ctrlFactory.addFromPackage(CTRL_PACKAGE, true);
		MapObject mapObject = createDefaultMapObject();
		mapObject.getProperties().put("Mock1Controller", "");
		
		//when
		ZootActor actor = factory.createFromMapObject(mapObject);
		actor.act(0.0f);
		
		//then
		assertFalse(actor.getControllers(Mock1Controller.class).isEmpty());
	}

	@Test
	public void createFromMapObjectShouldAddControllerWithParamsTest()
	{
		//given
		ctrlFactory.addFromPackage(CTRL_PACKAGE, true);
		MapObject mapObject = createDefaultMapObject();
		mapObject.getProperties().put("Mock2Controller", "a=1, b=2.2f, c=string");
		
		//when
		ZootActor actor = factory.createFromMapObject(mapObject);
		actor.act(0.0f);
		
		//then
		Mock2Controller ctrl = actor.getSingleController(Mock2Controller.class);
		assertNotNull(ctrl);
		assertEquals(1, ctrl.a);
		assertEquals(2.2f, ctrl.b, 0.0f);
		assertEquals("string", ctrl.c);
	}
	
	@Test
	public void createFromMapObjectShouldAddControllerWithSceneGlobalParamTest()
	{
		//given
		ctrlFactory.addFromPackage(CTRL_PACKAGE, true);
		MapObject mapObject = createDefaultMapObject();
		mapObject.getProperties().put("Mock3Controller", "param = 100");
		
		//then
		ZootActor actor = factory.createFromMapObject(mapObject);
		actor.act(0.0f);
		
		//then
		Mock3Controller ctrl = actor.getSingleController(Mock3Controller.class);
		assertNotNull(ctrl);
		assertEquals(100, ctrl.param);
		assertEquals(sceneMock, ctrl.scene);
	}
	
	@Test(expected = RuntimeZootException.class)
	public void createFromMapObjectShouldThrowWhenControllerParamsAreWrongTest()
	{
		//given
		ctrlFactory.addFromPackage(CTRL_PACKAGE, true);
		MapObject mapObject = createDefaultMapObject();
		mapObject.getProperties().put("Mock2Controller", "1, string");
		
		//then
		factory.createFromMapObject(mapObject);
	}
	
	@Test
	public void createFromMapObjectShouldSetActorBasicPropertiesTest() 
	{
		//given
		MapObject mapObject = createDefaultMapObject();
		
		//when
		ZootActor actor = factory.createFromMapObject(mapObject);
		
		//then
		assertNotNull(actor);
		assertEquals(mapObject.getName(), actor.getName());
		assertEquals(mapObject.getColor(), actor.getColor());
		assertEquals(mapObject.getOpacity(), actor.getOpacity(), 0.0f);
		assertEquals(mapObject.isVisible(), actor.isVisible());
		assertEquals(ACTOR_ID, actor.getId());
		assertEquals(ACTOR_X, actor.getX(), 0.0f);
		assertEquals(ACTOR_Y, actor.getY(), 0.0f);
		assertEquals(ACTOR_WIDTH, actor.getWidth(), 0.0f);
		assertEquals(ACTOR_HEIGHT, actor.getHeight(), 0.0f);
		assertEquals(ACTOR_ROTATION, actor.getRotation(), 0.0f);
	}
	
	@Test
	public void createFromMapObjectShouldSetDefaultNameIfNotPresentTest()
	{
		//given
		MapObject mapObject = createDefaultMapObject();
		
		//when
		mapObject.setName(null);
		ZootActor actor = factory.createFromMapObject(mapObject);
		
		//then
		assertNotNull(actor);
		assertEquals(ZootTiledSceneActorFactory.DEFAULT_NAME, actor.getName());
	}
	
	@Test
	public void createFromMapCellShouldSetBasicPropertiesTestCellTest()
	{
		//given
		ZootTiledMapCell cell = createDefaultCell();
		
		//when
		ZootActor actor = factory.createFromMapCell(cell);
		
		//then
		assertNotNull(actor);
		assertEquals(ACTOR_ID, actor.getId());
		assertEquals(CELL_X * CELL_WIDTH, actor.getX(), 0.0f);
		assertEquals(CELL_Y * CELL_HEIGHT, actor.getY(), 0.0f);
		assertEquals(CELL_WIDTH, actor.getWidth(), 0.0f);
		assertEquals(CELL_HEIGHT, actor.getHeight(), 0.0f);
		assertEquals(0.0f, actor.getRotation(), 0.0f);
		assertFalse(actor.getName().isEmpty());
	}

	public void createFromMapCellShouldNotSetAnyControllersWhenThereAreNoneInPropertiesTest()
	{
		//given
		ZootTiledMapCell cell = createDefaultCell();
		
		//when
		ZootActor actor = factory.createFromMapCell(cell);
		
		//then
		assertNotNull(actor);
		assertTrue(actor.getAllControllers().isEmpty());
	}
	
	@Test
	public void createFromMapCellShouldCreateControllersForActorTest()
	{
		//given				
		ZootTiledMapCell cell = createDefaultCell();
		tileProperties.put(SimpleController.class.getSimpleName(), "");
		ctrlFactory.addFromPackage("com.zootcat.controllers.factory.mocks", false);
		
		//when		
		ZootActor actor = factory.createFromMapCell(cell);
		
		//then
		assertNotNull(actor);
		assertNotNull(actor.getSingleController(SimpleController.class));
	}
		
	@Test
	public void createFromLayerRegionTest()
	{
		//given
		final int x = 5;
		final int y = 6;
		final int tileWidth = 32;
		final int tileHeight = 24;
		final int regionWidth = 3;
		final int regionHeight = 4;
		
		ZootLayerRegion region = new ZootLayerRegion(x, y, tileWidth, tileHeight, innerCell);
		region.width = regionWidth;
		region.height = regionHeight;
		
		tileProperties.put(SimpleController.class.getSimpleName(), "");
		ctrlFactory.addFromPackage("com.zootcat.controllers.factory.mocks", false);
		
		//when
		ZootActor actor = factory.createFromLayerRegion(region);
		
		//then
		assertNotNull(actor);
		assertEquals(1, actor.getId());
		assertEquals(x * tileWidth, actor.getX(), 0.0f);
		assertEquals(y * tileHeight, actor.getY(), 0.0f);
		assertEquals(tileWidth * regionWidth, actor.getWidth(), 0.0f);
		assertEquals(tileHeight * regionHeight, actor.getHeight(), 0.0f);
		assertFalse(actor.getName().isEmpty());
		assertNotNull(actor.getSingleController(SimpleController.class));		
	}
	
	@Test
	public void shouldReturnActorWithValidTileProperties()
	{
		//given
		final int expectedTileId = 10;
		final int expectedActorId = 1;
		final float expectedWidth = 123.0f;
		final float expectedHeight = 321.0f;
		final String expectedName = "Actor name";		
		StaticTiledMapTile tile = new StaticTiledMapTile(mock(TextureRegion.class));
				
		//when
		tile.getProperties().put("name", expectedName);		
		tile.getProperties().put("id", expectedTileId);
		tile.getProperties().put("width", expectedWidth);
		tile.getProperties().put("height", expectedHeight);
		ZootActor createdActor = factory.createFromTile(tile);
		
		//then
		assertNotNull(createdActor);
		assertEquals(expectedName, createdActor.getName());
		assertEquals(expectedTileId, createdActor.getGid());
		assertEquals(expectedActorId, createdActor.getId());
		assertEquals(expectedWidth, createdActor.getWidth(), 0.0f);
		assertEquals(expectedHeight, createdActor.getHeight(), 0.0f);
	}
	
	@Test
	public void shouldReturnActorWithIdGreaterThanSceneMaxId()
	{
		//given
		ZootActor actor1 = new ZootActor();	actor1.setId(200);		
		ZootActor actor2 = new ZootActor(); actor2.setId(100);
				
		//when
		when(sceneMock.getActors()).thenReturn(Arrays.asList(actor1, actor2));		
		ZootActor createdActor = factory.createFromTile(new StaticTiledMapTile(mock(TextureRegion.class)));
		
		//then
		assertNotNull(createdActor);
		assertEquals(201, createdActor.getId());		
	}
	
	@Test
	public void shouldReturnActorWithIdGreaterThanMapObjectsMaxId()
	{
		//given
		MapObject mapObject1 = new MapObject();	mapObject1.getProperties().put("id", 400);		
		MapObject mapObject2 = new MapObject();	mapObject2.getProperties().put("id", 500);
		
		//when
		when(mapMock.getAllObjects()).thenReturn(Arrays.asList(mapObject1, mapObject2));
		ZootActor createdActor = factory.createFromTile(new StaticTiledMapTile(mock(TextureRegion.class)));
		
		//then
		assertNotNull(createdActor);
		assertEquals(501, createdActor.getId());		
	}
	
	@Test
	public void shouldReturnActorWithIdGreaterThanMapObjectsMaxIdAndSceneActorsMaxId()
	{
		//given
		ZootActor actor1 = new ZootActor(); actor1.setId(1);
		ZootActor actor2 = new ZootActor();	actor2.setId(2);
		MapObject mapObject1 = new MapObject();	mapObject1.getProperties().put("id", 3);
		MapObject mapObject2 = new MapObject();	mapObject2.getProperties().put("id", 4);
		StaticTiledMapTile tile = new StaticTiledMapTile(mock(TextureRegion.class));
				
		//when
		when(sceneMock.getActors()).thenReturn(Arrays.asList(actor1, actor2));		
		when(mapMock.getAllObjects()).thenReturn(Arrays.asList(mapObject1, mapObject2));	
		ZootActor createdActor = factory.createFromTile(tile);
		
		//then
		assertNotNull(createdActor);
		assertEquals(5, createdActor.getId());
	}
	
	private ZootTiledMapCell createDefaultCell()
	{		
		return new ZootTiledMapCell(CELL_X, CELL_Y, CELL_WIDTH, CELL_HEIGHT, innerCell);
	}
	
	private MapObject createDefaultMapObject()
	{		
		MapObject mapObject = new MapObject();		
		mapObject.setName(ACTOR_NAME);
		mapObject.setColor(ACTOR_COLOR);
		mapObject.setOpacity(ACTOR_OPACITY);
		mapObject.setVisible(ACTOR_VISIBLE);
		mapObject.getProperties().put("id",ACTOR_ID);
		mapObject.getProperties().put("x", ACTOR_X);
		mapObject.getProperties().put("y", ACTOR_Y);
		mapObject.getProperties().put("width", ACTOR_WIDTH);
		mapObject.getProperties().put("height", ACTOR_HEIGHT);
		mapObject.getProperties().put("rotation", ACTOR_ROTATION);
		return mapObject;
	}
}
