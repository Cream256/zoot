package com.zootcat.controllers.physics;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.scene.ZootActor;
import com.zootcat.utils.BitMaskConverter;

public class OnCollideControllerTest
{
	protected int enterCount = 0;
	protected int leaveCount = 0;
	protected OnCollideController ctrl;
	protected Filter ctrlActorFilter;
	protected Filter otherActorFilter;
	@Mock protected ZootActor ctrlActor;
	@Mock protected ZootActor otherActor;
	@Mock protected Fixture ctrlActorFixture;
	@Mock protected Fixture otherActorFixture;
	@Mock protected Fixture otherActorFixture2;
	@Mock protected Contact contact;
		
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		enterCount = 0;
		leaveCount = 0;
		ctrlActorFilter = new Filter();
		otherActorFilter = new Filter();		
		ctrl = new OnCollideController(){
			@Override
			public void onEnter(ZootActor actorA, ZootActor actorB, Contact contact)
			{
				++enterCount;
			}

			@Override
			public void onLeave(ZootActor actorA, ZootActor actorB, Contact contact)
			{
				++leaveCount;
			}};
						
		when(contact.getFixtureA()).thenReturn(ctrlActorFixture);
		when(contact.getFixtureB()).thenReturn(otherActorFixture);
		when(ctrlActorFixture.getFilterData()).thenReturn(ctrlActorFilter);
		when(otherActorFixture.getFilterData()).thenReturn(otherActorFilter);
		when(otherActorFixture2.getFilterData()).thenReturn(otherActorFilter);
		
		BitMaskConverter.Instance.clear();
	}
	
	@Test
	public void shouldReturnControllerActor()
	{
		//when
		ctrl.init(ctrlActor);
		
		//then
		assertEquals(ctrlActor, ctrl.getControllerActor());
	}
	
	@Test
	public void shouldCollideWithAllWhenMaskIsNotGiven()
	{
		//given
		ctrlActorFilter.categoryBits = 1;
		otherActorFilter.categoryBits = 2;
		
		ControllerAnnotations.setControllerParameter(ctrl, "category", "ABC");
		ControllerAnnotations.setControllerParameter(ctrl, "mask", null);
		ctrl.init(ctrlActor);
		
		//when
		ctrl.beginContact(ctrlActor, otherActor, contact);		
		
		//then
		assertEquals("Should match collision", 1, enterCount);
	}
	
	@Test
	public void shouldBeginAndEndCollisionOnlyOncePerActorEvenIfActorBodyHasMultiplyFixtures()
	{
		//given
		ctrl.init(ctrlActor);
		
		//when
		when(contact.getFixtureA()).thenReturn(ctrlActorFixture);
		when(contact.getFixtureB()).thenReturn(otherActorFixture);
		ctrl.beginContact(ctrlActor, otherActor, contact);
		
		//then
		assertEquals("Collision should begin", 1, enterCount);
		
		//when
		when(contact.getFixtureB()).thenReturn(otherActorFixture2);
		ctrl.beginContact(ctrlActor, otherActor, contact);
		
		//then
		assertEquals("Collision with already colliding actor should not be matched twice", 1, enterCount);
		
		//when
		when(contact.getFixtureB()).thenReturn(otherActorFixture);
		ctrl.endContact(ctrlActor, otherActor, contact);
		
		//then
		assertEquals("Collision should end", 1, leaveCount);
	}
		
	@Test
	public void shouldCollideWithCategoriesInMask()
	{
		//given
		final short categoryA = BitMaskConverter.Instance.fromString("A");
		final short categoryB = BitMaskConverter.Instance.fromString("B");
		final short categoryC = BitMaskConverter.Instance.fromString("C");
		
		ControllerAnnotations.setControllerParameter(ctrl, "category", "A");
		ControllerAnnotations.setControllerParameter(ctrl, "mask", "B | C");
		ctrl.init(ctrlActor);
		
		//when
		ctrlActorFilter.categoryBits = categoryA;
		otherActorFilter.categoryBits = categoryB;
		
		//then
		ctrl.beginContact(ctrlActor, otherActor, contact);
		ctrl.endContact(ctrlActor, otherActor, contact);
		assertEquals("Should collide on begin contact", 1, enterCount);
		assertEquals("Should collide on end contact", 1, leaveCount);
		
		//when
		otherActorFilter.categoryBits = categoryC;
		
		//then
		ctrl.beginContact(ctrlActor, otherActor, contact);
		ctrl.endContact(ctrlActor, otherActor, contact);
		assertEquals("Should collide on begin contact", 2, enterCount);
		assertEquals("Should collide on end contact", 2, leaveCount);
	}
	
	@Test
	public void shouldNotCollideWithCategoriesNotInMask()
	{
		//given
		final short categoryA = BitMaskConverter.Instance.fromString("A");
		final short categoryB = BitMaskConverter.Instance.fromString("B");
		final short categoryC = BitMaskConverter.Instance.fromString("C");
		
		ControllerAnnotations.setControllerParameter(ctrl, "category", "A");
		ControllerAnnotations.setControllerParameter(ctrl, "mask", "D | F");
		ctrl.init(ctrlActor);
		
		//when
		ctrlActorFilter.categoryBits = categoryA;
		otherActorFilter.categoryBits = categoryA;
		
		//then
		ctrl.beginContact(ctrlActor, otherActor, contact);
		ctrl.endContact(ctrlActor, otherActor, contact);
		assertEquals("Should not collide on begin contact", 0, enterCount);
		assertEquals("Should not collide on end contact", 0, leaveCount);
		
		//when
		otherActorFilter.categoryBits = categoryB;
		
		//then
		ctrl.beginContact(ctrlActor, otherActor, contact);
		ctrl.endContact(ctrlActor, otherActor, contact);
		assertEquals("Should not collide on begin contact", 0, enterCount);
		assertEquals("Should not collide on end contact", 0, leaveCount);
		
		//when
		otherActorFilter.categoryBits = categoryC;
		
		//then
		ctrl.beginContact(ctrlActor, otherActor, contact);
		ctrl.endContact(ctrlActor, otherActor, contact);
		assertEquals("Should not collide on begin contact", 0, enterCount);
		assertEquals("Should not collide on end contact", 0, leaveCount);
	}
	
	@Test
	public void shouldNotCollideIfOtherFixtureDoesNotMaskActorCategory()
	{
		//given
		final short categoryA = BitMaskConverter.Instance.fromString("A");
		final short categoryB = BitMaskConverter.Instance.fromString("B");
		
		ControllerAnnotations.setControllerParameter(ctrl, "category", "A");
		ControllerAnnotations.setControllerParameter(ctrl, "mask", "B");
		ctrl.init(ctrlActor);
		
		//when
		ctrlActorFilter.categoryBits = categoryA;		
		otherActorFilter.categoryBits = categoryB;
		otherActorFilter.maskBits = BitMaskConverter.Instance.fromString("B | C");
		
		//then
		ctrl.beginContact(ctrlActor, otherActor, contact);
		ctrl.endContact(ctrlActor, otherActor, contact);
		assertEquals("Should not collide on begin contact", 0, enterCount);
		assertEquals("Should not collide on end contact", 0, leaveCount);
	}
	
	@Test
	public void shouldNotCollideWithSensors()
	{
		//given
		ControllerAnnotations.setControllerParameter(ctrl, "collideWithSensors", false);
		ctrl.init(ctrlActor);
		
		//when
		when(otherActorFixture.isSensor()).thenReturn(true);
		ctrl.beginContact(ctrlActor, otherActor, contact);
		ctrl.endContact(ctrlActor, otherActor, contact);
		
		//then
		assertEquals("Should not begin collision with sensor fixture", 0, enterCount);
		assertEquals("Should not end collision with sensor fixture", 0, leaveCount);
	}
	
	@Test
	public void shouldCollideWithSensors()
	{
		//given
		ControllerAnnotations.setControllerParameter(ctrl, "collideWithSensors", true);
		ctrl.init(ctrlActor);
		
		//when
		when(otherActorFixture.isSensor()).thenReturn(true);
		ctrl.beginContact(ctrlActor, otherActor, contact);
		ctrl.endContact(ctrlActor, otherActor, contact);
		
		//then
		assertEquals("Should begin collision with sensor fixture", 1, enterCount);
		assertEquals("Should end collision with sensor fixture", 1, leaveCount);
	}
				
	@Test
	public void shouldDoNothingOnPreSolve()
	{
		ZootActor actorA = mock(ZootActor.class);
		ZootActor actorB = mock(ZootActor.class);
		Contact contact =  mock(Contact.class);
		Manifold manifold = mock(Manifold.class);
					
		ctrl.preSolve(actorA, actorB, contact, manifold);
		verifyZeroInteractions(actorA, actorB, contact, manifold);
	}
	
	@Test
	public void shouldDoNothingOnPostSolve()
	{
		ZootActor actorA = mock(ZootActor.class);
		ZootActor actorB = mock(ZootActor.class);
		ContactImpulse contactImpulse = mock(ContactImpulse.class);
				
		ctrl.postSolve(actorA, actorB, contactImpulse);
		verifyZeroInteractions(actorA, actorB, contactImpulse);
	}
}
