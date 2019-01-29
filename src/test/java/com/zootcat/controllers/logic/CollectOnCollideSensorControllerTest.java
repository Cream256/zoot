package com.zootcat.controllers.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.zootcat.controllers.physics.OnCollideWithSensorController.SensorCollisionResult;
import com.zootcat.fsm.events.ZootActorEventCounterListener;
import com.zootcat.scene.ZootActor;

public class CollectOnCollideSensorControllerTest
{
	@Mock private Contact contact;
	@Mock private ZootActor collector;
	@Mock private Fixture collectorFixture;
	@Mock private ZootActor collectible;
	@Mock private Fixture collectibleFixture;
	private CollectOnCollideSensorController collectingCtrl;
	private CollectOnCollideSensorController notCollectingCtrl;
	private boolean onCollectCalled;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);	
		onCollectCalled = false;
		collectingCtrl = new CollectOnCollideSensorController()
		{
			@Override
			public boolean onCollect(ZootActor collectible, ZootActor collector)
			{
				onCollectCalled = true;
				return true;
			}
		};
		notCollectingCtrl = new CollectOnCollideSensorController() 
		{
			@Override
			public boolean onCollect(ZootActor collectible, ZootActor collector)
			{
				onCollectCalled = true;
				return false;
			}
		};
		
		when(contact.getFixtureA()).thenReturn(collectorFixture);
		when(contact.getFixtureB()).thenReturn(collectibleFixture);
		when(collectorFixture.getUserData()).thenReturn(collector);
		when(collectibleFixture.getUserData()).thenReturn(collectible);
	}
	
	@Test
	public void shouldRemoveCollectibleOnFirstCollision()
	{
		//given
		collector = new ZootActor();
		collectible = new ZootActor();
		
		ZootActorEventCounterListener counter = new ZootActorEventCounterListener();
		collectible.addListener(counter);
			
		//when		
		collectingCtrl.init(collectible);
		collectingCtrl.onEnter(collectible, collector, contact);
		
		//then
		assertEquals("Dead event should be send", 1, counter.getCount());		
		assertTrue("Remove action should be added", collectible.getActions().size == 1);
		assertTrue("Remove action should be present", ClassReflection.isInstance(RemoveActorAction.class, collectible.getActions().get(0)));
		assertTrue("onCollect should be called", onCollectCalled);
		
		//when
		collectingCtrl.onEnter(collectible, collector, contact);
		
		//then
		assertEquals("Dead event should be send only once", 1, counter.getCount());
		assertTrue("Remove action should be added only once", collectible.getActions().size == 1);
	}
	
	@Test
	public void shouldRemoveCollectibleWhileColliding()
	{
		//given
		collector = new ZootActor();
		collectible = new ZootActor();
		
		ZootActorEventCounterListener counter = new ZootActorEventCounterListener();
		collectible.addListener(counter);
			
		//when		
		collectingCtrl.init(collectible);
		SensorCollisionResult collisionResult = collectingCtrl.onCollision(collectorFixture);
		
		//then
		assertEquals(SensorCollisionResult.StopProcessing, collisionResult);
		assertEquals("Dead event should be send", 1, counter.getCount());		
		assertTrue("Remove action should be added", collectible.getActions().size == 1);
		assertTrue("Remove action should be present", ClassReflection.isInstance(RemoveActorAction.class, collectible.getActions().get(0)));
		assertTrue("onCollect should be called", onCollectCalled);
		
		//when
		collectingCtrl.onCollision(collectorFixture);
		
		//then
		assertEquals("Dead event should be send only once", 1, counter.getCount());
		assertTrue("Remove action should be added only once", collectible.getActions().size == 1);
	}	
	
	@Test
	public void shouldNotRemoveCollectibleOnCollection()
	{
		//given
		collector = new ZootActor();
		collectible = new ZootActor();
		
		ZootActorEventCounterListener counter = new ZootActorEventCounterListener();
		collectible.addListener(counter);
			
		//when		
		notCollectingCtrl.init(collectible);
		notCollectingCtrl.onEnter(collectible, collector, contact);
		
		//then
		assertEquals("Dead event should not be send", 0, counter.getCount());		
		assertTrue("Remove action should not be added", collectible.getActions().size == 0);
		assertTrue("onCollect should be called", onCollectCalled);
	}
	
	@Test
	public void shuoldDoNothingOnCollect()
	{
		collectingCtrl.onCollect(collectible, collector);
		verifyZeroInteractions(collector, collectible);
	}
}
