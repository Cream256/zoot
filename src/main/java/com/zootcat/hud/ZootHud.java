package com.zootcat.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class ZootHud implements Disposable
{	
	private Skin skin;
	private Stage stage;
	
	public ZootHud()
	{
		skin = new Skin(Gdx.files.internal("data/gfx/ui/clean-crispy-ui.json"));
		stage = new Stage(new ScreenViewport());
	}
	
	@Override
	public void dispose()
	{
		if(stage != null)
		{
			stage.dispose();
			stage = null;
		}
	}

	public void update(float delta)
	{
		stage.act(delta);
	}
	
	public Skin getSkin()
	{
		return skin;
	}
	
	public void render()
	{		
		stage.draw();
	}
	
	public void addElement(Actor element)
	{
		stage.addActor(element);
	}
	
	public void removeElement(Actor element)
	{
		element.addAction(Actions.removeActor(element));
	}
	
	public InputProcessor getInputProcessor()
	{
		return stage;
	}

	public void resize(int width, int height)
	{
		stage.getViewport().update(width, height, true);
	}
}
