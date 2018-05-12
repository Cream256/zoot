package com.zootcat.controllers.logic;

import com.badlogic.gdx.math.Vector2;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.events.ZootEvent;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.tiled.ZootTiledScene;
import com.zootcat.scene.tiled.ZootTiledSceneActorSpawner;

public class SpawnActorOnDieController extends OnDieEventController
{
	@CtrlParam(required = true) private int tileId;
	@CtrlParam(required = true) private String tileset;
	
	@Override
	protected boolean onDie(ZootActor actor, ZootEvent event)
	{
		ZootTiledSceneActorSpawner spawner = new ZootTiledSceneActorSpawner((ZootTiledScene)actor.getScene());
		ZootActor spawnedActor = spawner.spawn(tileset, tileId);
		if(spawnedActor != null)
		{
			spawnedActor.controllerAction(PhysicsBodyController.class, ctrl -> 
			{
				Vector2 pos = actor.getController(PhysicsBodyController.class).getCenterPositionRef();				
				ctrl.setPosition(pos.x, pos.y);				
			});
						
			actor.getScene().addActor(spawnedActor);
		}
		
		return true;
	}
}
