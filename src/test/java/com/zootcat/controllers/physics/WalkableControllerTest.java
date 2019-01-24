package com.zootcat.controllers.physics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.math.Vector2;
import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;
import com.zootcat.testing.ZootActorStub;

public class WalkableControllerTest
{
	private static final float WALK_VEL = 5.0f;
	private static final float RUN_VEL = 10.0f;
	private static final float JUMP_UP_VEL = 25.0f;
	private static final float JUMP_FORWARD_VEL_X = 50.0f;
	private static final float JUMP_FORWARD_VEL_Y = 30.0f;
	private static final float IN_AIR_VEL_X = 12.0f;		
	private static final int JUMP_TIMEOUT = 100;
		
	private ZootActor actor;
	@Mock private PhysicsBodyController physicsCtrl;
	@Mock private DetectGroundSensorController groundCtrl;
	private WalkableController ctrl;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		
		actor = new ZootActorStub();
		actor.addController(physicsCtrl);
		actor.addController(groundCtrl);
		
		when(groundCtrl.isOnGround()).thenReturn(true);
		
		ctrl = new WalkableController();
		ControllerAnnotations.setControllerParameter(ctrl, "walkVel", WALK_VEL);
		ControllerAnnotations.setControllerParameter(ctrl, "runVel", RUN_VEL);
		ControllerAnnotations.setControllerParameter(ctrl, "jumpUpVel", JUMP_UP_VEL);
		ControllerAnnotations.setControllerParameter(ctrl, "jumpForwardVelX", JUMP_FORWARD_VEL_X);
		ControllerAnnotations.setControllerParameter(ctrl, "jumpForwardVelY", JUMP_FORWARD_VEL_Y);
		ControllerAnnotations.setControllerParameter(ctrl, "jumpTimeout", JUMP_TIMEOUT);
		ControllerAnnotations.setControllerParameter(ctrl, "inAirVelX", IN_AIR_VEL_X);
	}
	
	@Test(expected = RuntimeZootException.class)
	public void shouldThrowIfNoPhysicsControllerIsAssignedToActor()
	{
		//when
		actor.removeController(physicsCtrl);
		ctrl.onAdd(actor);		
		//then should throw		
	}
	
	@Test(expected = RuntimeZootException.class)
	public void shouldThrowIfNoGroundControllerIsAssignedToActor()
	{
		//when
		actor.removeController(groundCtrl);
		ctrl.onAdd(actor);		
		//then should throw		
	}
	
	@Test
	public void shouldNotInteractWithActorOnRemove()
	{
		//given
		ZootActor actor = mock(ZootActor.class);
		
		//when
		ctrl.onRemove(actor);
		
		//then
		verifyZeroInteractions(actor);
	}
	
	@Test
	public void shouldWalkRight()
	{
		//when
		ctrl.onAdd(actor);
		ctrl.walk(ZootDirection.Right);
		
		//then
		verify(physicsCtrl).setVelocity(WALK_VEL, 0.0f, true, false);
	}
	
	@Test
	public void shouldWalkLeft()
	{
		//when
		ctrl.onAdd(actor);
		ctrl.walk(ZootDirection.Left);
		
		//then
		verify(physicsCtrl).setVelocity(-WALK_VEL, 0.0f, true, false);		
	}
	
	@Test
	public void shouldRunRight()
	{
		//when
		ctrl.onAdd(actor);
		ctrl.run(ZootDirection.Right);
		
		//then
		verify(physicsCtrl).setVelocity(RUN_VEL, 0.0f, true, false);
	}
	
	@Test
	public void shouldRunLeft()
	{
		//when
		ctrl.onAdd(actor);
		ctrl.run(ZootDirection.Left);
		
		//then
		verify(physicsCtrl).setVelocity(-RUN_VEL, 0.0f, true, false);
	}
	
	
	@Test
	public void shouldNotRunWhenCanRunIsSetToFalse()
	{
		//when
		ctrl.onAdd(actor);
		ctrl.setCanRun(false);
		ctrl.run(ZootDirection.Left);
		ctrl.run(ZootDirection.Right);
		
		//then
		verify(physicsCtrl, times(0)).setVelocity(-RUN_VEL, 0.0f, true, false);
		verify(physicsCtrl, times(0)).setVelocity(RUN_VEL, 0.0f, true, false);
	}
	
	@Test
	public void shouldJumpUp()
	{
		//when
		ctrl.onAdd(actor);
		ctrl.jumpUp();
		
		//then
		verify(physicsCtrl).setVelocity(0.0f, JUMP_UP_VEL, false, true);
	}
	
	@Test
	public void shouldNotJumpUpWhenNotTouchingGround()
	{
		//when
		when(groundCtrl.isOnGround()).thenReturn(false);
		ctrl.onAdd(actor);
		ctrl.jumpUp();
		
		//then
		verify(physicsCtrl, never()).setVelocity(0.0f, JUMP_UP_VEL, false, true);		
	}
	
	@Test
	public void shouldJumpUpEvenWhenNotTouchingGround()
	{
		//when
		when(groundCtrl.isOnGround()).thenReturn(false);
		ctrl.onAdd(actor);
		ctrl.jumpUp(false);
		
		//then
		verify(physicsCtrl).setVelocity(0.0f, JUMP_UP_VEL, false, true);		
	}
	
	@Test
	public void shouldNotJumpUpWhenJumpTimeoutIsExceeded()
	{
		//when
		ctrl.onAdd(actor);
		ctrl.jumpUp();
		
		//then
		verify(physicsCtrl).setVelocity(0.0f, JUMP_UP_VEL, false, true);
		
		//when half timeout is reached
		ctrl.onUpdate(JUMP_TIMEOUT / 2000.0f, actor);
		ctrl.jumpUp();
		
		//then can't jump
		verify(physicsCtrl, times(1)).setVelocity(0.0f, JUMP_UP_VEL, false, true);
		
		//when timeout is reached
		ctrl.onUpdate(JUMP_TIMEOUT / 2000.0f, actor);
		ctrl.jumpUp();	
		
		//then can jump
		verify(physicsCtrl, times(2)).setVelocity(0.0f, JUMP_UP_VEL, false, true);
	}
	
	@Test
	public void shouldJumpForwardToTheRight()
	{
		//when
		ctrl.onAdd(actor);
		ctrl.jumpForward(ZootDirection.Right);
		
		//then
		verify(physicsCtrl).setVelocity(JUMP_FORWARD_VEL_X, JUMP_FORWARD_VEL_Y, true, true);
	}
	
	@Test
	public void shouldJumpForwardToTheLeft()
	{
		//when
		ctrl.onAdd(actor);
		ctrl.jumpForward(ZootDirection.Left);
		
		//then
		verify(physicsCtrl).setVelocity(-JUMP_FORWARD_VEL_X, JUMP_FORWARD_VEL_Y, true, true);
	}
	
	@Test
	public void shouldJumpForwardWithNewVelocitySet()	
	{
		//when
		ctrl.onAdd(actor);
		ctrl.setForwardJumpVelocity(2.56f, 5.12f);
		ctrl.jumpForward(ZootDirection.Right);
				
		//then
		verify(physicsCtrl).setVelocity(2.56f, 5.12f, true, true);
	}
	
	@Test
	public void shouldNotJumpAtAllWhenCanJumpIsSetToFalse()
	{
		//when
		ctrl.onAdd(actor);
		ctrl.setCanJump(false);
		ctrl.jumpForward(ZootDirection.Right);
		ctrl.jumpForward(ZootDirection.Left);
		ctrl.jumpForward(ZootDirection.Up);
		
		//then
		verify(physicsCtrl, times(0)).setVelocity(JUMP_FORWARD_VEL_X, JUMP_FORWARD_VEL_Y, true, true);
	}
	
	@Test
	public void shouldNotJumpForwardWhenNotTouchingGround()
	{
		//when
		when(groundCtrl.isOnGround()).thenReturn(false);
		ctrl.onAdd(actor);
		ctrl.jumpForward(ZootDirection.Right);
		
		//then
		verify(physicsCtrl, never()).setVelocity(JUMP_FORWARD_VEL_X, JUMP_UP_VEL, true, true);
	}
	
	@Test
	public void shouldJumpForwardEvenWhenNotTouchingGround()
	{
		//when
		when(groundCtrl.isOnGround()).thenReturn(false);
		ctrl.onAdd(actor);
		ctrl.jumpForward(ZootDirection.Right, false);
		
		//then
		verify(physicsCtrl).setVelocity(JUMP_FORWARD_VEL_X, JUMP_FORWARD_VEL_Y, true, true);
	}
	
	@Test
	public void shouldNotJumpForwardWhenJumpTimeoutIsExceeded()
	{
		//when
		ctrl.onAdd(actor);
		ctrl.jumpForward(ZootDirection.Right);
		
		//then
		verify(physicsCtrl).setVelocity(JUMP_FORWARD_VEL_X, JUMP_FORWARD_VEL_Y, true, true);
		
		//when half timeout is reached
		ctrl.onUpdate(JUMP_TIMEOUT / 2000.0f, actor);
		ctrl.jumpForward(ZootDirection.Right);
		
		//then can't jump
		verify(physicsCtrl, times(1)).setVelocity(JUMP_FORWARD_VEL_X, JUMP_FORWARD_VEL_Y, true, true);
		
		//when timeout is reached
		ctrl.onUpdate(JUMP_TIMEOUT / 2000.0f, actor);
		ctrl.jumpForward(ZootDirection.Right);	
		
		//then can jump
		verify(physicsCtrl, times(2)).setVelocity(JUMP_FORWARD_VEL_X, JUMP_FORWARD_VEL_Y, true, true);
	}
	
	@Test
	public void shouldMoveInAirToTheRight()
	{
		//when
		when(physicsCtrl.getVelocity()).thenReturn(new Vector2(0.0f, 0.0f));
		ctrl.onAdd(actor);
		ctrl.onUpdate(1.0f, actor);
		ctrl.moveInAir(ZootDirection.Right);
		
		//then
		verify(physicsCtrl).setVelocity(IN_AIR_VEL_X, 0.0f, true, false);
	}
	
	@Test
	public void shouldMoveInAirToTheLeft()
	{
		//when
		when(physicsCtrl.getVelocity()).thenReturn(new Vector2(0.0f, 0.0f));
		ctrl.onAdd(actor);
		ctrl.onUpdate(1.0f, actor);
		ctrl.moveInAir(ZootDirection.Left);
		
		//then
		verify(physicsCtrl).setVelocity(-IN_AIR_VEL_X, 0.0f, true, false);
	}
	
	@Test
	public void shouldNotClipToNormalInAirVelocityIfAlreadyMovingFasterToTheRight()
	{
		//when
		when(physicsCtrl.getVelocity()).thenReturn(new Vector2(JUMP_FORWARD_VEL_X, 0.0f));
		ctrl.onAdd(actor);
		ctrl.onUpdate(1.0f, actor);
		ctrl.moveInAir(ZootDirection.Right);
		
		//then
		verify(physicsCtrl).setVelocity(JUMP_FORWARD_VEL_X, 0.0f, true, false);
	}
	
	@Test
	public void shouldSetAndReturnRunVelocity()
	{
		assertEquals(RUN_VEL, ctrl.getRunVelocity(), 0.0f);
		
		ctrl.setRunVelocity(1.23f);
		assertEquals(1.23f, ctrl.getRunVelocity(), 0.0f);
	}
	
	@Test
	public void shouldStopHorizontalMovement()
	{
		//when
		ctrl.onAdd(actor);
		ctrl.stop();
		
		//then
		verify(physicsCtrl).setVelocity(0.0f, 0.0f, true, false);
	}	
	
	@Test
	public void shouldReturnJumpTimeout()
	{
		assertEquals(JUMP_TIMEOUT, ctrl.getJumpTimeout());
	}
	
	@Test
	public void shouldSetJumpTimeout()
	{
		ctrl.setJumpTimeout(256);
		assertEquals(256, ctrl.getJumpTimeout());
	}
	
	@Test
	public void shouldReturnJumpUpVelocity()
	{
		assertEquals(JUMP_UP_VEL, ctrl.getJumpUpVelocity(), 0.0f);
	}
	
	@Test
	public void shouldSetJumpUpVelocity()
	{
		ctrl.setJumpUpVelocity(0.5f);
		assertEquals(0.5f, ctrl.getJumpUpVelocity(), 0.0f);		
	}
	
	@Test
	public void shouldGetDefaultCanJumpValue()
	{
		assertTrue(ctrl.canJump());
	}
	
	@Test
	public void shouldSetCanJump()
	{
		ctrl.setCanJump(false);
		assertFalse(ctrl.canJump());
		
		ctrl.setCanJump(true);
		assertTrue(ctrl.canJump());
	}
	
	@Test
	public void shouldGetDefaultCanRunValue()
	{
		assertTrue(ctrl.canRun());
	}
	
	@Test
	public void shouldSetCanRun()
	{
		ctrl.setCanRun(false);
		assertFalse(ctrl.canRun());
		
		ctrl.setCanRun(true);
		assertTrue(ctrl.canRun());		
	}
	
	@Test
	public void shouldBeSingleton()
	{
		assertTrue(ctrl.isSingleton());
	}
}
