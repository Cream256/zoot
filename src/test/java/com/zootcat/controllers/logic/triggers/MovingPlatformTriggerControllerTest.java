package com.zootcat.controllers.logic.triggers;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.controllers.logic.MovingPlatformController;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;

public class MovingPlatformTriggerControllerTest
{
	private ZootActor platformActor;
	private MovingPlatformTriggerController ctrl;
	
	@Mock private ZootActor ctrlActor;
	@Mock private ZootScene scene;
	@Mock private MovingPlatformController movingPlatformCtrl;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		
		platformActor = new ZootActor();
		platformActor.addController(movingPlatformCtrl);
		when(scene.getActors(any())).thenReturn(Arrays.asList(platformActor));
		
		ctrl = new MovingPlatformTriggerController();
		ControllerAnnotations.setControllerParameter(ctrl, "scene", scene);
		ctrl.init(ctrlActor);
	}
	
	@Test
	public void shouldEnableMovingPlatformsWhenTriggeredOn()
	{
		ctrl.triggerOn(ctrlActor);
		verify(movingPlatformCtrl).setEnabled(true);
	}
	
	@Test
	public void shouldDisableMovingPlatformsWhenTriggeredOff()
	{
		ctrl.triggerOff(ctrlActor);
		verify(movingPlatformCtrl).setEnabled(false);
	}
}
