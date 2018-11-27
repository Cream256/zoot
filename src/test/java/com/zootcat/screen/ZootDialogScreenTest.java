package com.zootcat.screen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.zootcat.dialogs.ZootDialog;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.game.ZootGame;
import com.zootcat.gfx.ZootGraphicsFactory;
import com.zootcat.scene.ZootActor;

public class ZootDialogScreenTest
{	
	@Mock private ZootGame game;
	@Mock private ZootGraphicsFactory spriteFactory;
	@Mock private ZootScreen previousScreen;
	@Rule public ExpectedException expectedEx = ExpectedException.none();
	
	private ZootDialogScreen dialogScreen;
		
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		when(game.getGraphicsFactory()).thenReturn(spriteFactory);
		
		dialogScreen = new ZootDialogScreen(game);
	}
	
	@Ignore
	@Test
	public void shouldRenderPreviousScreenFirst()
	{				
		when(game.getPreviousScreen()).thenReturn(previousScreen);
		
		dialogScreen.setDialog(mock(ZootDialog.class));
		dialogScreen.show();
		dialogScreen.render(1.0f);
		verify(previousScreen).render(1.0f);		
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
		expectedEx.expect(RuntimeZootException.class);
		expectedEx.expectMessage("No previous screen was set for DialogScreen");
		
		dialogScreen.setDialog(mock(ZootDialog.class));
		dialogScreen.show();		
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
}
