package com.zootcat.controllers.gfx;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.zootcat.controllers.factory.CtrlDebug;
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.controllers.logic.DirectionController;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.game.ZootGame;
import com.zootcat.gfx.ZootAnimation;
import com.zootcat.gfx.ZootAnimationFile;
import com.zootcat.gfx.ZootAnimationOffset;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;
import com.zootcat.scene.ZootScene;

public class AnimatedSpriteController extends RenderControllerAdapter 
{
	@CtrlParam(required = true) private String file;
	@CtrlParam private boolean useActorSize = false;
	@CtrlParam private boolean keepAspectRatio = false;
	@CtrlParam private boolean centerX = false;
	@CtrlParam private boolean centerY = false;
	@CtrlParam private float scaleX = 1.0f;
	@CtrlParam private float scaleY = 1.0f;
	@CtrlParam private String startingAnimation = "Idle";
	@CtrlParam(global = true) private ZootScene scene;
	@CtrlParam(global = true) private ZootGame game;
	@CtrlParam(global = true) private AssetManager assetManager;
	@CtrlDebug private ZootAnimation currentAnimation;
	
	private Sprite sprite;
	private float firstAnimationWidth;
	private float firstAnimationHeight;	
	private Map<Integer, ZootAnimation> animations;
				
	@Override
	public void init(ZootActor actor)
	{
		try
		{			
			FileHandle animationFileHandle = Gdx.files.internal(file);			
			ZootAnimationFile zootAnimationFile = new ZootAnimationFile(animationFileHandle.file()); 
			
			Map<String, Texture> spriteSheets = new HashMap<String, Texture>();
			zootAnimationFile.getSpriteSheets().forEach((k, v) -> spriteSheets.put(k, assetManager.get(animationFileHandle.parent().path() + "/" + v)));
			
			animations = zootAnimationFile.createAnimations(spriteSheets); 		
			setAnimation(startingAnimation);
			calculateFirstAnimationFrameSize(getCurrentAnimation());
			
			sprite = game.getGraphicsFactory().createSprite();
			if(currentAnimation != null) updateSprite(actor);
		}
		catch (Exception e)
		{
			throw new RuntimeZootException("Unable to initialize animated sprite for " + actor.getName(), e);
		}
	}

	private void calculateFirstAnimationFrameSize(ZootAnimation firstAnimation)
	{		
		firstAnimationWidth = firstAnimation.getKeyFrame().getRegionWidth();
		firstAnimationHeight = firstAnimation.getKeyFrame().getRegionHeight();
	}

	@Override
	public void onAdd(ZootActor actor)
	{
		//noop
	}

	@Override
	public void onRemove(ZootActor actor)
	{
		//noop
	}

	@Override
	public void onUpdate(float delta, ZootActor actor)
	{
		if(currentAnimation == null)
		{
			return;
		}
		
		currentAnimation.step(delta);
		updateSprite(actor);
	}

	@Override
	public void onRender(Batch batch, float parentAlpha, ZootActor actor, float delta)
	{
		if(currentAnimation == null || !actor.isVisible())
		{
			return;
		}
		
		sprite.draw(batch);
	}
	
	public ZootAnimation getCurrentAnimation()
	{
		return currentAnimation;
	}
	
	public ZootAnimation getAnimation(String animationName)
	{
		int newId = ZootAnimation.getAnimationId(animationName);				
		return animations.get(newId);
	}
	
	public void setAnimation(String animationName)
	{
		if(currentAnimation != null)
		{
			currentAnimation.stop();
		}
		
		currentAnimation = getAnimation(animationName);
		
		if(currentAnimation != null)
		{
			currentAnimation.restart();
		}
	}
	
	private void updateSprite(ZootActor actor)
	{		
		TextureRegion currentFrame = currentAnimation.getKeyFrame();		
		sprite.setTexture(currentFrame.getTexture());
		sprite.setRegion(currentFrame);
		sprite.setColor(actor.getColor());
		
		ZootAnimationOffset offset = currentAnimation.getKeyFrameOffset();		
		
		ZootDirection direction = getDirection(actor);		
		sprite.setFlip(direction == ZootDirection.Left, false);
		Vector2 directionOffset = direction == ZootDirection.Left ? offset.left : offset.right;
		
		float sceneUnitScale = scene.getUnitScale();
		float frameWidth = currentFrame.getRegionWidth() * sceneUnitScale;
		float frameHeight = currentFrame.getRegionHeight() * sceneUnitScale;
		
		if(useActorSize)
		{
			if(keepAspectRatio)
			{
				float frameScaleX = currentFrame.getRegionWidth() / firstAnimationWidth;
				float frameScaleY = currentFrame.getRegionHeight() / firstAnimationHeight;								
				frameWidth = actor.getWidth() * frameScaleX;
				frameHeight = actor.getHeight() * frameScaleY;				
			}
			else
			{
				frameWidth = actor.getWidth();
				frameHeight = actor.getHeight();	
			}
		}
		frameWidth *= scaleX;
		frameHeight *= scaleY;
		
		float frameX = actor.getX() + directionOffset.x * sceneUnitScale + getOffsetX();
		float frameY = actor.getY() + directionOffset.y * sceneUnitScale + getOffsetY();
		if(centerX)
		{		
			frameX = frameX - (frameWidth * 0.5f) + (actor.getWidth() * 0.5f);			
		}
		if(centerY)
		{
			frameY = frameY - (frameHeight * 0.5f) + (actor.getHeight() * 0.5f);
		}
				
		sprite.setBounds(frameX, frameY, frameWidth, frameHeight);		
		sprite.setOriginCenter();
		sprite.setRotation(actor.getRotation());
	}

	private ZootDirection getDirection(ZootActor actor)
	{
		DirectionController ctrl = actor.tryGetSingleController(DirectionController.class);
		if(ctrl != null)
		{
			return ctrl.getDirection();
		}
		return ZootDirection.Right;
	}
}
