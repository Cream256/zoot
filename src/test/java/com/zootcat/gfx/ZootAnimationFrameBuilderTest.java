package com.zootcat.gfx;

import static org.mockito.Mockito.*;

import java.util.Arrays;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.zootcat.exceptions.RuntimeZootException;

public class ZootAnimationFrameBuilderTest
{
	private static final int COLS = 5;
	private static final int ROWS = 6;
	private static final int FRAME_WIDTH = 10;
	private static final int FRAME_HEIGHT = 25;
	private static final int OFFSET_X = 100;
	private static final int OFFSET_Y = 200;
	private static final int FRAME_LIMIT = 10;
	
	private Texture spriteSheet;
	private ZootAnimationFrameBuilder builder;

	@Before
	public void setup()
	{
		spriteSheet = mock(Texture.class);
		builder = new ZootAnimationFrameBuilder();
	}
	
	@Test
	public void buildTest()
	{
		TextureRegion[] frames = builder.setCols(COLS)
										.setRows(ROWS)
										.setFrameWidth(FRAME_WIDTH)
										.setFrameHeight(FRAME_HEIGHT)
										.setOffsetX(OFFSET_X)
										.setOffsetY(OFFSET_Y)
										.build(spriteSheet);			
		assertNotNull(frames);
		assertEquals(COLS * ROWS, frames.length);
		
		Arrays.stream(frames).forEach((frame) ->
		{		
			assertEquals(spriteSheet, frame.getTexture());			
			assertEquals(FRAME_WIDTH, frame.getRegionWidth());
			assertEquals(FRAME_HEIGHT, frame.getRegionHeight());
		});
	}
	
	@Test
	public void buildWithFrameLimitTest()
	{
		TextureRegion[] frames = builder.setCols(COLS)
										.setRows(ROWS)
										.setFrameWidth(FRAME_WIDTH)
										.setFrameHeight(FRAME_HEIGHT)
										.setOffsetX(OFFSET_X)
										.setOffsetY(OFFSET_Y)
										.setFrameLimit(FRAME_LIMIT)
										.build(spriteSheet);				
		assertNotNull(frames);
		assertEquals(FRAME_LIMIT, frames.length);
		Arrays.stream(frames).forEach((frame) ->
		{		
			assertEquals(spriteSheet, frame.getTexture());			
			assertEquals(FRAME_WIDTH, frame.getRegionWidth());
			assertEquals(FRAME_HEIGHT, frame.getRegionHeight());
		});
	}
	
	@Test
	public void buildShouldResetBuilderStateTest()
	{
		builder.setCols(1).setRows(2).setFrameHeight(3).setFrameWidth(4).setOffsetX(5).setOffsetY(6).setFrameLimit(7);
		builder.build(spriteSheet);
		
		assertEquals(0, builder.getCols());
		assertEquals(0, builder.getRows());
		assertEquals(0, builder.getFrameWidth());
		assertEquals(0, builder.getFrameHeight());
		assertEquals(0, builder.getOffsetX());
		assertEquals(0, builder.getOffsetY());
		assertEquals(0, builder.getFrameLimit());
	}
		
	@Test
	public void buildShouldThrowIfNotAllParametersAreSetTest()
	{
		shouldThrowOnBuildWithNotAllParams(builder.setCols(0).setRows(1).setFrameWidth(1).setFrameHeight(1));
		shouldThrowOnBuildWithNotAllParams(builder.setCols(1).setRows(0).setFrameWidth(1).setFrameHeight(1));
		shouldThrowOnBuildWithNotAllParams(builder.setCols(1).setRows(1).setFrameWidth(0).setFrameHeight(1));
		shouldThrowOnBuildWithNotAllParams(builder.setCols(1).setRows(1).setFrameWidth(1).setFrameHeight(0));
	}
		
	@Test(expected = RuntimeZootException.class)
	public void buildShouldThrowIfSpriteSheetIsNotGivenTest()
	{
		builder.setCols(1).setRows(1).setFrameHeight(1).setFrameWidth(1);
		builder.build(null);
	}
	
	@Test
	public void setFrameLimitTest()
	{
		assertEquals(0, builder.getFrameLimit());
		assertEquals(50, builder.setFrameLimit(50).getFrameLimit());
	}
	
	@Test
	public void setRowsTest()
	{
		assertEquals(0, builder.getRows());		
		assertEquals(256, builder.setRows(256).getRows());
	}
	
	@Test
	public void setColsTest()
	{
		assertEquals(0, builder.getCols());		
		assertEquals(128, builder.setCols(128).getCols());
	}
	
	@Test
	public void setOffsetXTest()
	{
		assertEquals(0, builder.getOffsetX());		
		assertEquals(64, builder.setOffsetX(64).getOffsetX());
	}
	
	@Test
	public void setOffsetYTest()
	{
		assertEquals(0, builder.getOffsetY());		
		assertEquals(32, builder.setOffsetY(32).getOffsetY());
	}
	
	@Test
	public void setFrameWidthTest()
	{
		assertEquals(0, builder.getFrameWidth());		
		assertEquals(10, builder.setFrameWidth(10).getFrameWidth());
	}
	
	@Test
	public void setFrameHeightTest()
	{
		assertEquals(0, builder.getFrameHeight());	
		assertEquals(5, builder.setFrameHeight(5).getFrameHeight());
	}
	
	private void shouldThrowOnBuildWithNotAllParams(ZootAnimationFrameBuilder builder)
	{
		try
		{
			builder.build(spriteSheet);
			fail("Builder did not throw when all required parameters were not set");
		}
		catch(Exception e)
		{
			//ok
		}
	}
}
