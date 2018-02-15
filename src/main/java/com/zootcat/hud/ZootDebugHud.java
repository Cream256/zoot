package com.zootcat.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.zootcat.scene.ZootActor;

public class ZootDebugHud implements Disposable
{
	private static final String INFO_TEXT = "Press F9 for debug info";
	
	private Skin skin;
	private Stage stage;	
	private ZootDebugWindow debugWindow;
	
	public ZootDebugHud()
	{
		skin = new Skin(Gdx.files.internal("data/gfx/ui/clean-crispy-ui.json"));
		stage = new Stage(new ScreenViewport());
		
		//info label
		Label infoLabel = new Label(INFO_TEXT, skin);
		Container<Label> infoLabelContainer = new Container<Label>();
		infoLabelContainer.setActor(infoLabel);
		infoLabelContainer.setFillParent(true);
		infoLabelContainer.align(Align.topLeft);
		stage.addActor(infoLabelContainer);
		
		//debug window		
		debugWindow = new ZootDebugWindow(skin);		
		debugWindow.setVisible(false);
		stage.addActor(debugWindow);
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

	public void render(float delta)
	{
		stage.act(delta);
		stage.draw();
	}
	
	public InputProcessor getInputProcessor()
	{
		return stage;
	}

	public void resize(int width, int height)
	{
		stage.getViewport().update(width, height, true);
	}

	public void setDebugActor(ZootActor actor)
	{
		debugWindow.setDebugActor(actor);
	}
	
	public void setWindowVisible(boolean visible)
	{
		debugWindow.setVisible(visible);
	}
}
