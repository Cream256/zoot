package com.zootcat.controllers.physics;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;

public class FlyableControllerTest
{
	private FlyableController flyableCtrl;
	
	@Mock private ZootActor ctrlActor;
	@Mock private PhysicsBodyController physicsBodyCtrl;
		
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		when(ctrlActor.getSingleController(PhysicsBodyController.class)).thenReturn(physicsBodyCtrl);
		
		flyableCtrl = new FlyableController();
	}
	
	@Test
	public void shouldGetPhysicsBodyController()
	{
		flyableCtrl.onAdd(ctrlActor);
		verify(ctrlActor).getSingleController(PhysicsBodyController.class);
	}
	
	@Test
	public void shouldDoNothingWhenRemovingController()
	{
		flyableCtrl.onRemove(ctrlActor);
		verifyZeroInteractions(ctrlActor);
	}
	
	@Test
	public void shouldFlyInGivenDirection()
	{
		//when
		flyableCtrl.onAdd(ctrlActor);
		flyableCtrl.fly(ZootDirection.Left);
		
		//then
		verify(physicsBodyCtrl).setVelocity(-1.0f, 0.0f, true, false);
		
		//when
		flyableCtrl.fly(ZootDirection.Right);
		
		//then
		verify(physicsBodyCtrl).setVelocity(1.0f, 0.0f, true, false);
	}
	
	@Test
	public void shouldStop()
	{
		//when
		flyableCtrl.onAdd(ctrlActor);
		flyableCtrl.stop();
		
		//then
		verify(physicsBodyCtrl).setVelocity(0.0f, 0.0f, true, false);
	}	
}
