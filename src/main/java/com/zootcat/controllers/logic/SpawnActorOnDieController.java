package com.zootcat.controllers.logic;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootSceneActorSpawner;

public class SpawnActorOnDieController extends OnDieEventController
{
	@CtrlParam(required = true) private int tileId;
	@CtrlParam(required = true) private String tileset;
	@CtrlParam private int count = 1;
	@CtrlParam private float velocityMinX = 0.0f;
	@CtrlParam private float velocityMaxX = 0.0f;
	@CtrlParam private float velocityMinY = 0.0f;
	@CtrlParam private float velocityMaxY = 0.0f;
	
	@Override
	protected boolean onDie(ZootActor actor, ZootEvent event)
	{
		ZootSceneActorSpawner spawner = actor.getScene().getActorSpawner();		
		Vector2 position = actor.getSingleController(PhysicsBodyController.class).getCenterPositionRef().cpy();		
		
		for(int i = 0; i < count; ++i)
		{
			float velocityX = MathUtils.random(velocityMinX, velocityMaxX);
			float velocityY = MathUtils.random(velocityMinY, velocityMaxY);
						
			ZootActor spawnedActor = spawner.spawn(tileset, tileId, position, new Vector2(velocityX, velocityY));			
			actor.getScene().addActor(spawnedActor);
		}
		
		return true;
	}
}
