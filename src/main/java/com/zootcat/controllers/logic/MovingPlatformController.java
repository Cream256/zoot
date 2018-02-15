package com.zootcat.controllers.logic;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;
import com.zootcat.scene.ZootScene;

public class MovingPlatformController extends OnCollideFromAboveController
{
	@CtrlParam(debug = true) private float range = 0.0f;
	@CtrlParam(debug = true) private float speed = 1.0f;
	@CtrlParam(debug = true) private boolean enabled = true;
	@CtrlParam(debug = true) private boolean comeback = true;
	@CtrlParam(debug = true) private ZootDirection direction = ZootDirection.Right;
	@CtrlParam(global = true) private ZootScene scene;
	
	private Vector2 start;
	private Vector2 current;
	private float worldRange;
	private float platformVx;
	private float platformVy;
	private Set<ZootActor> connectedActors = new HashSet<ZootActor>();
	
	@Override
	public void init(ZootActor actor) 
	{
		super.init(actor);
		
		start = new Vector2(actor.getX(), actor.getY());
		current = start.cpy();
		worldRange = range * scene.getUnitScale();
	}
	
	@Override
	public void onUpdate(float delta, ZootActor actor)
	{
		if(!enabled)
		{
			actor.controllerAction(PhysicsBodyController.class, ctrl -> ctrl.setVelocity(0.0f, 0.0f));
			return;
		}
		
		current.x = actor.getX();
		current.y = actor.getY();
		if(current.dst(start) >= worldRange) 
		{
			direction = direction.invert();
			enabled = comeback;
			start.set(current);
		}
		
		platformVx = (direction.isHorizontal() ? speed : 0.0f) * direction.getHorizontalValue();
		platformVy = (direction.isVertical() ? speed : 0.0f) * direction.getVerticalValue();		
		actor.controllerAction(PhysicsBodyController.class, ctrl -> ctrl.setVelocity(platformVx, platformVy));
		
		updateConnectedActors();
	}
	
	public void setEnabled(boolean value)
	{
		enabled = value;
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}
	
	public void setComeback(boolean value)
	{
		comeback = value;
	}
	
	public boolean getComeback()
	{
		return comeback;
	}
	
	public ZootDirection getDirection()
	{
		return direction;
	}

	@Override
	public void onCollidedFromAbove(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		connectedActors.add(getControllerActor() == actorA ? actorB : actorA);
	}
	
	@Override
	public void onLeave(ZootActor actorA, ZootActor actorB, Contact contact)
	{
		connectedActors.remove(getControllerActor() == actorA ? actorB : actorA);
	}
	
	private void updateConnectedActors()
	{
		for(ZootActor actor : connectedActors)
		{
			PhysicsBodyController bodyCtrl = actor.getController(PhysicsBodyController.class);						
			Vector2 actorVelocity = bodyCtrl.getVelocity();
			
			//horizontal velocity
			if(platformVx >= 0.0f && Math.abs(actorVelocity.x) < platformVx) actorVelocity.x = Math.max(actorVelocity.x, platformVx);
			else if(platformVx < 0.0f && Math.abs(actorVelocity.x) < Math.abs(platformVx)) actorVelocity.x = Math.min(actorVelocity.x, platformVx);		
			
			//vertical velocity
			if(platformVy < 0.0f && (actorVelocity.y <= 0.0f || actorVelocity.y <= Math.abs(platformVy))) actorVelocity.y = platformVy;
			
			bodyCtrl.setVelocity(actorVelocity.x, actorVelocity.y);
		}
	}
}
