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
import com.zootcat.controllers.factory.CtrlParam;
import com.zootcat.controllers.logic.DirectionController;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.gfx.ZootAnimation;
import com.zootcat.gfx.ZootAnimationFile;
import com.zootcat.gfx.ZootAnimationOffset;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;
import com.zootcat.scene.ZootScene;

public class AnimatedSpriteController implements RenderController 
{
	@CtrlParam(required = true, debug = true) private String file;
	@CtrlParam(debug = true) private boolean useActorSize = true;
	@CtrlParam(debug = true) private String startingAnimation = "IDLE";
	@CtrlParam(global = true) private ZootScene scene;
	@CtrlParam(global = true) private AssetManager assetManager;
	
	private Sprite sprite;
	private ZootAnimation currentAnimation;
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
			
			sprite = new Sprite();
			updateSprite(actor);
		}
		catch (Exception e)
		{
			throw new RuntimeZootException("Unable to initialize animated sprite", e);
		}
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
		if(currentAnimation == null)
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
		if(currentAnimation == null)
		{
			return;
		}
		
		TextureRegion frame = currentAnimation.getKeyFrame();		
		sprite.setTexture(frame.getTexture());
		sprite.setRegion(frame);				
		
		ZootAnimationOffset offset = currentAnimation.getFrameOffset();		
		boolean leftOffset = actor.controllerCondition(DirectionController.class, c -> c.getDirection() == ZootDirection.Left);			
		Vector2 directionOffset = leftOffset ? offset.left : offset.right;
				
		float scale = scene.getUnitScale();
		float x = actor.getX() + directionOffset.x * scale;
		float y = actor.getY() + directionOffset.y * scale;
		
		ZootDirection direction = getDirection(actor);		
		sprite.setFlip(direction == ZootDirection.Left, false);
		
		if(useActorSize)
		{
			sprite.setBounds(x, y, actor.getWidth(), actor.getHeight());
		}
		else
		{
			sprite.setBounds(x, y, frame.getRegionWidth() * scale, frame.getRegionHeight() * scale);
		}
		
		sprite.setOriginCenter();
		sprite.setRotation(actor.getRotation());
	}

	private ZootDirection getDirection(ZootActor actor)
	{
		DirectionController ctrl = actor.tryGetController(DirectionController.class);
		if(ctrl != null)
		{
			return ctrl.getDirection();
		}
		return ZootDirection.Right;
	}
}
