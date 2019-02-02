package com.zootcat.controllers.gfx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessFiles;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.zootcat.assets.ZootAssetManager;
import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.controllers.logic.DirectionController;
import com.zootcat.exceptions.RuntimeZootException;
import com.zootcat.game.ZootGame;
import com.zootcat.gfx.ZootGraphicsFactory;
import com.zootcat.scene.ZootActor;
import com.zootcat.scene.ZootDirection;
import com.zootcat.scene.ZootScene;
import com.zootcat.testing.ZootTestUtils;

public class AnimatedSpriteControllerTest
{
	private static final float ACTOR_WIDTH = 100.0f;
	private static final float ACTOR_HEIGHT = 200.0f;
	private static final float FRAME_WIDTH = 32.0f;
	private static final float FRAME_HEIGHT = 64.0f;
	
	@Mock private ZootGame game;
	@Mock private ZootScene scene;
	@Mock private ZootAssetManager assetManager;	
	@Mock private ZootGraphicsFactory graphicsFactory;
	@Mock private ZootActor actor;
	@Mock private Texture animationImage;
	@Mock private Sprite sprite;
	@Mock private DirectionController directionCtrl;
	private String animationFilePath;
	private AnimatedSpriteController animatedSpriteCtrl;
				
	@Before
	public void setup()
	{
		Gdx.files = new HeadlessFiles();
		
		MockitoAnnotations.initMocks(this);
		when(actor.isVisible()).thenReturn(true);
		when(actor.getName()).thenReturn("Animated Actor");		
		when(actor.tryGetSingleController(DirectionController.class)).thenReturn(directionCtrl);
		when(actor.getWidth()).thenReturn(ACTOR_WIDTH);
		when(actor.getHeight()).thenReturn(ACTOR_HEIGHT);
		
		
		when(scene.getUnitScale()).thenReturn(1.0f);
		when(game.getAssetManager()).thenReturn(assetManager);
		when(game.getGraphicsFactory()).thenReturn(graphicsFactory);
		when(assetManager.get(anyString())).thenReturn(animationImage);
		when(graphicsFactory.createSprite()).thenReturn(sprite);
		
		animationFilePath = ZootTestUtils.getResourcePath("assets/Animation2.anm", this); 
		
		animatedSpriteCtrl = new AnimatedSpriteController();
		ControllerAnnotations.setControllerParameter(animatedSpriteCtrl, "file", animationFilePath);
		ControllerAnnotations.setControllerParameter(animatedSpriteCtrl, "scene", scene);
		ControllerAnnotations.setControllerParameter(animatedSpriteCtrl, "game", game);
		ControllerAnnotations.setControllerParameter(animatedSpriteCtrl, "assetManager", assetManager);
	}
	
	@After
	public void tearDown()
	{
		Gdx.files = null;
	}
	
	@Test(expected=RuntimeZootException.class)
	public void shouldThrowIfFileIsNotFound()
	{
		//given
		ControllerAnnotations.setControllerParameter(animatedSpriteCtrl, "file", "notExistingFile.png");
		
		//when
		animatedSpriteCtrl.init(actor);
		
		//then throw
	}
	
	@Test
	public void shouldInitializeWithIdleAnimationByDefault()
	{
		//when
		animatedSpriteCtrl.init(actor);
		
		//then
		assertNotNull(animatedSpriteCtrl.getCurrentAnimation());
		assertEquals("Idle", animatedSpriteCtrl.getCurrentAnimation().getName());
	}
	
	@Test
	public void shouldInitializeWithProvidedStartingAnimation()
	{
		//given		
		ControllerAnnotations.setControllerParameter(animatedSpriteCtrl, "startingAnimation", "Startup");
		
		//when
		animatedSpriteCtrl.init(actor);
		
		//then
		assertNotNull(animatedSpriteCtrl.getCurrentAnimation());
		assertEquals("Startup", animatedSpriteCtrl.getCurrentAnimation().getName());
	}
	
	@Test
	public void shouldHaveAllAnimationsIncludedInTheFile()
	{
		//when
		animatedSpriteCtrl.init(actor);
		
		//then
		assertNotNull(animatedSpriteCtrl.getAnimation("Idle"));
		assertNotNull(animatedSpriteCtrl.getAnimation("Startup"));
		assertNotNull(animatedSpriteCtrl.getAnimation("Funky"));		
	}
	
	@Test
	public void shouldReturnNullForNotExistingAnimation()
	{
		//when
		animatedSpriteCtrl.init(actor);
		
		//then
		assertNull(animatedSpriteCtrl.getAnimation("IDontExist"));
	}
	
	@Test
	public void shouldSetCurrentAnimation()
	{
		//given
		animatedSpriteCtrl.init(actor);
		
		//when
		animatedSpriteCtrl.setAnimation("Funky");		
		
		//then
		assertEquals("Funky", animatedSpriteCtrl.getCurrentAnimation().getName());

		//when
		animatedSpriteCtrl.setAnimation("Startup");
		
		//then
		assertEquals("Startup", animatedSpriteCtrl.getCurrentAnimation().getName());

		//when
		animatedSpriteCtrl.setAnimation("Idle");
		
		//then
		assertEquals("Idle", animatedSpriteCtrl.getCurrentAnimation().getName());
	}
	
	@Test
	public void shouldStopPreviousAnimationWhenAnimationHasChanged()
	{
		//given
		animatedSpriteCtrl.init(actor);
		animatedSpriteCtrl.setAnimation("Idle");
		animatedSpriteCtrl.getCurrentAnimation().start();
		assertTrue(animatedSpriteCtrl.getCurrentAnimation().isPlaying());
		
		//when
		animatedSpriteCtrl.setAnimation("Funky");
		
		//then
		assertFalse(animatedSpriteCtrl.getAnimation("Idle").isPlaying());
	}
	
	@Test
	public void shouldRestartAnimationThatWasJustSet()
	{
		//given
		animatedSpriteCtrl.init(actor);
		animatedSpriteCtrl.getAnimation("Funky").finish();
		assertFalse(animatedSpriteCtrl.getAnimation("Funky").isPlaying());
		assertTrue(animatedSpriteCtrl.getAnimation("Funky").isFinished());
		
		//when
		animatedSpriteCtrl.setAnimation("Funky");
		
		//then
		assertTrue(animatedSpriteCtrl.getCurrentAnimation().isPlaying());
		assertFalse(animatedSpriteCtrl.getCurrentAnimation().isFinished());
	}
	
	@Test
	public void shouldDoNothingOnAdd()
	{
		animatedSpriteCtrl.onAdd(actor);
		verifyZeroInteractions(actor);
	}
	
	@Test
	public void shouldDoNothingOnRemove()
	{
		animatedSpriteCtrl.onRemove(actor);
		verifyZeroInteractions(actor);
	}
	
	@Test
	public void shouldNotRenderSpriteIfCurrentAnimationIsNull()
	{
		//given		
		animatedSpriteCtrl.init(actor);
		animatedSpriteCtrl.setAnimation("IDontExist");
		
		//when		
		animatedSpriteCtrl.onRender(mock(Batch.class), 1.0f, actor, 1.0f);
		
		//then
		verify(sprite, never()).draw(any());		
	}
	
	@Test
	public void shouldNotRenderSpriteIfActorIsNotVisible()
	{
		//given		
		when(actor.isVisible()).thenReturn(false);
		animatedSpriteCtrl.init(actor);
		animatedSpriteCtrl.setAnimation("Idle");
		
		//when		
		animatedSpriteCtrl.onRender(mock(Batch.class), 1.0f, actor, 1.0f);
		
		//then
		verify(sprite, never()).draw(any());		
	}
	
	@Test
	public void shouldRenderSprite()
	{
		//given
		Batch batch = mock(Batch.class);
		animatedSpriteCtrl.init(actor);
		animatedSpriteCtrl.setAnimation("Idle");
		
		//when
		animatedSpriteCtrl.onRender(batch, 1.0f, actor, 1.0f);
		
		//then
		verify(sprite).draw(batch);		
	}
	
	@Test
	public void shouldUpdateCurrentAnimationTimeOnUpdate()
	{
		//given
		animatedSpriteCtrl.init(actor);
		assertEquals(0.0f, animatedSpriteCtrl.getCurrentAnimation().getAnimationTime(), 0.0f);
		
		//when
		animatedSpriteCtrl.onUpdate(0.5f, actor);
		
		//then
		assertEquals(0.5f, animatedSpriteCtrl.getCurrentAnimation().getAnimationTime(), 0.0f);		
	}
	
	@Test
	public void shouldDoNothingWithSpriteOnUpdateIfCurrentAnimationIsNull()
	{
		//given
		animatedSpriteCtrl.init(actor);
		animatedSpriteCtrl.setAnimation("IDontExist");
		reset(sprite);
		
		//when		
		animatedSpriteCtrl.onUpdate(0.5f, actor);
		
		//then
		verifyZeroInteractions(sprite);		
	}
	
	@Test
	public void shouldSetCurrentAnimationFrameForSprite()
	{
		//given
		Color actorColor = Color.BLUE;
		when(actor.getColor()).thenReturn(actorColor);
		animatedSpriteCtrl.init(actor);
				
		//then
		verify(sprite).setTexture(animationImage);
		verify(sprite).setRegion((TextureRegion)any());
		verify(sprite).setColor(actorColor);
	}
	
	@Test
	public void shouldFlipSpriteHorizontallyIfActorIsFacingLeft()
	{
		//given
		when(directionCtrl.getDirection()).thenReturn(ZootDirection.Left);
		
		//when		
		animatedSpriteCtrl.init(actor);
				
		//then
		verify(sprite).setFlip(true, false);
	}
	
	@Test
	public void shouldNotFlipSpriteHorizontallyIfActorIsFacingRight()
	{
		//given
		when(directionCtrl.getDirection()).thenReturn(ZootDirection.Right);

		//when		
		animatedSpriteCtrl.init(actor);
				
		//then
		verify(sprite).setFlip(false, false);
	}
	
	@Test
	public void shouldNotFlipSpriteHorizontallyIfActorHasNoDirectionController()
	{
		//given
		when(actor.tryGetSingleController(DirectionController.class)).thenReturn(null);
		
		//when
		animatedSpriteCtrl.init(actor);
				
		//then
		verify(sprite).setFlip(false, false);
	}
	
	@Test
	public void shouldUseActorSizeForSpriteSize()
	{
		//given
		ControllerAnnotations.setControllerParameter(animatedSpriteCtrl, "useActorSize", true);
		ControllerAnnotations.setControllerParameter(animatedSpriteCtrl, "keepAspectRatio", false);
		
		//when
		animatedSpriteCtrl.init(actor);
		
		//then
		verify(sprite).setBounds(anyFloat(), anyFloat(), eq(ACTOR_WIDTH), eq(ACTOR_HEIGHT));		
	}
	
	@Test
	public void shouldUseActorSizeForSpriteSizeWhileKeepingAspectRatio()
	{
		//given
		final float expectedScaledWidth = 150.0f;
		final float expectedScaledHeight = 287.5f;
		ControllerAnnotations.setControllerParameter(animatedSpriteCtrl, "useActorSize", true);
		ControllerAnnotations.setControllerParameter(animatedSpriteCtrl, "keepAspectRatio", true);
		animatedSpriteCtrl.init(actor);
		animatedSpriteCtrl.setAnimation("Funky");
		reset(sprite);
		
		//when		
		animatedSpriteCtrl.onUpdate(1.0f, actor);
		
		//then
		verify(sprite).setBounds(anyFloat(), anyFloat(), eq(expectedScaledWidth), eq(expectedScaledHeight));		
	}
	
	@Test
	public void shouldUseAnimationFrameSizeForSpriteSize()
	{
		//given
		ControllerAnnotations.setControllerParameter(animatedSpriteCtrl, "useActorSize", false);
		
		//when
		animatedSpriteCtrl.init(actor);
		
		//then
		verify(sprite).setBounds(anyFloat(), anyFloat(), eq(FRAME_WIDTH), eq(FRAME_HEIGHT));		
	}
	
	@Test
	public void shouldScaleSpriteSize()
	{
		//given
		final float scaleX = 2.0f;
		final float scaleY = 3.0f;
		ControllerAnnotations.setControllerParameter(animatedSpriteCtrl, "useActorSize", false);
		ControllerAnnotations.setControllerParameter(animatedSpriteCtrl, "scaleX", scaleX);
		ControllerAnnotations.setControllerParameter(animatedSpriteCtrl, "scaleY", scaleY);
		
		//when
		animatedSpriteCtrl.init(actor);
		
		//then
		verify(sprite).setBounds(anyFloat(), anyFloat(), eq(FRAME_WIDTH * scaleX), eq(FRAME_HEIGHT * scaleY));		
	}
	
	@Test
	public void shouldSetActorRotationForSprite()
	{
		//given
		final float expectedRotation = 2.56f;
		when(actor.getRotation()).thenReturn(expectedRotation);
		
		//when
		animatedSpriteCtrl.init(actor);
		
		//then
		verify(sprite).setRotation(expectedRotation);
	}
	
	@Test
	public void shouldCenterSpriteHorizontallyOnActor()
	{
		//given
		final float expectedX = 0 - (FRAME_WIDTH * 0.5f) + (ACTOR_WIDTH * 0.5f);
		ControllerAnnotations.setControllerParameter(animatedSpriteCtrl, "centerX", true);
		
		//when
		animatedSpriteCtrl.init(actor);
		
		//then
		verify(sprite).setBounds(eq(expectedX), anyFloat(), anyFloat(), anyFloat());		
	}
	
	@Test
	public void shouldCenterSpriteVerticallyOnActor()
	{
		//given
		final float expectedY = 0 - (FRAME_HEIGHT * 0.5f) + (ACTOR_HEIGHT * 0.5f);
		ControllerAnnotations.setControllerParameter(animatedSpriteCtrl, "centerY", true);
		
		//when
		animatedSpriteCtrl.init(actor);
		
		//then
		verify(sprite).setBounds(anyFloat(), eq(expectedY), anyFloat(), anyFloat());		
	}
	
	@Test
	public void shouldSetOriginationAsSpriteCenter()
	{		
		//when
		animatedSpriteCtrl.init(actor);
		
		//then
		verify(sprite).setOriginCenter();		
	}
}
