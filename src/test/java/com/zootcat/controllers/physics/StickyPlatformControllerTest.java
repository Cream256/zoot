package com.zootcat.controllers.physics;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.eq;
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
	public void shouldUpdateOtherActorHorziontalVelocityIfActorIsNotMoving()
	{
		//given
		when(platformPhysicsBodyCtrl.getVelocity()).thenReturn(EXPECTED_PLATFORM_VELOCITY);
		
		//when		
		stickyPlatformCtrl.onEnter(platformActor, otherActor, mock(Contact.class));
		stickyPlatformCtrl.onUpdate(0.0f, platformActor);
		
		//then
		verify(otherPhysicsBodyCtrl).setVelocity(EXPECTED_PLATFORM_VELOCITY.x, EXPECTED_PLATFORM_VELOCITY.y, true, false);
	}
	
	@Test
	public void shouldMoveActorRightIfActorIsMovingSlowerThanThePlatform()
	{
		//given
		when(platformPhysicsBodyCtrl.getVelocity()).thenReturn(EXPECTED_PLATFORM_VELOCITY);
		when(otherPhysicsBodyCtrl.getVelocity()).thenReturn(new Vector2(EXPECTED_PLATFORM_VELOCITY.x / 2.0f, 0.0f));
		
		//when
		stickyPlatformCtrl.onEnter(platformActor, otherActor, mock(Contact.class));
		stickyPlatformCtrl.onUpdate(0.0f, platformActor);
		
		//then
		verify(otherPhysicsBodyCtrl).setVelocity(EXPECTED_PLATFORM_VELOCITY.x, EXPECTED_PLATFORM_VELOCITY.y, true, false);		
	}
	
	@Test
	public void shouldNotMoveActorRightIfActorIsMovingFasterThanThePlatform()
	{
		//given
		when(platformPhysicsBodyCtrl.getVelocity()).thenReturn(EXPECTED_PLATFORM_VELOCITY);
		when(otherPhysicsBodyCtrl.getVelocity()).thenReturn(new Vector2(EXPECTED_PLATFORM_VELOCITY.x * 2.0f, 0.0f));
		
		//when
		stickyPlatformCtrl.onEnter(platformActor, otherActor, mock(Contact.class));
		stickyPlatformCtrl.onUpdate(0.0f, platformActor);
		
		//then
		verify(otherPhysicsBodyCtrl).setVelocity(EXPECTED_PLATFORM_VELOCITY.x, EXPECTED_PLATFORM_VELOCITY.y, false, false);		
	}
	
	@Test
	public void shouldNotMoveActorRightIfActorIsMovingAndPlatformIsNot()
	{
		//given
		when(platformPhysicsBodyCtrl.getVelocity()).thenReturn(new Vector2(0.0f, 0.0f));
		when(otherPhysicsBodyCtrl.getVelocity()).thenReturn(new Vector2(EXPECTED_PLATFORM_VELOCITY.x * 2.0f, 0.0f));
		
		//when
		stickyPlatformCtrl.onEnter(platformActor, otherActor, mock(Contact.class));
		stickyPlatformCtrl.onUpdate(0.0f, platformActor);
		
		//then
		verify(otherPhysicsBodyCtrl).setVelocity(0.0f, 0.0f, false, false);		
	}
	
	@Test
	public void shouldNotMoveActorRightIfActorIsNotAndPlatformIsNotMoving()
	{
		//given
		when(platformPhysicsBodyCtrl.getVelocity()).thenReturn(new Vector2(0.0f, 0.0f));
		when(otherPhysicsBodyCtrl.getVelocity()).thenReturn(new Vector2(0.0f, 0.0f));
		
		//when
		stickyPlatformCtrl.onEnter(platformActor, otherActor, mock(Contact.class));
		stickyPlatformCtrl.onUpdate(0.0f, platformActor);
		
		//then
		verify(otherPhysicsBodyCtrl).setVelocity(0.0f, 0.0f, false, false);		
	}
		
	@Test
	public void shouldNotMoveActorLeftIfActorIsMovingAndPlatformIsNot()
	{
		//given
		when(platformPhysicsBodyCtrl.getVelocity()).thenReturn(new Vector2(0.0f, 0.0f));
		when(otherPhysicsBodyCtrl.getVelocity()).thenReturn(new Vector2(-EXPECTED_PLATFORM_VELOCITY.x * 2.0f, 0.0f));
		
		//when
		stickyPlatformCtrl.onEnter(platformActor, otherActor, mock(Contact.class));
		stickyPlatformCtrl.onUpdate(0.0f, platformActor);
		
		//then
		verify(otherPhysicsBodyCtrl).setVelocity(0.0f, 0.0f, false, false);		
	}
			
	@Test
	public void shouldNotUpdateOtherActorHorizontalVelocityIfActorIsJumping()
	{
		//given
		when(platformPhysicsBodyCtrl.getVelocity()).thenReturn(EXPECTED_PLATFORM_VELOCITY);
		when(otherPhysicsBodyCtrl.getVelocity()).thenReturn(new Vector2(0.0f, StickyPlatformController.VELOCITY_Y_JUMP_THRESHOLD));
		
		//when
		stickyPlatformCtrl.onEnter(platformActor, otherActor, mock(Contact.class));
		stickyPlatformCtrl.onUpdate(0.0f, platformActor);
		
		//then
		verify(otherPhysicsBodyCtrl).setVelocity(anyFloat(), anyFloat(), eq(false), eq(false));		
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
