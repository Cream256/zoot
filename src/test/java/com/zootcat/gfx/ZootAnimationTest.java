package com.zootcat.gfx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ZootAnimationTest 
{
	private static final int FRAME_WIDTH = 0;
	private static final int FRAME_HEIGHT = 0;
	private static final float FRAME_DURATION = 0.25f;
	
	private Texture textureMock;
	private TextureRegion[] frames;
	private ZootAnimation animation;
	
	@Before
	public void setup()
	{
		textureMock = mock(Texture.class);
		frames = new TextureRegion[3]; 
		frames[0] = new TextureRegion(textureMock, 0, 0, FRAME_WIDTH, FRAME_HEIGHT);	
		frames[1] = new TextureRegion(textureMock, FRAME_WIDTH, 0, FRAME_WIDTH, FRAME_HEIGHT);
		frames[2] = new TextureRegion(textureMock, FRAME_WIDTH * 2, 0, FRAME_WIDTH, FRAME_HEIGHT);
		animation = new ZootAnimation("Anim", frames, FRAME_DURATION);
	}
			
	@Test
	public void stepTest()
	{
		assertEquals(0.0f, animation.getAnimationTime(), 0.0f);
		
		animation.start();		
		animation.step(0.25f);
		assertEquals(0.25f, animation.getAnimationTime(), 0.0f);
		
		animation.step(0.25f);
		assertEquals(0.50f, animation.getAnimationTime(), 0.0f);
		
		animation.pause();
		animation.step(0.25f);
		assertEquals("After pause animation should not step further", 0.50f, animation.getAnimationTime(), 0.0f);
	}
	
	@Test
	public void getKeyFrameTest()
	{
		assertEquals(frames[0], animation.getKeyFrame());
		
		animation.start();
		animation.step(0.0f);
		assertEquals("After start should be at first frame", frames[0], animation.getKeyFrame());
		
		animation.step(FRAME_DURATION / 2.0f);
		assertEquals("During 1/2 frame duration should be at first frame", frames[0], animation.getKeyFrame());
		
		animation.step(FRAME_DURATION / 2.0f);
		assertEquals("After frame duration passes should go to next frame", frames[1], animation.getKeyFrame());
		
		animation.step(FRAME_DURATION);
		assertEquals("After frame duration passes go to last frame", frames[2], animation.getKeyFrame());
	}
	
	@Test
	public void startTest()
	{
		assertFalse(animation.isPlaying());
		
		animation.start();
		assertTrue(animation.isPlaying());
		
		animation.step(0.1f);
		animation.start();
		assertTrue(animation.isPlaying());
		assertEquals(0.1f, animation.getAnimationTime(), 0.0f);
	}
	
	@Test
	public void stopTest()
	{
		animation.start();
		assertTrue(animation.isPlaying());
		
		animation.step(1.0f);
		animation.stop();
		assertFalse(animation.isPlaying());
		assertEquals(0.0f, animation.getAnimationTime(), 0.0f);
	}
	
	@Test
	public void restartTest()
	{
		assertEquals(0.0f, animation.getAnimationTime(), 0.0f);
		
		animation.start();
		animation.step(0.50f);
		assertTrue(animation.isPlaying());
		assertEquals(0.50f, animation.getAnimationTime(), 0.0f);
		
		animation.restart();
		assertTrue(animation.isPlaying());
		assertEquals(0.00f, animation.getAnimationTime(), 0.0f);
	}
	
	@Test
	public void getIdTest()
	{
		ZootAnimation anim1 = new ZootAnimation("Anim1", frames, 0.0f);		
		ZootAnimation anim2 = new ZootAnimation("Anim2", frames, 0.0f);		
		ZootAnimation anim3 = new ZootAnimation("Anim3", frames, 0.0f);
		
		assertTrue(anim1.getId() != anim2.getId() && anim1.getId() != anim3.getId());
		assertTrue(anim2.getId() != anim3.getId());
	}
	
	@Test
	public void hashCodeTest()
	{
		ZootAnimation anim1 = new ZootAnimation("Anim1", frames, 0.0f);		
		ZootAnimation anim2 = new ZootAnimation("Anim2", frames, 0.0f);
		ZootAnimation anim3 = new ZootAnimation("Anim3", frames, 0.0f);
		
		assertEquals(anim1.getId(), anim1.hashCode());
		assertEquals(anim2.getId(), anim2.hashCode());
		assertEquals(anim3.getId(), anim3.hashCode());
	}
	
	@Test
	public void equalsTest()
	{
		ZootAnimation anim1 = new ZootAnimation("Anim1", frames, 0.0f);		
		ZootAnimation anim2 = new ZootAnimation("Anim2", frames, 0.0f); 
		ZootAnimation anim3 = new ZootAnimation("Anim1", frames, 0.0f);
		
		assertTrue(anim1.equals(anim1));
		assertTrue(anim1.equals(anim3));
		assertTrue(anim3.equals(anim1));
		assertFalse(anim1.equals(new Integer(0)));
		assertFalse(anim1.equals("String"));
		assertFalse(anim1.equals(anim2));
		assertFalse(anim2.equals(anim1));
	}
	
	@Test
	public void setPlayModeTest()
	{
		assertEquals("Default play mode should be NORMAL", PlayMode.NORMAL, animation.getPlayMode());
		
		animation.setPlayMode(PlayMode.LOOP);
		assertEquals(PlayMode.LOOP, animation.getPlayMode());
		
		animation.setPlayMode(PlayMode.LOOP_PINGPONG);
		assertEquals(PlayMode.LOOP_PINGPONG, animation.getPlayMode());
		
		animation.setPlayMode(PlayMode.LOOP_REVERSED);
		assertEquals(PlayMode.LOOP_REVERSED, animation.getPlayMode());
		
		animation.setPlayMode(PlayMode.REVERSED);
		assertEquals(PlayMode.REVERSED, animation.getPlayMode());
		
		animation.setPlayMode(PlayMode.NORMAL);
		assertEquals(PlayMode.NORMAL, animation.getPlayMode());
	}
	
	@Test
	public void getFrameCountTest()
	{
		assertEquals(frames.length, animation.getFrameCount());
	}
	
	@Test
	public void getFrameDurationTest()
	{
		assertEquals(FRAME_DURATION, animation.getFrameDuration(), 0.0f);
	}
}
