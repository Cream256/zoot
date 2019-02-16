package com.zootcat.controllers.logic;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.zootcat.camera.ZootCameraScrollingStrategy;
import com.zootcat.camera.ZootScrollToScrollingStrategy;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.controllers.physics.OnCollideWithSensorController;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.scene.ZootActor;

public class CameraFocusSensor extends OnCollideWithSensorController
{
	@CtrlParam(required = true) private String focusedActorName;
	
	private int collisions = 0;
	private ZootCameraScrollingStrategy previousScrollingStrategy = null;
		
	@Override
	public void onEnterCollision(Fixture fixture)
	{
		if(++collisions > 1) return;
				
		ZootActor focusActor = scene.getFirstActor(act -> act.getName().equalsIgnoreCase(focusedActorName));
		if(focusActor == null) throw new RuntimeZootException("Actor to focus not found: " + focusedActorName);
		
		previousScrollingStrategy = scene.getCamera().getScrollingStrategy();
		scene.getCamera().setScrollingStrategy(new ZootScrollToScrollingStrategy(focusActor, 1.0f));
	}

	@Override
	public void onLeaveCollision(Fixture fixture)
	{
		if(--collisions != 0) return;
				
		scene.getCamera().setScrollingStrategy(previousScrollingStrategy);
		previousScrollingStrategy = null;
	}
}
