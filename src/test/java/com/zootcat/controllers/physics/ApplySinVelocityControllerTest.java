package com.zootcat.controllers.physics;

import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.math.MathUtils;
import com.zootcat.scene.ZootActor;

public class ApplySinVelocityControllerTest
{
	private ZootActor actor;
	private ApplySinVelocityController ctrl;
	@Mock private PhysicsBodyController physicsBodyCtrl;
			
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		
		actor = new ZootActor();
		actor.addController(physicsBodyCtrl);
		
		ctrl = new ApplySinVelocityController();
	}

	@Test
	public void shouldApplyDefaultValues()
	{
		ctrl.onUpdate(MathUtils.PI / 2.0f, actor);
		verify(physicsBodyCtrl).setVelocity(1.0f, 1.0f, true, true);	
	}
	
	@Test
	public void shouldSetTimeMultiplier()
	{
		ctrl.setMul(2.0f);
		ctrl.onUpdate(MathUtils.PI, actor);
		verify(physicsBodyCtrl).setVelocity(0.0f, 0.0f, true, true);
	}
	
	@Test
	public void shouldSetImpulses()
	{
		ctrl.setVelocityX(2.0f);
		ctrl.setVelocityY(-2.0f);
		ctrl.onUpdate(MathUtils.PI / 2.0f, actor);
		verify(physicsBodyCtrl).setVelocity(2.0f, -2.0f, true, true);
	}
	
	@Test
	public void shouldUseXVelocityOnly()
	{
		ctrl.setUseX(true);
		ctrl.setUseY(false);
		ctrl.onUpdate(MathUtils.PI / 2.0f, actor);		
		verify(physicsBodyCtrl).setVelocity(1.0f, 1.0f, true, false);	
	}
	
	@Test
	public void shouldUseYVelocityOnly()
	{
		ctrl.setUseX(false);
		ctrl.setUseY(true);
		ctrl.onUpdate(MathUtils.PI / 2.0f, actor);		
		verify(physicsBodyCtrl).setVelocity(1.0f, 1.0f, false, true);	
	}
}
