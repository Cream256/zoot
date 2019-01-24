package com.zootcat.controllers.logic;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.fsm.events.ZootActorEventCounterListener;
import com.zootcat.scene.ZootActor;

public class DieOnCollideSensorControllerTest
{
	private static final float TIME = 5.0f;
		
	private DieOnCollideSensorController ctrl;
		
	@Before
	public void setup()
	{
		ctrl = new DieOnCollideSensorController();
	}
				
	@Test
	public void shouldKillActorAfterTimeHasPassed()
	{
		//given
		ZootActor ctrlActor = new ZootActor();
		ZootActorEventCounterListener eventCounter = new ZootActorEventCounterListener();		
		ctrlActor.addListener(eventCounter);
		
		//when
		ControllerAnnotations.setControllerParameter(ctrl, "delay", TIME);
		ctrl.init(ctrlActor);
		ctrl.onEnterCollision(mock(Fixture.class));
		ctrlActor.act(TIME);
				
		//then
		assertEquals("Dead event should be send", 1, eventCounter.getCount());		
	}
	
	@Test
	public void shouldNotKillActorBeforeTimeHasPassed()
	{
		//given
		ZootActor ctrlActor = new ZootActor();
		ZootActorEventCounterListener eventCounter = new ZootActorEventCounterListener();		
		ctrlActor.addListener(eventCounter);
		
		//when
		ctrl.init(ctrlActor);
		ControllerAnnotations.setControllerParameter(ctrl, "delay", TIME);
		ctrl.onCollision(mock(Fixture.class));
		ctrlActor.act(TIME / 2);
				
		//then
		assertEquals("Dead event should not be send", 0, eventCounter.getCount());
	}
	
	@Test
	public void shouldKillActorOnlyOnce()
	{
		//given
		ZootActor ctrlActor = new ZootActor();
		ZootActorEventCounterListener eventCounter = new ZootActorEventCounterListener();		
		ctrlActor.addListener(eventCounter);
		
		//when
		ctrl.init(ctrlActor);
		ControllerAnnotations.setControllerParameter(ctrl, "delay", TIME);
		ctrl.onEnterCollision(mock(Fixture.class));
		ctrlActor.act(TIME * 2);
				
		//then
		assertEquals("Dead event should be send only once", 1, eventCounter.getCount());	
	}
}
