package com.zootcat.assets;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.graphics.Texture;

public class ZootAssetRecognizerTest
{
	private ZootAssetRecognizer recognizer;
	
	@Before
	public void setup()
	{
		recognizer = new ZootAssetRecognizer();
	}
	
	@Test
	public void setAssetTypeShouldOmitLeadingDotTest()
	{
		recognizer.setAssetType(".png", Texture.class);
		assertEquals(Texture.class, recognizer.getAssetType("image.png"));
	}
	
	@Test
	public void setAssetTypeShouldBeCaseInsensitiveTest()
	{
		recognizer.setAssetType("PNG", Texture.class);
		assertEquals(Texture.class, recognizer.getAssetType("image.png"));
		
		recognizer.setAssetType("Bmp", Integer.class);
		assertEquals(Integer.class, recognizer.getAssetType("image.bmp"));
		
		recognizer.setAssetType("jpg", Boolean.class);
		assertEquals(Boolean.class, recognizer.getAssetType("image.jpg"));
	}
	
	@Test
	public void getAssetTypeShouldBeCaseInsensitiveTest()
	{
		recognizer.setAssetType("png", Texture.class);
		assertEquals(Texture.class, recognizer.getAssetType("image.png"));
		assertEquals(Texture.class, recognizer.getAssetType("IMAGE.PNG"));
		assertEquals(Texture.class, recognizer.getAssetType("Image.Png"));
	}
	
	@Test
	public void getAssetTypeShouldReturnNullOnUnknownTypeTest()
	{
		assertNull(recognizer.getAssetType("unknownType.txt"));
	}
	
	@Test
	public void getAssetTypeShouldReturnNullOnNullValueTest()
	{
		assertNull(recognizer.getAssetType(null));
	}
	
	@Test
	public void getAssetTypeShouldReturnNullIfNoExtensionIsProvidedTest()
	{
		assertNull(recognizer.getAssetType("FileWithNoExtension"));
	}
	
	@Test
	public void getAssetDescriptorTest()
	{
		recognizer.setAssetType(".png", Texture.class);
		assertEquals("data/image.png", recognizer.getAssetDescriptor("data/image.png").fileName);
		assertEquals(Texture.class, recognizer.getAssetDescriptor("data/image.png").type);
	}
	
	@Test
	public void getAssetDescriptorShouldReturnNullOnUnknownTypeTest()
	{
		assertNull(recognizer.getAssetDescriptor("unknownType.txt"));
	}
	
}
