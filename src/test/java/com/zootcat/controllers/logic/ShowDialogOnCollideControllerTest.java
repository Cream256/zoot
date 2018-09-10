package com.zootcat.controllers.logic;

import static org.mockito.Mockito.mock;
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
import com.zootcat.game.ZootGame;
import com.zootcat.scene.ZootActor;

public class ShowDialogOnCollideControllerTest
{
	@Mock private ZootGame game;
	@Mock private ZootActor otherActor;
	@Mock private ZootActor controllerActor;
	@Mock private ZootAssetManager assetManager;
	@Rule public ExpectedException expectedEx = ExpectedException.none();
	
	private ShowDialogOnCollideController controller;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
				
		controller = new ShowDialogOnCollideController();
		ControllerAnnotations.setControllerParameter(controller, "game", game);
		ControllerAnnotations.setControllerParameter(controller, "assetManager", assetManager);
		controller.init(controllerActor);
	}
		
	@Test
	public void shouldThrowIfNoPathIsGiven()
	{
		expectedEx.expect(RuntimeZootException.class);
		expectedEx.expectMessage("No dialog path was given for ShowDialogOnCollideController");		
		controller.onEnter(controllerActor, otherActor, mock(Contact.class));		
	}
	
	@Test
	public void shouldThrowIfNoTokenIsGiven()
	{
		ControllerAnnotations.setControllerParameter(controller, "path", "FakePath");
		
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
		verifyZeroInteractions(otherActor, controllerActor, contact);
	}
}
