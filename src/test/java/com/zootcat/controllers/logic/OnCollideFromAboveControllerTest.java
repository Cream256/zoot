package com.zootcat.controllers.logic;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.zootcat.scene.ZootActor;

public class OnCollideFromAboveControllerTest
{
	private int aboveCollisionCount;
	private int belowCollisionCount;
	private Filter ctrlActorFilter;
	private Filter otherActorFilter;
	private PolygonShape ctrlShape;
	private PolygonShape otherShape;
	private OnCollideFromAboveOrBelowController ctrl;
	
	@Mock private ZootActor ctrlActor;
	@Mock private ZootActor otherActor;
	@Mock private Fixture ctrlFixture;
	@Mock private Fixture otherFixture;
	@Mock private Contact contact;	
	@Mock private Body ctrlBody;
	@Mock private Body otherBody;
	
	@BeforeClass
	public static void setupClass()
	{
		Box2D.init();
	}
		
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		aboveCollisionCount = 0;
		belowCollisionCount = 0;
		ctrlActorFilter = new Filter();
		otherActorFilter = new Filter();	
		ctrlShape = new PolygonShape();
		ctrlShape.setAsBox(5, 5);
		otherShape = new PolygonShape();		
		otherShape.setAsBox(5, 5);
		
		ctrl = new OnCollideFromAboveOrBelowController() {
			@Override
			public void onCollidedFromAbove(ZootActor actorA, ZootActor actorB, Contact contact)
			{
				++aboveCollisionCount;
			}

			@Override
			public void onCollidedFromBelow(ZootActor actorA, ZootActor actorB, Contact contact)
			{
				++belowCollisionCount;
			}
		};
			
		when(ctrlFixture.getBody()).thenReturn(ctrlBody);
		when(ctrlFixture.getShape()).thenReturn(ctrlShape);
		when(ctrlFixture.getFilterData()).thenReturn(ctrlActorFilter);
		when(otherFixture.getShape()).thenReturn(otherShape);
		when(otherFixture.getFilterData()).thenReturn(otherActorFilter);
		when(otherFixture.getBody()).thenReturn(otherBody);
		when(contact.getFixtureA()).thenReturn(ctrlFixture);		
		when(contact.getFixtureB()).thenReturn(otherFixture);
		when(ctrlBody.getType()).thenReturn(BodyType.StaticBody);
		when(otherBody.getType()).thenReturn(BodyType.DynamicBody);
	}
	
	@After
	public void tearDown()
	{
		ctrlShape.dispose();
		ctrlShape = null;
		
		otherShape.dispose();
		otherShape = null;
	}
	
	@Test
	public void beginContactWithBodyOnTopTest()
	{
		//given				
		Vector2 ctrlBodyPosition = new Vector2(0.0f, 0.0f);
		when(ctrlBody.getPosition()).thenReturn(ctrlBodyPosition);		
		
		Vector2 otherBodyPosition = new Vector2(0.0f, 10.0f);
		when(otherBody.getPosition()).thenReturn(otherBodyPosition);
		
		//when
		ctrl.init(ctrlActor);		
		ctrl.beginContact(ctrlActor, otherActor, contact);

		//then
		assertEquals("Should accept above collision", 1, aboveCollisionCount);
		assertEquals("Should not accept below collision", 0, belowCollisionCount);
	}
	
	@Test
	public void beginContactWithBodyBottomInTheMiddleOfCtrlBodyTest()
	{
		//given				
		Vector2 ctrlBodyPosition = new Vector2(0.0f, 0.0f);
		when(ctrlBody.getPosition()).thenReturn(ctrlBodyPosition);		
		
		Vector2 otherBodyPosition = new Vector2(0.0f, 5.0f);
		when(otherBody.getPosition()).thenReturn(otherBodyPosition);
		
		//when
		ctrl.init(ctrlActor);		
		ctrl.beginContact(ctrlActor, otherActor, contact);
			
		//then
		assertEquals("Should accept above collision", 1, aboveCollisionCount);
		assertEquals("Should not accept below collision", 0, belowCollisionCount);
	}
	
	@Test
	public void beginContactWithBodyBottomAfterTheMiddleOfCtrlBodyTest()
	{
		//given				
		Vector2 ctrlBodyPosition = new Vector2(0.0f, 0.0f);
		when(ctrlBody.getPosition()).thenReturn(ctrlBodyPosition);		
		
		Vector2 otherBodyPosition = new Vector2(0.0f, 4.5f);
		when(otherBody.getPosition()).thenReturn(otherBodyPosition);
		
		//when
		ctrl.init(ctrlActor);		
		ctrl.beginContact(ctrlActor, otherActor, contact);
			
		//then
		assertEquals("Should not accept above collision", 0, aboveCollisionCount);
		assertEquals("Should accept below collision", 1, belowCollisionCount);
	}
	
	@Test
	public void beginContactWithDynamicBodyAsControllerBodyTest()
	{
		//given
		when(ctrlBody.getType()).thenReturn(BodyType.DynamicBody);
		Vector2 ctrlBodyPosition = new Vector2(0.0f, 0.0f);
		when(ctrlBody.getPosition()).thenReturn(ctrlBodyPosition);		
		
		Vector2 otherBodyPosition = new Vector2(0.0f, 10.0f);
		when(otherBody.getPosition()).thenReturn(otherBodyPosition);
		
		//when
		ctrl.init(ctrlActor);		
		ctrl.beginContact(ctrlActor, otherActor, contact);
		
		//then
		assertEquals("Should accept above collision", 1, aboveCollisionCount);
		assertEquals("Should not accept below collision", 0, belowCollisionCount);
		
		//when
		ctrlBodyPosition.y += 10.0f;
		ctrl.beginContact(ctrlActor, otherActor, contact);
		
		//then
		assertEquals("Should not accept above collision", 1, aboveCollisionCount);
		assertEquals("Should accept below collision", 1, belowCollisionCount);
	}
	
	@Test
	public void onLeaveTest()
	{
		ctrl.onLeave(ctrlActor, otherActor, contact);
		verifyZeroInteractions(ctrlActor, otherActor, contact);
	}
	
}
