package com.zootcat.actions;

import com.badlogic.gdx.math.Vector2;
import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.math.ParaboleMovementPattern;

public class ZootParabolicMovementAction extends ZootAction
{
	private static final Vector2 DEFAULT_PEAK = new Vector2(5.0f, 5.0f);
	private static final Vector2 DEFAULT_POINT = new Vector2(0.0f, 1.0f);
	
	private float time;
	private ParaboleMovementPattern parabole;
			
	public ZootParabolicMovementAction()
	{
		time = 0.0f;
		parabole = new ParaboleMovementPattern(DEFAULT_PEAK, DEFAULT_POINT);
	}
	
	public void setParaboleParams(Vector2 peakPoint, Vector2 pointOnParabole)
	{
		parabole = new ParaboleMovementPattern(peakPoint, pointOnParabole);
	}
	
	@Override
	public void reset()
	{
		super.reset();
		parabole = null;
		time = 0.0f;		
	}
	
	public ParaboleMovementPattern getParabole()
	{
		return parabole;
	}
	
	public float getTime()
	{
		return time;
	}
	
	@Override
	public boolean act(float delta)
	{
		time += delta;
		getTargetZootActor().controllerAction(PhysicsBodyController.class, ctrl -> 
		{
			Vector2 pos = ctrl.getCenterPositionRef();
			Vector2 mov = parabole.at(time);
			ctrl.setPosition(pos.x + mov.x, pos.y + mov.y);
		});		
		return true;
	}
}
