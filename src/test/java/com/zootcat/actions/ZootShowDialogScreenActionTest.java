package com.zootcat.actions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.function.Consumer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.zootcat.assets.ZootAssetManager;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.fsm.events.ZootActorEventCounterListener;
import com.zootcat.game.ZootGame;
import com.zootcat.gfx.ZootGraphicsFactory;
import com.zootcat.scene.ZootActor;
import com.zootcat.screen.ZootDialogScreen;
import com.zootcat.testing.ZootTestUtils;

public class ZootShowDialogScreenActionTest
{
	private static final String DIALOG_TOKEN = "Test1";
	
	@Mock private ZootGame game;
	@Mock private ZootAssetManager assetManager;
	@Mock private ZootGraphicsFactory graphicsFactory;
	@Rule public ExpectedException expectedEx = ExpectedException.none();
	
	private String dialogPath;
	private ZootActor triggeringActor;
	private ZootActorEventCounterListener eventCounter;
	private ZootShowDialogScreenAction action;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		when(game.getAssetManager()).thenReturn(assetManager);
		when(game.getGraphicsFactory()).thenReturn(graphicsFactory);
						
		eventCounter = new ZootActorEventCounterListener();		
		triggeringActor = new ZootActor();
		triggeringActor.addListener(eventCounter);
		
		dialogPath = ZootTestUtils.getResourcePath("dialogs/TestDialog.dialog", this);
		
		action = new ZootShowDialogScreenAction();
		action.setZootGame(game);
		action.setDialogPath(dialogPath);
		action.setDialogToken(DIALOG_TOKEN);
		action.setTarget(triggeringActor);
	}
	
	@Test
	public void shouldSetZootGame()
	{
		assertEquals(game, action.getZootGame());
	}
	
	@Test
	public void shouldSetDialogPath()
	{
		assertEquals(dialogPath, action.getDialogPath());
	}
	
	@Test
	public void shouldSetDialogToken()
	{
		assertEquals(DIALOG_TOKEN, action.getDialogToken());
	}
			
	@Test
	public void shouldSetDialogScreenForGame()
	{
		//given
		ArgumentCaptor<ZootDialogScreen> captor = ArgumentCaptor.forClass(ZootDialogScreen.class);
		
		//when
		action.act(0.0f);
		
		//then
		verify(game).setScreen(captor.capture());
		assertNotNull(captor.getValue());
		assertNotNull(captor.getValue().getDialog());
		assertEquals(triggeringActor, captor.getValue().getTriggeringActor());
	}
	
	@Test
	public void shouldEndActionWhenDialogEnds()
	{		
		//given
		ArgumentCaptor<ZootDialogScreen> captor = ArgumentCaptor.forClass(ZootDialogScreen.class);
		
		//when		
		assertFalse("Action should not end when dialog is created", action.act(0.0f));
		
		//then
		verify(game).setScreen(captor.capture());
		
		//when
		when(game.getScreen()).thenReturn(captor.getValue());
		
		//then
		assertFalse("Action should not end while dialog is running", action.act(0.0f));
		
		//when
		when(game.getScreen()).thenReturn(null);
		
		//then
		assertTrue("Action should end after dialog ends", action.act(0.0f));		
	}
	
	@Test
	public void shouldThrowIfNoPathIsGiven()
	{
		expectedEx.expect(RuntimeZootException.class);
		expectedEx.expectMessage("No dialog path was given for ZootShowDialogScreenAction");		
	
		action.setDialogPath(null);
		action.act(0.0f);
	}
	
	@Test
	public void shouldThrowIfPathIsEmpty()
	{
		expectedEx.expect(RuntimeZootException.class);
		expectedEx.expectMessage("No dialog path was given for ZootShowDialogScreenAction");		
	
		action.setDialogPath("");
		action.act(0.0f);
	}
	
	@Test
	public void shouldThrowIfNoTokenIsGiven()
	{
		expectedEx.expect(RuntimeZootException.class);
		expectedEx.expectMessage("No dialog token was given for ZootShowDialogScreenAction");		
		
		action.setDialogToken(null);
		action.act(0.0f);
	}
	
	@Test
	public void shouldThrowIfTokenIsEmpty()
	{
		expectedEx.expect(RuntimeZootException.class);
		expectedEx.expectMessage("No dialog token was given for ZootShowDialogScreenAction");		
		
		action.setDialogToken("");
		action.act(0.0f);
	}
	
	@Test
	public void shouldThrowIfDialogFileDoesNotExist()
	{
		expectedEx.expect(RuntimeZootException.class);
		expectedEx.expectMessage("File FakePath does not exist!");
		
		action.setDialogPath("FakePath");
		action.setDialogToken("FakeToken");
		action.act(0.0f);
	}
	
	@Test
	public void shouldThrowIfNoGameIsGiven()
	{
		expectedEx.expect(RuntimeZootException.class);
		expectedEx.expectMessage("No zoot game was given for ZootShowDialogScreenAction");		
		
		action.setZootGame(null);
		action.act(0.0f);
	}
		
	@Test
	public void shouldReset()
	{
		//given
		action.setOnShowAction(game -> {});
		action.setOnHideAction(game -> {});
		
		//when
		action.reset();
		
		//then
		assertNull(action.getTargetZootActor());
		assertNull(action.getDialogToken());
		assertNull(action.getDialogPath());
		assertNull(action.getZootGame());
		assertNull(action.getOnShowAction());
		assertNull(action.getOnHideAction());
	}
	
	@Test
	public void shouldSetOnShowAction()
	{
		//given
		Consumer<ZootGame> onShowAction = game -> {};
		
		//when
		action.setOnShowAction(onShowAction);
		
		//then
		assertEquals(onShowAction, action.getOnShowAction());
	}
	
	@Test
	public void shouldSetOnHideAction()
	{
		//given
		Consumer<ZootGame> onHideAction = game -> {};
		
		//when
		action.setOnHideAction(onHideAction);
		
		//then
		assertEquals(onHideAction, action.getOnHideAction());		
	}
}
