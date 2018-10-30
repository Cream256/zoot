package com.zootcat.controllers.ai;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.math.Vector2;
import com.zootcat.controllers.ControllerPriority;
import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.controllers.gfx.AnimatedSpriteController;
import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.fsm.events.ZootActorEventCounterListener;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.events.ZootEvents;
import com.zootcat.gfx.ZootAnimation;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;
import com.zootcat.scene.ZootScene;

public class PatrolAIControllerTest
{		
	private static final int DISTANCE = 250;
	private static final String START_DIRECTION = "Right";
	
	@Mock private ZootScene scene;
	@Mock private PhysicsBodyController physicsCtrl;
	
	private ZootActor actor;
	private PatrolAIController patrolAI;
	private ZootActorEventCounterListener eventCounter;
			
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		when(scene.getUnitScale()).thenReturn(1.0f);
		when(physicsCtrl.getCenterPositionRef()).thenReturn(new Vector2());
		
		eventCounter = new ZootActorEventCounterListener();		
		
		actor = new ZootActor();
		actor.addController(physicsCtrl);
		actor.addListener(eventCounter);
		
		patrolAI = new PatrolAIController();
		ControllerAnnotations.setControllerParameter(patrolAI, "distance", DISTANCE);
		ControllerAnnotations.setControllerParameter(patrolAI, "startDirection", START_DIRECTION);
		ControllerAnnotations.setControllerParameter(patrolAI, "scene", scene);
	}
	
	@Test
	public void shouldSetValidStartingDirection()
	{
		//when
		patrolAI.onAdd(actor);
		
		//then
		assertEquals(ZootDirection.fromString(START_DIRECTION), patrolAI.getCurrentDirection());	
	}
	
	@Test
	public void shouldGoTowardsStartingDirection()
	{
		//when
		patrolAI.onAdd(actor);	
		patrolAI.onUpdate(1.0f, actor);
		
		//then
		assertEquals(ZootEventType.WalkRight, eventCounter.getLastZootEvent().getType());		
	}
	
	@Test
	public void shouldWalkLeft()
	{
		//given
		ControllerAnnotations.setControllerParameter(patrolAI, "startDirection", "Right");
		
		//when
		when(physicsCtrl.getCenterPositionRef()).thenReturn(new Vector2(0.0f, 0.0f));		
		patrolAI.onAdd(actor);		
		
		when(physicsCtrl.getCenterPositionRef()).thenReturn(new Vector2(DISTANCE + 1, 0.0f));
		patrolAI.onUpdate(1.0f, actor);
		
		//then
		assertEquals(ZootEventType.WalkLeft, eventCounter.getLastZootEvent().getType());		
	}
	
	@Test
	public void shouldWalkRight()
	{
		//given
		ControllerAnnotations.setControllerParameter(patrolAI, "startDirection", "Left");
		
		//when
		when(physicsCtrl.getCenterPositionRef()).thenReturn(new Vector2(0.0f, 0.0f));		
		patrolAI.onAdd(actor);		
		
		when(physicsCtrl.getCenterPositionRef()).thenReturn(new Vector2(-DISTANCE - 1, 0.0f));
		patrolAI.onUpdate(1.0f, actor);
		
		//then
		assertEquals(ZootEventType.WalkRight, eventCounter.getLastZootEvent().getType());				
	}
	
	@Test
	public void shouldFlyLeft()
	{
		//given
		ControllerAnnotations.setControllerParameter(patrolAI, "isFlying", true);
		ControllerAnnotations.setControllerParameter(patrolAI, "startDirection", "Left");		
		
		//when
		when(physicsCtrl.getCenterPositionRef()).thenReturn(new Vector2(0.0f, 0.0f));		
		patrolAI.onAdd(actor);		
		
		when(physicsCtrl.getCenterPositionRef()).thenReturn(new Vector2(DISTANCE + 1, 0.0f));
		patrolAI.onUpdate(1.0f, actor);
		
		//then
		assertEquals(ZootEventType.FlyLeft, eventCounter.getLastZootEvent().getType());		
	}
	
	@Test
	public void shouldFlyRight()
	{
		//given
		ControllerAnnotations.setControllerParameter(patrolAI, "isFlying", true);
		ControllerAnnotations.setControllerParameter(patrolAI, "startDirection", "Right");		
		
		//when
		when(physicsCtrl.getCenterPositionRef()).thenReturn(new Vector2(0.0f, 0.0f));		
		patrolAI.onAdd(actor);		
		
		when(physicsCtrl.getCenterPositionRef()).thenReturn(new Vector2(-DISTANCE - 1, 0.0f));
		patrolAI.onUpdate(1.0f, actor);
		
		//then
		assertEquals(ZootEventType.FlyRight, eventCounter.getLastZootEvent().getType());		
	}
		
	@Test
	public void shouldTurnOnObstacle()
	{
		//when
		patrolAI.onAdd(actor);
		patrolAI.handleZootEvent(ZootEvents.get(ZootEventType.Obstacle));
		
		//then
		assertEquals(ZootDirection.Left, patrolAI.getCurrentDirection());
		
		//when
		patrolAI.onUpdate(PatrolAIController.DEFAULT_TURN_COOLDOWN / 2.0f, actor);
		patrolAI.handleZootEvent(ZootEvents.get(ZootEventType.Obstacle));
		
		//then
		assertEquals(ZootDirection.Left, patrolAI.getCurrentDirection());
		
		//when
		patrolAI.onUpdate(PatrolAIController.DEFAULT_TURN_COOLDOWN / 2.0f, actor);
		patrolAI.handleZootEvent(ZootEvents.get(ZootEventType.Obstacle));
		
		//then
		assertEquals(ZootDirection.Right, patrolAI.getCurrentDirection());
	}
	
	@Test
	public void shouldTurnOnObstacleUsingTurnAnimationDurationAsCooldown()
	{
		//given
		final float frameDuration = 1.0f;
		final int frameCount = 5;
		final float cooldown = frameDuration * frameCount;
		
		ZootAnimation turnAnimation = mock(ZootAnimation.class);
		when(turnAnimation.getFrameDuration()).thenReturn(frameDuration);
		when(turnAnimation.getFrameCount()).thenReturn(frameCount);
		
		AnimatedSpriteController animatedSpriteCtrl = mock(AnimatedSpriteController.class);		
		when(animatedSpriteCtrl.getAnimation("Turn")).thenReturn(turnAnimation);		
		
		//when
		actor.addController(animatedSpriteCtrl);
		patrolAI.onAdd(actor);
		patrolAI.handleZootEvent(ZootEvents.get(ZootEventType.Obstacle));
		
		//then
		assertEquals(ZootDirection.Left, patrolAI.getCurrentDirection());
		
		//when
		patrolAI.onUpdate(cooldown / 2.0f, actor);
		patrolAI.handleZootEvent(ZootEvents.get(ZootEventType.Obstacle));
		
		//then
		assertEquals(ZootDirection.Left, patrolAI.getCurrentDirection());
		
		//when
		patrolAI.onUpdate(cooldown / 2.0f, actor);
		patrolAI.handleZootEvent(ZootEvents.get(ZootEventType.Obstacle));
		
		//then
		assertEquals(ZootDirection.Left, patrolAI.getCurrentDirection());
		
		//when
		patrolAI.onUpdate(PatrolAIController.TURN_TIME_PADDING + 0.1f, actor);
		patrolAI.handleZootEvent(ZootEvents.get(ZootEventType.Obstacle));
		
		//then
		assertEquals(ZootDirection.Right, patrolAI.getCurrentDirection());		
	}
	
	@Test
	public void shouldTurnWhenNoGroundAhead()
	{
		//when
		patrolAI.onAdd(actor);
		patrolAI.handleZootEvent(ZootEvents.get(ZootEventType.NoGroundAhead));
		
		//then
		assertEquals(ZootDirection.Left, patrolAI.getCurrentDirection());
		
		//when
		patrolAI.onUpdate(PatrolAIController.DEFAULT_TURN_COOLDOWN / 2.0f, actor);
		patrolAI.handleZootEvent(ZootEvents.get(ZootEventType.NoGroundAhead));
		
		//then
		assertEquals(ZootDirection.Left, patrolAI.getCurrentDirection());
		
		//when
		patrolAI.onUpdate(PatrolAIController.DEFAULT_TURN_COOLDOWN / 2.0f, actor);
		patrolAI.handleZootEvent(ZootEvents.get(ZootEventType.NoGroundAhead));
		
		//then
		assertEquals(ZootDirection.Right, patrolAI.getCurrentDirection());	
	}
	
	@Test
	public void shouldDoNothingOnRemove()
	{
		patrolAI.onRemove(actor);	//to increase code coverage
	}
		
	@Test
	public void shouldReturnHighPriority()
	{		
		assertEquals(ControllerPriority.High, patrolAI.getPriority());
	}	
}
