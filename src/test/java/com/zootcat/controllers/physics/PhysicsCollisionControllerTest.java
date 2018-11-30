package com.zootcat.controllers.physics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.zootcat.controllers.ControllerPriority;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.scene.ZootActor;

public class PhysicsCollisionControllerTest
{
	private PhysicsCollisionController ctrl;
	private boolean onPreSolveCalled;
	private boolean onPostSolveCalled;
	private boolean onEndContactCalled;
	private boolean onBeginContactCalled;
	
	@Before
	public void setup()
	{
		onPreSolveCalled = false;
		onPostSolveCalled = false;
		onEndContactCalled = false;
		onBeginContactCalled = false;
		ctrl = new PhysicsCollisionController()
		{
			@Override
			public void onPreSolve(ZootActor actorA, ZootActor actorB, Contact contact, Manifold manifold)
			{
				onPreSolveCalled = true;
			}
			
			@Override
			public void onPostSolve(ZootActor actorA, ZootActor actorB, ContactImpulse contactImpulse)
			{
				onPostSolveCalled = true;
			}
			
			@Override
			public void onEndContact(ZootActor actorA, ZootActor actorB, Contact contact)
			{
				onEndContactCalled = true;
			}
			
			@Override
			public void onBeginContact(ZootActor actorA, ZootActor actorB, Contact contact)
			{
				onBeginContactCalled = true;
			}
		};
	}
	
	@Test(expected = RuntimeZootException.class)
	public void shouldThrowWhenControllerIsNotInitialized()
	{
		assertNull(ctrl.getControllerActor());
	}
	
	@Test
	public void shouldSetControllerActorOnInit()
	{
		//given
		ZootActor actor = mock(ZootActor.class);
		
		//when
		ctrl.init(actor);
		
		//then
		assertEquals(actor, ctrl.getControllerActor());
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
	
	@Test
	public void shouldBeEnabledByDefault()
	{
		assertTrue(ctrl.isEnabled());
	}
	
	@Test
	public void shouldSetEnabled()
	{
		ctrl.setEnabled(false);
		assertFalse(ctrl.isEnabled());
		
		ctrl.setEnabled(true);
		assertTrue(ctrl.isEnabled());
	}
	
	@Test
	public void shouldCallBeginContactWhenEnabled()
	{
		//given
		ctrl.setEnabled(true);
		
		//when
		ctrl.beginContact(mock(ZootActor.class), mock(ZootActor.class), mock(Contact.class));
		
		//then
		assertTrue(onBeginContactCalled);		
	}
	
	@Test
	public void shouldNotCallBeginContactWhenDisabled()
	{
		//given
		ctrl.setEnabled(false);
		
		//when
		ctrl.beginContact(mock(ZootActor.class), mock(ZootActor.class), mock(Contact.class));
		
		//then
		assertFalse(onBeginContactCalled);		
	}
	
	@Test
	public void shouldCallEndContactWhenEnabled()
	{
		//given
		ctrl.setEnabled(true);
		
		//when
		ctrl.endContact(mock(ZootActor.class), mock(ZootActor.class), mock(Contact.class));
		
		//then
		assertTrue(onEndContactCalled);		
	}
	
	@Test
	public void shouldNotCallEndContactWhenDisabled()
	{
		//given
		ctrl.setEnabled(false);
		
		//when
		ctrl.endContact(mock(ZootActor.class), mock(ZootActor.class), mock(Contact.class));
		
		//then
		assertFalse(onEndContactCalled);		
	}
	
	@Test
	public void shouldCallPreSolveWhenEnabled()
	{
		//given
		ctrl.setEnabled(true);
		
		//when
		ctrl.preSolve(mock(ZootActor.class), mock(ZootActor.class), mock(Contact.class), mock(Manifold.class));
		
		//then
		assertTrue(onPreSolveCalled);
	}
	
	@Test
	public void shouldNotCallPreSolveWhenDisabled()
	{
		//given
		ctrl.setEnabled(false);
		
		//when
		ctrl.preSolve(mock(ZootActor.class), mock(ZootActor.class), mock(Contact.class), mock(Manifold.class));
		
		//then
		assertFalse(onPreSolveCalled);
	}
	
	@Test
	public void shouldCallPostSolveWhenEnabled()
	{
		//given
		ctrl.setEnabled(true);
		
		//when
		ctrl.postSolve(mock(ZootActor.class), mock(ZootActor.class), mock(ContactImpulse.class));
		
		//then
		assertTrue(onPostSolveCalled);
	}
	
	@Test
	public void shouldNotCallPostSolveWhenDisabled()
	{
		//given
		ctrl.setEnabled(false);
		
		//when
		ctrl.postSolve(mock(ZootActor.class), mock(ZootActor.class), mock(ContactImpulse.class));
		
		//then
		assertFalse(onPostSolveCalled);
	}
	
	@Test
	public void shouldReturnOtherActor()
	{
		//given
		ZootActor otherActor = mock(ZootActor.class);
		ZootActor ctrlActor = mock(ZootActor.class);
		
		//when
		ctrl.init(ctrlActor);
		
		//then
		assertEquals(otherActor, ctrl.getOtherActor(ctrlActor, otherActor));
		assertEquals(otherActor, ctrl.getOtherActor(otherActor, ctrlActor));
	}
	
	@Test
	public void shouldReturnControllerActorFixture()
	{
		//given
		ZootActor otherActor = mock(ZootActor.class);
		ZootActor ctrlActor = mock(ZootActor.class);
		Fixture otherActorFixture = mock(Fixture.class);
		Fixture ctrlActorFixture = mock(Fixture.class);		
		Contact contact = mock(Contact.class);
		
		//when
		when(contact.getFixtureA()).thenReturn(ctrlActorFixture);
		when(contact.getFixtureB()).thenReturn(otherActorFixture);
		ctrl.init(ctrlActor);
		
		//then
		assertEquals(ctrlActorFixture, ctrl.getControllerActorFixture(ctrlActor, otherActor, contact));
		
		//when
		when(contact.getFixtureA()).thenReturn(otherActorFixture);
		when(contact.getFixtureB()).thenReturn(ctrlActorFixture);
		
		//then
		assertEquals(ctrlActorFixture, ctrl.getControllerActorFixture(otherActor, ctrlActor, contact));
	}
	
	@Test
	public void shouldReturnOtherActorFixture()
	{
		//given
		ZootActor otherActor = mock(ZootActor.class);
		ZootActor ctrlActor = mock(ZootActor.class);
		Fixture otherActorFixture = mock(Fixture.class);
		Fixture ctrlActorFixture = mock(Fixture.class);		
		Contact contact = mock(Contact.class);
		
		//when
		when(contact.getFixtureA()).thenReturn(ctrlActorFixture);
		when(contact.getFixtureB()).thenReturn(otherActorFixture);
		ctrl.init(ctrlActor);
		
		//then
		assertEquals(otherActorFixture, ctrl.getOtherFixture(ctrlActor, otherActor, contact));
		
		//when
		when(contact.getFixtureA()).thenReturn(otherActorFixture);
		when(contact.getFixtureB()).thenReturn(ctrlActorFixture);
		
		//then
		assertEquals(otherActorFixture, ctrl.getOtherFixture(otherActor, ctrlActor, contact));		
	}
	
}
