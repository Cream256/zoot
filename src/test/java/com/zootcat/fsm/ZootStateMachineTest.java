package com.zootcat.fsm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.zootcat.events.ZootEvent;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.fsm.states.BasicState;
import com.zootcat.fsm.states.NullState;
import com.zootcat.scene.ZootActor;

public class ZootStateMachineTest
{
	private ZootStateMachine sm;
	private ZootActor owner;
	
	@Before
	public void setup()
	{
		sm = new ZootStateMachine();
		owner = new ZootActor();
	}
	
	@Test
	public void addStateTest()
	{
		ZootState state1 = new BasicState("state1");
		ZootState state2 = NullState.INSTANCE;
		
		sm.addState(state1);
		assertEquals(1, sm.getStates().size());
		assertTrue(sm.getStates().contains(state1));
		
		sm.addState(state2);
		assertEquals(2, sm.getStates().size());
		assertTrue(sm.getStates().contains(state1));
		assertTrue(sm.getStates().contains(state2));
		
		sm.addState(state1);
		assertEquals("Should not add twice the same state", 2, sm.getStates().size());
		assertTrue(sm.getStates().contains(state1));
		assertTrue(sm.getStates().contains(state2));
	}
	
	@Test
	public void getStateByIdTeset()
	{
		//given
		ZootState state1 = new BasicState("test");
		ZootState state2 = new BasicState("test2");
		
		//when
		sm.addState(state1);
		sm.addState(state2);
		
		//then
		assertEquals(state1, sm.getStateById(state1.getId()));
		assertEquals(state2, sm.getStateById(state2.getId()));		
	}
	
	@Test(expected = RuntimeZootException.class)
	public void getStateByClassShouldThrowOnNotExistingIdTest()
	{
		sm.getStateById(-1);
	}
	
	@Test
	public void initTest()
	{
		//given
		ZootState initialState = mock(ZootState.class);
		
		//when
		sm.setOwner(owner);
		sm.init(initialState);
		
		//then
		verify(initialState, times(1)).onEnter(eq(owner), any(ZootEvent.class));
		assertEquals("Initial state should be current", initialState, sm.getCurrentState());		
		assertEquals("Previous state should reset", null, sm.getPreviousState());
		assertEquals("Initial state should be added to state machine", 1, sm.getStates().size());
		assertTrue("Initial state should be added to state machine", sm.getStates().contains(initialState));
	}
	
	@Test
	public void setOwnerTest()
	{
		sm.setOwner(owner);
		assertEquals(owner, sm.getOwner());
		sm.setOwner(null);
		assertEquals(null, sm.getOwner());
	}
	
	@Test(expected = RuntimeZootException.class)
	public void changeStateShouldThrowOnNullStateTest()
	{
		sm.changeState(null, null);
	}
	
	@Test
	public void changeStateTest()
	{
		//given
		ZootState firstState = mock(ZootState.class);
		ZootState secondState = mock(ZootState.class);
		
		sm.setOwner(owner);
		sm.init(firstState);
		sm.addState(secondState);
		
		//when
		sm.changeState(secondState, null);
		
		//then
		verify(firstState, times(1)).onLeave(owner, null);
		verify(secondState, times(1)).onEnter(owner, null);		
		assertEquals(secondState, sm.getCurrentState());
		assertEquals(firstState, sm.getPreviousState());
	}
	
	@Test
	public void handleTest()
	{
		//given
		ZootState state = mock(ZootState.class);
		sm.init(state);	
		
		//when
		when(state.handle(anyObject())).thenReturn(true);		

		//then
		assertTrue(sm.handle(new ZootEvent()));
		verify(state, times(1)).handle(anyObject());
		
		//when
		when(state.handle(anyObject())).thenReturn(false);		

		//then
		assertFalse(sm.handle(new ZootEvent()));
		verify(state, times(2)).handle(anyObject());
		
		//then
		assertFalse(sm.handle(new Event()));	//not ZootStateEvent should not be processed
		verify(state, times(2)).handle(anyObject());
	}
	
	@Test
	public void updateTest()
	{
		//given
		ZootState state = mock(ZootState.class);
		sm.init(state);
		sm.setOwner(owner);
		
		//when
		sm.update(0.0f);
		
		//then
		verify(state, times(1)).onUpdate(owner, 0.0f);
		
		//when
		sm.update(0.33f);

		//then
		verify(state, times(1)).onUpdate(owner, 0.33f);
	}
}
