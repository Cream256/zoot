package com.zootcat.controllers.physics;

import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.math.MathUtils;
import com.zootcat.controllers.recognizer.MockControllerRecognizer;
import com.zootcat.scene.ZootActor;

public class ApplySinImpulseControllerTest
{
	private ZootActor actor;
	private ApplySinImpulseController ctrl;
	@Mock private PhysicsBodyController physicsBodyCtrl;
			
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		
		actor = new ZootActor();
		actor.setControllerRecognizer(MockControllerRecognizer.Instance);
		actor.addController(physicsBodyCtrl);
				
		ctrl = new ApplySinImpulseController();
	}

	@Test
	public void shouldApplyDefaultValues()
	{
		ctrl.onUpdate(MathUtils.PI / 2.0f, actor);
		verify(physicsBodyCtrl).applyImpulse(1.0f, 1.0f);	
	}
	
	@Test
	public void shouldSetTimeMultiplier()
	{
		ctrl.setMul(2.0f);
		ctrl.onUpdate(MathUtils.PI, actor);
		verify(physicsBodyCtrl).applyImpulse(0.0f, 0.0f);
	}
	
	@Test
	public void shouldSetImpulses()
	{
		ctrl.setImpulseX(2.0f);
		ctrl.setImpulseY(-2.0f);
		ctrl.onUpdate(MathUtils.PI / 2.0f, actor);
		verify(physicsBodyCtrl).applyImpulse(2.0f, -2.0f);
	}
}
