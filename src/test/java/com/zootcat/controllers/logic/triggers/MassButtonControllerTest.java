package com.zootcat.controllers.logic.triggers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.physics.box2d.Contact;
import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.scene.ZootActor;

public class MassButtonControllerTest 
{
	private static final float REQUIRED_MASS = 1.25f;
	
	@Mock private Contact contact;
	@Mock private ZootActor otherActor;
	@Mock private ZootActor controllerActor;
	@Mock private PhysicsBodyController physicsBodyCtrl;
	private MassButtonController massBtnCtrl;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		
		massBtnCtrl = new MassButtonController();
		ControllerAnnotations.setControllerParameter(massBtnCtrl, "requiredMass", REQUIRED_MASS);
		massBtnCtrl.init(controllerActor);
		
		when(otherActor.tryGetController(PhysicsBodyController.class)).thenReturn(physicsBodyCtrl);
	}
	
	@Test
	public void shouldPressButtonWhenActorMassIsEqualToRequired()
	{
		throw new RuntimeException("Not implmeneted");
	}
	
	@Test
	public void shouldNotPressButtonWhenActorHasNoPhysicsBodyController()
	{
		throw new RuntimeException("Not implmeneted");
	}
		
	@Test
	public void shouldPressButtonWhenActorMassIsGreaterThanRequired()
	{
		throw new RuntimeException("Not implmeneted");
	}
	
	@Test
	public void shouldNotPressButtonWhenActorMassIsLessThanRequired()
	{
		throw new RuntimeException("Not implmeneted");
	}
	
	@Test
	public void shouldPressButtonWhenSeveralBodiesHaveRequiredMass()
	{
		throw new RuntimeException("Not implmeneted");
	}
	
	
	
}
