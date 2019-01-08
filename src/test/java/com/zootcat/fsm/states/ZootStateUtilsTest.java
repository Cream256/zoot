package com.zootcat.fsm.states;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.zootcat.controllers.logic.LifeController;
import com.zootcat.controllers.physics.WalkableController;
import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;
import com.zootcat.testing.ZootActorStub;

public class ZootStateUtilsTest
{
	@Test
	public void shouldRecognizeJumpEvent()
	{
		assertTrue(ZootStateUtils.isJumpEvent(new ZootEvent(ZootEventType.JumpUp)));
		assertTrue(ZootStateUtils.isJumpEvent(new ZootEvent(ZootEventType.JumpForward)));
		assertFalse(ZootStateUtils.isJumpEvent(new ZootEvent(ZootEventType.WalkLeft)));
		assertFalse(ZootStateUtils.isJumpEvent(new ZootEvent(ZootEventType.WalkRight)));
		assertFalse(ZootStateUtils.isJumpEvent(new ZootEvent(ZootEventType.RunLeft)));
		assertFalse(ZootStateUtils.isJumpEvent(new ZootEvent(ZootEventType.RunRight)));
		assertFalse(ZootStateUtils.isJumpEvent(new ZootEvent(ZootEventType.Attack)));
		assertFalse(ZootStateUtils.isJumpEvent(new ZootEvent(ZootEventType.Collide)));
		assertFalse(ZootStateUtils.isJumpEvent(new ZootEvent(ZootEventType.Dead)));
		assertFalse(ZootStateUtils.isJumpEvent(new ZootEvent(ZootEventType.Fall)));
		assertFalse(ZootStateUtils.isJumpEvent(new ZootEvent(ZootEventType.Ground)));
		assertFalse(ZootStateUtils.isJumpEvent(new ZootEvent(ZootEventType.None)));
		assertFalse(ZootStateUtils.isJumpEvent(new ZootEvent(ZootEventType.Stop)));
		assertFalse(ZootStateUtils.isJumpEvent(new ZootEvent(ZootEventType.Update)));
	}
	
	@Test
	public void shouldRecognizeMoveEvent()
	{
		assertTrue(ZootStateUtils.isMoveEvent(new ZootEvent(ZootEventType.WalkLeft)));
		assertTrue(ZootStateUtils.isMoveEvent(new ZootEvent(ZootEventType.WalkRight)));
		assertTrue(ZootStateUtils.isMoveEvent(new ZootEvent(ZootEventType.RunLeft)));
		assertTrue(ZootStateUtils.isMoveEvent(new ZootEvent(ZootEventType.RunRight)));
		assertFalse(ZootStateUtils.isMoveEvent(new ZootEvent(ZootEventType.Attack)));
		assertFalse(ZootStateUtils.isMoveEvent(new ZootEvent(ZootEventType.Collide)));
		assertFalse(ZootStateUtils.isMoveEvent(new ZootEvent(ZootEventType.Dead)));
		assertFalse(ZootStateUtils.isMoveEvent(new ZootEvent(ZootEventType.Fall)));
		assertFalse(ZootStateUtils.isMoveEvent(new ZootEvent(ZootEventType.JumpUp)));
		assertFalse(ZootStateUtils.isMoveEvent(new ZootEvent(ZootEventType.Ground)));
		assertFalse(ZootStateUtils.isMoveEvent(new ZootEvent(ZootEventType.None)));
		assertFalse(ZootStateUtils.isMoveEvent(new ZootEvent(ZootEventType.Stop)));
		assertFalse(ZootStateUtils.isMoveEvent(new ZootEvent(ZootEventType.Update)));
	}
	
	@Test
	public void shouldReturnProperDirectionFromEvent()
	{
		assertEquals(ZootDirection.Left, ZootStateUtils.getDirectionFromEvent(new ZootEvent(ZootEventType.WalkLeft)));
		assertEquals(ZootDirection.Left, ZootStateUtils.getDirectionFromEvent(new ZootEvent(ZootEventType.RunLeft)));
		assertEquals(ZootDirection.Left, ZootStateUtils.getDirectionFromEvent(new ZootEvent(ZootEventType.FlyLeft)));		
		assertEquals(ZootDirection.Right, ZootStateUtils.getDirectionFromEvent(new ZootEvent(ZootEventType.WalkRight)));
		assertEquals(ZootDirection.Right, ZootStateUtils.getDirectionFromEvent(new ZootEvent(ZootEventType.RunRight)));
		assertEquals(ZootDirection.Right, ZootStateUtils.getDirectionFromEvent(new ZootEvent(ZootEventType.FlyRight)));
		assertEquals(ZootDirection.None, ZootStateUtils.getDirectionFromEvent(new ZootEvent(ZootEventType.Attack)));
		assertEquals(ZootDirection.None, ZootStateUtils.getDirectionFromEvent(new ZootEvent(ZootEventType.Collide)));
		assertEquals(ZootDirection.None, ZootStateUtils.getDirectionFromEvent(new ZootEvent(ZootEventType.Dead)));
		assertEquals(ZootDirection.None, ZootStateUtils.getDirectionFromEvent(new ZootEvent(ZootEventType.Fall)));
		assertEquals(ZootDirection.None, ZootStateUtils.getDirectionFromEvent(new ZootEvent(ZootEventType.JumpUp)));
		assertEquals(ZootDirection.None, ZootStateUtils.getDirectionFromEvent(new ZootEvent(ZootEventType.Ground)));
		assertEquals(ZootDirection.None, ZootStateUtils.getDirectionFromEvent(new ZootEvent(ZootEventType.None)));
		assertEquals(ZootDirection.None, ZootStateUtils.getDirectionFromEvent(new ZootEvent(ZootEventType.Stop)));
		assertEquals(ZootDirection.None, ZootStateUtils.getDirectionFromEvent(new ZootEvent(ZootEventType.Update)));	
	}
	
	@Test
	public void shouldRecognizeRunEvent()
	{
		assertTrue(ZootStateUtils.isRunEvent(new ZootEvent(ZootEventType.RunLeft)));
		assertTrue(ZootStateUtils.isRunEvent(new ZootEvent(ZootEventType.RunRight)));
		assertFalse(ZootStateUtils.isRunEvent(new ZootEvent(ZootEventType.WalkLeft)));
		assertFalse(ZootStateUtils.isRunEvent(new ZootEvent(ZootEventType.WalkRight)));
		assertFalse(ZootStateUtils.isRunEvent(new ZootEvent(ZootEventType.Attack)));
		assertFalse(ZootStateUtils.isRunEvent(new ZootEvent(ZootEventType.Collide)));
		assertFalse(ZootStateUtils.isRunEvent(new ZootEvent(ZootEventType.Stop)));
		assertFalse(ZootStateUtils.isRunEvent(new ZootEvent(ZootEventType.Update)));
	}
	
	@Test
	public void shouldRecognizeWalkEvent()
	{
		assertTrue(ZootStateUtils.isWalkEvent(new ZootEvent(ZootEventType.WalkLeft)));
		assertTrue(ZootStateUtils.isWalkEvent(new ZootEvent(ZootEventType.WalkRight)));
		assertFalse(ZootStateUtils.isWalkEvent(new ZootEvent(ZootEventType.RunLeft)));
		assertFalse(ZootStateUtils.isWalkEvent(new ZootEvent(ZootEventType.RunRight)));
		assertFalse(ZootStateUtils.isWalkEvent(new ZootEvent(ZootEventType.Attack)));
		assertFalse(ZootStateUtils.isWalkEvent(new ZootEvent(ZootEventType.Collide)));
		assertFalse(ZootStateUtils.isWalkEvent(new ZootEvent(ZootEventType.Stop)));
		assertFalse(ZootStateUtils.isWalkEvent(new ZootEvent(ZootEventType.Update)));	
	}
	
	@Test
	public void shouldRecognizeFlyEvent()
	{
		assertTrue(ZootStateUtils.isFlyEvent(new ZootEvent(ZootEventType.FlyLeft)));
		assertTrue(ZootStateUtils.isFlyEvent(new ZootEvent(ZootEventType.FlyRight)));
		assertFalse(ZootStateUtils.isFlyEvent(new ZootEvent(ZootEventType.RunLeft)));
		assertFalse(ZootStateUtils.isFlyEvent(new ZootEvent(ZootEventType.RunRight)));
		assertFalse(ZootStateUtils.isFlyEvent(new ZootEvent(ZootEventType.Attack)));
		assertFalse(ZootStateUtils.isFlyEvent(new ZootEvent(ZootEventType.Collide)));
		assertFalse(ZootStateUtils.isFlyEvent(new ZootEvent(ZootEventType.Stop)));
		assertFalse(ZootStateUtils.isFlyEvent(new ZootEvent(ZootEventType.Update)));		
	}
	
	@Test
	public void shouldReturnCanRunValueFromController()
	{
		//given
		WalkableController moveableCtrl = mock(WalkableController.class);
		ZootActor actor = new ZootActorStub();
		actor.addController(moveableCtrl);
		
		ZootEvent event = new ZootEvent();
		event.setTarget(actor);
		
		//when
		when(moveableCtrl.canRun()).thenReturn(true);
		
		//then
		assertTrue(ZootStateUtils.canActorRun(event));
		
		//when
		when(moveableCtrl.canRun()).thenReturn(false);
	
		//then
		assertFalse(ZootStateUtils.canActorRun(event));		
	}
	
	@Test
	public void shouldReturnTrueForCanRunWhenNoMovementControllerIsPresent()
	{
		ZootActor actor = new ZootActor();
		ZootEvent event = new ZootEvent();
		event.setTarget(actor);
		
		assertTrue(ZootStateUtils.canActorRun(event));
	}
	
	@Test
	public void shouldReturnTrueForCanRunWhenEventHasNoActor()
	{
		assertTrue(ZootStateUtils.canActorRun(new ZootEvent()));
	}
	
	@Test
	public void shouldReturnCanJumpValueFromController()
	{
		//given
		WalkableController moveableCtrl = mock(WalkableController.class);
		ZootActor actor = new ZootActorStub();
		actor.addController(moveableCtrl);
		
		ZootEvent event = new ZootEvent();
		event.setTarget(actor);
		
		//when
		when(moveableCtrl.canJump()).thenReturn(true);
		
		//then
		assertTrue(ZootStateUtils.canActorJump(event));
		
		//when
		when(moveableCtrl.canJump()).thenReturn(false);
	
		//then
		assertFalse(ZootStateUtils.canActorJump(event));		
	}
	
	@Test
	public void shouldReturnTrueForCanJumpWhenNoMovementControllerIsPresent()
	{
		ZootActor actor = new ZootActor();
		ZootEvent event = new ZootEvent();
		event.setTarget(actor);
		
		assertTrue(ZootStateUtils.canActorJump(event));
	}
	
	@Test
	public void shouldReturnFalseForCanJumpWhenEventHasNoActor()
	{
		assertTrue(ZootStateUtils.canActorJump(new ZootEvent()));
	}
	
	@Test
	public void shouldBeAbleToHurtActorIfNoLifeControllerIsPresent()
	{
		ZootActor actor = new ZootActor();
		ZootEvent event = new ZootEvent();
		event.setTarget(actor);
		
		assertTrue(ZootStateUtils.canHurtActor(event));
	}
	
	@Test
	public void shouldBeAbleToHurtActorIfLifeControllerIsNotFrozen()
	{
		//given
		LifeController lifeCtrl = new LifeController();
		
		ZootActor actor = new ZootActor();		
		actor.addController(lifeCtrl);
		
		ZootEvent event = new ZootEvent();
		event.setTarget(actor);
		
		//when
		lifeCtrl.setFrozen(false);
		
		//then
		assertTrue(ZootStateUtils.canHurtActor(event));		
	}
	
	@Test
	public void shouldNotBeAbleToHurtActorIfLifeControllerIsFrozen()
	{
		//given
		LifeController lifeCtrl = new LifeController();
		
		ZootActor actor = new ZootActor();		
		actor.addController(lifeCtrl);
		
		ZootEvent event = new ZootEvent();
		event.setTarget(actor);
		
		//when
		lifeCtrl.setFrozen(true);
		
		//then
		assertFalse(ZootStateUtils.canHurtActor(event));		
	}
}
