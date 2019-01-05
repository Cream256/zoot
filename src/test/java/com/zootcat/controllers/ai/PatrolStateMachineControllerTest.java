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
import com.zootcat.fsm.states.PatrolState;
import com.zootcat.scene.ZootActor;
import com.zootcat.testing.ZootActorStub;

public class PatrolStateMachineControllerTest
{
	private ZootActor actor;
	private PatrolStateMachineController ctrl;
	@Mock private PhysicsBodyController physicsBodyCtrl;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		
		actor = new ZootActorStub();
		actor.addController(physicsBodyCtrl);
		when(physicsBodyCtrl.getCenterPositionRef()).thenReturn(new Vector2());
		
		ctrl = new PatrolStateMachineController();		
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
	public void shouldSetStartingPositionForPatrolState()
	{
		//given
		final float expectedStartX = 128.0f;
		when(physicsBodyCtrl.getCenterPositionRef()).thenReturn(new Vector2(expectedStartX, 0.0f));
		
		//when
		ctrl.onAdd(actor);
		
		//then
		PatrolState patrolState = (PatrolState) actor.getStateMachine().getStateById(PatrolState.ID);
		assertEquals(expectedStartX, patrolState.getStartX(), 0.0f);
	}
	
	@Test
	public void shouldSetPatrolRangeForPatrolState()
	{
		//given
		final int expectedPatrolRange = 256;
		ControllerAnnotations.setControllerParameter(ctrl, "patrolRange", expectedPatrolRange);
		
		//when
		ctrl.onAdd(actor);
		
		//then
		PatrolState patrolState = (PatrolState) actor.getStateMachine().getStateById(PatrolState.ID);
		assertEquals(expectedPatrolRange, patrolState.getPatrolRange(), 0.0f);		
	}
}
