package com.zootcat.controllers.logic;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.zootcat.controllers.physics.DetectFallController;
import com.zootcat.scene.ZootActor;

public class HurtOnJumpController extends HurtOnCollideController
{
	@Override
	public boolean canHurt(Fixture otherFixture)
	{
		ZootActor otherActor = (ZootActor) otherFixture.getUserData();		
		return otherActor.controllersAllMatch(DetectFallController.class, ctrl -> ctrl.isFalling()); 			
	}
}
