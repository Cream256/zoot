package com.zootcat.gfx;

import java.util.Arrays;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.zootcat.utils.ZootUtils;

public class ZootAnimation 
{	
	private final String name;	//name must be final in order to make getId() immutable	
	private float animationTime = 0.0f;	
	private boolean playing = false;
	private int repeatsCount = 0;
	private int repeatsDone = 0;
	private Animation<TextureRegion> animation;
	private ZootAnimationOffset[] offsets;
		
	public ZootAnimation(String name, TextureRegion[] frames, float frameDuration)
	{
		this.name = name;
		animation = new Animation<TextureRegion>(frameDuration, frames);
		offsets = new ZootAnimationOffset[frames.length];
		Arrays.fill(offsets, new ZootAnimationOffset());
	}
		
	public static int getAnimationId(String name)
	{
		return name.hashCode();
	}
		
	public void setOffsets(ZootAnimationOffset[] offsets)
	{
		this.offsets = offsets;
	}
	
	public ZootAnimationOffset[] getOffsets()
	{
		return offsets;
	}
	
	public ZootAnimationOffset getKeyFrameOffset()
	{
		return offsets[animation.getKeyFrameIndex(animationTime)];
	}
	
	public int getFrameCount()
	{
		return animation.getKeyFrames().length;
	}
	
	public float getFrameDuration()
	{
		return animation.getFrameDuration();
	}
	
	public void setPlayMode(PlayMode playMode)
	{
		animation.setPlayMode(playMode);
	}
	
	public PlayMode getPlayMode()
	{
		return animation.getPlayMode();
	}
	
	public TextureRegion getKeyFrame()
	{
		return animation.getKeyFrame(animationTime);		
	}
	
	public Texture getKeyFrameTexture()
	{
		return getKeyFrame().getTexture();
	}
		
	public String getName()
	{
		return name;
	}
		
	public float getAnimationTime()
	{
		return animationTime;
	}
	
	public void step(float delta)
	{
		if(!playing)
		{
			return;
		}
		
		animationTime += delta;
		
		if(getPlayMode() != PlayMode.NORMAL && repeatsCount > 0)
		{
			int loopMul = getPlayMode() == PlayMode.LOOP_PINGPONG ? 2 : 1;
			repeatsDone = ZootUtils.trunc(animationTime / (animation.getAnimationDuration() * loopMul));
			if(repeatsDone >= repeatsCount) stop();		
		}
	}
	
	public void start()
	{
		playing = true;
	}
	
	public void pause()
	{
		playing = false;
	}
	
	public void stop()
	{
		playing = false;
		repeatsDone = 0;
		animationTime = 0.0f;		
	}
	
	public void restart()
	{
		stop();
		start();
	}
	
	public boolean isPlaying()
	{
		return playing;
	}
	
	public boolean isFinished()
	{
		return animation.isAnimationFinished(animationTime);
	}
	
	public int getId()
	{
		return getAnimationId(name);
	}
	
	@Override
	public int hashCode()
	{
		return getId();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj == this) return true;
		if(!(obj instanceof ZootAnimation)) return false;
		return getId() == ((ZootAnimation)obj).getId();
	}

	public void setRepeatCount(int value)
	{
		repeatsCount = Math.max(0, value);
	}
	
	public int getRepeatCount()
	{
		return repeatsCount;
	}
}
