package com.zootcat.controllers.logic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.collision.BoundingBox;
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
import com.zootcat.math.ZootBoundingBoxFactory;
import com.zootcat.physics.ZootShapeFactory;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;
import com.zootcat.utils.BitMaskConverter;

//TODO add test
//TODO update doc
/**
 * Climb Controller - used to enable actor to climb on platforms and ledges.
 * 
 * @ctrlParam mask - collision filter mask used to determine with which bodies can be climbed
 * @ctrlParam timeout - timeout after which actor can try to climb again in sec, 1.0f by default
 * @author Cream
 */
public class ClimbController extends PhysicsCollisionController
{	
	@CtrlParam(debug = true) private String mask;
	@CtrlParam(debug = true) private float timeout = 1.0f;
	@CtrlParam(debug = true) private float maxVelocity = 1.0f;
	@CtrlParam(global = true) private ZootScene scene;
	
	private float climbTimeout;
	private Fixture climbSensorFixture;	
	private Set<Fixture> collidedFixtures = new HashSet<Fixture>();
	private Map<Fixture, Contact> fixtureContacts = new HashMap<Fixture, Contact>();
	
	@Override
	public void onAdd(ZootActor actor)
	{
		super.onAdd(actor);
		climbTimeout = 0.0f;
		
		//create sensor
		Shape climbSensorShape = createClimbSensorShape(actor);
		FixtureDef climbSensorFixtureDef = createClimbSensorFixtureDef(actor, climbSensorShape);		
		climbSensorFixture = actor.getController(PhysicsBodyController.class).addFixture(climbSensorFixtureDef, actor);
				
		//cleanup
		climbSensorShape.dispose();
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
				
		if(!canActorClimb(actor))
		{
			return;
		}
		
		Fixture climbableFixture = collidedFixtures.stream().filter(fixture -> canGrabFixture(actor, fixture)).findFirst().orElse(null); 
		if(climbableFixture != null)
		{
			grab(actor, climbableFixture);
		}
	}
	
	@Override
	public void beginContact(ZootActor actorA, ZootActor actorB, Contact contact)
	{		
		if(isContactWithClimbSensor(contact))
		{
			Fixture otherFixture = getOtherFixture(contact);
			fixtureContacts.put(otherFixture, contact);	//TODO maybe this could be just contact list?
			collidedFixtures.add(otherFixture);
		}
	}

	@Override
	public void endContact(ZootActor actorA, ZootActor actorB, Contact contact)
	{		
		collidedFixtures.remove(getOtherFixture(contact));
		fixtureContacts.remove(getOtherFixture(contact));
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

	public boolean canActorClimb(ZootActor actor)
	{
		boolean timeoutOk = climbTimeout == 0;
		boolean enoughVelocityToClimb = actor.getController(PhysicsBodyController.class).getVelocity().y <= maxVelocity;
		return timeoutOk && enoughVelocityToClimb && !isActorClimbing(actor);
	}

	public void grab(ZootActor actor, Fixture climbableFixture)
	{
		ZootEvents.fireAndFree(actor, ZootEventType.Climb);
	}
	
	public Fixture getClimbSensor()
	{
		return climbSensorFixture;
	}
	
	public boolean isActorClimbing(ZootActor actor)
	{
		return actor.getStateMachine().getCurrentState().getId() == ClimbState.ID;
	}
	
	public boolean canGrabFixture(ZootActor actor, Fixture fixture)
	{
		boolean notSensor = !fixture.isSensor();
		boolean collidingWithFixtureTop = isCollidingWithFixtureTop(fixture);
		boolean enoughSpaceAbove = isEnoughSpaceToClimb(fixture, actor.getWidth(), actor.getHeight());		
		return notSensor && collidingWithFixtureTop && enoughSpaceAbove;
	}	
	
	private boolean isCollidingWithFixtureTop(Fixture fixture)
	{
		BoundingBox box = ZootBoundingBoxFactory.create(fixture);
		
		Contact contact = fixtureContacts.get(fixture);
		
		Vector2[] points = contact.getWorldManifold().getPoints();
		for(Vector2 point : points)
		{
			Vector2 localPoint = fixture.getBody().getLocalPoint(point);
			float diff = box.max.y - localPoint.y;
			System.out.println(diff);
			if(diff < 0.1f)
			{
				return true;
			}
		}
		return false;
	}

	private boolean isEnoughSpaceToClimb(Fixture fixture, float requiredWidth, float requiredHeight)
	{		
		BoundingBox box = ZootBoundingBoxFactory.create(fixture);
		List<Fixture> fixturesAbove = scene.getPhysics().getFixturesInArea(box.min.x, box.max.y, requiredWidth, requiredHeight);
		
		boolean result = fixturesAbove.stream().filter(fix -> fix != fixture).allMatch(fix -> fix.isSensor()); 		
		//System.out.println(fixturesAbove.size() + " " + result);
		return result;
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
