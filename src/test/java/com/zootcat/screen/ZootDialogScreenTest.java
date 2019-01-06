package com.zootcat.screen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.function.Consumer;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.zootcat.dialogs.ZootDialog;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.game.ZootGame;
import com.zootcat.gfx.ZootGraphicsFactory;
import com.zootcat.input.ZootInputManager;
import com.zootcat.scene.ZootActor;

public class ZootDialogScreenTest
{	
	@Mock private ZootGame game;
	@Mock private ZootScreen previousScreen;
	@Mock private SpriteBatch spriteBatch;
	@Mock private ZootGraphicsFactory spriteFactory;	
	@Rule public ExpectedException expectedEx = ExpectedException.none();
	private boolean actionExecuted;
	
	private ZootDialogScreen dialogScreen;
		
	@BeforeClass
	public static void setupClass()
	{
		Gdx.graphics = mock(Graphics.class);
	}
	
	@AfterClass
	public static void tearDownClass()
	{
		Gdx.graphics = null;
	}
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		when(spriteFactory.createShapeRenderer()).thenReturn(mock(ShapeRenderer.class));
		when(spriteFactory.createSpriteBatch()).thenReturn(spriteBatch);
		when(spriteFactory.createBitmapFont()).thenReturn(mock(BitmapFont.class));
		when(game.getGraphicsFactory()).thenReturn(spriteFactory);
		when(game.getPreviousScreen()).thenReturn(previousScreen);
		when(game.getInputManager()).thenReturn(mock(ZootInputManager.class));		
		
		actionExecuted = false;
		dialogScreen = new ZootDialogScreen(game);
	}
		
	@Test
	public void shouldRenderPreviousScreenFirst()
	{				
		dialogScreen.setDialog(mock(ZootDialog.class));
		dialogScreen.show();
		dialogScreen.render(1.0f);
		verify(previousScreen).onRender(1.0f);		
	}
	
	@Test
	public void shuldThrowIfNoDialogIsSet()
	{
		expectedEx.expect(RuntimeZootException.class);
		expectedEx.expectMessage("No dialog was set for DialogScreen");
		
		dialogScreen.show();
	}
	
	@Test
	public void shouldThrowIfNoPreviousScreenWasSet()
	{
		//given
		expectedEx.expect(RuntimeZootException.class);
		expectedEx.expectMessage("No previous screen was set for DialogScreen");
	
		//when
		when(game.getPreviousScreen()).thenReturn(null);		
		dialogScreen.setDialog(mock(ZootDialog.class));
		dialogScreen.show();
		
		//then throw
	}
	
	@Test
	public void shouldSetTriggeringActor()
	{
		//given
		ZootActor actor = mock(ZootActor.class);
		
		//then
		assertNull(dialogScreen.getTriggeringActor());
		
		//when	
		dialogScreen.setTriggeringActor(actor);
		
		//then
		assertEquals(actor, dialogScreen.getTriggeringActor());
	}
	
	@Test
	public void shouldSetDialog()
	{
		//given
		ZootDialog dialog = mock(ZootDialog.class);
		
		//then
		assertNull(dialogScreen.getDialog());
		
		//when
		dialogScreen.setDialog(dialog);
		
		//then
		assertEquals(dialog, dialogScreen.getDialog());
	}
	
	@Test
	public void shouldHaveNullOnShowAndOnHideActionsByDefault()
	{
		assertNull(dialogScreen.getOnShowAction());
		assertNull(dialogScreen.getOnHideAction());
	}
	
	@Test
	public void shouldSetOnShowAction()
	{
		//given
		Consumer<Game> action = game -> {};
		
		//when
		dialogScreen.setOnShowAction(action);
		
		//then
		assertEquals(action, dialogScreen.getOnShowAction());
	}
	
	@Test
	public void shouldSetOnHideAction()
	{
		//given
		Consumer<Game> action = game -> {};
		
		//when
		dialogScreen.setOnHideAction(action);
		
		//then
		assertEquals(action, dialogScreen.getOnHideAction());		
	}
	
	@Test
	public void shouldExecuteOnShowAction()
	{
		//given
		assertFalse(actionExecuted);
		Consumer<Game> action = game -> actionExecuted = true;
		
		//when		
		dialogScreen.setDialog(mock(ZootDialog.class));
		dialogScreen.setOnShowAction(action);
		dialogScreen.show();
		
		//then
		assertTrue(actionExecuted);				
	}
	
	@Test
	public void shouldExecuteOnHideAction()
	{
		//given
		assertFalse(actionExecuted);
		Consumer<Game> action = game -> actionExecuted = true;
		
		//when		
		dialogScreen.setDialog(mock(ZootDialog.class));
		dialogScreen.setOnHideAction(action);
		dialogScreen.hide();
		
		//then
		assertTrue(actionExecuted);				
	}
	
	@Test
	public void shouldDisposeBatch()
	{
		//when
		dialogScreen.dispose();
		
		//then
		verify(spriteBatch).dispose();		
	}
	
	@Test
	public void shouldAdvanceDialog()
	{
		//given
		ZootDialog dialog = mock(ZootDialog.class);
		
		//when
		dialogScreen.setDialog(dialog);
		
		//then
		assertTrue(dialogScreen.advanceDialog());
		verify(dialog).nextFrame();
	}
	
	@Test
	public void shouldQuitDialog()
	{
		//given
		ZootDialog dialog = mock(ZootDialog.class);
		
		//when
		dialogScreen.setDialog(dialog);
		
		//then
		assertTrue(dialogScreen.quitDialog());
		verify(dialog).forceFinish();		
	}
}
