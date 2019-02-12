package com.zootcat.controllers.physics;

import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.controllers.ControllerPriority;
import com.zootcat.controllers.factory.CtrlDebug;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.physics.ZootPhysicsUtils;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;

public class PhysicsBodyController extends ControllerAdapter
{
	@CtrlParam protected float linearDamping = 0.0f;
	@CtrlParam protected float angularDamping = 0.0f;	
	@CtrlParam protected float gravityScale = 1.0f;
	@CtrlParam protected boolean bullet = false;
	@CtrlParam protected boolean canRotate = true;
	@CtrlParam protected boolean canSleep = true;
	@CtrlParam protected BodyType type = BodyType.DynamicBody;
	@CtrlParam(global = true) protected ZootScene scene;

	@CtrlDebug private float velocityX = 0.0f;
	@CtrlDebug private float velocityY = 0.0f;	
	private Body body;
	private Array<Fixture> fixtures;
	
	@Override
	public void init(ZootActor actor)
	{
		fixtures = new Array<Fixture>(false, 4);
		body = scene.getPhysics().createBody(createBodyDef(actor));
		body.setActive(false);
		body.setUserData(actor);
	}
	
	@Override
	public boolean isSingleton()
	{
		return true;
	}
	
	@Override
	public void onAdd(ZootActor actor) 
	{
		body.setActive(true);
	}

	@Override
	public void onRemove(ZootActor actor)
	{
		scene.getPhysics().removeBody(body);
		fixtures = null;
		body = null;
	}

	@Override
	public void onUpdate(float delta, ZootActor actor) 
	{
		float bottomLeftX = body.getPosition().x - actor.getWidth() * 0.5f; 
		float bottomLeftY = body.getPosition().y - actor.getHeight() * 0.5f;
		actor.setPosition(bottomLeftX, bottomLeftY);
		actor.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
		
		Vector2 velocity = body.getLinearVelocity();
		velocityX = velocity.x;
		velocityY = velocity.y;
	}
	
	@Override
	public ControllerPriority getPriority()
	{
		return ControllerPriority.High;
	}
		
	public Body getBody()
	{
		return body;
	}
	
	public ImmutableArray<Fixture> getFixtures()
	{
		return new ImmutableArray<Fixture>(fixtures);
	}
	
	public void setCollisionFilter(Filter collisionFilter)
	{
		fixtures.forEach((fixture) -> fixture.setFilterData(collisionFilter));
	}	
		
	public void setVelocity(float vx, float vy)
	{
		setVelocity(vx, vy, true, true);
	}
	
	public Vector2 getVelocity()
	{
		return body.getLinearVelocity();
	}
	
	public void setVelocity(float vx, float vy, boolean setX, boolean setY)
	{
		Vector2 velocity = body.getLinearVelocity();
		body.setLinearVelocity(setX ? vx : velocity.x, setY ? vy : velocity.y);	
	}
	
	public float getAngularVelocity()
	{
		return body.getAngularVelocity();
	}
	
	public void setAngularVelocity(float omega)
	{
		body.setAngularVelocity(omega);
	}
	
	public void setLinearDamping(float damping)
	{
		body.setLinearDamping(damping);
	}
	
	public float getLinearDamping()
	{
		return body.getLinearDamping();
	}
		
	public void setAwake(boolean awake)
	{
		body.setAwake(awake);
	}
	
	public boolean isSleepingAllowed()
	{
		return body.isSleepingAllowed();
	}
	
	public Fixture addFixture(FixtureDef fixtureDef, ZootActor actor)
	{
		Fixture fixture = scene.getPhysics().createFixture(body, fixtureDef);		
		fixture.setUserData(actor);
		
		fixtures.add(fixture);		
		return fixture;
	}
	
	public void removeFixture(Fixture fixture)
	{
		if(body != null && fixtures != null && fixture != null)
		{
			body.destroyFixture(fixture);
			fixtures.removeValue(fixture, true);
		}
	}
	
	public void setGravityScale(float scale)
	{
		body.setGravityScale(scale);	
		body.setAwake(true);
	}
	
	public float getGravityScale()
	{
		return body.getGravityScale();
	}
	
	public float getMass()
	{
		return body.getMass();
	}
	
	public void applyImpulse(float vx, float vy)
	{
		float cx = body.getPosition().x;
		float cy = body.getPosition().y;		
		body.applyLinearImpulse(vx, vy, cx, cy, true);
	}
	
	public void applyAngularImpulse(float i)
	{
		body.applyAngularImpulse(i, true);
	}
	
	public void setPosition(float x, float y)
	{
		body.setTransform(x, y, body.getAngle());
	}
	
	public Vector2 getCenterPositionRef()
	{
		return body.getPosition();
	}
	
	public void setCanRotate(boolean canRotate)
	{
		this.canRotate = canRotate;
		this.body.setFixedRotation(!canRotate);
	}
	
	public void scale(PhysicsBodyScale bodyScale)
	{
		fixtures.forEach(f ->
		{
			if(f.isSensor() && !bodyScale.scaleSensors) return;			
			ZootPhysicsUtils.scaleFixture(f, bodyScale.radiusScale, bodyScale.scaleX, bodyScale.scaleY);
		});
		body.setAwake(true);
	}
		
	protected BodyDef createBodyDef(ZootActor actor) 
	{
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.x = actor.getX() + actor.getWidth() * 0.5f;
		bodyDef.position.y = actor.getY() + actor.getHeight() * 0.5f;
		bodyDef.angle = actor.getRotation() * MathUtils.degreesToRadians;		
		bodyDef.active = true;
		bodyDef.allowSleep = canSleep;
		bodyDef.angularDamping = angularDamping;
		bodyDef.angularVelocity = 0.0f;
		bodyDef.awake = true;
		bodyDef.bullet = bullet;
		bodyDef.fixedRotation = !canRotate;
		bodyDef.gravityScale = gravityScale;
		bodyDef.linearDamping = linearDamping;
		bodyDef.type = type;		
		return bodyDef;
	}
}
