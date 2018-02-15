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
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

public class ZootPhysics implements Disposable
{
	private static final int POSITION_ITERATIONS = 2;
	private static final int VELOCITY_ITERATIONS = 6;
	private static final Vector2 DEFAULT_GRAVITY = new Vector2(0.0f, -9.80f);
	
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
	
	public void setGravity(float x, float y, float z)
	{
		world.setGravity(new Vector2(x, y));
	}
	
	public Body createBody(BodyDef bodyDef)
	{
		return world.createBody(bodyDef);
	}
	
	public void removeBody(Body body)
	{
		world.destroyBody(body);
	}
		
	public List<Fixture> createFixtures(Body body, List<FixtureDef> fixtureDefs) 
	{
		List<Fixture> fixtures = new ArrayList<Fixture>();
		fixtureDefs.forEach((def) -> 
		{
			fixtures.add(body.createFixture(def));
			def.shape.dispose();
			def.shape = null;
		});
		return fixtures;
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
}
