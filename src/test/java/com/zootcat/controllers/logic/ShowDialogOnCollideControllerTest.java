package com.zootcat.controllers.logic;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.physics.box2d.Contact;
import com.zootcat.assets.ZootAssetManager;
import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.fsm.events.ZootActorEventCounterListener;
import com.zootcat.fsm.events.ZootEventType;
import com.zootcat.game.ZootGame;
import com.zootcat.gfx.ZootGraphicsFactory;
import com.zootcat.scene.ZootActor;
import com.zootcat.screen.ZootDialogScreen;
import com.zootcat.testing.ZootTestUtils;

public class ShowDialogOnCollideControllerTest
{
	@Mock private ZootGame game;
	@Mock private ZootActor controllerActor;
	@Mock private ZootAssetManager assetManager;
	@Mock private ZootGraphicsFactory graphicsFactory;
	@Rule public ExpectedException expectedEx = ExpectedException.none();
	
	private ZootActor otherActor;
	private ZootActorEventCounterListener eventCounter;
	private ShowDialogOnCollideController controller;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
				
		controller = new ShowDialogOnCollideController();
		ControllerAnnotations.setControllerParameter(controller, "game", game);
		ControllerAnnotations.setControllerParameter(controller, "assetManager", assetManager);
		ControllerAnnotations.setControllerParameter(controller, "graphicsFactory", graphicsFactory);
				
		String dialogPath = ZootTestUtils.getResourcePath("dialogs/TestDialog.dialog", this);
		ControllerAnnotations.setControllerParameter(controller, "path", dialogPath);
		ControllerAnnotations.setControllerParameter(controller, "token", "Test1");
		
		eventCounter = new ZootActorEventCounterListener();		
		otherActor = new ZootActor();
		otherActor.addListener(eventCounter);
		
		controller.init(controllerActor);
	}
			
	@Test
	public void shouldStopTriggeringActor()
	{
		controller.onEnter(otherActor, controllerActor, mock(Contact.class));
		
		assertEquals(1, eventCounter.getCount());
		assertEquals(ZootEventType.Stop, eventCounter.getLastZootEvent().getType());
	}
	
	@Test
	public void shouldEnterDialogOnlyOnce()
	{
		controller.onEnter(otherActor, controllerActor, mock(Contact.class));
		controller.onEnter(otherActor, controllerActor, mock(Contact.class));
		controller.onEnter(otherActor, controllerActor, mock(Contact.class));
		verify(game, times(1)).setScreen(any(ZootDialogScreen.class));
	}
	
	@Test
	public void shouldSetNewGameScreen()
	{
		controller.onEnter(otherActor, controllerActor, mock(Contact.class));
		verify(game).setScreen(any(ZootDialogScreen.class));
	}
	
	@Test
	public void shouldThrowIfNoPathIsGiven()
	{
		expectedEx.expect(RuntimeZootException.class);
		expectedEx.expectMessage("No dialog path was given for ShowDialogOnCollideController");		
	
		ControllerAnnotations.setControllerParameter(controller, "path", "");
		controller.onEnter(controllerActor, otherActor, mock(Contact.class));		
	}
	
	@Test
	public void shouldThrowIfNoTokenIsGiven()
	{
		ControllerAnnotations.setControllerParameter(controller, "token", "");
		
		expectedEx.expect(RuntimeZootException.class);
		expectedEx.expectMessage("No dialog token was given for ShowDialogOnCollideController");		
		controller.onEnter(controllerActor, otherActor, mock(Contact.class));
	}
	
	@Test
	public void shouldThrowIfDialogFileDoesNotExist()
	{
		expectedEx.expect(RuntimeZootException.class);
		expectedEx.expectMessage("File FakePath does not exist!");
		
		ControllerAnnotations.setControllerParameter(controller, "path", "FakePath");
		ControllerAnnotations.setControllerParameter(controller, "token", "FakeToken");		
		controller.onEnter(otherActor, controllerActor, mock(Contact.class));
	}
	
	@Test
	public void shouldDoNothingOnLeave()
	{
		Contact contact = mock(Contact.class);
		controller.onLeave(otherActor, controllerActor, contact);
		verifyZeroInteractions(controllerActor, contact);
	}
}
