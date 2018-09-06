package com.zootcat.dialogs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.zootcat.assets.ZootAssetManager;
import com.zootcat.exceptions.ZootException;
import com.zootcat.scene.ZootDirection;
import com.zootcat.testing.ZootTestUtils;

public class DialogTest
{
	private String dialogPath;
	private String dialogWithSettingsPath;
	
	@Mock private Texture firstFace;
	@Mock private Texture secondFace;
	@Mock private Texture thirdFace;
	@Mock private Texture firstImage;
	@Mock private Texture secondImage;
	@Mock private Texture thirdImage;
	@Mock private ZootAssetManager assetManager;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		when(assetManager.get("/data/gfx/characters/First.png", Texture.class)).thenReturn(firstFace);
		when(assetManager.get("/data/gfx/characters/Second.png", Texture.class)).thenReturn(secondFace);
		when(assetManager.get("/data/gfx/characters/Third.png", Texture.class)).thenReturn(thirdFace);
		when(assetManager.get("/data/gfx/items/FirstDialogImage.jpg", Texture.class)).thenReturn(firstImage);
		when(assetManager.get("/data/gfx/items/SecondDialogImage.jpg", Texture.class)).thenReturn(secondImage);
		when(assetManager.get("/data/gfx/items/ThirdDialogImage.jpg", Texture.class)).thenReturn(thirdImage);
		when(assetManager.get("/data/gfx/characters/NoFace.png", Texture.class)).thenThrow(new GdxRuntimeException("Asset not loaded"));
		when(assetManager.get("/data/gfx/items/NoImage.jpg", Texture.class)).thenThrow(new GdxRuntimeException("Asset not loaded"));
		
		dialogPath = ZootTestUtils.getResourcePath("dialogs/TestDialog.dialog", this);
		dialogWithSettingsPath = ZootTestUtils.getResourcePath("dialogs/TestDialogWithSettings.dialog", this);
	}
	
	@Test
	public void shouldInitializeDialog() throws ZootException
	{
		Dialog dialog = new Dialog(dialogPath, ":Test1", ":~Test1", assetManager);
		assertTrue(dialog.isRealTime());
		assertFalse(dialog.finished());	
		assertFalse(dialog.currentFrameFinished());
		assertEquals("", dialog.getVisibleText());
	}
	
	@Test
	public void shouldLoadSettings() throws ZootException
	{		
		Dialog dialog = new Dialog(dialogWithSettingsPath, ":Test1", ":~Test1", assetManager);
		assertFalse(dialog.isRealTime());		
	}
	
	@Test
	public void shouldShowEnter() throws ZootException
	{
		Dialog dialog = new Dialog(dialogWithSettingsPath, ":Test1", ":~Test1", assetManager);
		assertTrue(dialog.showEnter());
	}
	
	@Test
	public void shouldRewindDialog() throws ZootException
	{
		//when
		Dialog dialog = new Dialog(dialogPath, ":Test1", ":~Test1", assetManager);
		dialog.nextFrame();
		dialog.nextFrame();
		dialog.nextFrame();
		
		//then
		assertTrue(dialog.finished());
		assertEquals("Face from last frame should be visible", thirdFace, dialog.getCurrentFace());
		
		//when
		dialog.rewind();
		
		//then
		assertFalse(dialog.finished());
		assertEquals("Face from first frame should be visible", firstFace, dialog.getCurrentFace());
		assertEquals("", dialog.getVisibleText());
	}
	
	@Test
	public void shouldFinishDialogByForce() throws ZootException
	{
		//when
		Dialog dialog = new Dialog(dialogPath, ":Test1", ":~Test1", assetManager);
		dialog.forceFinish();
		
		//then
		assertTrue(dialog.finished());
		assertTrue(dialog.currentFrameFinished());
	}
		
	@Test
	public void shouldShowMoreTextOnUpdate() throws ZootException
	{
		//when
		Dialog dialog = new Dialog(dialogPath, ":Test1", ":~Test1", assetManager);
		
		//then
		assertEquals("", dialog.getVisibleText());
		
		//when
		dialog.update(1.0f);
		
		//then
		assertEquals("Line 1\nLine 2\nLine 3\n\n\n", dialog.getVisibleText());
		assertTrue(dialog.currentFrameFinished());
	}
	
	@Test
	public void shouldReturnNullForFacesAndImages() throws ZootException
	{
		//when
		Dialog dialog = new Dialog(dialogPath, ":NoFace", ":~NoFace", assetManager);
		
		//then
		assertNull(dialog.getCurrentFace());
		assertNull(dialog.getCurrentImage());
	}
	
	@Test(expected = ZootException.class)
	public void shouldThrowIfDialogTokensAreNotFound() throws ZootException
	{
		new Dialog(dialogPath, ":NoSuchTokens", ":~NoSuchTokens", assetManager);
	}
	
	@Test
	public void shouldReadDialogFrames() throws ZootException
	{
		//when
		Dialog dialog = new Dialog(dialogPath, ":Test1", ":~Test1", assetManager);
		
		//then
		assertEquals(3, dialog.getFrames().size());		
		assertEquals("First", dialog.getCurrentName());
		assertEquals(ZootDirection.Left, dialog.getFacePosition());
		assertEquals("/data/gfx/characters/First.png", dialog.getCurrentFaceFileName());
		assertEquals(firstFace, dialog.getCurrentFace());
		assertEquals(firstImage, dialog.getCurrentImage());
		
		//when
		dialog.nextFrame();
		
		//then		
		assertEquals("Second", dialog.getCurrentName());
		assertEquals(ZootDirection.Right, dialog.getFacePosition());
		assertEquals("/data/gfx/characters/Second.png", dialog.getCurrentFaceFileName());
		assertEquals(secondFace, dialog.getCurrentFace());
		assertEquals(secondImage, dialog.getCurrentImage());
		
		//when
		dialog.nextFrame();
		
		//then		
		assertEquals("Third", dialog.getCurrentName());
		assertEquals(ZootDirection.None, dialog.getFacePosition());
		assertEquals("/data/gfx/characters/Third.png", dialog.getCurrentFaceFileName());
		assertEquals(thirdFace, dialog.getCurrentFace());
		assertEquals(thirdImage, dialog.getCurrentImage());
	}	
}
