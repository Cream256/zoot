package com.zootcat.map.tiled;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.zootcat.controllers.Controller;
import com.zootcat.controllers.factory.ControllerFactory;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.map.tiled.optimizer.ZootLayerRegion;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.tiled.ZootTiledScene;

public class ZootTiledMapActorFactory 
{
	public static final String DEFAULT_NAME = "";
	private static final String SCENE_GLOBAL_PARAM = "scene";
	private static final String ASSET_MANAGER_GLOBAL_PARAM = "assetManager";

	private float scale;
	private ControllerFactory controllerFactory;		
			
	public ZootTiledMapActorFactory(ZootTiledScene scene, ControllerFactory controllerFactory, AssetManager assetManager)
	{
		this.scale = scene.getUnitScale();
		this.controllerFactory = controllerFactory;
		this.controllerFactory.addGlobalParameter(SCENE_GLOBAL_PARAM, scene);
		this.controllerFactory.addGlobalParameter(ASSET_MANAGER_GLOBAL_PARAM, assetManager);
	}
	
	public ZootActor createFromMapObject(final MapObject mapObject)
	{		
		ZootActor actor = new ZootActor();		
		setActorBasicProperties(mapObject, actor);		
		setActorControllers(mapObject.getProperties(), actor);
		return actor;
	}
	
	public ZootActor createFromMapCell(final ZootTiledMapCell cell)
	{
		ZootActor cellActor = new ZootActor();
		cellActor.setId(cell.cell.getTile().getProperties().get("id", 0, Integer.class));		
		cellActor.setName("Cell " + cell.x + "x" + cell.y);
		cellActor.setBounds(cell.x * cell.width * scale, cell.y * cell.height * scale, cell.width * scale, cell.height * scale);
		setActorControllers(cell.cell.getTile().getProperties(), cellActor);		
		return cellActor;
	}
	
	public ZootActor createFromLayerRegion(ZootLayerRegion region)
	{
		ZootActor cellActor = new ZootActor();
		cellActor.setId(region.cell.getTile().getProperties().get("id", 0, Integer.class));		
		cellActor.setName("Region (" + region.x + "," + region.y + ";" + region.width + "," + region.height + ")");
		cellActor.setBounds(region.x * region.tileWidth * scale, 
							region.y * region.tileHeight * scale, 
							region.width * region.tileWidth * scale,
							region.height * region.tileHeight * scale);
		setActorControllers(region.cell.getTile().getProperties(), cellActor);		
		return cellActor;
	}
	
	public List<ZootActor> createFromMapCells(final List<ZootTiledMapCell> cells) 
	{
		return cells.stream().map(cell -> createFromMapCell(cell)).collect(Collectors.toList());
	}
	
	public List<ZootActor> createFromMapObjects(final Collection<MapObject> objects)
	{
		return objects.stream().map(obj -> createFromMapObject(obj)).collect(Collectors.toList());
	}
	
	public List<ZootActor> createFromLayerRegions(final List<ZootLayerRegion> regions)
	{
		return regions.stream().map(obj -> createFromLayerRegion(obj)).collect(Collectors.toList());
	}
		
	protected void setActorBasicProperties(final MapObject mapObject, ZootActor actor) 
	{
		actor.setName(getNameOrDefault(mapObject));
		actor.setColor(mapObject.getColor());
		actor.setVisible(mapObject.isVisible());
		actor.setOpacity(mapObject.getOpacity());
		actor.setId(Integer.valueOf(getPropertyOrThrow(mapObject, "id")));
		actor.setGid(Integer.valueOf(getPropertyOrDefault(mapObject, "gid", "-1")));		
		actor.setRotation(Float.valueOf(getPropertyOrDefault(mapObject, "rotation", "0.0f")));
				
		boolean isPolygon = ClassReflection.isInstance(PolygonMapObject.class, mapObject);
		if(isPolygon)
		{
			Rectangle boundingRectangle = ((PolygonMapObject)mapObject).getPolygon().getBoundingRectangle();
			float x = boundingRectangle.getX() * scale;
			float y = boundingRectangle.getY() * scale;
			float width = boundingRectangle.getWidth() * scale;
			float height = boundingRectangle.getHeight() * scale;
			actor.setBounds(x, y, width, height);
		}
		else
		{
			float x = Float.valueOf(getPropertyOrThrow(mapObject, "x")) * scale;
			float y = Float.valueOf(getPropertyOrThrow(mapObject, "y")) * scale;
			float width = Float.valueOf(getPropertyOrThrow(mapObject, "width")) * scale;
			float height = Float.valueOf(getPropertyOrThrow(mapObject, "height")) * scale;
			actor.setBounds(x, y, width, height);
		}
	}

	private String getNameOrDefault(final MapObject mapObject)
	{
		String name = mapObject.getName();
		return name != null ? name : DEFAULT_NAME;
	}

	protected void setActorControllers(final MapProperties actorProperties, ZootActor actor)
	{		
		List<Controller> createdControllers = new ArrayList<Controller>();
		
		actorProperties.getKeys().forEachRemaining(ctrlName ->
		{								
			if(controllerFactory.contains(ctrlName))
			{
				String controllerParams = actorProperties.get(ctrlName, String.class);
				Controller controller = controllerFactory.create(ctrlName, controllerParams);
				controller.init(actor);		//initialize first, then assign
				createdControllers.add(controller);
			}
		});
		
		actor.addControllers(createdControllers);
	}
			
	protected String getPropertyOrDefault(MapObject mapObject, String key, String defaultValue)
	{
		Object value = mapObject.getProperties().get(key);
		return value != null ? value.toString() : defaultValue;	
	}
	
	protected String getPropertyOrThrow(MapObject mapObject, String key)
	{
		if(!mapObject.getProperties().containsKey(key))
		{
			throw new RuntimeZootException("Object property missing: " + key + " for object with name: " + getNameOrDefault(mapObject));
		}
		return mapObject.getProperties().get(key).toString();
	}
}
