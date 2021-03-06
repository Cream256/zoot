package com.zootcat.controllers.logic;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.zootcat.controllers.physics.DetectFallController;
import com.zootcat.scene.ZootActor;
import com.zootcat.testing.ZootActorStub;

public class HurtOnJumpControllerTest
{	
	@Mock private Fixture otherFixture;
	@Mock private DetectFallController detectFall;
	private ZootActor actor;
	private HurtOnJumpController ctrl;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);				
		actor = new ZootActorStub();
		actor.addController(detectFall);
		when(otherFixture.getUserData()).thenReturn(actor);
		
		ctrl = new HurtOnJumpController();		
	}
	
	@Test
	public void shouldAllowToHurtWhenActorIsJumping()
	{
		when(detectFall.isFalling()).thenReturn(true);
		assertTrue(ctrl.canHurt(otherFixture));
	}
	
	@Test
	public void shouldNotAllowToHurtWhenActorIsNotJumping()
	{
		when(detectFall.isFalling()).thenReturn(false);
		assertFalse(ctrl.canHurt(otherFixture));
	}
}
