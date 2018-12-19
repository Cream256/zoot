package com.zootcat.controllers.ai;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.math.Vector2;
import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.controllers.physics.PhysicsBodyController;
import com.zootcat.controllers.recognizer.MockControllerRecognizer;
import com.zootcat.fsm.states.PatrolState;
import com.zootcat.fsm.states.ground.PatrolAndChaseState;
import com.zootcat.scene.ZootActor;

public class ChaseStateMachineControllerTest
{
	private ZootActor actor;
	private ChaseStateMachineController ctrl;
	@Mock private PhysicsBodyController physicsBodyCtrl;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		
		actor = new ZootActor();
		actor.setControllerRecognizer(MockControllerRecognizer.Instance);
		actor.addController(physicsBodyCtrl);
		when(physicsBodyCtrl.getCenterPositionRef()).thenReturn(new Vector2());
		
		ctrl = new ChaseStateMachineController();		
	}
		
	@Test
	public void shouldAddPatrolState()
	{
		//when
		ctrl.onAdd(actor);
		
		//then
		assertNotNull(actor.getStateMachine().getStateById(PatrolState.ID));
	}
	
	@Test
	public void shouldSetStartingPositionForChaseState()
	{
		//given
		final float expectedStartX = 128.0f;
		when(physicsBodyCtrl.getCenterPositionRef()).thenReturn(new Vector2(expectedStartX, 0.0f));
		
		//when
		ctrl.onAdd(actor);
		
		//then
		PatrolAndChaseState patrolAndChaseState = (PatrolAndChaseState) actor.getStateMachine().getStateById(PatrolAndChaseState.ID);
		assertEquals(expectedStartX, patrolAndChaseState.getStartX(), 0.0f);
	}
	
	@Test
	public void shouldSetPatrolRangeForChaseState()
	{
		//given
		final int expectedPatrolRange = 256;
		ControllerAnnotations.setControllerParameter(ctrl, "patrolRange", expectedPatrolRange);
		
		//when
		ctrl.onAdd(actor);
		
		//then
		PatrolAndChaseState patrolAndChaseState = (PatrolAndChaseState) actor.getStateMachine().getStateById(PatrolAndChaseState.ID);
		assertEquals(expectedPatrolRange, patrolAndChaseState.getPatrolRange());		
	}
	
	@Test
	public void shouldSetLookRangeForChaseState()
	{
		//given
		final int expectedLookRange = 256;
		ControllerAnnotations.setControllerParameter(ctrl, "lookRange", expectedLookRange);
		
		//when
		ctrl.onAdd(actor);
		
		//then
		PatrolAndChaseState patrolAndChaseState = (PatrolAndChaseState) actor.getStateMachine().getStateById(PatrolAndChaseState.ID);
		assertEquals(expectedLookRange, patrolAndChaseState.getLookRange());				
	}
}
