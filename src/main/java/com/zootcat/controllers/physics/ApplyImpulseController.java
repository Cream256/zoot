package com.zootcat.controllers.physics;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.physics.box2d.Contact;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.scene.ZootActor;

public class ApplyImpulseController extends OnCollideController
{
	@CtrlParam private float impulseX = 0.0f;
	@CtrlParam private float impulseY = 0.0f;
	@CtrlParam private boolean continous = true;	
	
	private Set<ZootActor> collidedActors = new HashSet<ZootActor>();
	
	@Override
	public void onEnter(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		collidedActors.add(getOtherActor(actorA, actorB));
	}

	@Override
	public void onLeave(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		collidedActors.remove(getOtherActor(actorA, actorB));
	}
	
	@Override
	public void onUpdate(float delta, ZootActor actor) 
	{
		super.onUpdate(delta, actor);		
		collidedActors.forEach(act -> act.controllerAction(PhysicsBodyController.class, ctrl -> ctrl.applyImpulse(impulseX * delta, impulseY * delta)));
		
		if(!continous) collidedActors.clear();
	}
}