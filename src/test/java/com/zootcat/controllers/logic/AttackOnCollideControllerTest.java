package com.zootcat.controllers.logic;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.zootcat.actions.ZootFireEventAction;
import com.zootcat.controllers.physics.OnCollideWithSensorController.SensorCollisionResult;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.scene.ZootActor;

public class AttackOnCollideControllerTest
{
	private ZootActor ctrlActor;
	private AttackOnCollideController ctrl;
			
	@Before
	public void setup()
	{
		ctrlActor = new ZootActor();		
		ctrl = new AttackOnCollideController();
		ctrl.init(ctrlActor);
	}
	
	@Test
	public void shouldProcessOnlyOneCollision()
	{
		assertEquals(SensorCollisionResult.StopProcessing, ctrl.onCollision(mock(Fixture.class)));
	}
	
	@Test
	public void shouldAddFireEventAction()
	{
		//given
		ZootActor actorBeingAttacked = mock(ZootActor.class);
		Fixture fixture = mock(Fixture.class);
		when(fixture.getUserData()).thenReturn(actorBeingAttacked);
		
		//when
		ctrl.onEnterCollision(fixture);
		
		//then
		assertEquals(1, ctrlActor.getActions().size);
		ZootFireEventAction fireEventAction = (ZootFireEventAction) ctrlActor.getActions().get(0);
		assertEquals(ZootEventType.Attack, fireEventAction.getEvent().getType());
		assertEquals(actorBeingAttacked, fireEventAction.getEvent().getUserObject(ZootActor.class));
	}
}
