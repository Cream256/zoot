package com.zootcat.controllers.logic.triggers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.zootcat.actions.ZootKillActorAction;
import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.scene.ZootActor;
import com.zootcat.testing.ZootSceneMock;

public class KillActorsTriggerControllerTest
{
	private static final String ACTOR_TO_KILL_NAME = "Actor1";
	
	@Mock private ZootActor ctrlActor;
	private ZootSceneMock scene;
	private ZootActor actorToKill;
	private KillActorsTriggerController ctrl;
			
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		
		actorToKill = new ZootActor();	
		actorToKill.setName(ACTOR_TO_KILL_NAME);
		scene = new ZootSceneMock();
		scene.addActor(actorToKill);
		
		ctrl = new KillActorsTriggerController();
		ControllerAnnotations.setControllerParameter(ctrl, "scene", scene);
		ControllerAnnotations.setControllerParameter(ctrl, "actorName", ACTOR_TO_KILL_NAME);
	}
	
	@Test
	public void shouldKillActorOnTrigger()
	{
		//when		
		ctrl.triggerOn(ctrlActor);
		
		//then
		assertEquals(1, actorToKill.getActions().size);
		assertTrue(ClassReflection.isInstance(ZootKillActorAction.class, actorToKill.getActions().get(0)));		
		verifyZeroInteractions(ctrlActor);
	}
	
	@Test
	public void shouldNotKillActorWithInvalidName()
	{
		//given
		ZootActor otherActor = new ZootActor();
		otherActor.setName("other");
		
		//when		
		scene.removeActor(actorToKill);
		scene.addActor(otherActor);
		ctrl.triggerOn(ctrlActor);
		
		//then
		assertEquals(0, actorToKill.getActions().size);
		assertEquals(0, otherActor.getActions().size);		
		verifyZeroInteractions(ctrlActor);
	}
	
	@Test
	public void shouldDoNothingOnTriggerOff()
	{
		ctrl.triggerOff(ctrlActor);
		verifyZeroInteractions(ctrlActor);
	}
}
