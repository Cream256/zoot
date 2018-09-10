package com.zootcat.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.zootcat.dialogs.ZootDialog;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.game.ZootGame;
import com.zootcat.gfx.ZootGraphicsFactory;
import com.zootcat.scene.ZootActor;

public class ZootDialogScreen extends ScreenAdapter
{
	private ZootGame game;
	private ZootDialog dialog;
	private ZootActor triggeringActor;
	private Screen previousScreen;
	
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	
	public ZootDialogScreen(ZootGame game, ZootGraphicsFactory graphicsFactory)
	{
		this.game = game;
		this.batch = graphicsFactory.createSpriteBatch();
		this.shapeRenderer = graphicsFactory.createShapeRenderer();
	}
	
	@Override
	public void show()
	{
		if(dialog == null) throw new RuntimeZootException("No dialog was set for DialogScreen");
		dialog.rewind();
		
		previousScreen = game.getPreviousScreen();
		if(previousScreen == null) throw new RuntimeZootException("No previous screen was set for DialogScreen");
	}

	@Override
	public void render(float delta)
	{
		previousScreen.render(delta);
		
		Texture face = dialog.getCurrentFace();
		if(face != null) 
		{			
			shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(1.0f, 1.0f, 1.0f, 0.5f);
			shapeRenderer.rect(0.0f, 0.0f, Gdx.graphics.getWidth(), face.getHeight());			
			shapeRenderer.end();
			
			batch.begin();
			batch.draw(face, 0, 0);
			batch.end();
		}
	}

	@Override
	public void dispose()
	{
		batch.dispose();
	}
	
	public void setDialog(ZootDialog dialog)
	{
		this.dialog = dialog;
	}
	
	public ZootDialog getDialog()
	{
		return dialog;
	}
	
	public void setTriggeringActor(ZootActor actor)
	{
		this.triggeringActor = actor;
	}
	
	public ZootActor getTriggeringActor()
	{
		return triggeringActor;
	}
}
