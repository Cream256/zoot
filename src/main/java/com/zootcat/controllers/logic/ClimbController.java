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
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.events.ZootEvents;
import com.zootcat.fsm.states.ClimbState;
import com.zootcat.math.ZootBoundingBoxFactory;
import com.zootcat.physics.ZootDefaultContactFilter;
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
	@CtrlParam protected float timeout = 1.0f;
	@CtrlParam protected float maxVelocity = 1.0f;
	@CtrlParam protected float treshold = 0.15f;
	
	private Joint grabJoint;
	private float climbTimeout;
	private Fixture climbingFixture;
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
		climbingFixture = null;
	}
	
	@Override
	public void preUpdate(float delta, ZootActor actor) 
	{
		if(isActorClimbing(actor))
		{
			climbTimeout = timeout;
			return;
		}		
		climbTimeout = Math.max(0.0f, climbTimeout - delta);
	}
		
	@Override
	protected boolean shouldCollide(Fixture fixture)
	{
		//we must ignore disabled contacts, so one way platforms
		//will have continous collision, not only when moving downward
		return true;
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
			climbingFixture = fixture;
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
		def.bodyB = climbingFixture.getBody();
		def.collideConnected = false;		
		
		//actor anchor
		float halfClimbOffset = getClimbHorizontalOffset(grabbingActor) / 2.0f;
		def.localAnchorA.y = grabbingActor.getHeight() / 2.0f;
		def.localAnchorA.x = halfClimbOffset;	
		
		//climabble fixture anchor		
		def.localAnchorB.x = actorBody.getPosition().x - climbingFixture.getBody().getPosition().x + halfClimbOffset;
		def.localAnchorB.y = ZootBoundingBoxFactory.create(climbingFixture).getHeight() / 2.0f;
		
		grabJoint = scene.getPhysics().createJoint(def);			
	}
	
	public ZootDirection getSensorDirection()
	{
		return sensorPosition;
	}
	
	public void setSensorPosition(ZootDirection position)
	{
		ZootActor ctrlActor = getControllerActor();
		if(position == sensorPosition || isActorClimbing(ctrlActor))
		{
			return;
		}
		
		sensorPosition = position;		
		
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
		
		Vector2 climbingActorCenter = climbingActor.getController(PhysicsBodyController.class).getCenterPositionRef();
		float climbingActorLeftBorder = (climbingActorCenter.x - actorWidth / 2.0f) + getClimbHorizontalOffset(climbingActor) / 2.0f;
		
		ZootBoundingBoxFactory.createAtRef(climbingFixture, fixtureBoxCache);
		float platformTop = climbingFixture.getBody().getPosition().y + fixtureBoxCache.getHeight() / 2.0f;
		
		float precisionPatch = 0.15f;
		List<Fixture> found = scene.getPhysics().getFixturesInArea(
				climbingActorLeftBorder + precisionPatch, 
				platformTop + precisionPatch, 
				climbingActorLeftBorder + actorWidth + precisionPatch, 
				platformTop + actorHeight - precisionPatch);		

		List<Fixture> filtered = found.stream()
							   .filter(fix -> fix != getSensor())
							   .filter(fix -> fix.getUserData() != getSensor().getUserData())
							   .filter(fix -> fix != climbingFixture)
							   .filter(fix -> !fix.isSensor())
							   .filter(fix -> !isFixtureOneWayPlatform(fix))
							   .filter(fix -> isCollisionBetweenFixtureAndActor(fix, climbingActor))
							   .collect(Collectors.toList());		
				
		return filtered.isEmpty();					   
	}
	
	private boolean isCollisionBetweenFixtureAndActor(Fixture fix, ZootActor climbingActor)
	{
		return climbingActor.controllerCondition(PhysicsBodyController.class, ctrl -> 
		{
			for(Fixture actorFix : ctrl.getFixtures())
			{
				if(actorFix.isSensor()) continue;				
				if(ZootDefaultContactFilter.shouldCollide(fix, actorFix))
				{
					return true;
				}				
			}
			return false;
		});
	}
	
	private boolean isFixtureOneWayPlatform(Fixture fix)
	{
		ZootActor fixtureActor = (ZootActor) fix.getUserData();
		if(fixtureActor == null) return false;
		return fixtureActor.tryGetController(OneWayPlatformController.class) != null;
	}
	
	public void letGo()
	{
		destroyGrabJoint();
		climbingFixture = null;
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
		boolean hasGrabbableProperty = hasGrabbableProperty(grabbableFixture);
		boolean isEnoughSpaceToGrab = isEnoughSpaceToGrab(sensorFixture, actor);		
		return notSensor && collidingWithFixtureTop && hasGrabbableProperty && isEnoughSpaceToGrab;
	}
	
	private boolean hasGrabbableProperty(Fixture grabbableFixture)
	{
		ZootActor fixtureActor = (ZootActor) grabbableFixture.getUserData();
		if(fixtureActor == null) return true;
		
		ClimbPropertiesController ctrl = fixtureActor.tryGetController(ClimbPropertiesController.class);
		return ctrl == null ? true : ctrl.canGrab();
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
	
	private boolean isEnoughSpaceToGrab(Fixture grabSensor, ZootActor climbingActor)
	{
		Vector2 actorCenter = climbingActor.getController(PhysicsBodyController.class).getCenterPositionRef();		
		Vector2 sensorCenter = ZootPhysicsUtils.getFixtureCenter(grabSensor).add(actorCenter);
		ZootBoundingBoxFactory.createAtRef(grabSensor, fixtureBoxCache);
		
		List<Fixture> found = scene.getPhysics().getFixturesInArea(
				sensorCenter.x - fixtureBoxCache.getWidth(), 
				sensorCenter.y - fixtureBoxCache.getHeight(), 
				sensorCenter.x + fixtureBoxCache.getWidth(),
				sensorCenter.y + fixtureBoxCache.getHeight());		

		List<Fixture> filtered = found.stream()
							   .filter(fix -> fix != grabSensor)
							   .filter(fix -> fix.getUserData() != grabSensor.getUserData())
							   .filter(fix -> fix != climbingFixture)
							   .filter(fix -> !fix.isSensor())
							   .filter(fix -> !isFixtureOneWayPlatform(fix))
							   .filter(fix -> isCollisionBetweenFixtureAndActor(fix, climbingActor))
							   .collect(Collectors.toList());		
		return filtered.size() <= 1;	//first object is the object that was grabbed
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
