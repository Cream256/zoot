package com.zootcat.scene;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.zootcat.camera.ZootCamera;
import com.zootcat.gfx.ZootRender;
import com.zootcat.hud.ZootHud;
import com.zootcat.physics.ZootPhysics;

public class ZootStageScene implements ZootScene 
{
	private boolean isDebugMode = false;	
	private ZootRender render = null;
	private ZootPhysics physics = null;	
	private InputProcessor inputProcessor = null;
	private ZootHud hud = null;
	private Stage stage = null;
	private ZootCamera camera;
	
	public ZootStageScene(Stage stage)
	{
		this.stage = stage;
		this.inputProcessor = stage;
	}
	
	public ZootStageScene(Viewport viewport)
	{
		stage = new Stage(viewport);
		inputProcessor = stage;
	}
		
	@Override
	public void update(float delta)
	{
		stage.act(delta);
	}
	
	@Override
	public void render(float delta)
	{	
		if(render != null) render.render(delta);
		else stage.draw();
	}
	
	@Override
	public boolean isDebugMode() 
	{
		return isDebugMode;
	}
	
	@Override
	public void setDebugMode(boolean debug)
	{
		isDebugMode = debug;
	}
	
	@Override
	public void setPhysics(ZootPhysics physics)
	{
		this.physics = physics;
	}
	
	@Override
	public ZootPhysics getPhysics()
	{
		return physics;
	}
	
	@Override
	public ZootRender getRender()
	{
		return render;
	}
	
	@Override
	public void setRender(ZootRender render)
	{
		this.render = render;
	}	
	
	@Override
	public InputProcessor getInputProcessor()
	{
		return inputProcessor;
	}
	
	@Override
	public void setInputProcessor(InputProcessor inputProcessor)
	{
		this.inputProcessor = inputProcessor;
	}
	
	@Override
	public ZootHud getHud()
	{
		return hud;
	}
	
	@Override
	public void setHud(ZootHud hud)
	{
		this.hud = hud;
	}
	
	@Override
	public ZootCamera getCamera()
	{
		return camera;
	}
	
	@Override
	public void setCamera(ZootCamera camera)
	{
		this.camera = camera;
		camera.setScene(this);
	}
	
	@Override
	public void addListener(EventListener listener) 
	{
		stage.addListener(listener);
	}

	@Override
	public void removeListener(EventListener listener) 
	{
		stage.removeListener(listener);
	}
	
	@Override
	public void addActor(ZootActor actor)
	{
		stage.addActor(actor);		
		actor.setScene(this);
	}
	
	@Override
	public void removeActor(ZootActor actor) 
	{
		if(actor.getScene() == this)
		{
			actor.remove();
			actor.setScene(null);
		}
	}

	@Override
	public List<ZootActor> getActors() 
	{		
		return getActors((act) -> true);
	}
	
	@Override
	public List<ZootActor> getActors(Predicate<ZootActor> filter) 
	{				
		return StreamSupport.stream(stage.getActors().spliterator(), false)
							 .filter(act -> ClassReflection.isInstance(ZootActor.class, act))
							 .map(act -> (ZootActor)act)							 
							 .filter(filter)
							 .collect(Collectors.toList());
	}
	
	@Override
	public ZootActor getFirstActor(Predicate<ZootActor> filter)
	{
		return StreamSupport.stream(stage.getActors().spliterator(), false)
				 .filter(act -> ClassReflection.isInstance(ZootActor.class, act))
				 .map(act -> (ZootActor)act)							 
				 .filter(filter)
				 .findFirst()
				 .orElse(null);
	}
	
	@Override
	public void addAction(Action action)
	{
		stage.getRoot().addAction(action);
	}
	

	@Override
	public void resize(int width, int height) 
	{		
		if(hud != null) hud.resize(width, height);
	}
	
	@Override
	public Viewport getViewport()
	{
		return stage.getViewport();
	}
	
	@Override
	public void setViewport(Viewport viewport)
	{
		stage.setViewport(viewport);
	}
	
	@Override
	public float getWidth()
	{
		return stage.getWidth();
	}
	
	@Override
	public float getHeight()
	{
		return stage.getHeight();
	}
	
	@Override
	public float getUnitScale()
	{
		return 1.0f;
	}
	
	@Override
	public void dispose() 
	{		
		if(stage != null)
		{
			stage.dispose();
			stage = null;
		}
				
		if(render != null)
		{
			render.dispose();
			render = null;
		}
		
		if(physics != null)
		{
			physics.dispose();
			physics = null;
		}
				
		if(hud != null)
		{
			hud.dispose();
			hud = null;
		}
	}
	
	@Override
	public void setFocusedActor(ZootActor actor)
	{
		stage.setKeyboardFocus(actor);
	}
	
	public Stage getStage()
	{
		return stage;
	}
}
