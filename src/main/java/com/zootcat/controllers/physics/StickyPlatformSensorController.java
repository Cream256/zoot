package com.zootcat.controllers.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.zootcat.math.ZootBoundingBoxFactory;
import com.zootcat.scene.ZootActor;

public class StickyPlatformSensorController extends OnCollideWithSensorController
{
	public static final float VELOCITY_Y_JUMP_THRESHOLD = 0.1f;
	private final BoundingBox actorBoundingBox = new BoundingBox();
	private final BoundingBox sensorBoundingBox = new BoundingBox();
	
	@Override
	public SensorCollisionResult onCollision(Fixture fixture)
	{
		//actor on platform
		ZootActor actorOnPlatform = (ZootActor)fixture.getUserData();
		PhysicsBodyController actorPhysicsCtrl = actorOnPlatform.tryGetSingleController(PhysicsBodyController.class);
		if(actorPhysicsCtrl == null) return SensorCollisionResult.ProcessNext;
		
		//platform
		PhysicsBodyController platformPhysicsCtrl = getControllerActor().getSingleController(PhysicsBodyController.class);
		Vector2 platformVelocityRef = platformPhysicsCtrl.getVelocity();
		
		//is actor on platform sensor?
		float sensorTop = ZootBoundingBoxFactory.createAtRef(getSensor(), sensorBoundingBox).max.y + platformPhysicsCtrl.getCenterPositionRef().y;
		float actorBottom = ZootBoundingBoxFactory.createAtRef(fixture, actorBoundingBox).min.y + actorPhysicsCtrl.getCenterPositionRef().y;
		if(actorBottom < sensorTop) return SensorCollisionResult.ProcessNext;
				
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
		
		//process next fixture
		return SensorCollisionResult.ProcessNext;
	}
}
