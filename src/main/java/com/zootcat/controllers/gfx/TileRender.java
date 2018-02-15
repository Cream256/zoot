package com.zootcat.controllers.gfx;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;

public class TileRender extends ControllerAdapter implements RenderController
{
	@CtrlParam(global = true) private ZootScene scene;	
	
	private Sprite sprite;
	private TextureRegion textureRegion;
		
	@Override
	public void init(ZootActor actor)
	{
		TiledMapTile tile = scene.getMap().getTilesets().getTile(actor.getGid());
		textureRegion = tile != null ? tile.getTextureRegion() : null;
		sprite = textureRegion != null ? new Sprite(textureRegion) : null;
	}

	@Override
	public void onRender(Batch batch, float parentAlpha, ZootActor actor, float delta)
	{
		if(textureRegion == null || sprite == null)
		{
			return;
		}
				
		sprite.setBounds(actor.getX(), actor.getY(), actor.getWidth(), actor.getHeight());
		sprite.setOriginCenter();
		sprite.setRotation(actor.getRotation());
		sprite.draw(batch);
	}
}
