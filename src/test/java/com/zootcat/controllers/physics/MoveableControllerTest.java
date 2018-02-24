package com.zootcat.controllers.physics;

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

public class MoveableControllerTest
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
	@Mock private DetectGroundController groundCtrl;
	private MoveableController ctrl;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		
		actor = new ZootActor();
		actor.addController(physicsCtrl);
		actor.addController(groundCtrl);
		
		when(groundCtrl.isOnGround()).thenReturn(true);
		
		ctrl = new MoveableController();
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
}
