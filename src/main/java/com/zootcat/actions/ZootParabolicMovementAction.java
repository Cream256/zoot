package com.zootcat.actions;

import com.badlogic.gdx.math.Vector2;
import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.math.ParaboleMovementPattern;

public class ZootParabolicMovementAction extends ZootAction
{
	private float time;
	private ParaboleMovementPattern parabole;
			
	//TODO add test
	//TODO add to zoot actions
	//TODO add tests for parabole movement pattern
	public ZootParabolicMovementAction()
	{
		time = 0.0f;
		parabole = new ParaboleMovementPattern(new Vector2(5.0f, 5.0f), new Vector2(0.0f, 0.0f));
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
