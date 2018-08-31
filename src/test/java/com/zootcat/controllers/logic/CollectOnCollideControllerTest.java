package com.zootcat.controllers.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.zootcat.fsm.events.ZootActorEventCounterListener;
import com.zootcat.scene.ZootActor;

public class CollectOnCollideControllerTest
{
	@Mock private Contact contact;
	@Mock private ZootActor collector;
	@Mock private ZootActor collectible;
	private CollectOnCollideController collectingCtrl;
	private CollectOnCollideController notCollectingCtrl;
	private boolean onCollectCalled;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);	
		onCollectCalled = false;
		collectingCtrl = new CollectOnCollideController()
		{
			@Override
			public boolean onCollect(ZootActor collectible, ZootActor collector)
			{
				onCollectCalled = true;
				return true;
			}
		};
		notCollectingCtrl = new CollectOnCollideController() 
		{
			@Override
			public boolean onCollect(ZootActor collectible, ZootActor collector)
			{
				onCollectCalled = true;
				return false;
			}
		};
	}
	
	@Test
	public void shouldRemoveCollectibleOnCollection()
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
		assertTrue("Remove action should be added", collectible.getActions().size > 0);
		assertTrue("Remove action should be present", ClassReflection.isInstance(RemoveActorAction.class, collectible.getActions().get(0)));
		assertTrue("onCollect should be called", onCollectCalled);
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
	public void shouldDoNothingOnLeave()
	{
		collectingCtrl.onLeave(collector, collectible, contact);
		verifyZeroInteractions(collector, collectible, contact);
	}

	@Test
	public void shuoldDoNothingOnCollect()
	{
		collectingCtrl.onCollect(collectible, collector);
		verifyZeroInteractions(collector, collectible);
	}
}
