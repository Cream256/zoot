package com.zootcat.controllers.logic;

import com.badlogic.gdx.math.Vector2;
import com.zootcat.controllers.ControllerAdapter;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;
import com.zootcat.scene.ZootScene;

public class MovingPlatformController extends ControllerAdapter
{
	@CtrlParam private float range = 0.0f;
	@CtrlParam private float speed = 1.0f;
	@CtrlParam private boolean moving = true;
	@CtrlParam private boolean comeback = true;
	@CtrlParam private ZootDirection direction = ZootDirection.Right;
	@CtrlParam(global = true) private ZootScene scene;
	
	private Vector2 start;
	private Vector2 current;
	private float worldRange;
	private float platformVx;
	private float platformVy;
	
	@Override
	public void init(ZootActor actor) 
	{		
		start = new Vector2(actor.getX(), actor.getY());
		current = start.cpy();
		worldRange = range * scene.getUnitScale();
	}
	
	@Override
	public void onUpdate(float delta, ZootActor actor)
	{
		if(!moving)
		{
			actor.controllersAction(PhysicsBodyController.class, ctrl -> ctrl.setVelocity(0.0f, 0.0f));
			return;
		}
		
		current.x = actor.getX();
		current.y = actor.getY();
		if(current.dst(start) >= worldRange) 
		{
			direction = direction.invert();
			moving = comeback;
			start.set(current);
		}
		
		platformVx = (direction.isHorizontal() ? speed : 0.0f) * direction.getHorizontalValue();
		platformVy = (direction.isVertical() ? speed : 0.0f) * direction.getVerticalValue();		
		actor.controllersAction(PhysicsBodyController.class, ctrl -> ctrl.setVelocity(platformVx, platformVy));
	}
	
	public void setMoving(boolean value)
	{
		moving = value;
	}
	
	public boolean isMoving()
	{
		return moving;
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
}
