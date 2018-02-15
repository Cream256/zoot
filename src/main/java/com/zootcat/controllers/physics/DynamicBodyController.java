package com.zootcat.controllers.physics;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.zootcat.scene.ZootActor;

public class DynamicBodyController extends PhysicsBodyController 
{
	@Override
	public void init(ZootActor actor)
	{
		super.type = BodyType.DynamicBody;		
		super.init(actor);
	}
}
