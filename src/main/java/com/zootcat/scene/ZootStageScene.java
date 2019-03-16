package com.zootcat.scene;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.zootcat.camera.ZootCamera;
import com.zootcat.camera.ZootCameraRegistry;
import com.zootcat.gfx.ZootRender;
import com.zootcat.hud.ZootHud;
import com.zootcat.physics.ZootPhysics;

public class ZootStageScene implements ZootScene 
{
	public static final float FIXED_TIME_STEP = 1.0f / 60.0f;
	public static final float MIN_TIME_STEP = 1.0f / 4.0f;
		
	private ZootHud hud;
	private ZootCamera activeCamera;
	private ZootPhysics physics;
	private ZootRender render;
	private InputProcessor inputProcessor;
	private ZootSceneActorSpawner actorSpawner;
	private ZootCameraRegistry cameraRegistry;
	
	private Stage stage = null;
	private float timeAccumulator = 0.0f;
	
	private boolean isDebugMode = false;
	private Box2DDebugRenderer debugRender = null;
	
	public ZootStageScene(Stage stage)
	{
		this.stage = stage;
		this.inputProcessor = stage;
		this.cameraRegistry = new ZootCameraRegistry();
	}
	
	public ZootStageScene(Viewport viewport)
	{
		stage = new Stage(viewport);
		inputProcessor = stage;
		cameraRegistry = new ZootCameraRegistry();
	}
		
	@Override
	public void update(float delta)
	{		
		timeAccumulator += Math.min(MIN_TIME_STEP, delta);       
		while(timeAccumulator >= FIXED_TIME_STEP)
		{
			stage.act(FIXED_TIME_STEP);			
			
			if(physics != null) physics.step(FIXED_TIME_STEP);
			
			timeAccumulator -= FIXED_TIME_STEP;
		}
		
		activeCamera.update(delta, true);
		if(hud != null) hud.update(delta);
	}
		
	@Override
	public void render(float delta)
	{	
		if(render != null)
		{
			render.setView(activeCamera);
			render.render(delta);	
		}
		
		stage.draw();
		
		if(isDebugMode) debugRender.render(physics.getWorld(), activeCamera.combined);
		if(hud != null) hud.render();
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
	public void setDebugRender(Box2DDebugRenderer render)
	{
		debugRender = render;
	}
	
	@Override
	public Box2DDebugRenderer getDebugRender()
	{
		return debugRender;
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
	public ZootCamera getActiveCamera()
	{
		return activeCamera;
	}
	
	@Override
	public void setActiveCamera(ZootCamera camera)
	{
		activeCamera = camera;
		activeCamera.setScene(this);		
		getViewport().setCamera(camera);		
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
		
		if(debugRender != null)
		{
			debugRender.dispose();
			debugRender = null;
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

	@Override
	public ZootSceneActorSpawner getActorSpawner()
	{
		return actorSpawner;
	}

	@Override
	public void setActorSpawner(ZootSceneActorSpawner spawner)
	{
		this.actorSpawner = spawner;
	}

	@Override
	public ZootCameraRegistry getCameraRegistry()
	{
		return cameraRegistry;
	}
}
