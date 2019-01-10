package com.zootcat.controllers.physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.utils.Array;
import com.zootcat.scene.ZootActor;

public class StickyPlatformController extends OnCollideController
{
	public static final float VELOCITY_Y_JUMP_THRESHOLD = 0.1f;
	
	private Array<ZootActor> actors = new Array<ZootActor>(false, 8);
		
	@Override
	public void onEnter(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		actors.add(getOtherActor(actorA, actorB));		
	}

	@Override
	public void onLeave(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		actors.removeValue(getOtherActor(actorA, actorB), true);
	}
	
	@Override
	public void onUpdate(float delta, ZootActor actor) 
	{
		super.onUpdate(delta, actor);
			
		float platformVelocityX = actor.getSingleController(PhysicsBodyController.class).getVelocity().x;		
		actors.forEach(actorOnPlatform -> 
		{
			PhysicsBodyController actorPhysicsCtrl = actorOnPlatform.tryGetSingleController(PhysicsBodyController.class);
			if(actorPhysicsCtrl == null) return;
			
			//don't set the velocity if player is jumping
			float actorVelY = actorPhysicsCtrl.getVelocity().y;
			if(actorVelY >= VELOCITY_Y_JUMP_THRESHOLD) return;	
						
			actorPhysicsCtrl.setVelocity(platformVelocityX, 0.0f, true, false);
		});		
	}
}
