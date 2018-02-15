package com.zootcat.controllers.logic;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Before;
import org.junit.Test;

import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.events.ZootEventType;
import com.zootcat.events.ZootEvents;
import com.zootcat.fsm.states.DeadState;
import com.zootcat.fsm.states.IdleState;
import com.zootcat.scene.ZootActor;

public class OnEnterStateControllerTest
{
	private OnEnterStateController ctrl;
	private int onEnterStateCount;
	
	@Before
	public void setup()
	{
		onEnterStateCount = 0;
		ctrl = new OnEnterStateController()
		{
			public void onEnterState(ZootActor actor)
			{
				++onEnterStateCount;
			}
		};
	}
	
	@Test
	public void onEnterStateTest()
	{
		//given
		ZootActor actor = mock(ZootActor.class);		
		OnEnterStateController ctrl = new OnEnterStateController();
		
		//when
		ctrl.onEnterState(actor);
		
		//then
		verifyZeroInteractions(actor);
	}
	
	@Test
	public void onUpdateTest()
	{
		//given
		ZootActor actor = new ZootActor();
		actor.getStateMachine().init(new IdleState());
		actor.getStateMachine().addState(new DeadState());		
		ControllerAnnotations.setControllerParameter(ctrl, "stateName", new DeadState().getName());
		
		//when
		ctrl.onUpdate(1.0f, actor);
		
		//then
		assertEquals("Should not fire onEnterState in wrong state", 0, onEnterStateCount);
		
		//when
		ZootEvents.fireAndFree(actor, ZootEventType.Dead);		
		ctrl.onUpdate(1.0f, actor);
		
		//then
		assertEquals("Should fire onEnterState", 1, onEnterStateCount);
		
		//when
		ctrl.onUpdate(1.0f, actor);
		
		//then
		assertEquals("Should not fire onEnterState in the same state", 1, onEnterStateCount);
	}
}
