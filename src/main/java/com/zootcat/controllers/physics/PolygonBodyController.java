package com.zootcat.controllers.physics;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.zootcat.physics.ZootBodyShape;
import com.zootcat.scene.ZootActor;

public class PolygonBodyController extends PhysicsBodyController
{
	@Override
	public void init(ZootActor actor)
	{
		type = BodyType.StaticBody;
		shape = ZootBodyShape.POLYGON;
		super.init(actor);
	}	
}
