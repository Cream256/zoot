package com.zootcat.scene;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.zootcat.gfx.ZootRender;
import com.zootcat.hud.ZootHud;
import com.zootcat.physics.ZootPhysics;

public class ZootStageSceneTest
{
	@Mock private Stage stage;
	@Mock private Group stageRoot;
	@Mock private Viewport viewport;	
	@Mock private ZootRender render;
	@Mock private ZootPhysics physics;
	@Mock private InputProcessor inputProcessor;
	@Mock private ZootHud hud;
	@Mock private ZootActor actor;
	@Mock private EventListener eventListener;
	@Mock private Action action;
	private ZootStageScene scene;
		
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		when(stage.getRoot()).thenReturn(stageRoot);
		
		scene = new ZootStageScene(stage);
	}
	
	@Test
	public void shouldConstructWithStage()
	{
		assertEquals(stage, scene.getStage());
	}
	
	@Test
	public void shouldConstructWithDefaultValues()
	{
		assertEquals(1.0f, scene.getUnitScale(), 0.0f);
		assertNull(scene.getPhysics());
		assertNull(scene.getRender());
		assertNull(scene.getInputProcessor());
		assertNull(scene.getHud());
		assertNull(scene.getViewport());		
	}

	@Test
	public void shouldActOnStage()
	{
		scene.update(1.0f);
		verify(stage).act(1.0f);
	}
	
	@Test
	public void shouldDrawStageIfNoRendererIsSet()
	{
		scene.setRender(null);
		scene.render(1.0f);
		verify(stage).draw();		
	}
	
	@Test
	public void shouldUseRenderIfSet()
	{
		scene.setRender(render);
		scene.render(1.0f);
		verify(render).render(1.0f);
		verify(stage, never()).draw();		
	}
	
	@Test
	public void shouldSetDebugMode()
	{
		scene.setDebugMode(true);
		assertTrue(scene.isDebugMode());
		
		scene.setDebugMode(false);
		assertFalse(scene.isDebugMode());		
	}
	
	@Test
	public void shouldSetPhysics()
	{
		scene.setPhysics(physics);
		assertEquals(physics, scene.getPhysics());
	}
	
	@Test
	public void shouldSetRender()
	{
		scene.setRender(render);
		assertEquals(render, scene.getRender());
	}
	
	@Test
	public void shouldSetInputProcessor()
	{
		scene.setInputProcessor(inputProcessor);
		assertEquals(inputProcessor, scene.getInputProcessor());
	}
	
	@Test
	public void shouldSetHud()
	{
		scene.setHud(hud);
		assertEquals(hud, scene.getHud());
	}
	
	@Test
	public void shouldResizeHud()
	{
		scene.setHud(hud);
		scene.resize(100, 200);
		verify(hud).resize(100, 200);
	}
	
	@Test
	public void shouldNotThrowOnResizeIfNoHudIsSet()
	{
		scene.setHud(null);
		scene.resize(100, 200);
		//ok
	}
	
	@Test
	public void shouldSetViewportOnStage()
	{
		scene.setViewport(viewport);
		verify(stage).setViewport(viewport);
	}
	
	@Test
	public void shouldReturnStageWidth()
	{
		when(stage.getWidth()).thenReturn(128.0f);
		assertEquals(128.0f, scene.getWidth(), 0.0f);
	}
	
	@Test
	public void shouldReturnStageHeight()
	{
		when(stage.getHeight()).thenReturn(256.0f);
		assertEquals(256.0f, scene.getHeight(), 0.0f);
	}
	
	@Test
	public void shouldSetFocusedActor()
	{
		scene.setFocusedActor(actor);
		verify(stage).setKeyboardFocus(actor);
	}
	
	@Test
	public void shouldAddEventListenerToStage()
	{
		scene.addListener(eventListener);
		verify(stage).addListener(eventListener);
	}
	
	@Test
	public void shouldRemoveEventListenerFromStage()
	{
		scene.removeListener(eventListener);
		verify(stage).removeListener(eventListener);
	}
	
	@Test
	public void shouldDisposeStage()
	{
		scene.dispose();
		verify(stage).dispose();
	}
	
	@Test
	public void shouldDisposeHud()
	{
		scene.setHud(hud);
		scene.dispose();
		verify(hud).dispose();
	}
	
	@Test
	public void shouldDisposePhysics()
	{
		scene.setPhysics(physics);
		scene.dispose();
		verify(physics).dispose();
	}
	
	@Test
	public void shouldNotThrowOnDisposeIfHudOrPhysicsAreNotSet()
	{
		scene.setHud(null);
		scene.setPhysics(null);
		scene.dispose();
		//ok
	}
	
	@Test
	public void shouldAddActionToStage()
	{
		scene.addAction(action);
		verify(stageRoot).addAction(action);
	}
	
	@Test
	public void shouldAddActorToStage()
	{
		scene.addActor(actor);
		verify(stage).addActor(actor);
		verify(actor).setScene(scene);		
	}
	
	@Test
	public void shouldRemoveActorFromScene()
	{
		when(actor.getScene()).thenReturn(scene);
		scene.removeActor(actor);
		
		verify(actor).remove();
		verify(actor).setScene(null);				
	}
	
	@Test
	public void shouldNotRemoveActorFromDifferentScene()
	{
		when(actor.getScene()).thenReturn(mock(ZootScene.class));
		scene.removeActor(actor);
		
		verify(actor, never()).remove();
		verify(actor, never()).setScene(null);				
	}
	
	@Test
	public void shouldReturnAllActors()
	{
		//given
		ZootActor actor1 = new ZootActor();
		ZootActor actor2 = new ZootActor();
		ZootActor actor3 = new ZootActor();
		Array<Actor> actors = new Array<Actor>();
		actors.add(actor1, actor2, actor3);
		when(stage.getActors()).thenReturn(actors);
		
		//when
		List<ZootActor> result = scene.getActors();
		assertEquals(3, result.size());
		assertEquals(actor1, result.get(0));
		assertEquals(actor2, result.get(1));
		assertEquals(actor3, result.get(2));
	}
	
	@Test
	public void shouldReturnActorsMatchingPredicate()
	{
		//given
		ZootActor actor1 = new ZootActor();
		actor1.setName("Match");
		ZootActor actor2 = new ZootActor();
		actor2.setName("NoMatch");
		ZootActor actor3 = new ZootActor();
		actor3.setName("Match");
		Array<Actor> actors = new Array<Actor>();
		actors.add(actor1, actor2, actor3);
		when(stage.getActors()).thenReturn(actors);
		
		//when
		List<ZootActor> result = scene.getActors(act -> act.getName().equals("Match"));
		assertEquals(2, result.size());
		assertEquals(actor1, result.get(0));
		assertEquals(actor3, result.get(1));		
	}
	
	@Test
	public void shuoldGetFirstActorMatchingPredicate()
	{
		//given
		ZootActor actor1 = new ZootActor();
		actor1.setName("Match");
		ZootActor actor2 = new ZootActor();
		actor2.setName("NoMatch");
		ZootActor actor3 = new ZootActor();
		actor3.setName("Match");
		Array<Actor> actors = new Array<Actor>();
		actors.add(actor1, actor2, actor3);
		when(stage.getActors()).thenReturn(actors);
		
		//when
		assertEquals(actor1, scene.getFirstActor(act -> act.getName().equals("Match")));		
	}
}
