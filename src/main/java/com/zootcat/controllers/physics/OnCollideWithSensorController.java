package com.zootcat.controllers.physics;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.Shape;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.physics.ZootPhysicsUtils;
import com.zootcat.physics.ZootShapeFactory;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;

/**
 * OnCollideWithSensor Controller - controller that checks for collision within an actor
 * only with specific sensor that was created for the actor. The sensor can have custom
 * size and position, and a filter specification. Fixtures that collided with sensor are
 * processed in order of collision.
 * 
 * @override onCollideWithSensor - should return false to stop processing other fixtures. 
 * 
 * @ctrlParam sensorWidth - width of the sensor, default 1.0
 * @ctrlParam sensorHeight - height of the sensor, default 1.0
 * @ctrlParam sensorX - X center of the sensor, default 0.0
 * @ctlrParam sensorY - Y center of the sensor, default 0.0 
 * 
 * @author Cream
 *
 */
public class OnCollideWithSensorController extends OnCollideController
{
	@CtrlParam protected float sensorWidth = 1.0f;
	@CtrlParam protected float sensorHeight = 1.0f;
	@CtrlParam protected float sensorX = 0.0f;
	@CtrlParam protected float sensorY = 0.0f;
	@CtrlParam protected boolean useActorSize = false;
	@CtrlParam protected boolean useScaledSize = false;
	@CtrlParam(global = true) protected ZootScene scene;
	
	public enum SensorCollisionResult { ProcessNext, StopProcessing };
	
	private Fixture sensor;
	private Set<Fixture> collidedFixtures = new LinkedHashSet<Fixture>();
	private Set<Fixture> disabledFixtures = new HashSet<Fixture>();

	public OnCollideWithSensorController()
	{
		//noop
	}
	
	public OnCollideWithSensorController(float sensorWidth, float sensorHeight, float sensorX, float sensorY)
	{
		this.sensorX = sensorX;
		this.sensorY = sensorY;
		this.sensorWidth = sensorWidth;
		this.sensorHeight = sensorHeight;
	}
	
	@Override
	public void onAdd(ZootActor actor)
	{
		super.onAdd(actor);
		
		//create sensor
		Shape sensorShape = createSensorShape(actor);
		FixtureDef sensorFixtureDef = createSensorFixtureDef(actor, sensorShape);		
		sensor = actor.getController(PhysicsBodyController.class).addFixture(sensorFixtureDef, actor);

		//cleanup
		//sensorShape.dispose();	// TODO this causes tests to crash
		collidedFixtures.clear();
	}
	
	@Override
	public void onRemove(ZootActor actor) 
	{
		collidedFixtures.clear();
		disabledFixtures.clear();
		
		actor.getController(PhysicsBodyController.class).removeFixture(sensor);
		sensor = null;		
		
		super.onRemove(actor);
	}
	
	@Override
	public void onBeginContact(ZootActor actorA, ZootActor actorB, Contact contact)
	{						
		ZootActor otherActor = getOtherActor(actorA, actorB);
		if(collidedWithSensor(contact) && beginCollisionCounts(otherActor))
		{
			collidingActors.add(otherActor);
			onEnter(actorA, actorB, contact);
		}
	}
	
	@Override
	public void onEnter(ZootActor actorA, ZootActor actorB, Contact contact)
	{	
		Fixture otherFixture = getOtherFixture(actorA, actorB, contact); 
		collidedFixtures.add(otherFixture);
		onEnterCollision(otherFixture);
	}
	
	@Override
	public void onEndContact(ZootActor actorA, ZootActor actorB, Contact contact)
	{		
		ZootActor otherActor = getOtherActor(actorA, actorB);
		if(collidedWithSensor(contact) && endCollisionCounts(otherActor))
		{			
			onLeave(actorA, actorB, contact);
			collidingActors.remove(otherActor);
		}
	}
	
	@Override
	public void onLeave(ZootActor actorA, ZootActor actorB, Contact contact)
	{		
		Fixture otherFixture = getOtherFixture(actorA, actorB, contact);
		collidedFixtures.remove(otherFixture);
		disabledFixtures.remove(otherFixture);
		onLeaveCollision(otherFixture);
	}
			
	@Override
	public final void onUpdate(float delta, ZootActor actor)
	{
		preUpdate(delta, actor);
		
		List<Fixture> collided = collidedFixtures.stream().filter(fix -> shouldCollide(fix)).collect(Collectors.toList());		
		for(Fixture fixture : collided)
		{
			if(onCollision(fixture) == SensorCollisionResult.StopProcessing)
			{
				break;
			}
		}
		
		postUpdate(delta, actor);
	}
	
	public SensorCollisionResult onCollision(Fixture fixture)
	{
		//noop, to be overriden by derived classes		
		return SensorCollisionResult.StopProcessing;
	}
	
	public void onEnterCollision(Fixture fixture)
	{
		//noop
	}
	
	public void onLeaveCollision(Fixture fixture)
	{
		//noop
	}
	
	public void preUpdate(float delta, ZootActor actor)
	{
		//noop, to be overriden by derived classes
	}
	
	public void postUpdate(float delta, ZootActor actor)
	{
		//noop, to be overriden by derived classes		
	}
	
	private boolean collidedWithSensor(Contact contact)
	{
		return contact.getFixtureA() == sensor || contact.getFixtureB() == sensor;
	}
	
	protected boolean shouldCollide(Fixture fixture)
	{
		boolean contactEnabled = !disabledFixtures.contains(fixture);	
		return contactEnabled;
	}
	
	//Box2D enables all contacts after postSolve step, so we need to keep track of them in the preSolve step
	@Override
	public void onPreSolve(ZootActor actorA, ZootActor actorB, Contact contact, Manifold manifold)
	{	
		Fixture otherFixture = getOtherFixture(actorA, actorB, contact);
		if(!contact.isEnabled() && collidedFixtures.contains(otherFixture))
		{
			disabledFixtures.add(otherFixture);
		}
		else if(contact.isEnabled() && disabledFixtures.contains(otherFixture))
		{
			disabledFixtures.remove(otherFixture);
		}
	}
		
	public void setSensorPosition(float x, float y)
	{
		ZootPhysicsUtils.setFixturePosition(sensor, x, y);
	}
	
	public Vector2 getSensorPosition()
	{
		return ZootPhysicsUtils.getFixtureCenter(sensor);
	}
		
	public void scaleSensor(float scale)
	{
		ZootPhysicsUtils.scaleFixture(sensor, 1.0f, scale, scale);		
	}
	
	public Fixture getSensor()
	{
		return sensor;
	}	
	
	@Override
	public void setFilter(Filter filter)	
	{
		getSensor().setFilterData(filter);
		super.setFilter(filter);		
	}
	
	public void setScene(ZootScene scene)
	{
		this.scene = scene;
	}
	
	public ZootScene getScene()
	{
		return scene;
	}
					
	private FixtureDef createSensorFixtureDef(ZootActor actor, Shape sensorShape)
	{
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = sensorShape;
		fixtureDef.isSensor = true;
		fixtureDef.filter.categoryBits = getFilter().categoryBits;
		fixtureDef.filter.maskBits = getFilter().maskBits;
		fixtureDef.filter.groupIndex = getFilter().groupIndex;
		return fixtureDef;
	}

	private Shape createSensorShape(ZootActor actor)
	{
		if(useScaledSize)
		{
			float width = actor.getWidth() * sensorWidth;
			float height = actor.getHeight() * sensorHeight;
			float x = actor.getWidth() * sensorX;
			float y = actor.getHeight() * sensorY;
			return ZootShapeFactory.createBox(width, height, x, y);
		}
		
		float width = useActorSize ? actor.getWidth() : sensorWidth * scene.getUnitScale();
		float height = useActorSize ? actor.getHeight() : sensorHeight * scene.getUnitScale();
		float x = sensorX * scene.getUnitScale();
		float y = sensorY * scene.getUnitScale();		
		return ZootShapeFactory.createBox(width, height, x, y);		
	}
}