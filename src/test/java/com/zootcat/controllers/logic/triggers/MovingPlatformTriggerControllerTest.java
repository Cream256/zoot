package com.zootcat.controllers.logic.triggers;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.controllers.logic.MovingPlatformController;
import com.zootcat.scene.ZootActor;
import com.zootcat.testing.ZootSceneMock;

public class MovingPlatformTriggerControllerTest
{
	private ZootSceneMock scene;
	private ZootActor platformActor;
	private MovingPlatformTriggerController ctrl;
	
	@Mock private ZootActor ctrlActor;
	@Mock private MovingPlatformController movingPlatformCtrl;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		
		platformActor = new ZootActor();
		platformActor.addController(movingPlatformCtrl);
		platformActor.setName("platform");
		
		scene = new ZootSceneMock();
		scene.addActor(platformActor);
		
		ctrl = new MovingPlatformTriggerController();
		ControllerAnnotations.setControllerParameter(ctrl, "scene", scene);
		ControllerAnnotations.setControllerParameter(ctrl, "platformActorName", "platform");
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
	
	@Test
	public void shouldNotEnableWhenActorWithValidNameIsNotFound()
	{
		//given
		ZootActor bunny = new ZootActor();
		bunny.setName("bunny");
		
		//when
		scene.removeActor(platformActor);
		scene.addActor(bunny);
		ctrl.triggerOn(ctrlActor);
		
		//then
		verify(movingPlatformCtrl, times(0)).setEnabled(true);		
	}
}
