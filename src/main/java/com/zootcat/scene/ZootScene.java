package com.zootcat.scene;

import java.util.List;
import java.util.function.Predicate;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.zootcat.camera.ZootCamera;
import com.zootcat.gfx.ZootRender;
import com.zootcat.hud.ZootHud;
import com.zootcat.map.ZootMap;
import com.zootcat.physics.ZootPhysics;

public interface ZootScene extends Disposable
{
	void update(float delta);
	void render(float delta);
	void resize(int width, int height);
	
	void addActor(ZootActor actor);
	void removeActor(ZootActor actor);	
	List<ZootActor> getActors();
	List<ZootActor> getActors(Predicate<ZootActor> filter);
	ZootActor getFirstActor(Predicate<ZootActor> filter);
	
	void addRootAction(Action action);
	
	ZootCamera getCamera();
	ZootPhysics getPhysics();
	ZootRender getRender();
	ZootMap getMap();
	InputProcessor getInputProcessor();
	Viewport getViewport();
	
	ZootHud getHud();
	
	boolean isDebugMode();
	void setDebugMode(boolean debug);
	
	void setFocusedActor(ZootActor actor);
	
	void addListener(EventListener listener);
	void removeListener(EventListener listener);
	
	float getUnitScale();
	ZootSceneActorSpawner getActorSpawner();
}
