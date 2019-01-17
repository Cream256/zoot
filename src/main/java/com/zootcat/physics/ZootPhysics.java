package com.zootcat.physics;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

public class ZootPhysics implements Disposable
{
	public static final Vector2 DEFAULT_GRAVITY = new Vector2(0.0f, -9.80f);
	
	private static final int POSITION_ITERATIONS = 2;
	private static final int VELOCITY_ITERATIONS = 6;
	
	private World world;
	private ZootContactFilter contactFilter = new ZootContactFilter();
	
	public ZootPhysics()
	{
		Box2D.init();
		world = new World(DEFAULT_GRAVITY, true);	
		world.setContactListener(new ZootPhysicsContactListener());
		world.setContactFilter(contactFilter);
	}
	
	public void addFixtureContactFilter(Fixture fixture, ContactFilter filter)
	{
		contactFilter.addFixtureFilter(fixture, filter);
	}
	
	public void removeFixtureContactFilter(Fixture fixture, ContactFilter filter)
	{
		contactFilter.removeFixtureFilter(fixture, filter);
	}
	
	public void removeFixtureContactFilters(Fixture fixture)
	{
		contactFilter.removeFixtureFilters(fixture);
	}
	
	public void setGravity(float x, float y)
	{
		world.setGravity(new Vector2(x, y));
	}
	
	public Vector2 getGravity()
	{
		return world.getGravity().cpy();
	}
	
	public Body createBody(BodyDef bodyDef)
	{
		return world.createBody(bodyDef);
	}
	
	public void removeBody(Body body)
	{
		world.destroyBody(body);
	}
		
	/**
	 * Creates a fixture and attaches it to a body.<br/>
	 * Note - you need to dispose the fixture definition shape on your own.
	 * @param body - body to attach fixture to
	 * @param fixtureDef - fixture definition 
	 * @return New fixture
	 */
	public Fixture createFixture(Body body, FixtureDef fixtureDef)
	{
		return body.createFixture(fixtureDef);
	}
	
	/**
	 * Creates a list of fixtures and attaches them to a body.<br/>
	 * Note - you need to dispose the fixture definition shapes on your own.
	 * @param body - body to attach fixtures to
	 * @param fixtureDef - list of fixture definitions 
	 * @return List of new fixtures
	 */
	public List<Fixture> createFixtures(Body body, List<FixtureDef> fixtureDefs) 
	{
		List<Fixture> fixtures = new ArrayList<Fixture>();
		fixtureDefs.forEach((def) -> fixtures.add(createFixture(body, def)));
		return fixtures;
	}
	
	public void destroyFixture(Body body, Fixture fixture)
	{
		body.destroyFixture(fixture);
	}
	
	public void step(float delta)
	{
		world.step(delta, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
	}
	
	public void dispose() 
	{		
		world.dispose();
		world = null;
	}

	public World getWorld() 
	{
		return world;
	}

	public List<Fixture> getFixturesInArea(float lowerX, float lowerY, float upperX, float upperY)
	{
		List<Fixture> result = new ArrayList<Fixture>();		
		world.QueryAABB(new QueryCallback()
		{			
			@Override
			public boolean reportFixture(Fixture fixture)
			{
				result.add(fixture);
				return true;
			}
		}, lowerX, lowerY, upperX, upperY);				
		return result;
	}

	public Joint createJoint(JointDef jointDef)
	{
		return world.createJoint(jointDef);
	}

	public void destroyJoint(Joint joint)
	{
		world.destroyJoint(joint);
	}
}
