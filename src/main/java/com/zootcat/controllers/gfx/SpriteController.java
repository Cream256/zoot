package com.zootcat.controllers.gfx;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.scene.ZootActor;

public class SpriteController extends RenderControllerAdapter
{
	@CtrlParam(required = true) private String file;
	@CtrlParam(global = true) private AssetManager assetManager;
	
	private Sprite sprite;
	
	@Override
	public void init(ZootActor actor)
	{
		sprite = new Sprite(assetManager.get(file, Texture.class));
	}
	
	@Override
	public void onAdd(ZootActor actor) 
	{
		updateSprite(actor);
	}
	
	@Override
	public void onRemove(ZootActor actor) 
	{
		sprite = null;
	}
	
	@Override
	public void onUpdate(float delta, ZootActor actor) 
	{
		//noop
	}

	@Override
	public void onRender(Batch batch, float parentAlpha, ZootActor actor, float delta) 
	{		
		updateSprite(actor);
		sprite.draw(batch);
	}
	
	protected void updateSprite(ZootActor actor)
	{		
		sprite.setBounds(actor.getX() + getOffsetX(), actor.getY() + getOffsetY(), actor.getWidth(), actor.getHeight());
		sprite.setOriginCenter();		
		sprite.setRotation(actor.getRotation());
	}
}
