package com.zootcat.controllers.physics;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.controllers.physics.StickyPlatformController;
import com.zootcat.exceptions.ZootControllerNotFoundException;
import com.zootcat.testing.ZootActorStub;

public class StickyPlatformControllerTest
{
	private static final Vector2 EXPECTED_PLATFORM_VELOCITY = new Vector2(50.0f, 25.0f);
	
	private ZootActorStub otherActor;
	private ZootActorStub platformActor;	
	private StickyPlatformController stickyPlatformCtrl;
	@Mock private PhysicsBodyController otherPhysicsBodyCtrl;
	@Mock private PhysicsBodyController platformPhysicsBodyCtrl;
		
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		when(platformPhysicsBodyCtrl.getVelocity()).thenReturn(EXPECTED_PLATFORM_VELOCITY);
		when(otherPhysicsBodyCtrl.getVelocity()).thenReturn(new Vector2(0.0f, 0.0f));
		
		otherActor = new ZootActorStub();
		otherActor.addController(otherPhysicsBodyCtrl);
		
		platformActor = new ZootActorStub();		
		platformActor.addController(platformPhysicsBodyCtrl);
		
		stickyPlatformCtrl = new StickyPlatformController();
		stickyPlatformCtrl.init(platformActor);
		platformActor.addController(stickyPlatformCtrl);
	}
	
	@Test(expected = ZootControllerNotFoundException.class)
	public void shouldThrowIfPlatformHasNoPhysicsBodyController()
	{
		//when
		platformActor.removeController(platformPhysicsBodyCtrl);
		
		//then throw
		stickyPlatformCtrl.onUpdate(0.0f, platformActor);		
	}
	
	@Test
	public void shouldNotThrowIfOtherActorHasNoPhysicsController()
	{
		//given
		otherActor.removeController(otherPhysicsBodyCtrl);
		
		//when
		stickyPlatformCtrl.onEnter(platformActor, otherActor, mock(Contact.class));
		stickyPlatformCtrl.onUpdate(0.0f, platformActor);
		
		//then ok		
	}
	
	@Test
	public void shouldUpdateOtherActorVelocity()
	{
		//when
		stickyPlatformCtrl.onEnter(platformActor, otherActor, mock(Contact.class));
		stickyPlatformCtrl.onUpdate(0.0f, platformActor);
		
		//then
		verify(otherPhysicsBodyCtrl).setVelocity(EXPECTED_PLATFORM_VELOCITY.x, 0.0f, true, false);
	}
	
	@Test
	public void shouldNotUpdateOtherActorVelocityIfActorIsJumping()
	{
		//given
		when(otherPhysicsBodyCtrl.getVelocity()).thenReturn(new Vector2(0.0f, StickyPlatformController.VELOCITY_Y_JUMP_THRESHOLD));
		
		//when
		stickyPlatformCtrl.onEnter(platformActor, otherActor, mock(Contact.class));
		stickyPlatformCtrl.onUpdate(0.0f, platformActor);
		
		//then
		verify(otherPhysicsBodyCtrl, never()).setVelocity(anyFloat(), anyFloat(), anyBoolean(), anyBoolean());		
	}
	
	@Test
	public void shouldNotUpdateOtherActorVelocityIfOtherActorLeftThePlatform()
	{
		//when
		stickyPlatformCtrl.onEnter(platformActor, otherActor, mock(Contact.class));
		stickyPlatformCtrl.onLeave(platformActor, otherActor, mock(Contact.class));
		stickyPlatformCtrl.onUpdate(0.0f, platformActor);
		
		//then
		verify(otherPhysicsBodyCtrl, never()).setVelocity(anyFloat(), anyFloat(), anyBoolean(), anyBoolean());
	}	
}
