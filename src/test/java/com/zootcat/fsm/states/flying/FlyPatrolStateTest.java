package com.zootcat.fsm.states.flying;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.badlogic.gdx.math.Vector2;
import com.zootcat.testing.ZootSceneMock;
import com.zootcat.testing.ZootStateTestCase;

public class FlyPatrolStateTest extends ZootStateTestCase
{
	private FlyPatrolState flyPatrolState;
	
	@Override
	public void setup()
	{
		super.setup();
		flyPatrolState = new FlyPatrolState();
	}
	
	@Test
	public void shouldReturnValidId()
	{
		assertEquals(FlyPatrolState.ID, flyPatrolState.getId());
	}
	
	@Test
	public void shouldReplaceFlyState()
	{
		assertEquals(FlyState.ID, flyPatrolState.getId());
	}
	
	@Test
	public void shouldSetFlyAnimation()
	{
		flyPatrolState.onEnter(actor, null);
		verify(animatedSpriteCtrlMock).setAnimation("Fly");
	}
	
	@Test
	public void shouldFly()
	{
		//given		
		actor.setScene(new ZootSceneMock());
		when(physicsBodyCtrlMock.getCenterPositionRef()).thenReturn(new Vector2());
				
		//when
		flyPatrolState.onUpdate(actor, 0.0f);
		
		//then
		verify(flyableCtrlMock).fly(any());
	}
}
