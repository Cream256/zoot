package com.zootcat.scene;

import java.util.List;
import java.util.function.Predicate;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.zootcat.camera.ZootCamera;
import com.zootcat.gfx.ZootRender;
import com.zootcat.hud.ZootHud;
import com.zootcat.physics.ZootPhysics;

public interface ZootScene extends Disposable
{
	void update(float delta);
	void render(float delta);
	void resize(int width, int height);
	
	float getWidth();
	float getHeight();
	float getUnitScale();
	
	ZootHud getHud();
	boolean isDebugMode();
	Viewport getViewport();	
	ZootCamera getCamera();
	ZootRender getRender();
	ZootPhysics getPhysics();	
	InputProcessor getInputProcessor();
	Box2DDebugRenderer getDebugRender();
	ZootSceneActorSpawner getActorSpawner();
	
	void setHud(ZootHud hud);
	void setDebugMode(boolean debug);
	void setViewport(Viewport viewport);
	void setCamera(ZootCamera camera);
	void setRender(ZootRender render);
	void setPhysics(ZootPhysics physics);
	void setInputProcessor(InputProcessor inputProcessor);	
	void setFocusedActor(ZootActor actor);
	void setDebugRender(Box2DDebugRenderer debugRender);
	void setActorSpawner(ZootSceneActorSpawner spawner);
	
	void addAction(Action action);
	void addListener(EventListener listener);
	void removeListener(EventListener listener);
	
	void addActor(ZootActor actor);
	void removeActor(ZootActor actor);	
	List<ZootActor> getActors();
	List<ZootActor> getActors(Predicate<ZootActor> filter);
	ZootActor getFirstActor(Predicate<ZootActor> filter);	
}
