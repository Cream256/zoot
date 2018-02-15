package com.zootcat.map.tiled;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.zootcat.gfx.ZootRender;

public class ZootTiledMapRender extends OrthogonalTiledMapRenderer implements ZootRender 
{
	private Color backgroundColor;
	private ZootTiledMapRenderConfig config;
	private ShapeRenderer shapeRenderer = new ShapeRenderer();
	
	public ZootTiledMapRender(ZootTiledMap map) 
	{
		this(map, new ZootTiledMapRenderConfig());
	}
	
	public ZootTiledMapRender(ZootTiledMap map, ZootTiledMapRenderConfig config)
	{
		super(map.getTiledMap(), config.unitScale);		
		this.config = config;
		this.backgroundColor = map.getBackgroundColor();
	}
	
	@Override
	public void render(float delta) 
	{
		super.render();
	}

	@Override
	public void setView(OrthographicCamera camera) 
	{
		super.setView(camera);
	}
	
	@Override
	public void renderObjects (MapLayer layer) 
	{
		if(config.renderTextureObjects)
		{
			renderTextureObjects(layer);
		}
		
		if(config.renderRectangleObjects)
		{
			renderRectangleObjects(layer);
		}
	}
	
	@Override
	public void renderObject (MapObject object) 
	{
		//noop, unused
	}
	
	@Override
	protected void beginRender()
	{
		if(config.clearBackground)
		{
			clearBackground();
		}
		super.beginRender();
	}

	protected void clearBackground() 
	{
		Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	protected void renderRectangleObjects(MapLayer layer) 
	{
		Array<RectangleMapObject> rectangleObjects = layer.getObjects().getByType(RectangleMapObject.class);
		if(rectangleObjects.size == 0)
		{
			return;
		}
		
		getBatch().end();		
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
		for(RectangleMapObject rectangleObject : rectangleObjects)
		{
			Rectangle rect = rectangleObject.getRectangle();
	        shapeRenderer.setColor(rectangleObject.getColor());
	        shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
		}
		shapeRenderer.end();		
		getBatch().begin();
	}

	protected void renderTextureObjects(MapLayer layer) 
	{		
		Array<TextureMapObject> textureObjects = layer.getObjects().getByType(TextureMapObject.class);
		for(TextureMapObject textureObject : textureObjects)
		{
			TextureRegion region = textureObject.getTextureRegion();
			final float y = textureObject.getY();	//drawing at bottom left corner
			
			batch.draw(region, textureObject.getX(), y,
	        textureObject.getOriginX(), textureObject.getOriginY(), region.getRegionWidth(), region.getRegionHeight(),
	        textureObject.getScaleX(), textureObject.getScaleY(), textureObject.getRotation());
		}
	}		
}
