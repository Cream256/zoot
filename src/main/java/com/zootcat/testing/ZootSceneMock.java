package com.zootcat.testing;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.zootcat.camera.ZootCamera;
import com.zootcat.gfx.ZootRender;
import com.zootcat.hud.ZootHud;
import com.zootcat.physics.ZootPhysics;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;
import com.zootcat.scene.ZootSceneActorSpawner;

public class ZootSceneMock implements ZootScene 
{
	private List<ZootActor> actors = new ArrayList<ZootActor>();
	
	@Override
	public void dispose() 
	{
		//noop		
	}

	@Override
	public void update(float delta) 
	{
		//noop
	}

	@Override
	public void render(float delta) 
	{
		//noop
	}

	@Override
	public void resize(int width, int height) 
	{
		//noop	
	}

	@Override
	public void addActor(ZootActor actor) 
	{
		actors.add(actor);	
	}

	@Override
	public void removeActor(ZootActor actor) 
	{
		actors.remove(actor);
	}

	@Override
	public List<ZootActor> getActors() 
	{
		return actors;
	}

	@Override
	public List<ZootActor> getActors(Predicate<ZootActor> filter) 
	{
		return actors.stream().filter(filter).collect(Collectors.toList());
	}

	@Override
	public ZootActor getFirstActor(Predicate<ZootActor> filter) 
	{
		return actors.stream().filter(filter).findFirst().orElse(null);
	}

	@Override
	public ZootCamera getActiveCamera() 
	{
		return null;
	}

	@Override
	public ZootPhysics getPhysics() 
	{
		return null;
	}

	@Override
	public ZootRender getRender() 
	{
		return null;
	}

	@Override
	public InputProcessor getInputProcessor() 
	{
		return null;
	}

	@Override
	public Viewport getViewport() 
	{
		return null;
	}

	@Override
	public ZootHud getHud() 
	{
		return null;
	}

	@Override
	public boolean isDebugMode() 
	{
		return false;
	}

	@Override
	public void setDebugMode(boolean debug) 
	{
		//noop	
	}

	@Override
	public void setFocusedActor(ZootActor actor) 
	{
		//noop	
	}

	@Override
	public void addListener(EventListener listener) 
	{
		//noop
	}

	@Override
	public void removeListener(EventListener listener) 
	{
		//noop	
	}

	@Override
	public float getUnitScale() 
	{
		return 1.0f;
	}

	@Override
	public float getWidth()
	{
		//noop
		return 0;
	}

	@Override
	public float getHeight()
	{
		//noop
		return 0;
	}

	@Override
	public void addAction(Action action)
	{
		//noop
	}

	@Override
	public void setViewport(Viewport viewport)
	{
		//noop
	}

	@Override
	public void setPhysics(ZootPhysics physics)
	{
		//noop
	}

	@Override
	public void setRender(ZootRender render)
	{
		//noop
	}

	@Override
	public void setInputProcessor(InputProcessor inputProcessor)
	{
		//noop
	}

	@Override
	public void setHud(ZootHud hud)
	{
		//noop
	}

	@Override
	public void setActiveCamera(ZootCamera camera)
	{
		//noop	
	}

	@Override
	public Box2DDebugRenderer getDebugRender()
	{
		return null;
	}

	@Override
	public void setDebugRender(Box2DDebugRenderer debugRender)
	{
		//noop
	}

	@Override
	public ZootSceneActorSpawner getActorSpawner()
	{
		return null;
	}

	@Override
	public void setActorSpawner(ZootSceneActorSpawner spawner)
	{
		//noop
	}
}
