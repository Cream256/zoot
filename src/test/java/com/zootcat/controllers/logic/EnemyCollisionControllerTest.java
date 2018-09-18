package com.zootcat.controllers.logic;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.physics.box2d.Contact;
import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.fsm.events.ZootActorEventCounterListener;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.scene.ZootActor;

public class EnemyCollisionControllerTest
{
	private static final int DAMAGE_TO_PLAYER = 5;
	private static final int DAMAGE_TO_ENEMY = 6;
	
	private ZootActor enemy;
	private ZootActor player;
	private ZootActorEventCounterListener eventCounter;
	private EnemyCollisionController controller;
	
	@Before
	public void setup()
	{
		enemy = new ZootActor();
		player = new ZootActor();
		eventCounter = new ZootActorEventCounterListener();
		
		controller = new EnemyCollisionController();
		ControllerAnnotations.setControllerParameter(controller, "damageToPlayer", DAMAGE_TO_PLAYER);
		ControllerAnnotations.setControllerParameter(controller, "damageToEnemy", DAMAGE_TO_ENEMY);
		
		controller.init(enemy);
	}
	
	@Test
	public void shouldReturnProperDefaults()
	{
		controller = new EnemyCollisionController();
		assertEquals(1, controller.getDamageToEnemy());
		assertEquals(1, controller.getDamageToPlayer());
	}
	
	@Test
	public void shouldHurtPlayer()
	{
		//given
		player.addListener(eventCounter);
		
		//when
		controller.onCollidedFromBelow(enemy, player, mock(Contact.class));
		
		//then
		assertEquals(1, eventCounter.getCount());
		assertEquals(ZootEventType.Hurt, eventCounter.getLastZootEvent().getType());
		assertEquals(DAMAGE_TO_PLAYER, (int)eventCounter.getLastZootEvent().getUserObject(Integer.class));
	}
	
	@Test
	public void shouldHurtEnemy()
	{
		//given
		enemy.addListener(eventCounter);
		
		//when
		controller.onCollidedFromAbove(enemy, player, mock(Contact.class));
		
		//then
		assertEquals(1, eventCounter.getCount());
		assertEquals(ZootEventType.Hurt, eventCounter.getLastZootEvent().getType());
		assertEquals(DAMAGE_TO_ENEMY, (int)eventCounter.getLastZootEvent().getUserObject(Integer.class));		
	}
	
}
