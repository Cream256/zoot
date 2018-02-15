package com.zootcat.fsm.states;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Before;
import org.junit.Test;

import com.zootcat.events.ZootEvent;
import com.zootcat.fsm.states.NullState;
import com.zootcat.scene.ZootActor;

public class NullStateTest
{
	private NullState state;
	
	@Before
	public void setup()
	{
		state = NullState.INSTANCE;
	}
		
	@Test
	public void handleTest()
	{
		assertFalse(state.handle(new ZootEvent()));
		assertFalse(state.handle(null));
	}
	
	@Test
	public void getIdTest()
	{
		assertEquals(0, state.getId());
	}
	
	@Test
	public void actionsShouldNotCauseAnySideEffectsTest()
	{
		//given
		ZootActor actor = mock(ZootActor.class);
		
		//when
		state.onEnter(actor, null);
		state.onUpdate(actor, 0.0f);
		state.onLeave(actor, null);
		
		//then
		verifyZeroInteractions(actor);		
	}
}
