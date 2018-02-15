package com.zootcat.scene;

import java.util.List;
import java.util.function.Predicate;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.zootcat.camera.ZootCamera;
import com.zootcat.gfx.ZootRender;
import com.zootcat.map.ZootMap;
import com.zootcat.physics.ZootPhysics;

public interface ZootScene extends Disposable
{
	void update(float delta);
	void render(float delta);
	void resize(int width, int height);
	void reload();
	
	void addActor(ZootActor actor);
	void removeActor(ZootActor actor);	
	List<ZootActor> getActors();
	List<ZootActor> getActors(Predicate<Actor> filter);
	
	ZootCamera getCamera();
	ZootPhysics getPhysics();
	ZootRender getRender();
	ZootMap getMap();
	InputProcessor getInputProcessor();
	Viewport getViewport();
	
	boolean isDebugMode();
	void setDebugMode(boolean debug);
	
	void setFocusedActor(ZootActor actor);
	
	void addListener(EventListener listener);
	void removeListener(EventListener listener);
	
	float getUnitScale();
}
