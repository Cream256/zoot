package com.zootcat.controllers.physics;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.zootcat.controllers.factory.CtrlDebug;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.events.ZootEvents;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;

/**
 * DetectGround controller - Creates a feet sensor, that is detecting if
 * the actor is in contact with the ground. When he is, the controller 
 * emits Ground {@link ZootEvent}.
 * 
 * @ctrlParam sensorWidth - feet sensor width. If not set, actor width will be used.
 * @author Cream
 *
 */
public class DetectGroundController extends PhysicsCollisionController
{
	public static final float SENSOR_HEIGHT_PERCENT = 0.2f;
	
	@CtrlParam private int sensorWidth = 0;		
	@CtrlParam(global = true) private ZootScene scene;
	@CtrlDebug private boolean isOnGround = false;
	
	private Fixture feetSensor;
	private ZootActor actorWithSensor;
	private PhysicsBodyController physicsCtrl;
	private Set<Fixture> collidedFixtures = new HashSet<Fixture>();
	private Set<Fixture> ignoredFixtures = new HashSet<Fixture>();
	
	@Override
	public void onAdd(ZootActor actor)
	{
		super.onAdd(actor);
		actorWithSensor = actor;
		physicsCtrl = actor.getController(PhysicsBodyController.class);
				
		//create feet shape		
		Shape feetShape = createFeetShape(actor);
		
		//create fixture
		FixtureDef feetDef = createFeetSensorFixtureDef(physicsCtrl.getBody(), actor, feetShape);		
		feetSensor = physicsCtrl.addFixture(feetDef, actor);
		
		//cleanup
		feetShape.dispose();
		collidedFixtures.clear();
		ignoredFixtures.clear();
	}

	@Override
	public void onRemove(ZootActor actor)
	{
		super.onRemove(actor);
		collidedFixtures.clear();
		ignoredFixtures.clear();
		physicsCtrl.removeFixture(feetSensor);
		physicsCtrl = null;
	}

	@Override
	public void onUpdate(float delta, ZootActor actor)
	{
		isOnGround = collidedFixtures.size() - ignoredFixtures.size() > 0;
		if(isOnGround)
		{
			ZootEvents.fireAndFree(actorWithSensor, ZootEventType.Ground);
		}
	}
	
	@Override
	public void beginContact(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		if(isContactWithFeetSensor(actorA, actorB, contact))
		{
			collidedFixtures.add(getOtherFixture(contact));
		}
	}

	@Override
	public void endContact(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		if(isContactWithFeetSensor(actorA, actorB, contact))
		{
			Fixture otherFixture = getOtherFixture(contact);			
			collidedFixtures.remove(otherFixture);
			ignoredFixtures.remove(otherFixture);
		}
	}
		
	@Override
	public void preSolve(ZootActor actorA, ZootActor actorB, Contact contact, Manifold manifold)
	{	
		Fixture otherFixture = getOtherFixture(contact);
		boolean shouldFilter = shouldFilter(contact, otherFixture);
		
		if(shouldFilter && !ignoredFixtures.contains(otherFixture) && collidedFixtures.contains(otherFixture)) 
		{			
			ignoredFixtures.add(otherFixture);
		}		
		else if(!shouldFilter && ignoredFixtures.contains(otherFixture))
		{	
			ignoredFixtures.remove(otherFixture);
		}
	}

	@Override
	public void postSolve(ZootActor actorA, ZootActor actorB, ContactImpulse contactImpulse)
	{
		//noop
	}
	
	public boolean isOnGround()
	{
		return isOnGround;
	}

	public Fixture getFeetFixture()
	{
		return feetSensor;
	}
	
	private boolean shouldFilter(Contact contact, Fixture otherFixture)
	{
		boolean contactNotEnabled = !contact.isEnabled();
		boolean fixtureIsSensor = otherFixture.isSensor();	
		return contactNotEnabled || fixtureIsSensor;
	}
	
	private boolean isContactWithFeetSensor(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		return contact.getFixtureA() == feetSensor || contact.getFixtureB() == feetSensor;		
	}
	
	private float calculateWidth(ZootActor actor)
	{
		if(sensorWidth == 0)
		{
			return actor.getWidth() / 2.0f;
		}
		return (sensorWidth / 2.0f) * scene.getUnitScale();
	}
	
	private Shape createFeetShape(ZootActor actor)
	{
		Vector2 center = new Vector2(0.0f, -actor.getHeight() / 2.0f);		
		
		float feetWidth = calculateWidth(actor);
		float feetHeight = (actor.getHeight() * SENSOR_HEIGHT_PERCENT) / 2.0f;	//10% of actor height
		
		PolygonShape feetShape = new PolygonShape();
		feetShape.setAsBox(feetWidth, feetHeight, center, 0.0f);				
		return feetShape;
	}
	
	private FixtureDef createFeetSensorFixtureDef(Body body, ZootActor actor, Shape feetShape)
	{
		FixtureDef def = new FixtureDef();
		def.isSensor = true;
		def.friction = 0.0f;		
		def.shape = feetShape;
		
		CollisionFilterController filterCtrl = actor.tryGetController(CollisionFilterController.class);
		if(filterCtrl != null)
		{
			Filter existingFilter = filterCtrl.getCollisionFilter();			
			def.filter.categoryBits = existingFilter.categoryBits;
			def.filter.maskBits = existingFilter.maskBits;
			def.filter.groupIndex = existingFilter.groupIndex;
		}
		return def;
	}
	
	private Fixture getOtherFixture(Contact contact)
	{
		return contact.getFixtureA() == feetSensor ? contact.getFixtureB() : contact.getFixtureA();		
	}
}
