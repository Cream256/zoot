package com.zootcat.fsm.states;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.badlogic.gdx.math.Vector2;
import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.events.ZootEvents;
import com.zootcat.gfx.ZootAnimation;
import com.zootcat.scene.ZootScene;
import com.zootcat.testing.ZootStateTestCase;

public class PatrolStateTest extends ZootStateTestCase
{
	private PatrolState patrolState;
	@Mock private ZootScene scene;
		
	@Before
	public void setup()
	{
		super.setup();		
		
		when(scene.getUnitScale()).thenReturn(1.0f);
		actor.setScene(scene);
		
		patrolState = new PatrolState();
	}
	
	@Test
	public void shouldReturnDefaultValues()
	{
		assertEquals(0.0f, patrolState.getPatrolRange(), 0.0f);
		assertEquals(0.0f, patrolState.getStartX(), 0.0f);		
	}
	
	@Test
	public void shouldReturnWalkStateId()
	{
		assertEquals(WalkState.ID, patrolState.getId());
	}
	
	@Test
	public void shouldReturnValidName()
	{
		assertEquals("Patrol", patrolState.getName());
	}	
	
	@Test
	public void shouldSetPatrolRange()
	{
		patrolState.setPatrolRange(100.0f);
		assertEquals(100.0f, patrolState.getPatrolRange(), 0.0f);
	}
	
	@Test
	public void shouldSetZeroPatrolRangeIfValueIsNegative()
	{
		patrolState.setPatrolRange(-1.0f);
		assertEquals(0.0f, patrolState.getPatrolRange(), 0.0f);
	}
	
	@Test
	public void shouldSetStartingPosition()
	{
		patrolState.setStartX(128.0f);
		assertEquals(128.0f, patrolState.getStartX(), 0.0f);
		
		patrolState.setStartX(-128.0f);
		assertEquals(-128.0f, patrolState.getStartX(), 0.0f);
	}
	
	@Test
	public void shouldSetWalkAnimation()
	{
		patrolState.onEnter(actor, null);
		verify(animatedSpriteCtrlMock).setAnimation(new WalkState().getName());		
	}
	
	@Test
	public void shouldTurnOnObstacle()
	{		
		ZootEvent obstacleEvent = ZootEvents.get(ZootEventType.Obstacle);
		obstacleEvent.setTarget(actor);
		
		assertTrue(patrolState.handle(obstacleEvent));
		assertEquals(TurnState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void shouldTurnOnObstacleUsingTurnAnimationDurationAsCooldown()
	{
		//given
		final int frameCount = 10;
		final float frameDuration = 0.5f;		
		ZootAnimation turnAnimation = mock(ZootAnimation.class);
		when(turnAnimation.getFrameCount()).thenReturn(frameCount);
		when(turnAnimation.getFrameDuration()).thenReturn(frameDuration);
		when(animatedSpriteCtrlMock.getAnimation("Turn")).thenReturn(turnAnimation);
		when(physicsBodyCtrlMock.getCenterPositionRef()).thenReturn(new Vector2());
		
		ZootEvent obstacleEvent = ZootEvents.get(ZootEventType.Obstacle);
		obstacleEvent.setTarget(actor);
		
		//when
		patrolState.handle(obstacleEvent);
		
		//then
		assertEquals(TurnState.ID, actor.getStateMachine().getCurrentState().getId());
				
		//when
		actor.getStateMachine().init(patrolState);		
		patrolState.onUpdate(actor, frameCount * frameDuration - 0.1f);
		patrolState.handle(obstacleEvent);

		//then
		assertEquals("Should not turn if not cooled down", patrolState.getId(), actor.getStateMachine().getCurrentState().getId());
		
		//when
		patrolState.onUpdate(actor, 0.11f + PatrolState.TURN_TIME_PADDING);
		patrolState.handle(obstacleEvent);
		
		//then
		assertEquals("Should turn after cooldown completes", TurnState.ID, actor.getStateMachine().getCurrentState().getId());
	}
	
	@Test
	public void shouldTurnWhenNoGroundAhead()
	{
		ZootEvent obstacleEvent = ZootEvents.get(ZootEventType.NoGroundAhead);
		obstacleEvent.setTarget(actor);
		
		assertTrue(patrolState.handle(obstacleEvent));
		assertEquals(TurnState.ID, actor.getStateMachine().getCurrentState().getId());
	}	
	
	@Test
	public void shouldNotTurnWhenInPatrolRange()
	{
		//given		
		when(physicsBodyCtrlMock.getCenterPositionRef()).thenReturn(new Vector2(0.0f, 0.0f));
		
		//when
		patrolState.setPatrolRange(100.0f);
		patrolState.setStartX(0.0f);
		patrolState.onUpdate(actor, 0.0f);
		
		//then
		assertEquals("Should not turn if in patrol range", IdleState.ID, actor.getStateMachine().getCurrentState().getId());		
	}
	
	@Test
	public void shouldTurnWhenOutsideOfPatrolRange()
	{
		//given		
		when(physicsBodyCtrlMock.getCenterPositionRef()).thenReturn(new Vector2(101.0f, 0.0f));
		
		//when
		patrolState.setPatrolRange(100.0f);
		patrolState.setStartX(0.0f);
		patrolState.onUpdate(actor, 0.0f);
		
		//then
		assertEquals("Should turn if outside of patrol range", TurnState.ID, actor.getStateMachine().getCurrentState().getId());		
	}
}