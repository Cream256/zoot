package com.zootcat.controllers.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.util.function.Consumer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.physics.box2d.Contact;
import com.zootcat.actions.ZootShowDialogScreenAction;
import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.game.ZootGame;
import com.zootcat.scene.ZootActor;

public class ShowDialogOnCollideControllerTest
{
	private static final String DIALOG_PATH = "/data/dialog.txt";
	private static final String DIALOG_TOKEN = "Token";
	
	@Mock private ZootGame game;
	@Mock private ZootActor controllerActor;
	
	private ZootActor triggeringActor;
	private ShowDialogOnCollideController controller;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		triggeringActor = new ZootActor();
		
		controller = new ShowDialogOnCollideController();
		ControllerAnnotations.setControllerParameter(controller, "game", game);				
		ControllerAnnotations.setControllerParameter(controller, "path", DIALOG_PATH);
		ControllerAnnotations.setControllerParameter(controller, "token", DIALOG_TOKEN);
		controller.init(controllerActor);		
	}

	@Test
	public void shouldSetActionOnTriggeringActor()
	{
		controller.onEnter(triggeringActor, controllerActor, mock(Contact.class));
		
		assertEquals(1, triggeringActor.getActions().size);
		ZootShowDialogScreenAction action = (ZootShowDialogScreenAction) triggeringActor.getActions().get(0);
		assertEquals(triggeringActor, action.getTargetZootActor());
		assertEquals(DIALOG_PATH, action.getDialogPath());
		assertEquals(DIALOG_TOKEN, action.getDialogToken());
		assertEquals(game, action.getZootGame());
	}
		
	@Test
	public void shouldSetActionOnTriggeringActorOnlyOnce()
	{
		controller.onEnter(triggeringActor, controllerActor, mock(Contact.class));
		controller.onEnter(triggeringActor, controllerActor, mock(Contact.class));
		controller.onEnter(triggeringActor, controllerActor, mock(Contact.class));
		assertEquals(1, triggeringActor.getActions().size);
	}
			
	@Test
	public void shouldDoNothingOnLeave()
	{
		Contact contact = mock(Contact.class);
		controller.onLeave(triggeringActor, controllerActor, contact);
		verifyZeroInteractions(controllerActor, contact);
	}
	
	@Test
	public void shouldHaveNullShowAndHideActionsByDefault()
	{
		assertNull(controller.getOnShowAction());
		assertNull(controller.getOnHideAction());
	}
	
	@Test
	public void shouldSetOnShowAction()
	{
		Consumer<ZootGame> action = game -> {};		
		controller.setOnShowAction(action);
		
		assertEquals(action, controller.getOnShowAction());		
	}
	
	@Test
	public void shouldSetOnHideAction()
	{
		Consumer<ZootGame> action = game -> {};		
		controller.setOnHideAction(action);
		
		assertEquals(action, controller.getOnHideAction());		
	}
}
