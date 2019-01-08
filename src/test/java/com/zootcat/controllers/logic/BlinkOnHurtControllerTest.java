package com.zootcat.controllers.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.fsm.events.ZootEvent;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.fsm.events.ZootEvents;
import com.zootcat.scene.ZootActor;

public class BlinkOnHurtControllerTest
{
	private ZootActor actor;	
	private ZootEvent hurtEvent;
	private LifeController lifeCtrl;
	private BlinkOnHurtController blinkOnHurtCtrl;
	
		
	@Before
	public void setup()
	{
		actor = new ZootActor();	
		
		lifeCtrl = new LifeController();
		actor.addController(lifeCtrl);
		
		hurtEvent = ZootEvents.get(ZootEventType.Hurt);
		hurtEvent.setTarget(actor);
		
		blinkOnHurtCtrl = new BlinkOnHurtController();
	}
	
	@Test
	public void shouldNotHandleEventIfItsNotHurtEvent()
	{
		Arrays.stream(ZootEventType.values()).filter(type -> type != ZootEventType.Hurt).forEach(type -> 
		{
			assertFalse(blinkOnHurtCtrl.onZootEvent(actor, ZootEvents.get(type)));	
		});
	}
	
	@Test
	public void shouldReturnTrueOnHurtEvent()
	{
		assertTrue(blinkOnHurtCtrl.onZootEvent(actor, hurtEvent));	
	}
	
	@Test
	public void shouldNotBeBlinkingByDefault()
	{
		assertFalse(blinkOnHurtCtrl.isBlinking());
	}
	
	@Test
	public void shouldStartBlinkingOnHurtEvent()
	{
		blinkOnHurtCtrl.onZootEvent(actor, hurtEvent);
		assertTrue(blinkOnHurtCtrl.isBlinking());
	}
	
	@Test
	public void shouldFreezeActorLifeControllerOnHurtEvent()
	{
		blinkOnHurtCtrl.onZootEvent(actor, hurtEvent);
		actor.act(0.0f);
		
		assertTrue(lifeCtrl.isFrozen());		
	}
	
	@Test
	public void shouldUnfreezeActorLifeControllerAfterProvidedDuration()
	{
		//given
		float expectedDuration = 2.0f;
		ControllerAnnotations.setControllerParameter(blinkOnHurtCtrl, "duration", expectedDuration);
		
		//when
		blinkOnHurtCtrl.onZootEvent(actor, hurtEvent);		
		actOnActor(expectedDuration);
		
		//then
		assertFalse(lifeCtrl.isFrozen());
	}
	
	@Test
	public void shouldFadeInAndOutInHalfSecond()
	{
		//given
		float expectedDuration = 0.5f;
		ControllerAnnotations.setControllerParameter(blinkOnHurtCtrl, "duration", expectedDuration);
		
		//when
		blinkOnHurtCtrl.onZootEvent(actor, hurtEvent);
		actOnActor(0.25f);
		
		//then
		assertEquals(0.0f, actor.getColor().a, 0.0f);
		
		//when
		actOnActor(0.25f);
		
		//then
		assertEquals(1.0f, actor.getColor().a, 0.0f);
	}
	
	@Test
	public void shouldFinishBlinkingBeforeBlinkingAgain()
	{
		//given
		float expectedDuration = 2.0f;
		ControllerAnnotations.setControllerParameter(blinkOnHurtCtrl, "duration", expectedDuration);
		
		//when
		blinkOnHurtCtrl.onZootEvent(actor, hurtEvent);		
		actOnActor(expectedDuration / 2.0f);
		
		//then still frozen
		assertTrue(lifeCtrl.isFrozen());
		
		//when
		blinkOnHurtCtrl.onZootEvent(actor, hurtEvent);
		actOnActor(expectedDuration / 2.0f);
		
		//then unfrozen
		assertFalse(lifeCtrl.isFrozen());
	}
	
	private void actOnActor(float actTime)
	{
		float time = 0.0f;
		float timeStep = 0.01f;
		while(time <= actTime + timeStep)
		{
			time += timeStep;
			actor.act(timeStep);	
		}
	}
}
