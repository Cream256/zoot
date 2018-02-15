package com.zootcat.controllers.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.events.ZootEvent;
import com.zootcat.scene.ZootActor;

public class HurtOnCollideControllerTest
{
	private static final int ACTOR_LIFE = 3;
	private static final int DAMAGE = 2;
	
	@Mock private Contact contact;
	private ZootActor hurtActor;
	private ZootActor controllerActor;
	private LifeController lifeCtrl;			
	private HurtOnCollideController ctrl = new HurtOnCollideController();
		
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		
		controllerActor = new ZootActor();
		hurtActor = new ZootActor();		
		
		lifeCtrl = new LifeController();
		lifeCtrl.init(hurtActor);
		lifeCtrl.setMaxValue(ACTOR_LIFE);
		lifeCtrl.setValue(ACTOR_LIFE);
		hurtActor.addController(lifeCtrl);
		
		ctrl = new HurtOnCollideController();
		ctrl.init(controllerActor);
	}
		
	@Test
	public void shouldDecreaseActorLifeTest()
	{
		//when
		ControllerAnnotations.setControllerParameter(ctrl, "damage", DAMAGE);
		ctrl.onEnter(controllerActor, hurtActor, contact);
		
		//then
		assertEquals(ACTOR_LIFE - DAMAGE, lifeCtrl.getValue());
	}
	
	@Test
	public void shouldDecreaseActorLifeEachTimeWhenHeEntersCollisionTest()
	{
		//given
		final int life = 10;
		final int times = 5;
		lifeCtrl.setMaxValue(life);
		lifeCtrl.setValue(life);
		
		//when
		ControllerAnnotations.setControllerParameter(ctrl, "damage", DAMAGE);		
		for(int i = 1; i <= times; ++i)
		{
			ctrl.onEnter(controllerActor, hurtActor, contact);
			assertEquals(life - DAMAGE * i, lifeCtrl.getValue());
		}
	}
	
	@Test
	public void shouldSendHurtEventTest()
	{
		//given
		ZootActor hurtActor = mock(ZootActor.class);
		ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);		
		
		//when
		ctrl.onEnter(controllerActor, hurtActor, contact);
		
		//then		
		verify(hurtActor, times(1)).fire(captor.capture());
		assertTrue(ClassReflection.isInstance(ZootEvent.class, captor.getValue()));
	}
	
	@Test
	public void shouldNotInteractTest()
	{
		//given
		ZootActor hurtActor = mock(ZootActor.class);
		ZootActor ctrlActor = mock(ZootActor.class);
		
		//when
		ctrl.onLeave(hurtActor, ctrlActor, contact);
		
		//then
		verifyZeroInteractions(hurtActor, ctrlActor, contact);
	}
}
