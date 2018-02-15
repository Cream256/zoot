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
import com.zootcat.events.ZootEvent;
import com.zootcat.events.ZootEventType;
import com.zootcat.events.ZootEvents;
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
	
	private Fixture feet;
	private ZootActor actorWithSensor;
	private PhysicsBodyController physicsCtrl;
	private Set<Contact> allContacts = new HashSet<Contact>();
	private Set<Contact> disabledContacts = new HashSet<Contact>();

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
		feet = physicsCtrl.addFixture(feetDef, actor);
		
		//cleanup
		feetShape.dispose();
	}

	@Override
	public void onRemove(ZootActor actor)
	{
		super.onRemove(actor);
		physicsCtrl.removeFixture(feet);
		physicsCtrl = null;
	}

	@Override
	public void onUpdate(float delta, ZootActor actor)
	{
		isOnGround = allContacts.size() > disabledContacts.size();
		if(isOnGround)
		{
			ZootEvents.fireAndFree(actorWithSensor, ZootEventType.Ground);
		}
	}
	
	@Override
	public void beginContact(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		if(isContactWithGroundSensor(actorA, actorB, contact)) allContacts.add(contact);
	}

	@Override
	public void endContact(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		if(isContactWithGroundSensor(actorA, actorB, contact)) 
		{
			allContacts.remove(contact);
			disabledContacts.remove(contact);
		}
	}

	@Override
	public void preSolve(ZootActor actorA, ZootActor actorB, Contact contact, Manifold manifold)
	{
		if(!contact.isEnabled()) disabledContacts.add(contact);		
		else disabledContacts.remove(contact);
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

	private boolean isContactWithGroundSensor(ZootActor actorA, ZootActor actorB, Contact contact)
	{		
		boolean contactWithFeet = contact.getFixtureA() == feet || contact.getFixtureB() == feet;
		boolean contactWithActor = actorA == actorWithSensor || actorB == actorWithSensor;
		boolean notSensors = !contact.getFixtureA().isSensor() || !contact.getFixtureB().isSensor();		
		return contactWithFeet && contactWithActor && notSensors;
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
}
