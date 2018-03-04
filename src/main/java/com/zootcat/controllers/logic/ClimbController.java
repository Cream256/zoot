package com.zootcat.controllers.logic;

import java.util.List;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.zootcat.controllers.factory.CtrlDebug;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.controllers.physics.OnCollideWithSensorController;
import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.events.ZootEventType;
import com.zootcat.events.ZootEvents;
import com.zootcat.fsm.states.ClimbState;
import com.zootcat.math.ZootBoundingBoxFactory;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;

/**
 * Climb Controller - used to enable actor to climb on platforms and ledges.
 * 
 * @ctrlParam timeout - timeout after which actor can try to climb again in sec, 1.0f by default
 * @ctrlParam maxVelocity - maximum velocity at which actor can climb
 * @ctrlParam treshold - how close to the platform top actor must be in order to climb
 * @author Cream
 */
public class ClimbController extends OnCollideWithSensorController
{		
	@CtrlParam(debug = true) private float timeout = 1.0f;
	@CtrlParam(debug = true) private float maxVelocity = 1.0f;
	@CtrlParam(debug = true) private float treshold = 0.15f;
	
	@CtrlDebug private float climbTimeout;
	
	@Override
	public void onAdd(ZootActor actor)
	{
		super.onAdd(actor);
		climbTimeout = 0.0f;
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
		if(!canActorClimb(getControllerActor()))
		{
			return SensorCollisionResult.StopProcessing;
		}
				
		if(canGrabFixture(getControllerActor(), getSensor(), fixture))
		{
			grab(getControllerActor(), fixture);
			return SensorCollisionResult.StopProcessing;
		}
		
		return SensorCollisionResult.ProcessNext;
	}
		
	public boolean canActorClimb(ZootActor actor)
	{
		boolean timeoutOk = climbTimeout == 0;
		boolean enoughVelocityToClimb = actor.getController(PhysicsBodyController.class).getVelocity().y <= maxVelocity;
		boolean notClimbingNow = !isActorClimbing(actor);
		return timeoutOk && enoughVelocityToClimb && notClimbingNow;
	}

	public void grab(ZootActor actor, Fixture climbableFixture)
	{
		ZootEvents.fireAndFree(actor, ZootEventType.Climb);
		climbTimeout = timeout;
	}
		
	public boolean isActorClimbing(ZootActor actor)
	{
		return actor.getStateMachine().getCurrentState().getId() == ClimbState.ID;
	}
	
	public boolean canGrabFixture(ZootActor actor, Fixture sensorFixture, Fixture grabbableFixture)
	{
		boolean notSensor = !grabbableFixture.isSensor();
		boolean collidingWithFixtureTop = isCollidingWithFixtureTop(sensorFixture, grabbableFixture);
		boolean enoughSpaceAbove = isEnoughSpaceToClimb(grabbableFixture, actor.getWidth(), actor.getHeight());		
		return notSensor && collidingWithFixtureTop && enoughSpaceAbove;
	}
	
	private boolean isCollidingWithFixtureTop(Fixture actorFixture, Fixture edgeFixture)
	{
		BoundingBox actorBox = ZootBoundingBoxFactory.create(actorFixture);
		BoundingBox edgeBox = ZootBoundingBoxFactory.create(edgeFixture);
		
		float diff = actorBox.max.y - edgeBox.max.y;		
		return 0.0f <= diff && diff <= treshold;
	}

	private boolean isEnoughSpaceToClimb(Fixture fixture, float requiredWidth, float requiredHeight)
	{		
		BoundingBox box = ZootBoundingBoxFactory.create(fixture);
		List<Fixture> fixturesAbove = getScene().getPhysics().getFixturesInArea(box.min.x, box.max.y, requiredWidth, requiredHeight);
		
		boolean result = fixturesAbove.stream().filter(fix -> fix != fixture).allMatch(fix -> fix.isSensor());
		return result;
	}
}
