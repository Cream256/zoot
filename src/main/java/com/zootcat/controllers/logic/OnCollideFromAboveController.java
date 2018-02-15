package com.zootcat.controllers.logic;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.zootcat.controllers.physics.OnCollideController;
import com.zootcat.math.ZootBoundingBoxFactory;
import com.zootcat.scene.ZootActor;

public abstract class OnCollideFromAboveController extends OnCollideController
{
	private BoundingBox cachedBoundingBox = null;
		
	@Override
	public void onEnter(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		Fixture ctrlFixture = getControllerActorFixture(actorA, actorB, contact);
		BoundingBox ctrlFixtureBox = getControllerFixtureBoundingBox(ctrlFixture);		
		float fixtureTop = ctrlFixtureBox.max.y + ctrlFixture.getBody().getPosition().y;
		float fixtureHalfHeight = ctrlFixtureBox.getHeight() / 2.0f;
		
		Fixture otherFixture = getOtherFixture(actorA, actorB, contact);
		BoundingBox otherFixtureBox = ZootBoundingBoxFactory.create(otherFixture);
		float otherFixtureBottom = otherFixtureBox.min.y + otherFixture.getBody().getPosition().y; 
		
		boolean collidedFromAbove = otherFixtureBottom >= fixtureTop - fixtureHalfHeight; 
		if(collidedFromAbove)
		{
			onCollidedFromAbove(actorA, actorB, contact);
		}
	}

	@Override
	public void onLeave(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		//noop
	}
	
	public abstract void onCollidedFromAbove(ZootActor actorA, ZootActor actorB, Contact contact);
	
	private BoundingBox getControllerFixtureBoundingBox(Fixture fixture)
	{
		if(fixture.getBody().getType() == BodyType.StaticBody)
		{
			if(cachedBoundingBox == null)
			{
				cachedBoundingBox = ZootBoundingBoxFactory.create(fixture);
			}			
			return cachedBoundingBox;
		}		
		return ZootBoundingBoxFactory.create(fixture);
	}
}
