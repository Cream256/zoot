package com.zootcat.controllers.logic;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.Shape;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.controllers.physics.CollisionFilterController;
import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.controllers.physics.PhysicsCollisionController;
import com.zootcat.events.ZootEventType;
import com.zootcat.events.ZootEvents;
import com.zootcat.fsm.states.ClimbState;
import com.zootcat.physics.ZootShapeFactory;
import com.zootcat.scene.ZootActor;
import com.zootcat.utils.BitMaskConverter;

//TODO add test
/**
 * Climb Controller - used to enable actor to climb on platforms and ledges.
 * 
 * @ctrlParam mask - collision filter mask used to determine with which bodies can be climbed
 * @author Cream
 */
public class ClimbController extends PhysicsCollisionController
{
	@CtrlParam(debug = true) private String mask;
	
	private Fixture climbSensorFixture;
	private Set<Fixture> collidedFixtures = new HashSet<Fixture>();
	
	@Override
	public void onAdd(ZootActor actor)
	{
		super.onAdd(actor);
		
		Shape climbSensorShape = createClimbSensorShape(actor);
		FixtureDef climbSensorFixtureDef = createClimbSensorFixtureDef(actor, climbSensorShape);		
		climbSensorFixture = actor.getController(PhysicsBodyController.class).addFixture(climbSensorFixtureDef, actor);
		
		//cleanup
		climbSensorShape.dispose();
	}
		
	@Override
	public void onUpdate(float delta, ZootActor actor) 
	{
		if(canClimb(actor))
		{
			ZootEvents.fireAndFree(actor, ZootEventType.Climb);
		}
	}
	
	@Override
	public void beginContact(ZootActor actorA, ZootActor actorB, Contact contact)
	{		
		if(isContactWithClimbSensor(contact))
		{
			collidedFixtures.add(getOtherFixture(contact));
		}
	}

	@Override
	public void endContact(ZootActor actorA, ZootActor actorB, Contact contact)
	{		
		collidedFixtures.remove(getOtherFixture(contact));
	}

	@Override
	public void preSolve(ZootActor actorA, ZootActor actorB, Contact contact, Manifold manifold)
	{
		//noop
	}

	@Override
	public void postSolve(ZootActor actorA, ZootActor actorB, ContactImpulse contactImpulse)
	{
		//noop
	}

	public Fixture getClimbSensor()
	{
		return climbSensorFixture;
	}
	
	private boolean canClimb(ZootActor actor)
	{
		boolean hasCollisions = collidedFixtures.size() > 0;
		boolean actorFalling = actor.getController(PhysicsBodyController.class).getVelocity().y <= 0.0f;		
		boolean actorNotClimbing = actor.getStateMachine().getCurrentState().getId() != ClimbState.ID;
		
		return hasCollisions && actorFalling && actorNotClimbing;
	}	
	
	private boolean isContactWithClimbSensor(Contact contact)
	{
		return contact.getFixtureA() == climbSensorFixture || contact.getFixtureB() == climbSensorFixture;
	}
	
	private Fixture getOtherFixture(Contact contact)
	{
		return contact.getFixtureA() == climbSensorFixture ? contact.getFixtureB() : contact.getFixtureA();
	}
	
	private FixtureDef createClimbSensorFixtureDef(ZootActor actor, Shape climbSensorShape)
	{
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = climbSensorShape;
		fixtureDef.isSensor = true;
		
		CollisionFilterController filterCtrl = actor.tryGetController(CollisionFilterController.class);
		if(filterCtrl != null)
		{
			Filter existingFilter = filterCtrl.getCollisionFilter();			
			fixtureDef.filter.categoryBits = existingFilter.categoryBits;
			fixtureDef.filter.groupIndex = existingFilter.groupIndex;			
		}
		fixtureDef.filter.maskBits = BitMaskConverter.Instance.fromString(mask);
		
		return fixtureDef;
	}

	private Shape createClimbSensorShape(ZootActor actor)
	{
		float sensorHeight = actor.getHeight() * 0.2f;
		float sensorY = actor.getHeight() / 2.0f + sensorHeight / 2.0f;
		
		return ZootShapeFactory.createBox(actor.getWidth(), sensorHeight, 0.0f, sensorY);		
	}
}
