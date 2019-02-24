package com.zootcat.controllers.logic;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.math.Vector2;
import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;
import com.zootcat.scene.ZootSceneActorSpawner;

public class SpawnActorOnDieControllerTest
{
	private static final int TILE_ID = 1;
	private static final String TILESET_NAME = "Tileset";
	
	@Mock private ZootActor actor;
	@Mock private ZootScene scene;
	@Mock private ZootSceneActorSpawner spawner;
	@Mock private PhysicsBodyController physicsBodyCtrl;
	private SpawnActorOnDieController ctrl;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		
		when(physicsBodyCtrl.getCenterPositionRef()).thenReturn(Vector2.Zero);
		//when(scene.getActorSpawner()).thenReturn(spawner);	//TODO
		when(actor.getScene()).thenReturn(scene);
		when(actor.getSingleController(PhysicsBodyController.class)).thenReturn(physicsBodyCtrl);
		
		ctrl = new SpawnActorOnDieController();
		ControllerAnnotations.setControllerParameter(ctrl, "tileId", TILE_ID);
		ControllerAnnotations.setControllerParameter(ctrl, "tileset", TILESET_NAME);
	}
	
	@Test
	public void shouldCreateCorrectNumberOfActors()
	{
		//given
		final int expectedCount = 3;
		
		//when
		ControllerAnnotations.setControllerParameter(ctrl, "count", expectedCount);
		ctrl.onDie(actor, null);
		
		//then
		verify(scene, times(expectedCount)).addActor(any(ZootActor.class));
	}
	
	@Test
	public void shouldCreateOneActorByDefault()
	{
		//when
		ctrl.onDie(actor, null);
		
		//then
		verify(scene, times(1)).addActor(any(ZootActor.class));		
	}
	
	@Test
	public void shouldSetZeroVelocityByDefault()
	{
		//when
		ctrl.onDie(actor, null);
		
		//then
		verify(spawner).spawn(TILESET_NAME, TILE_ID, Vector2.Zero, Vector2.Zero);				
	}
	
	@Test
	public void shouldSetCorrectVelocity()
	{
		//when
		ControllerAnnotations.setControllerParameter(ctrl, "velocityMinX", 5.0f);
		ControllerAnnotations.setControllerParameter(ctrl, "velocityMaxX", 5.0f);
		ControllerAnnotations.setControllerParameter(ctrl, "velocityMinY", -5.0f);
		ControllerAnnotations.setControllerParameter(ctrl, "velocityMaxY", -5.0f);
		ctrl.onDie(actor, null);
		
		//then
		verify(spawner).spawn(TILESET_NAME, TILE_ID, Vector2.Zero, new Vector2(5.0f, -5.0f));
	}
}
