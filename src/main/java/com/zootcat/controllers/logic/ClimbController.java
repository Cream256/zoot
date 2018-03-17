package com.zootcat.controllers.logic;

import java.util.List;
import java.util.stream.Collectors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.controllers.physics.OnCollideWithSensorController;
import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.events.ZootEventType;
import com.zootcat.events.ZootEvents;
import com.zootcat.fsm.states.ClimbState;
import com.zootcat.math.ZootBoundingBoxFactory;
import com.zootcat.physics.ZootPhysicsUtils;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;

/**
 * Climb Controller - used to enable actor to climb on platforms and ledges.
 * 
 * @ctrlParam timeout - timeout after which actor can try to climb again in sec, 1.0f by default
 * @ctrlParam maxVelocity - maximum velocity at which actor can climb
 * @ctrlParam treshold - how close to the platform top actor must be in order to climb, in pixels
 * @author Cream
 */
public class ClimbController extends OnCollideWithSensorController
{		
	@CtrlParam(debug = true) protected float timeout = 1.0f;
	@CtrlParam(debug = true) protected float maxVelocity = 1.0f;
	@CtrlParam(debug = true) protected float treshold = 0.15f;
	
	private Joint grabJoint;
	private float climbTimeout;
	private Fixture climbableFixture;
	private BoundingBox actorBoxCache = new BoundingBox();
	private BoundingBox fixtureBoxCache = new BoundingBox();
	private ZootDirection sensorPosition = ZootDirection.None;
		
	@Override
	public void onAdd(ZootActor actor)
	{
		//override parent values
		sensorX = 0.0f;
		sensorY = 0.0f;
		
		//create sensor and set default position
		super.onAdd(actor);
		setSensorPosition(ZootDirection.Up);
		
		//setup
		climbTimeout = 0.0f;
		destroyGrabJoint();
		climbableFixture = null;
	}
	
	@Override
	public void onUpdate(float delta, ZootActor actor) 
	{
		if(isActorClimbing(actor))
		{
			climbTimeout = timeout;
			return;
		}		
		climbTimeout = Math.max(0.0f, climbTimeout - delta);
		
		super.onUpdate(delta, actor);
	}
				
	@Override
	protected SensorCollisionResult onCollideWithSensor(Fixture fixture)
	{
		ZootActor controllerActor = getControllerActor(); 
		if(!canActorGrab(controllerActor))
		{
			return SensorCollisionResult.StopProcessing;
		}
				
		if(isFixtureGrabbable(controllerActor, getSensor(), fixture))
		{
			climbableFixture = fixture;
			ZootEvents.fireAndFree(controllerActor, this.sensorPosition == ZootDirection.Up ? ZootEventType.Grab : ZootEventType.GrabSide);			
			return SensorCollisionResult.StopProcessing;
		}
		
		return SensorCollisionResult.ProcessNext;
	}
		
	public boolean canActorGrab(ZootActor actor)
	{
		boolean timeoutOk = climbTimeout == 0;
		boolean enoughVelocityToClimb = actor.getController(PhysicsBodyController.class).getVelocity().y <= maxVelocity;
		boolean notClimbingNow = !isActorClimbing(actor);
		return timeoutOk && enoughVelocityToClimb && notClimbingNow;
	}

	public void grab()
	{		
		//setup
		ZootActor grabbingActor = getControllerActor();
		Body actorBody = grabbingActor.getController(PhysicsBodyController.class).getBody();
		
		//timeout
		climbTimeout = timeout;
		
		//grab joint
		DistanceJointDef def = new DistanceJointDef();
		def.length = 0.0f;
		def.dampingRatio = 1.0f;
		def.bodyA = actorBody;
		def.bodyB = climbableFixture.getBody();
		def.collideConnected = false;		
		
		//actor anchor
		float halfClimbOffset = getClimbHorizontalOffset(grabbingActor) / 2.0f;
		def.localAnchorA.y = grabbingActor.getHeight() / 2.0f;
		def.localAnchorA.x = halfClimbOffset;	
		
		//climabable fixture anchor		
		def.localAnchorB.x = actorBody.getPosition().x - climbableFixture.getBody().getPosition().x + halfClimbOffset;
		def.localAnchorB.y = ZootBoundingBoxFactory.create(climbableFixture).getHeight() / 2.0f;
		
		grabJoint = scene.getPhysics().createJoint(def);			
	}
	
	public ZootDirection getSensorPosition()
	{
		return sensorPosition;
	}
	
	public void setSensorPosition(ZootDirection position)
	{
		if(position == sensorPosition)
		{
			return;
		}		
		
		sensorPosition = position;		
		ZootActor ctrlActor = getControllerActor();
		if(position == ZootDirection.Right)
		{			
			ZootPhysicsUtils.setFixturePosition(getSensor(), ctrlActor.getWidth() / 2.0f, ctrlActor.getHeight() / 2.0f);
		}
		else if(position == ZootDirection.Left)
		{
			ZootPhysicsUtils.setFixturePosition(getSensor(), -ctrlActor.getWidth() / 2.0f, ctrlActor.getHeight() / 2.0f);
		}
		else if(position == ZootDirection.Up)
		{
			ZootPhysicsUtils.setFixturePosition(getSensor(), 0, ctrlActor.getHeight() / 2.0f);
		}
	}
	
	public boolean climb()
	{
		ZootActor actor = getControllerActor();
		if(!isEnoughSpaceToClimb(actor))
		{
			return false;
		}
			
		destroyGrabJoint();
		actor.controllerAction(PhysicsBodyController.class, physCtrl -> 
		{				
			Vector2 pos = physCtrl.getCenterPositionRef();
			float mx = getClimbHorizontalOffset(actor); 
			
			physCtrl.setPosition(pos.x + mx, pos.y + actor.getHeight()); 
			physCtrl.setVelocity(0.0f, 0.0f);				
		});
		return true;
	}
	
	private float getClimbHorizontalOffset(ZootActor actor)
	{
		switch(sensorPosition)
		{
		case Left:
		case Right:
			return actor.getWidth() * sensorPosition.getHorizontalValue();
		
		default:
			return 0.0f;
		}		
	}
	
	private boolean isEnoughSpaceToClimb(ZootActor climbingActor)
	{
		float actorWidth = climbingActor.getWidth();
		float actorHeight = climbingActor.getHeight();
		
		Vector2 actorCenter = climbingActor.getController(PhysicsBodyController.class).getCenterPositionRef();
		float actorLeftBorder = (actorCenter.x - actorWidth / 2.0f) + getClimbHorizontalOffset(climbingActor);
		
		ZootBoundingBoxFactory.createAtRef(climbableFixture, fixtureBoxCache);		
		float platformTop = climbableFixture.getBody().getPosition().y + fixtureBoxCache.getHeight() / 2.0f;
		
		float precisionPatch = 0.15f;
		List<Fixture> found = scene.getPhysics().getFixturesInArea(
				actorLeftBorder + precisionPatch, 
				platformTop + precisionPatch, 
				actorLeftBorder + actorWidth + precisionPatch, 
				platformTop + actorHeight - precisionPatch);		
				
		List<Fixture> filtered = found.stream()
							   .filter(fix -> fix != getSensor())
							   .filter(fix -> fix.getUserData() != getSensor().getUserData())
							   .filter(fix -> fix != climbableFixture)
							   .filter(fix -> !fix.isSensor())
							   .collect(Collectors.toList());		
		return filtered.isEmpty();					   
	}
	
	public void letGo()
	{
		destroyGrabJoint();
		climbableFixture = null;
		getControllerActor().controllerAction(PhysicsBodyController.class, ctrl -> ctrl.setVelocity(0.0f, 0.0f));
	}
		
	public boolean isActorClimbing(ZootActor actor)
	{
		return actor.getStateMachine().getCurrentState().getId() == ClimbState.ID;
	}
	
	public boolean isFixtureGrabbable(ZootActor actor, Fixture sensorFixture, Fixture grabbableFixture)
	{
		boolean notSensor = !grabbableFixture.isSensor();
		boolean collidingWithFixtureTop = isCollidingWithFixtureTop(sensorFixture, grabbableFixture);
		return notSensor && collidingWithFixtureTop;
	}
	
	private boolean isCollidingWithFixtureTop(Fixture actorSensorFixture, Fixture platformFixture)
	{
		ZootBoundingBoxFactory.createAtRef(actorSensorFixture, actorBoxCache);
		float actorTop = actorSensorFixture.getBody().getPosition().y + actorBoxCache.getHeight() / 2.0f;
		
		ZootBoundingBoxFactory.createAtRef(platformFixture, fixtureBoxCache);
		float platformTop = platformFixture.getBody().getPosition().y + fixtureBoxCache.getHeight() / 2.0f;
		
		float diff = actorTop - platformTop;		
		return Math.abs(diff) <= treshold * scene.getUnitScale();
	}
		
	private void destroyGrabJoint()
	{
		if(grabJoint != null)
		{
			scene.getPhysics().destroyJoint(grabJoint);
			grabJoint = null;
		}
	}
}