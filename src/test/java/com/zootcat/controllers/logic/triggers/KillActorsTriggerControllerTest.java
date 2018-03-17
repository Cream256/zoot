package com.zootcat.controllers.logic.triggers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.zootcat.actions.ZootKillActorAction;
import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootScene;

public class KillActorsTriggerControllerTest
{
	private static final String ACTOR_TO_KILL_NAME = "Actor1";
	
	@Mock private ZootScene scene;
	@Mock private ZootActor ctrlActor;
	private ZootActor actorToKill;
	private KillActorsTriggerController ctrl;
			
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		ctrl = new KillActorsTriggerController();
		actorToKill = new ZootActor();
		
		ControllerAnnotations.setControllerParameter(ctrl, "scene", scene);
		ControllerAnnotations.setControllerParameter(ctrl, "actorName", ACTOR_TO_KILL_NAME);
		when(scene.getActors(any())).thenReturn(Arrays.asList(actorToKill));
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
	public void shouldDoNothingOnTriggerOff()
	{
		ctrl.triggerOff(ctrlActor);
		verifyZeroInteractions(ctrlActor);
	}
	
}
