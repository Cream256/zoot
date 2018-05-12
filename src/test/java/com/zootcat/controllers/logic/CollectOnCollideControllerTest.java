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
	private CollectOnCollideController ctrl;
	private boolean onCollectCalled;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);	
		onCollectCalled = false;
		ctrl = new CollectOnCollideController(){
			@Override
			public void onCollect(ZootActor collectible, ZootActor collector)
			{
				onCollectCalled = true;
			}};
	}
	
	@Test
	public void onEnterTest()
	{
		//given
		collector = new ZootActor();
		collectible = new ZootActor();
		
		ZootActorEventCounterListener counter = new ZootActorEventCounterListener();
		collectible.addListener(counter);
			
		//when		
		ctrl.init(collectible);
		ctrl.onEnter(collectible, collector, contact);
		
		//then
		assertEquals("Dead event should be send", 1, counter.getCount());		
		assertTrue("Remove action should be added", collectible.getActions().size > 0);
		assertTrue("Remove action should be present", ClassReflection.isInstance(RemoveActorAction.class, collectible.getActions().get(0)));
		assertTrue("onCollect should be called", onCollectCalled);
	}
	
	@Test
	public void onLeaveTest()
	{
		ctrl.onLeave(collector, collectible, contact);
		verifyZeroInteractions(collector, collectible, contact);
	}

	@Test
	public void onCollectTest()
	{
		ctrl.onCollect(collectible, collector);
		verifyZeroInteractions(collector, collectible);
	}
}
