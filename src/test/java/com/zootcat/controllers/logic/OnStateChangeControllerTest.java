package com.zootcat.controllers.logic;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.zootcat.fsm.ZootState;
import com.zootcat.fsm.states.DeadState;
import com.zootcat.fsm.states.IdleState;
import com.zootcat.scene.ZootActor;

public class OnStateChangeControllerTest
{
	private OnStateChangeController ctrl;
	private int onEnterStateCount;
	private int onLeaveStateCount;
	private ZootActor actor;
	private ZootState lastEnteredState;
	private ZootState lastLeavedState;
	private IdleState idleState;
	private DeadState deadState;
	
	@Before
	public void setup()
	{
		onEnterStateCount = 0;
		onLeaveStateCount = 0;
		ctrl = new OnStateChangeController()
		{
			@Override
			public void onEnterState(ZootActor actor, ZootState state)
			{
				++onEnterStateCount;
				lastEnteredState = state;
			}

			@Override
			public void onLeaveState(ZootActor actor, ZootState state)
			{
				++onLeaveStateCount;
				lastLeavedState = state;
			}
		};
		
		idleState = new IdleState();
		deadState = new DeadState();
		
		actor = new ZootActor();
		actor.getStateMachine().init(idleState);
	}
		
	@Test
	public void shouldDoNothingWhenStateIsNotChanged()
	{
		//when
		ctrl.init(actor);
		ctrl.onUpdate(1.0f, actor);
		
		//then
		assertEquals(0, onEnterStateCount);
		assertEquals(0, onLeaveStateCount);
	}
	
	@Test
	public void shouldDetectEnteringNewState()
	{				
		//when
		ctrl.init(actor);
		actor.getStateMachine().changeState(deadState, null);
		ctrl.onUpdate(1.0f, actor);
		
		//then
		assertEquals(1, onEnterStateCount);
		assertEquals(deadState, lastEnteredState);
		
		//when
		ctrl.onUpdate(1.0f, actor);
		
		//then
		assertEquals(1, onEnterStateCount);
		assertEquals(deadState, lastEnteredState);
	}
	
	@Test
	public void shouldDetectLeavingOldState()
	{		
		//when
		ctrl.init(actor);
		actor.getStateMachine().changeState(deadState, null);
		ctrl.onUpdate(1.0f, actor);
		
		//then
		assertEquals(1, onLeaveStateCount);
		assertEquals(idleState, lastLeavedState);
		
		//when
		ctrl.onUpdate(1.0f, actor);
		
		//then
		assertEquals(1, onLeaveStateCount);
		assertEquals(idleState, lastLeavedState);
	}
}
