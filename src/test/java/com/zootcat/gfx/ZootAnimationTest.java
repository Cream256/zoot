package com.zootcat.gfx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ZootAnimationTest 
{
	private static final int FRAME_COUNT = 3;
	private static final int FRAME_WIDTH = 0;
	private static final int FRAME_HEIGHT = 0;
	private static final float FRAME_DURATION = 0.25f;
	private static final String ANIMATION_NAME = "MyAnimation";
		
	private Texture texture1;
	private Texture texture2;
	private Texture texture3;
	private TextureRegion[] frames;
	private ZootAnimation animation;
	
	@Before
	public void setup()
	{
		texture1 = mock(Texture.class);
		texture2 = mock(Texture.class);
		texture3 = mock(Texture.class);
		frames = new TextureRegion[FRAME_COUNT];
		frames[0] = new TextureRegion(texture1, 0, 0, FRAME_WIDTH, FRAME_HEIGHT);	
		frames[1] = new TextureRegion(texture2, FRAME_WIDTH, 0, FRAME_WIDTH, FRAME_HEIGHT);
		frames[2] = new TextureRegion(texture3, FRAME_WIDTH * 2, 0, FRAME_WIDTH, FRAME_HEIGHT);
		animation = new ZootAnimation(ANIMATION_NAME, frames, FRAME_DURATION);
	}
			
	@Test
	public void shouldStepAnimation()
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
	public void shouldReturnKeyFrame()
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
	public void shouldReturnKeyFrameTexture()
	{
		animation.start();
		animation.step(0.0f);
		assertEquals("Should return first texture", texture1, animation.getKeyFrameTexture());
		
		animation.step(FRAME_DURATION);
		assertEquals("Should return second texture", texture2, animation.getKeyFrameTexture());
		
		animation.step(FRAME_DURATION);
		assertEquals("Should return third texture", texture3, animation.getKeyFrameTexture());		
	}
	
	@Test
	public void shouldStartAnimation()
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
	public void shouldStopAnimation()
	{
		animation.start();
		assertTrue(animation.isPlaying());
		
		animation.step(1.0f);
		animation.stop();
		assertFalse(animation.isPlaying());
		assertEquals(0.0f, animation.getAnimationTime(), 0.0f);
	}
	
	@Test
	public void shouldRestartAnimation()
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
	public void shouldReturnAnimationId()
	{
		ZootAnimation anim1 = new ZootAnimation("Anim1", frames, 0.0f);		
		ZootAnimation anim2 = new ZootAnimation("Anim2", frames, 0.0f);		
		ZootAnimation anim3 = new ZootAnimation("Anim3", frames, 0.0f);
		
		assertTrue(anim1.getId() != anim2.getId() && anim1.getId() != anim3.getId());
		assertTrue(anim2.getId() != anim3.getId());
	}
	
	@Test
	public void shouldReturnHashCode()
	{
		ZootAnimation anim1 = new ZootAnimation("Anim1", frames, 0.0f);		
		ZootAnimation anim2 = new ZootAnimation("Anim2", frames, 0.0f);
		ZootAnimation anim3 = new ZootAnimation("Anim3", frames, 0.0f);
		
		assertEquals(anim1.getId(), anim1.hashCode());
		assertEquals(anim2.getId(), anim2.hashCode());
		assertEquals(anim3.getId(), anim3.hashCode());
	}
	
	@Test
	public void shouldImplementEquals()
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
	public void shouldSetPlayMode()
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
	public void shouldReturnFrameCount()
	{
		assertEquals(frames.length, animation.getFrameCount());
	}
	
	@Test
	public void shouldReturnFrameDuration()
	{
		assertEquals(FRAME_DURATION, animation.getFrameDuration(), 0.0f);
	}
	
	@Test
	public void shouldReturnDefaultRepeatCount()
	{
		assertEquals(0, animation.getRepeatCount());
	}
	
	@Test
	public void shouldSetProperRepeatCount()
	{
		animation.setRepeatCount(123);
		assertEquals(123, animation.getRepeatCount());
		
		animation.setRepeatCount(-123);
		assertEquals(0, animation.getRepeatCount());
		
		animation.setRepeatCount(0);
		assertEquals(0, animation.getRepeatCount());
	}
	
	@Test
	public void shouldStopAfterCertainNumberOfRepeatsForNormalLoop()
	{
		//given
		animation.setRepeatCount(2);
		animation.setPlayMode(PlayMode.LOOP);
		
		//when
		animation.start();
		animation.step(FRAME_COUNT * FRAME_DURATION);	//first repeat
		
		//then
		assertTrue(animation.isPlaying());
		
		//when
		animation.step(FRAME_COUNT * FRAME_DURATION);	//second repeat
		
		//then
		assertFalse(animation.isPlaying());
	}
	
	@Test
	public void shouldStopAfterCertainNumberOfRepeatsForPingPongLoop()
	{
		//given
		animation.setRepeatCount(2);
		animation.setPlayMode(PlayMode.LOOP_PINGPONG);
		
		//when
		animation.start();
		animation.step(FRAME_COUNT * FRAME_DURATION);	//half of first repeat
		animation.step(FRAME_COUNT * FRAME_DURATION);	//second half of first repeat
		
		//then
		assertTrue(animation.isPlaying());
		
		//when
		animation.step(FRAME_COUNT * FRAME_DURATION);	//half of second repeat
		animation.step(FRAME_COUNT * FRAME_DURATION);	//second half of second repeat
		
		//then
		assertFalse(animation.isPlaying());
	}
	
	@Test
	public void shouldReturnTrueWhenAnimationIsFinished()
	{
		//when
		animation.start();
		animation.step(FRAME_COUNT * FRAME_DURATION);
		
		//then
		assertTrue(animation.isFinished());
		
		//when
		animation.restart();
		
		//then
		assertFalse(animation.isFinished());
	}
	
	@Test
	public void shouldReturnAnimationName()
	{
		assertEquals(ANIMATION_NAME, animation.getName());
	}
	
	@Test
	public void shouldReturnZeroOffsetsByDefault()
	{
		assertNotNull(animation.getOffsets());
		assertEquals(FRAME_COUNT, animation.getOffsets().length);
		for(int i = 0; i < FRAME_COUNT; ++i)
		{
			assertEquals(0.0f, animation.getOffsets()[i].left.x, 0.0f);
			assertEquals(0.0f, animation.getOffsets()[i].left.y, 0.0f);
			assertEquals(0.0f, animation.getOffsets()[i].right.x, 0.0f);
			assertEquals(0.0f, animation.getOffsets()[i].right.y, 0.0f);
		}
	}
	
	@Test
	public void shouldSetAnimationOffsets()
	{
		//given
		ZootAnimationOffset[] offsets = new ZootAnimationOffset[]
		{
			new ZootAnimationOffset(1.0f, 2.0f, 3.0f, 4.0f),
			new ZootAnimationOffset(5.0f, 6.0f, 7.0f, 8.0f),
			new ZootAnimationOffset(9.0f, 10.0f, 11.0f, 12.0f)
		};
		
		//when
		animation.setOffsets(offsets);
		
		//then
		assertEquals(3, animation.getOffsets().length);
		assertEquals(1.0f, animation.getOffsets()[0].right.x, 0.0f);
		assertEquals(2.0f, animation.getOffsets()[0].right.y, 0.0f);
		assertEquals(3.0f, animation.getOffsets()[0].left.x, 0.0f);
		assertEquals(4.0f, animation.getOffsets()[0].left.y, 0.0f);
		assertEquals(5.0f, animation.getOffsets()[1].right.x, 0.0f);
		assertEquals(6.0f, animation.getOffsets()[1].right.y, 0.0f);
		assertEquals(7.0f, animation.getOffsets()[1].left.x, 0.0f);
		assertEquals(8.0f, animation.getOffsets()[1].left.y, 0.0f);
		assertEquals(9.0f, animation.getOffsets()[2].right.x, 0.0f);
		assertEquals(10.0f, animation.getOffsets()[2].right.y, 0.0f);
		assertEquals(11.0f, animation.getOffsets()[2].left.x, 0.0f);
		assertEquals(12.0f, animation.getOffsets()[2].left.y, 0.0f);
	}
		
	@Test
	public void shouldReturnKeyFrameOffset()
	{
		//given
		ZootAnimationOffset[] offsets = new ZootAnimationOffset[]
		{
			new ZootAnimationOffset(1.0f, 2.0f, 3.0f, 4.0f),
			new ZootAnimationOffset(5.0f, 6.0f, 7.0f, 8.0f),
			new ZootAnimationOffset(9.0f, 10.0f, 11.0f, 12.0f)
		};
		
		//when
		animation.setOffsets(offsets);
		animation.start();
		ZootAnimationOffset offset = animation.getKeyFrameOffset();
		
		//then
		assertEquals(1.0f, offset.right.x, 0.0f);
		assertEquals(2.0f, offset.right.y, 0.0f);
		assertEquals(3.0f, offset.left.x, 0.0f);
		assertEquals(4.0f, offset.left.y, 0.0f);
	}
	
	@Test
	public void shouldOutputAnimationAsToAnimationName()
	{
		assertEquals("MyAnimation", animation.toString());
	}
}
