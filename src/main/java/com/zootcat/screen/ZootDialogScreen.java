package com.zootcat.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.zootcat.dialogs.ZootDialog;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.game.ZootGame;
import com.zootcat.scene.ZootActor;

//TODO add test
public class ZootDialogScreen implements Screen
{
	private ZootGame game;
	private ZootDialog dialog;
	private ZootActor triggeringActor;
	private Screen previousScreen;
	private SpriteBatch batch;
	
	public ZootDialogScreen(ZootGame game)
	{
		this.game = game;
		this.batch = new SpriteBatch();
	}
	
	@Override
	public void show()
	{
		if(dialog == null) throw new RuntimeZootException("No dialog was set for DialogScreen");
		dialog.rewind();
		
		previousScreen = game.getPreviousScreen();
		if(previousScreen == null)  throw new RuntimeZootException("No previous screen was set for DialogScreen");
	}

	@Override
	public void render(float delta)
	{
		previousScreen.render(delta);
		
		Texture face = dialog.getCurrentFace();
		if(face != null) batch.draw(face, 0, 0);
	}

	@Override
	public void resize(int width, int height)
	{
		//noop
	}

	@Override
	public void pause()
	{
		//noop
	}

	@Override
	public void resume()
	{
		//noop
	}

	@Override
	public void hide()
	{
		//noop
	}

	@Override
	public void dispose()
	{
		//noop
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
