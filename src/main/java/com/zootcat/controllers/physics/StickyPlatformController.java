package com.zootcat.controllers.physics;

import com.badlogic.gdx.math.Vector2;
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
			
		Vector2 platformVelocityRef = actor.getSingleController(PhysicsBodyController.class).getVelocity();		
		actors.forEach(actorOnPlatform -> updateActorOnPlatform(actorOnPlatform, platformVelocityRef));
	}
	
	private void updateActorOnPlatform(ZootActor actorOnPlatform, Vector2 platformVelocityRef)
	{
		PhysicsBodyController actorPhysicsCtrl = actorOnPlatform.tryGetSingleController(PhysicsBodyController.class);
		if(actorPhysicsCtrl == null) return;

		//set X velocity if actor is moving slower than the platform
		Vector2 actorVelRef = actorPhysicsCtrl.getVelocity();
		boolean actorJumping = actorVelRef.y >= VELOCITY_Y_JUMP_THRESHOLD; 			
		boolean moveActorToRight = platformVelocityRef.x >= 0.0f && Math.abs(actorVelRef.x) < platformVelocityRef.x;
		boolean moveActorToLeft = platformVelocityRef.x < 0.0f && Math.abs(actorVelRef.x) < Math.abs(platformVelocityRef.x);			
		boolean setX = !actorJumping && (moveActorToRight || moveActorToLeft);
			
		//set the Y velocity if platform is moving downwards && player is not jumping
		boolean platformMovingDown = platformVelocityRef.y < 0.0f;
		boolean actorMovingDownSlowerThanPlatform = actorVelRef.y <= 0.0f || actorVelRef.y <= Math.abs(platformVelocityRef.y); 			
		boolean setY = platformMovingDown && actorMovingDownSlowerThanPlatform;
				
		//set the velocity
		actorPhysicsCtrl.setVelocity(platformVelocityRef.x, platformVelocityRef.y, setX, setY);
	}
}
