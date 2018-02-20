package com.zootcat.controllers.physics;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.zootcat.controllers.ControllerPriority;
import com.zootcat.scene.ZootActor;

public class PhysicsCollisionControllerTest
{
	private PhysicsCollisionController ctrl;

	@Before
	public void setup()
	{
		ctrl = new PhysicsCollisionController()
		{
			@Override
			public void preSolve(ZootActor actorA, ZootActor actorB, Contact contact, Manifold manifold)
			{
				//noop
			}
			
			@Override
			public void postSolve(ZootActor actorA, ZootActor actorB, ContactImpulse contactImpulse)
			{
				//noop
			}
			
			@Override
			public void endContact(ZootActor actorA, ZootActor actorB, Contact contact)
			{
				//noop
			}
			
			@Override
			public void beginContact(ZootActor actorA, ZootActor actorB, Contact contact)
			{
				//noop
			}
		};
	}
	
	@Test
	public void shouldDoNothingOnInit()
	{
		//given
		ZootActor actor = mock(ZootActor.class);
		
		//when
		ctrl.init(actor);
		
		//then
		verifyZeroInteractions(actor);
	}
	
	@Test
	public void shouldAddListenerToActor()
	{
		//given
		ZootActor actor = new ZootActor();
		
		//when
		ctrl.onAdd(actor);
		
		//then
		assertTrue(actor.getListeners().contains(ctrl, true));
	}
	
	@Test
	public void shouldRemoveListenerFromActor()
	{
		//given
		ZootActor actor = new ZootActor();
		actor.addListener(ctrl);
		
		//when
		ctrl.onRemove(actor);
		
		//then
		assertFalse(actor.getListeners().contains(ctrl, true));
	}
	
	@Test
	public void shouldDoNothingOnUpdate()
	{
		//given
		ZootActor actor = mock(ZootActor.class);
		
		//when
		ctrl.onUpdate(0.0f, actor);
		
		//then
		verifyZeroInteractions(actor);
	}
	
	@Test
	public void shouldReturnNormalPriority()
	{
		assertEquals(ControllerPriority.Normal, ctrl.getPriority());
	}
}
