package com.zootcat.controllers.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.scene.ZootActor;
import com.zootcat.utils.ZootUtils;

public class CharacterFixtureController extends FixtureController
{	
	@CtrlParam private boolean vertical = true;
	
	@Override
	public void init(ZootActor actor)
	{
		super.init(actor);
	}
			
	@Override
	protected Array<FixtureDef> createFixtureDefs(ZootActor actor)
	{
		return vertical ? createVerticalCharacterFixtures(actor) : createHorizontalCharacterFixtures(actor);
	}
	
	private Array<FixtureDef> createVerticalCharacterFixtures(ZootActor actor)
	{
		//feet fixture def
		FixtureDef feetDef = new FixtureDef();
		feetDef.density = super.density;
		feetDef.friction = super.friction;
		feetDef.restitution = super.restitution;
		feetDef.isSensor = super.sensor;
		setupFilter(feetDef.filter);
		
		//feet shape
		float circleRadius = actor.getWidth() / 2.0f;
		float feetY = -(actor.getHeight() / 2.0f - circleRadius);
		CircleShape feetShape = new CircleShape();
		feetShape.setRadius(circleRadius);
		feetShape.setPosition(new Vector2(0.0f, feetY));
		feetDef.shape = feetShape;
		
		//body fixture def
		FixtureDef bodyDef = new FixtureDef();
		bodyDef.density = super.density;
		bodyDef.friction = super.friction;
		bodyDef.restitution = super.restitution;
		bodyDef.isSensor = super.sensor;		
		setupFilter(bodyDef.filter);
		
		//body shape
		float bodyWidth = actor.getWidth();
		float bodyHeight = actor.getHeight() - circleRadius;		
		PolygonShape bodyShape = new PolygonShape();
		bodyShape.setAsBox(bodyWidth / 2.0f, bodyHeight / 2.0f, new Vector2(0, circleRadius / 2.0f), 0.0f);
		bodyDef.shape = bodyShape;
		
		//result
		Array<FixtureDef> fixtureDefs = new Array<FixtureDef>(2);
		fixtureDefs.add(feetDef, bodyDef);
		return fixtureDefs;
	}
	
	private Array<FixtureDef> createHorizontalCharacterFixtures(ZootActor actor) 
	{		
		float circleRadius = actor.getHeight() / 4.0f;		
		int circleCount = ZootUtils.trunc(actor.getWidth() / circleRadius);			
		Array<FixtureDef> fixtureDefs = new Array<FixtureDef>(circleCount + 1);
		
		//feet fixture 
		float sx = -(actor.getWidth() / 2.0f) + circleRadius;
		float ex = actor.getWidth() / 2.0f - circleRadius;
		float y = actor.getHeight() / 2.0f - circleRadius;
		for(int circleIndex = 0; circleIndex < circleCount; ++circleIndex)
		{		
			FixtureDef feetPartDef = new FixtureDef();
			feetPartDef.density = super.density;
			feetPartDef.friction = super.friction;
			feetPartDef.restitution = super.restitution;
			feetPartDef.isSensor = super.sensor;		
			
			CircleShape feetShape = new CircleShape();
			feetShape.setRadius(circleRadius);
			feetShape.setPosition(new Vector2(sx, -y));
			feetPartDef.shape = feetShape;
			
			fixtureDefs.add(feetPartDef);
			
			sx = Math.min(ex, sx + circleRadius);
		}
		
		//body fixture def
		FixtureDef bodyDef = new FixtureDef();
		bodyDef.density = super.density;
		bodyDef.friction = super.friction;
		bodyDef.restitution = super.restitution;
		bodyDef.isSensor = super.sensor;	
		
		//body shape
		float bodyWidth = actor.getWidth();
		float bodyHeight = actor.getHeight() - circleRadius;
		PolygonShape bodyShape = new PolygonShape();
		bodyShape.setAsBox(bodyWidth / 2.0f, bodyHeight / 2.0f, new Vector2(0, circleRadius / 2.0f), 0.0f);
		bodyDef.shape = bodyShape;
		
		fixtureDefs.add(bodyDef);
		
		//result
		return fixtureDefs;
	}
}
