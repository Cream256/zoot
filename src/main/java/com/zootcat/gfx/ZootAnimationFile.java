package com.zootcat.gfx;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.zootcat.exceptions.ZootException;
import com.zootcat.textdata.TextDataFile;
import com.zootcat.textdata.TextDataSection;

public class ZootAnimationFile
{
	private TextDataFile animationFile;
    private TextDataSection spriteSheetData;
    private List<TextDataSection> animationData;
    
	public ZootAnimationFile(File file) throws ZootException
	{
		animationFile = new TextDataFile(file);
		spriteSheetData = animationFile.readSections(":SpriteSheets", ":~SpriteSheets").get(0);
		animationData = animationFile.readSections(":Animation", ":~Animation");
	}

	public Map<String, String> getSpriteSheets()
	{        
		return spriteSheetData.getAllKeys().stream().collect(Collectors.toMap(key -> key, key -> spriteSheetData.get(key)));
	}
				
	public Map<Integer, ZootAnimation> createAnimations(Map<String, Texture> spriteSheets)
	{		
		Map<Integer, ZootAnimation> animations = new HashMap<Integer, ZootAnimation>();
		for(TextDataSection data : animationData)
		{
			TextureRegion[] frames = buildFrames(data, getSpriteSheet(spriteSheets, data));
			TextureRegion[] orderedFrames = orderFrames(data, frames);
			ZootAnimationOffset[] offsets = buildOffsets(data, orderedFrames);
			
			ZootAnimation animation = new ZootAnimation(getName(data), orderedFrames, getFrameDuration(data));		
			animation.setPlayMode(getPlayMode(data));
			animation.setOffsets(offsets);			
			animations.put(animation.getId(), animation);
		}
		return animations;
	}

	private Texture getSpriteSheet(Map<String, Texture> spriteSheets, TextDataSection data)
	{
		String sheetName = data.get("sheet");				
		return spriteSheets.get(sheetName != null ? sheetName : "default");
	}

	private ZootAnimationOffset[] buildOffsets(TextDataSection data, TextureRegion[] frames)
	{
		Vector2[] rightOffsets = data.getVectors("rightoffsets");
		Vector2[] leftOffsets = data.getVectors("leftoffsets");
		if(rightOffsets.length > 0 && leftOffsets.length == rightOffsets.length)
		{
			ZootAnimationOffset[] offsets = new ZootAnimationOffset[rightOffsets.length];	
			for(int i = 0; i < offsets.length; ++i)
			{
				offsets[i] = new ZootAnimationOffset();
				offsets[i].right = rightOffsets[i];
				offsets[i].left = leftOffsets[i];
			}			
			return offsets;
		}
		
		int rightOffsetX = data.getInt("rightoffsetx", 0);
		int rightOffsetY = data.getInt("rightoffsety", 0);			
		int leftOffsetX = data.getInt("leftoffsetx", 0);
		int leftOffsetY = data.getInt("leftoffsety", 0);
		
		ZootAnimationOffset[] offsets = new ZootAnimationOffset[frames.length];
		for(int i = 0; i < offsets.length; ++i)
		{
			ZootAnimationOffset offset = new ZootAnimationOffset();
			offset.right.x = rightOffsetX;
			offset.right.y = rightOffsetY;
			offset.left.x = leftOffsetX;
			offset.left.y = leftOffsetY;
			offsets[i] = offset;
		}		
		return offsets;
	}
	
	private float getFrameDuration(TextDataSection data)
	{
		return data.getInt("Speed", 100) / 1000.0f;
	}
	
	private String getName(TextDataSection data)
	{
		return data.get("Type");
	}
	
	private PlayMode getPlayMode(TextDataSection data)
	{
		boolean loop = data.getBoolean("Loop", false);
		boolean pingPong = data.getBoolean("PingPong", false);
		if(!loop)
		{
			return PlayMode.NORMAL;
		}
		return pingPong ? PlayMode.LOOP_PINGPONG : PlayMode.LOOP;
	}
	
	private TextureRegion[] orderFrames(TextDataSection data, TextureRegion[] frames)
	{
		int[] frameOrder = data.getIntArray("FrameOrder");
		if(frameOrder.length == 0)
		{
			return frames;
		}
		
		TextureRegion[] orderedFrames = new TextureRegion[frameOrder.length];
		for(int i = 0; i < frameOrder.length; ++i)
		{
			int frameIndex = frameOrder[i];
			orderedFrames[i] = frames[frameIndex];
		}
		return orderedFrames;
	}
	
	private TextureRegion[] buildFrames(TextDataSection data, Texture spriteSheet)
	{
		int totalFrames = data.getInt("Frames", 0);
		int framesPerRow = data.getInt("FramesPerRow", totalFrames);
		int cols = framesPerRow;
		int rows = totalFrames / framesPerRow;
		
		ZootAnimationFrameBuilder frameBuilder = new ZootAnimationFrameBuilder();
		return frameBuilder.setFrameWidth(data.getInt("FrameWidth", 0))
						   .setFrameHeight(data.getInt("FrameHeight", 0))
						   .setOffsetX(data.getInt("FirstFrameX", 0))
						   .setOffsetY(data.getInt("FirstFrameY", 0))
						   .setRows(rows)
						   .setCols(cols)
						   .setFrameLimit(totalFrames)
						   .build(spriteSheet);
	}
}
