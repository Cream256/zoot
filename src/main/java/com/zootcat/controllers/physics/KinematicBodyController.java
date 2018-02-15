package com.zootcat.controllers.physics;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.zootcat.scene.ZootActor;

public class KinematicBodyController extends PhysicsBodyController
{
	@Override
	public void init(ZootActor actor)
	{
		super.type = BodyType.KinematicBody;		
		super.init(actor);
	}
}
