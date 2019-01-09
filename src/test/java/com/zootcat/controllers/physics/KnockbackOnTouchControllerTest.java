package com.zootcat.controllers.physics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.zootcat.actions.ZootKnockbackAction;
import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.controllers.logic.LifeController;
import com.zootcat.scene.ZootActor;

public class KnockbackOnTouchControllerTest
{
	@Mock private ZootActor ctrlActor;
	@Mock private PhysicsBodyController otherActorPhysicsBodyCtrl;
	@Mock private PhysicsBodyController ctrlActorPhysicsBodyCtrl;
	
	private ZootActor otherActor;	
	private KnockbackOnTouchController ctrl;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		
		otherActor = new ZootActor();
		otherActor.addController(otherActorPhysicsBodyCtrl);
		
		when(ctrlActor.getSingleController(PhysicsBodyController.class)).thenReturn(ctrlActorPhysicsBodyCtrl);
				
		ctrl = new KnockbackOnTouchController();
		ctrl.init(ctrlActor);
	}
	
	@Test
	public void shouldSetDefaultParameters()
	{		
		assertFalse(ctrl.getVaryHorizontal());
		assertEquals(1.0f, ctrl.getKnockbackX(), 0.0f);
		assertEquals(1.0f, ctrl.getKnockbackY(), 0.0f);
	}
		
	@Test
	public void shouldAddKnockbackActionIfActorHasNoLifeController()
	{
		//given
		Fixture otherFixture = mock(Fixture.class);
		when(otherFixture.getUserData()).thenReturn(otherActor);
		
		//when		
		ctrl.onEnterCollision(otherFixture);
		
		//then
		assertEquals(1, otherActor.getActions().size);
		assertEquals(ZootKnockbackAction.class, otherActor.getActions().get(0).getClass());
	}
	
	@Test
	public void shouldNotAddKnockbackActionIfActorIsInvurnerable()
	{
		//given		
		LifeController lifeCtrl = new LifeController();
		lifeCtrl.setFrozen(true);
		otherActor.addController(lifeCtrl);
		
		Fixture otherFixture = mock(Fixture.class);
		when(otherFixture.getUserData()).thenReturn(otherActor);
		
		//when
		ControllerAnnotations.setControllerParameter(ctrl, "filterInvulnerable", true);
		ctrl.onEnterCollision(otherFixture);
		
		//then
		assertFalse(otherActor.hasActions());		
	}
	
	@Test
	public void shouldAddKnockbackActionIfActorIsInvurnerableAndWeDontFilterIt()
	{
		//given		
		LifeController lifeCtrl = new LifeController();
		lifeCtrl.setFrozen(true);
		otherActor.addController(lifeCtrl);
		
		Fixture otherFixture = mock(Fixture.class);
		when(otherFixture.getUserData()).thenReturn(otherActor);
		
		//when
		ControllerAnnotations.setControllerParameter(ctrl, "filterInvulnerable", false);
		ctrl.onEnterCollision(otherFixture);
		
		//then
		assertEquals(1, otherActor.getActions().size);
		assertEquals(ZootKnockbackAction.class, otherActor.getActions().get(0).getClass());		
	}
	
	@Test
	public void shouldAddKnockbackActionIfActorIsNotInvurnerable()
	{
		//given		
		LifeController lifeCtrl = new LifeController();
		lifeCtrl.setFrozen(false);
		otherActor.addController(lifeCtrl);
		
		Fixture otherFixture = mock(Fixture.class);
		when(otherFixture.getUserData()).thenReturn(otherActor);
		
		//when
		ControllerAnnotations.setControllerParameter(ctrl, "filterInvulnerable", true);
		ctrl.onEnterCollision(otherFixture);
		
		//then
		assertEquals(1, otherActor.getActions().size);
		assertEquals(ZootKnockbackAction.class, otherActor.getActions().get(0).getClass());		
	}
			
	@Test
	public void shouldSetKnockback()
	{
		//given
		final float expectedKnockbackX = 1.28f;
		final float expectedKnockbackY = -2.56f;
	
		//when
		ctrl.setKnockback(expectedKnockbackX, expectedKnockbackY);
		
		//then
		assertEquals(expectedKnockbackX, ctrl.getKnockbackX(), 0.0f);
		assertEquals(expectedKnockbackY, ctrl.getKnockbackY(), 0.0f);
	}
	
	@Test
	public void shouldSetVaryHorizontal()
	{
		ctrl.setVaryHorizontal(true);
		assertTrue(ctrl.getVaryHorizontal());
		
		ctrl.setVaryHorizontal(false);
		assertFalse(ctrl.getVaryHorizontal());
	}
}
