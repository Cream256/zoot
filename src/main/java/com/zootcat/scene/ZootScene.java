package com.zootcat.scene;

import java.util.List;
import java.util.function.Predicate;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.zootcat.gfx.ZootRender;
import com.zootcat.hud.ZootHud;
import com.zootcat.physics.ZootPhysics;

//TODO clean this up + extract ZootDefaultScene from ZootTiledScene
public interface ZootScene extends Disposable
{
	//ZootCamera getCamera(String name);
	//void addCamera(ZootCamera camera);
	//void removeCamera(String name);
	//void setActiveCamera(String name);
	//ZootCamera getActiveCamera();
						
	//ZootSceneActorSpawner getActorSpawner();	//nie pasuje tutaj
	//ZootActor spawn(String tilesetName, int tileId, final Vector2 position, final Vector2 velocity);
	
	float getUnitScale();
	
	void update(float delta);
	void render(float delta);
	
	//ZootMap getMap();	//TODO tego nie powinno tu byc
	
	void setFocusedActor(ZootActor actor);
	
	float getWidth();
	float getHeight();
	
	void resize(int width, int height);
	void addAction(Action action);
	
	Viewport getViewport();
	public void setViewport(Viewport viewport);
	
	void addListener(EventListener listener);
	void removeListener(EventListener listener);
	
	boolean isDebugMode();
	void setDebugMode(boolean debug);
	
	ZootPhysics getPhysics();
	void setPhysics(ZootPhysics physics);
	
	ZootRender getRender();
	void setRender(ZootRender render);
	
	InputProcessor getInputProcessor();
	void setInputProcessor(InputProcessor inputProcessor);
	
	ZootHud getHud();
	void setHud(ZootHud hud);
	
	void addActor(ZootActor actor);
	void removeActor(ZootActor actor);	
	List<ZootActor> getActors();
	List<ZootActor> getActors(Predicate<ZootActor> filter);
	ZootActor getFirstActor(Predicate<ZootActor> filter);
}
